package io.tracee.binding.jaxws;

import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import io.tracee.transport.SoapHeaderTransport;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.JAXBException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TraceeClientHandlerTest {

	private final TraceeBackend backend = spy(new SimpleTraceeBackend());
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;
	private final TraceeClientHandler unit = new TraceeClientHandler(backend, filterConfiguration);
	private final SOAPMessageContext messageContext = mock(SOAPMessageContext.class);

	private SOAPMessage message;

	@Before
	public void setup() throws SOAPException {
		message = spy(MessageFactory.newInstance().createMessage());
		when(messageContext.getMessage()).thenReturn(message);
	}

	@Test
	public void faultsShouldBeHandledWithoutMessageInteraction() throws SOAPException {
		assertThat(unit.handleFault(messageContext), is(true));
		verify(message, never()).getSOAPHeader();
	}

	@Test
	public void skipProcessingWithoutErrorIfNoTpicHeaderIsInMessage() throws SOAPException {
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void skipProcessingWithoutErrorIfNoSoapHeaderIsInMessage() throws SOAPException {
		when(message.getSOAPHeader()).thenReturn(null);
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void catchExceptionOnReadTpicHeader() throws SOAPException {
		when(message.getSOAPHeader()).thenThrow(new SOAPException());
		unit.handleIncoming(messageContext);
		assertThat(backend.isEmpty(), is(true));
	}

	@Test
	public void readSoapHeaderIntoToBackend() throws SOAPException, JAXBException {
		final Map<String, String> context = new HashMap<>();
		context.put("abc", "123");

		new SoapHeaderTransport().renderSoapHeader(context, message.getSOAPHeader());
		when(messageContext.getMessage()).thenReturn(message);

		unit.handleIncoming(messageContext);
		assertThat(backend.copyToMap(), hasEntry("abc", "123"));
	}

	@Test
	public void addSoapHeaderIfNooneIsOnMessage() throws SOAPException {
		backend.put("abcd", "12");
		when(message.getSOAPHeader()).thenReturn(null);
		when(message.getSOAPPart()).thenReturn(mock(SOAPPart.class));
		final SOAPEnvelope envelope = mock(SOAPEnvelope.class);
		when(message.getSOAPPart().getEnvelope()).thenReturn(envelope);
		when(message.getSOAPPart().getEnvelope().addHeader()).thenReturn(mock(SOAPHeader.class, Mockito.RETURNS_DEEP_STUBS));

		unit.handleOutgoing(messageContext);
		verify(envelope).addHeader();
	}

	@Test
	public void renderBackendToSoapHeader() throws SOAPException {
		backend.put("my header", "Wow!");
		unit.handleOutgoing(messageContext);
		final NodeList tpicElements = message.getSOAPHeader().getElementsByTagNameNS(TraceeConstants.SOAP_HEADER_NAMESPACE, TraceeConstants.TPIC_HEADER);
		assertThat(tpicElements.getLength(), is(1));
		final Node tpicEntry = tpicElements.item(0).getChildNodes().item(0);
		assertThat(tpicEntry.getLocalName(), is("entry"));
		assertThat(tpicEntry.getAttributes().getNamedItem("key").getNodeValue(), is("my header"));
		assertThat(tpicEntry.getChildNodes().item(0).getNodeValue(), is("Wow!"));
	}

	@Test
	public void skipProcessingIfBackendIsEmpty() throws SOAPException {
		unit.handleOutgoing(messageContext);
		verify(message, never()).getSOAPHeader();
	}
}
