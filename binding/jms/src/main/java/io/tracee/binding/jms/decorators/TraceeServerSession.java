package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;

import javax.jms.JMSException;
import javax.jms.ServerSession;

class TraceeServerSession implements ServerSession {

	private final ServerSession delegate;
	private final TraceeBackend backend;

	public TraceeServerSession(ServerSession delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public TraceeSession getSession() throws JMSException {
		return new TraceeSession(delegate.getSession(), backend);
	}

	@Override
	public void start() throws JMSException {
		delegate.start();
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeServerSession) {
			return delegate.equals(((TraceeServerSession)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
