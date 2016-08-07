package io.tracee.binding.jaxrs2;

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
import org.mockito.Mockito;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

public class TraceeClientFilterRequestTest {

	private final TraceeBackend traceeBackend = new SimpleTraceeBackend();
	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;
	private final TraceeClientFilter unit = new TraceeClientFilter(traceeBackend, filterConfiguration);
	private final ClientRequestContext clientRequestContext = Mockito.mock(ClientRequestContext.class);

	@Before
	public void setUp() {
		final MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		when(clientRequestContext.getHeaders()).thenReturn(headers);
	}

	@Test
	public void testFilterWritesBackendToRequestContextHeaders() throws IOException {
		traceeBackend.put("foo", "bar");
		unit.filter(clientRequestContext);
		assertThat((String) clientRequestContext.getHeaders().getFirst(TraceeConstants.TPIC_HEADER),
				containsString("foo=bar"));
	}

	@Test
	public void doNotCreateInvocationIdOnOutgoingRequest() throws IOException {
		unit.filter(clientRequestContext);
		assertThat(traceeBackend.get(TraceeConstants.INVOCATION_ID_KEY), isEmptyOrNullString());
		assertThat(clientRequestContext.getHeaders().getFirst(TraceeConstants.TPIC_HEADER),
				is(nullValue()));
	}

	@Test
	public void testFilterKeepsExistingInvocationId() throws IOException {
		traceeBackend.put(TraceeConstants.INVOCATION_ID_KEY, "foo");
		unit.filter(clientRequestContext);
		assertThat(traceeBackend.get(TraceeConstants.INVOCATION_ID_KEY), equalTo("foo"));
		assertThat((String) clientRequestContext.getHeaders().getFirst(TraceeConstants.TPIC_HEADER),
				containsString(TraceeConstants.INVOCATION_ID_KEY + "=foo"));
	}

	@Test
	public void defaultConstructorUsesDefaultProfile() {
		final TraceeClientFilter clientFilter = new TraceeClientFilter();
		MatcherAssert.assertThat((TraceeBackend) FieldAccessUtil.getFieldVal(clientFilter, "backend"), is(Tracee.getBackend()));
	}
}
