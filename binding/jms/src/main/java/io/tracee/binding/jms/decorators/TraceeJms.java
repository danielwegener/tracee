package io.tracee.binding.jms.decorators;



import io.tracee.TraceeBackend;

import javax.jms.*;

public final class TraceeJms {
	private TraceeJms() {}

	public static Connection wrapConnection(Connection connection, TraceeBackend backend) {
		if (connection == null) {
			return null;
		} else if (connection instanceof TraceeConnection) {
			return connection;
		} else {
			return new TraceeConnection(connection, backend);
		}
	}

	public static ConnectionConsumer wrapConnectionConsumer(ConnectionConsumer connectionConsumer, TraceeBackend backend) {
		if (connectionConsumer == null) {
			return null;
		} else if (connectionConsumer instanceof TraceeConnectionConsumer) {
			return connectionConsumer;
		} else {
			return new TraceeConnectionConsumer(connectionConsumer, backend);
		}
	}

	public static MessageConsumer wrapMessageConsumer(MessageConsumer messageConsumer, TraceeBackend backend) {
		if (messageConsumer == null) {
			return null;
		} else if (messageConsumer instanceof TraceeMessageConsumer) {
			return messageConsumer;
		} else {
			return new TraceeMessageConsumer(messageConsumer, backend);
		}
	}


	public static MessageListener wrapMessageListener(MessageListener messageListener, TraceeBackend backend) {
		if (messageListener == null) {
			return null;
		} else if (messageListener instanceof TraceeMessageListener) {
			return messageListener;
		} else {
			return new TraceeMessageListener(messageListener, backend);
		}
	}

	public static MessageProducer wrapMessageProducer (MessageProducer messageProducer, TraceeBackend backend) {
		if (messageProducer == null) {
			return null;
		} else if (messageProducer instanceof TraceeMessageProducer) {
			return messageProducer;
		} else {
			return new TraceeMessageProducer(messageProducer, backend);
		}
	}

	public static ServerSession wrapServerSession (ServerSession serverSession, TraceeBackend backend) {
		if (serverSession == null) {
			return null;
		} else if (serverSession instanceof TraceeServerSession) {
			return serverSession;
		} else {
			return new TraceeServerSession(serverSession, backend);
		}
	}

	public static ServerSessionPool wrapServerSessionPool (ServerSessionPool serverSessionPool, TraceeBackend backend) {
		if (serverSessionPool == null) {
			return null;
		} else if (serverSessionPool instanceof TraceeServerSessionPool) {
			return serverSessionPool;
		} else {
			return new TraceeServerSessionPool(serverSessionPool, backend);
		}
	}

	public static Session wrapServerSessionPool (Session session, TraceeBackend backend) {
		if (session == null) {
			return null;
		} else if (session instanceof TraceeSession) {
			return session;
		} else {
			return new TraceeSession(session, backend);
		}
	}

}
