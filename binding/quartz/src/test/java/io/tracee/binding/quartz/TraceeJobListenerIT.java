package io.tracee.binding.quartz;

import io.tracee.TraceeConstants;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.testhelper.PermitAllTraceeFilterConfiguration;
import io.tracee.testhelper.SimpleTraceeBackend;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasKey;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

public class TraceeJobListenerIT {

	private static SimpleTraceeBackend jobBackend;

	private SimpleTraceeBackend schedulerBackend;

	private final TraceeFilterConfiguration filterConfiguration = PermitAllTraceeFilterConfiguration.INSTANCE;

	private Scheduler scheduler;

	@Before
	public void before() throws SchedulerException {
		jobBackend = spy(new SimpleTraceeBackend());
		schedulerBackend = spy(new SimpleTraceeBackend());
		scheduler = new StdSchedulerFactory().getScheduler();
		scheduler.getListenerManager().addJobListener(new TraceeJobListener(jobBackend, PermitAllTraceeFilterConfiguration.INSTANCE));
		scheduler.start();
	}

	@After
	public void after() throws SchedulerException {
		scheduler.shutdown();
		jobBackend.clear();
		schedulerBackend.clear();
	}

	@Test
	public void shouldRunJobWithNewGeneratedInvocationId() throws SchedulerException {
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		scheduler.scheduleJob(jobDetail, trigger);
		verify(jobBackend, timeout(1000)).put(eq(TraceeConstants.INVOCATION_ID_KEY), any(String.class));
	}

	@Test
	public void traceeContextShouldBePropagatedToJobByDecoratedTrigger() throws SchedulerException {
		schedulerBackend.put("testKey", "testValue");
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		new TraceeContextInjector(schedulerBackend, filterConfiguration).injectContext(trigger);
		scheduler.scheduleJob(jobDetail, trigger);
		verify(jobBackend, timeout(1000)).put(eq(TraceeConstants.INVOCATION_ID_KEY), any(String.class));
		assertThat(jobBackend.getValuesBeforeLastClear(), hasEntry("testKey", "testValue"));
	}

	@Test
	public void shouldResetBackendAfterJob() throws SchedulerException {
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		scheduler.scheduleJob(jobDetail, trigger);
		verify(jobBackend, timeout(1000)).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
		verify(jobBackend, timeout(1000)).clear();
	}

	@Test
	public void shouldResetBackendOnError() throws SchedulerException {
		final JobDetail jobDetail = JobBuilder.newJob(TestJob.class).build();
		final Trigger trigger = TriggerBuilder.newTrigger().forJob(jobDetail).startNow().build();
		trigger.getJobDataMap().put("throwException", true);
		scheduler.scheduleJob(jobDetail, trigger);
		verify(jobBackend, timeout(1000)).put(eq(TraceeConstants.INVOCATION_ID_KEY), anyString());
		verify(jobBackend, timeout(1000)).clear();
	}

	public static class TestJob implements Job {
		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
			if (context.getMergedJobDataMap().getBoolean("throwException")) {
				throw new IllegalStateException("Mööp!");
			}
			assertThat(jobBackend.copyToMap(), hasKey(TraceeConstants.INVOCATION_ID_KEY));
		}
	}
}
