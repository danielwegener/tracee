package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;
import io.tracee.binding.jms.TraceeJmsHelper;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;

final class TraceeMessageConsumer implements MessageConsumer {

	private final MessageConsumer delegate;
	private final TraceeBackend backend;

	TraceeMessageConsumer(MessageConsumer delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}


	@Override
	public String getMessageSelector() throws JMSException {
		return delegate.getMessageSelector();
	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		return delegate.getMessageListener();
	}

	@Override
	public void setMessageListener(MessageListener messageListener) throws JMSException {
		delegate.setMessageListener(new TraceeMessageListener(messageListener, backend));
	}

	@Override
	public Message receive() throws JMSException {
		final Message receive = delegate.receive();
		TraceeJmsHelper.readTpicFromMessage(receive, backend);
		return receive;
	}

	@Override
	public Message receive(long l) throws JMSException {
		final Message receive = delegate.receive();
		TraceeJmsHelper.readTpicFromMessage(receive, backend);
		return receive;
	}

	@Override
	public Message receiveNoWait() throws JMSException {
		final Message receive = delegate.receiveNoWait();
		TraceeJmsHelper.readTpicFromMessage(receive, backend);
		return receive;
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
		if (obj instanceof TraceeMessageConsumer) {
			return delegate.equals(((TraceeMessageConsumer)obj).delegate);
		}
		return delegate.equals(obj);
	}
	@Override
	public String toString() {
		return delegate.toString();
	}

}
