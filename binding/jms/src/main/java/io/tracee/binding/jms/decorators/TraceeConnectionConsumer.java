package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;

import javax.jms.ConnectionConsumer;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;

public class TraceeConnectionConsumer implements ConnectionConsumer {


	private final ConnectionConsumer delegate;
	private final TraceeBackend backend;

	public TraceeConnectionConsumer(ConnectionConsumer delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public ServerSessionPool getServerSessionPool() throws JMSException {
		return delegate.getServerSessionPool();
	}

	@Override
	public void close() throws JMSException {
		delegate.close();
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeConnectionConsumer) {
			return delegate.equals(((TraceeConnectionConsumer)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
