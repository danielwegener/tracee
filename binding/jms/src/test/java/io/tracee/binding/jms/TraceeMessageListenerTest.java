package io.tracee.binding.jms;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.testhelper.FieldAccessUtil;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ejb.EJB;
import javax.interceptor.InvocationContext;
import javax.jms.Message;
import java.util.HashMap;
import java.util.Map;

import static io.tracee.TraceeConstants.INVOCATION_ID_KEY;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class TraceeMessageListenerTest {

	private final SimpleTraceeBackend backend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;
	private final TraceeMessageListener unit = new TraceeMessageListener(backend, filterConfiguration);
	private final InvocationContext invocationContext = mock(InvocationContext.class);
	private final Message message = mock(Message.class);
	private final Map<String, String> encodedContext = new HashMap<>();
	private final MdbLike mdbLike = spy(new MdbLike());

	@Before
	public void setupMocks() throws Exception {
		when(invocationContext.getMethod()).thenReturn(MdbLike.class.getMethod("onMessage", Message.class));
		when(invocationContext.getParameters()).thenReturn(new Message[]{message});
		when(invocationContext.proceed()).then(new Answer<Object>() {
			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				mdbLike.onMessage(message);
				return null;
			}
		});
		when(message.getObjectProperty(TraceeConstants.TPIC_HEADER)).thenReturn(encodedContext);
	}

	@Test
	public void testDecodesFromMessage() throws Exception {
		encodedContext.put("contextFromMessage", "yes");
		encodedContext.put(INVOCATION_ID_KEY, "an invocationId");
		unit.intercept(invocationContext);
		assertThat(backend.getValuesBeforeLastClear(), hasEntry(INVOCATION_ID_KEY, "an invocationId"));
		assertThat(backend.getValuesBeforeLastClear(), hasEntry("contextFromMessage", "yes"));
		verify(mdbLike).onMessage(message);
	}

	@Test
	public void testCleansUpContextAfterProcessing() throws Exception {
		encodedContext.put("contextFromMessage", "yes");
		unit.intercept(invocationContext);
		assertThat(backend.isEmpty(), is(true));
		verify(mdbLike).onMessage(message);
	}

	@Test
	public void testInvocationIdGeneratedIfAbsent() throws Exception {
		encodedContext.put("contextFromMessage", "yes");
		unit.intercept(invocationContext);
		assertThat(backend.getValuesBeforeLastClear(), hasEntry(is(INVOCATION_ID_KEY), notNullValue()));
		verify(mdbLike).onMessage(message);
	}

	@EJB
	private class MdbLike {
		public void onMessage(Message message) {
			assertThat(backend.get("contextFromMessage"), is("yes"));
		}
	}

	@Test
	public void defaultConstructorUsesTraceeBackend() {
		final TraceeMessageListener listener = new TraceeMessageListener();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(listener, "backend"), is(Tracee.getBackend()));
	}
}
