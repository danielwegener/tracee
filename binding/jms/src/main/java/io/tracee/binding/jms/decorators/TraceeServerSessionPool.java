package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;

import javax.jms.JMSException;
import javax.jms.ServerSessionPool;

public class TraceeServerSessionPool implements ServerSessionPool {

	private final ServerSessionPool delegate;
	private final TraceeBackend backend;

	public TraceeServerSessionPool(ServerSessionPool delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public TraceeServerSession getServerSession() throws JMSException {
		return new TraceeServerSession(delegate.getServerSession(), backend);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeServerSessionPool) {
			return delegate.equals(((TraceeServerSessionPool)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
