package io.tracee.binding.jms;

import io.tracee.TraceeBackend;
import io.tracee.binding.jms.decorators.TraceeConnection;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class TraceeConnectionFactoryAdapter implements ConnectionFactory {

	private final ConnectionFactory delegate;
	private final TraceeBackend backend;

	public TraceeConnectionFactoryAdapter(ConnectionFactory delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public Connection createConnection() throws JMSException {
		return new TraceeConnection(delegate.createConnection(), backend);
	}

	@Override
	public Connection createConnection(String s, String s1) throws JMSException {
		return new TraceeConnection(delegate.createConnection(s, s1), backend);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeConnectionFactoryAdapter) {
			return delegate.equals(((TraceeConnectionFactoryAdapter)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
