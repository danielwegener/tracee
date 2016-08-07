package io.tracee.binding.jaxws;


import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.transport.SoapHeaderTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeServerHandler extends AbstractTraceeHandler {

	private static final Logger logger = LoggerFactory.getLogger(TraceeServerHandler.class);

	private final SoapHeaderTransport transportSerialization;

	/**
	 * @deprecated Use full constructor.
	 */
	@Deprecated
	public TraceeServerHandler() {
		this(Tracee.getBackend(), PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT, new SoapHeaderTransport());
	}

	public TraceeServerHandler(TraceeBackend traceeBackend, TraceeFilterConfiguration filterConfiguration, SoapHeaderTransport soapHeaderTransport) {
		super(traceeBackend, filterConfiguration);
		this.transportSerialization = soapHeaderTransport;
	}

	protected final void handleIncoming(SOAPMessageContext context) {
		final SOAPMessage soapMessage = context.getMessage();
		try {
			final SOAPHeader header = soapMessage.getSOAPHeader();

			if (header != null && filterConfiguration.shouldProcessContext(IncomingRequest)) {
				final Map<String, String> parsedContext = transportSerialization.parseSoapHeader(header);
				final Map<String, String> filteredContext = filterConfiguration.filterDeniedParams(parsedContext, IncomingRequest);
				traceeBackend.putAll(filteredContext);
			}
		} catch (final SOAPException e) {
			logger.warn("Error during precessing of inbound soap header: {}", e.getMessage());
			logger.debug("Detailed: Error during precessing of inbound soap header: {}", e.getMessage(), e);
		}

		Utilities.generateInvocationIdIfNecessary(traceeBackend, filterConfiguration);
	}

	protected final void handleOutgoing(SOAPMessageContext context) {
		final SOAPMessage msg = context.getMessage();
		try {
			if (msg != null && !traceeBackend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingResponse)) {

				// get or create header
				final SOAPHeader header = getOrCreateHeader(msg);

				final Map<String, String> filteredContext = filterConfiguration.filterDeniedParams(traceeBackend.copyToMap(), OutgoingResponse);
				transportSerialization.renderSoapHeader(filteredContext, header);

				msg.saveChanges();
				context.setMessage(msg);
			}

		} catch (final SOAPException e) {
			logger.error("TraceeServerHandler : Exception occurred during processing of outbound message.");
			logger.debug("Detailed: TraceeServerHandler : Exception occurred during processing of outbound message: {}", e.getMessage(), e);
		} finally {
			// must reset tracee context
			traceeBackend.clear();
		}
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		this.handleOutgoing(context);
		return true;
	}
}
