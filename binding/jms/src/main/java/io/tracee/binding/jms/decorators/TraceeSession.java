package io.tracee.binding.jms.decorators;


import io.tracee.TraceeBackend;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.StreamMessage;
import javax.jms.TemporaryQueue;
import javax.jms.TemporaryTopic;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicSubscriber;
import java.io.Serializable;

final class TraceeSession implements Session {

	private final Session delegate;
	private final TraceeBackend backend;

	TraceeSession(Session delegate, TraceeBackend backend) {
		this.delegate = delegate;
		this.backend = backend;
	}

	@Override
	public BytesMessage createBytesMessage() throws JMSException {
		return delegate.createBytesMessage();
	}

	@Override
	public MapMessage createMapMessage() throws JMSException {
		return delegate.createMapMessage();
	}

	@Override
	public Message createMessage() throws JMSException {
		return delegate.createMessage();
	}

	@Override
	public ObjectMessage createObjectMessage() throws JMSException {
		return delegate.createObjectMessage();
	}

	@Override
	public ObjectMessage createObjectMessage(Serializable serializable) throws JMSException {
		return delegate.createObjectMessage(serializable);
	}

	@Override
	public StreamMessage createStreamMessage() throws JMSException {
		return delegate.createStreamMessage();
	}

	@Override
	public TextMessage createTextMessage() throws JMSException {
		return delegate.createTextMessage();
	}

	@Override
	public TextMessage createTextMessage(String s) throws JMSException {
		return delegate.createTextMessage(s);
	}

	@Override
	public boolean getTransacted() throws JMSException {
		return delegate.getTransacted();
	}

	@Override
	public int getAcknowledgeMode() throws JMSException {
		return delegate.getAcknowledgeMode();
	}

	@Override
	public void commit() throws JMSException {
		delegate.commit();
	}

	@Override
	public void rollback() throws JMSException {
		delegate.rollback();
	}

	@Override
	public void close() throws JMSException {
		delegate.close();
	}

	@Override
	public void recover() throws JMSException {
		delegate.recover();
	}

	@Override
	public MessageListener getMessageListener() throws JMSException {
		return delegate.getMessageListener();
	}

	@Override
	public void setMessageListener(MessageListener messageListener) throws JMSException {
		delegate.setMessageListener(messageListener);
	}

	@Override
	public void run() {
		delegate.run();
	}

	@Override
	public TraceeMessageProducer createProducer(Destination destination) throws JMSException {
		return new TraceeMessageProducer(delegate.createProducer(destination), backend);
	}

	@Override
	public TraceeMessageConsumer createConsumer(Destination destination) throws JMSException {
		return new TraceeMessageConsumer(delegate.createConsumer(destination), backend);
	}

	@Override
	public TraceeMessageConsumer createConsumer(Destination destination, String s) throws JMSException {
		return new TraceeMessageConsumer(delegate.createConsumer(destination, s), backend);
	}

	@Override
	public MessageConsumer createConsumer(Destination destination, String s, boolean b) throws JMSException {
		return new TraceeMessageConsumer(delegate.createConsumer(destination, s, b), backend);
	}

	@Override
	public Queue createQueue(String s) throws JMSException {
		return delegate.createQueue(s);
	}

	@Override
	public Topic createTopic(String s) throws JMSException {
		return delegate.createTopic(s);
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String s) throws JMSException {
		return delegate.createDurableSubscriber(topic, s);
	}

	@Override
	public TopicSubscriber createDurableSubscriber(Topic topic, String s, String s1, boolean b) throws JMSException {
		return delegate.createDurableSubscriber(topic, s, s1, b);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue) throws JMSException {
		return delegate.createBrowser(queue);
	}

	@Override
	public QueueBrowser createBrowser(Queue queue, String s) throws JMSException {
		return delegate.createBrowser(queue, s);
	}

	@Override
	public TemporaryQueue createTemporaryQueue() throws JMSException {
		return delegate.createTemporaryQueue();
	}

	@Override
	public TemporaryTopic createTemporaryTopic() throws JMSException {
		return delegate.createTemporaryTopic();
	}

	@Override
	public void unsubscribe(String s) throws JMSException {
		delegate.unsubscribe(s);
	}

	@Override
	public int hashCode() {
		return delegate.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TraceeSession) {
			return delegate.equals(((TraceeSession)obj).delegate);
		}
		return delegate.equals(obj);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}

}
