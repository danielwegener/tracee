package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import io.tracee.configuration.TraceeFilterConfiguration;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

public class TraceeJobListener extends JobListenerSupport {

	/**
	 * Property that can be configured to pass the quartz job key into the tracee context.
	 * Set it to {@code true} to enable job key propagation. Defaults to {@code false}
	 */
	public static final String PROPAGATE_JOB_KEY = "binding.quartz.PropagateJobKey";

	/**
	 *
	 */
	public static final String JOB_KEY = "jobKey";

	private final TraceeBackend backend;
	private final String profile;

	public TraceeJobListener() {
		this(Tracee.getBackend(), null);
	}

	TraceeJobListener(final TraceeBackend backend) {
		this(backend, null);
	}

	TraceeJobListener(final TraceeBackend backend, final String profile) {
		this.backend = backend;
		this.profile = profile;
	}

	@Override
	public String getName() {
		return "TracEE job listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		if (backend.getConfiguration(profile).shouldProcessContext(TraceeFilterConfiguration.Channel.AsyncProcess)) {
			Utilities.generateRequestIdIfNecessary(backend);
			if (backend.getConfiguration(profile).getBoolean(PROPAGATE_JOB_KEY)) {
				backend.put(JOB_KEY, context.getJobDetail().getKey().toString());
			}
		}
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		backend.clear();
	}
}
