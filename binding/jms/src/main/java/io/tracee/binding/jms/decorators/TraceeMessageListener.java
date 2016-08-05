package io.tracee.binding.jms.decorators;


import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.binding.jms.TraceeJmsHelper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

class TraceeMessageListener implements MessageListener {

	private final MessageListener delegate;
	private final TraceeBackend backend;

	TraceeMessageListener(MessageListener delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public void onMessage(Message message) {
		try {
			try {
				backend.clear();
				TraceeJmsHelper.readTpicFromMessage(message);
			} catch (JMSException jmse) {  /*  */ }
			delegate.onMessage(message);
		} finally {
			backend.clear();
		}
	}


	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeMessageListener) {
			return delegate.equals(((TraceeMessageListener)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
