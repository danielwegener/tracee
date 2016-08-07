package io.tracee.binding.jms;


import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

public class TraceeConnectionFactory implements ConnectionFactory {

	private final ConnectionFactory delegate;

	public TraceeConnectionFactory(ConnectionFactory delegate) {
		this.delegate = delegate;
	}

	@Override
	public Connection createConnection() throws JMSException {
		//TODO: proxy to TraceeConnection
		return delegate.createConnection();
	}

	@Override
	public Connection createConnection(String userName, String password) throws JMSException {
		//TODO: proxy to TraceeConnection
		return delegate.createConnection();
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeConnectionFactory) {
			return delegate.equals(((TraceeConnectionFactory) obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
