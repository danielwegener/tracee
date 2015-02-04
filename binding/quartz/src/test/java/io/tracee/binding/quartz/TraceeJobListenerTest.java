package io.tracee.binding.quartz;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TraceeJobListenerTest {

	private TraceeBackend backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());

	private final TraceeJobListener unit = new TraceeJobListener(backend);

	@Test
	public void generateRequestIdWhenJobStarts() {
		unit.jobToBeExecuted(mock(JobExecutionContext.class));
		assertThat(backend.copyToMap(), Matchers.hasKey(TraceeConstants.REQUEST_ID_KEY));
		verify(backend).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
	}

	@Test
	public void resetBackendWhenJobHasFinished() {
		unit.jobWasExecuted(mock(JobExecutionContext.class), mock(JobExecutionException.class));
		verify(backend).clear();
	}
}
