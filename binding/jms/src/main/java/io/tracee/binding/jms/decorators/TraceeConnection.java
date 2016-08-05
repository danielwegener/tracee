package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;

import javax.jms.Connection;
import javax.jms.ConnectionMetaData;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.ServerSessionPool;
import javax.jms.Topic;

public final class TraceeConnection implements Connection {


	private final Connection delegate;
	private final TraceeBackend backend;

	public TraceeConnection(Connection delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public TraceeSession createSession(boolean b, int i) throws JMSException {
		return new TraceeSession(delegate.createSession(b, i), backend);
	}

	@Override
	public String getClientID() throws JMSException {
		return delegate.getClientID();
	}

	@Override
	public void setClientID(String s) throws JMSException {
		delegate.setClientID(s);

	}

	@Override
	public ConnectionMetaData getMetaData() throws JMSException {
		return delegate.getMetaData();
	}

	@Override
	public ExceptionListener getExceptionListener() throws JMSException {
		return delegate.getExceptionListener();
	}

	@Override
	public void setExceptionListener(ExceptionListener exceptionListener) throws JMSException {
		delegate.setExceptionListener(exceptionListener);

	}

	@Override
	public void start() throws JMSException {
		delegate.start();

	}

	@Override
	public void stop() throws JMSException {
		delegate.stop();

	}

	@Override
	public void close() throws JMSException {
		delegate.close();

	}

	@Override
	public TraceeConnectionConsumer createConnectionConsumer(Destination destination, String s, ServerSessionPool serverSessionPool, int i) throws JMSException {
		return new TraceeConnectionConsumer(delegate.createConnectionConsumer(destination, s, serverSessionPool, i), backend);
	}

	@Override
	public TraceeConnectionConsumer createDurableConnectionConsumer(Topic topic, String s, String s1, ServerSessionPool serverSessionPool, int i) throws JMSException {
		return new TraceeConnectionConsumer(delegate.createDurableConnectionConsumer(topic, s, s1, serverSessionPool, i), backend);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeConnection) {
			return delegate.equals(((TraceeConnection)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
