package io.tracee.binding.springws;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointInterceptor;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingRequest;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public final class TraceeEndpointInterceptor extends AbstractTraceeInterceptor implements EndpointInterceptor {

	public TraceeEndpointInterceptor() {
		this(Tracee.getBackend(), null);
	}

	public TraceeEndpointInterceptor(final String profile) {
		this(Tracee.getBackend(), profile);
	}

	TraceeEndpointInterceptor(final TraceeBackend backend, final String profile) {
		super(backend, profile);
	}

	@Override
	public boolean handleRequest(MessageContext messageContext, Object o) throws Exception {
		parseContextFromSoapHeader(messageContext.getRequest(), IncomingRequest);

		Utilities.generateInvocationIdIfNecessary(backend);
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext, Object o) throws Exception {
		serializeContextToSoapHeader(messageContext.getResponse(), OutgoingResponse);
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object o) throws Exception {
		return handleResponse(messageContext, o);
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Object o, Exception e) throws Exception {
		backend.clear();
	}
}
