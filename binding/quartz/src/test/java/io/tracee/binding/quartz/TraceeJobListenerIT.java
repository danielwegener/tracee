package io.tracee.binding.quartz;

import io.tracee.SimpleTraceeBackend;
import io.tracee.TraceeBackend;
import io.tracee.TraceeConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class TraceeJobListenerIT {

	private static TraceeBackend backend;

	private Scheduler scheduler;

	@Before
	public void before() throws SchedulerException {
		backend = spy(SimpleTraceeBackend.createNonLoggingAllPermittingBackend());
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getListenerManager().addJobListener(new TraceeJobListener(backend));
		scheduler.start();
	}

	@After
	public void after() throws SchedulerException {
		scheduler.shutdown();
	}

	@Test
	public void shouldRunJobWithRequestId() throws SchedulerException, InterruptedException {
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		final HashMap<String, String> traceeContext = new HashMap<String, String>();
		trigger.getJobDataMap().put("TPIC_TEST", traceeContext);
		scheduler.scheduleJob(jobDetail, trigger);
		Thread.sleep(500L);
		assertThat(traceeContext, hasKey(TraceeConstants.REQUEST_ID_KEY));
	}

	@Test
	public void shouldResetBackendAfterJob() throws SchedulerException, InterruptedException {
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		scheduler.scheduleJob(jobDetail, trigger);
		Thread.sleep(500L);
		verify(backend).put(eq(TraceeConstants.REQUEST_ID_KEY), anyString());
		verify(backend).clear();
	}

	public static class TestJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			@SuppressWarnings("unchecked")
			final Map<String, String> traceeContext = (Map<String, String>) context.getTrigger().getJobDataMap().get("TPIC_TEST");
			traceeContext.putAll(backend.copyToMap());
			assertThat(backend.copyToMap(), hasKey(TraceeConstants.REQUEST_ID_KEY));
		}
	}
}
