package io.tracee.binding.cxf.interceptor;

import io.tracee.TraceeBackend;
import io.tracee.configuration.PropertiesBasedTraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.message.MessageUtils;
import org.apache.cxf.phase.Phase;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingResponse;

public class TraceeResponseOutInterceptor extends AbstractTraceeOutInterceptor {

	public TraceeResponseOutInterceptor(TraceeBackend backend) {
		this(backend, PropertiesBasedTraceeFilterConfiguration.instance().DEFAULT);
	}

	public TraceeResponseOutInterceptor(TraceeBackend backend, TraceeFilterConfiguration filterConfiguration) {
		super(Phase.USER_LOGICAL, OutgoingResponse, backend, filterConfiguration);
	}

	@Override
	public void handleMessage(Message message) throws Fault {
		super.handleMessage(message);
		if (shouldHandleMessage(message)) {
			backend.clear();
		}
	}

	@Override
	protected boolean shouldHandleMessage(Message message) {
		return !MessageUtils.isRequestor(message);
	}
}
