package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;
import io.tracee.binding.jms.TraceeJmsHelper;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;

final class TraceeMessageProducer implements MessageProducer {

	private final MessageProducer delegate;
	private final TraceeBackend backend;

	TraceeMessageProducer(MessageProducer delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}


	@Override
	public void setDisableMessageID(boolean b) throws JMSException {
		delegate.setDisableMessageID(b);

	}

	@Override
	public boolean getDisableMessageID() throws JMSException {
		return delegate.getDisableMessageID();
	}

	@Override
	public void setDisableMessageTimestamp(boolean b) throws JMSException {
		delegate.setDisableMessageTimestamp(b);

	}

	@Override
	public boolean getDisableMessageTimestamp() throws JMSException {
		return delegate.getDisableMessageTimestamp();
	}

	@Override
	public void setDeliveryMode(int i) throws JMSException {
		delegate.setDeliveryMode(i);

	}

	@Override
	public int getDeliveryMode() throws JMSException {
		return delegate.getDeliveryMode();
	}

	@Override
	public void setPriority(int i) throws JMSException {
		delegate.setPriority(i);

	}

	@Override
	public int getPriority() throws JMSException {
		return delegate.getPriority();
	}

	@Override
	public void setTimeToLive(long l) throws JMSException {
		delegate.setTimeToLive(l);

	}

	@Override
	public long getTimeToLive() throws JMSException {
		return delegate.getTimeToLive();
	}

	@Override
	public Destination getDestination() throws JMSException {
		return delegate.getDestination();
	}

	@Override
	public void close() throws JMSException {
		delegate.close();

	}

	@Override
	public void send(Message message) throws JMSException {
		TraceeJmsHelper.writeTpicToMessage(message, backend);
		delegate.send(message);
	}

	@Override
	public void send(Message message, int i, int i1, long l) throws JMSException {
		TraceeJmsHelper.writeTpicToMessage(message, backend);
		delegate.send(message,i, i1, l);

	}

	@Override
	public void send(Destination destination, Message message) throws JMSException {
		TraceeJmsHelper.writeTpicToMessage(message, backend);
		delegate.send(destination, message);
	}

	@Override
	public void send(Destination destination, Message message, int i, int i1, long l) throws JMSException {
		TraceeJmsHelper.writeTpicToMessage(message, backend);
		delegate.send(destination, message, i, i1, l);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeMessageProducer) {
			return delegate.equals(((TraceeMessageProducer)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
