package io.tracee.binding.quartz;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.Utilities;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobListenerSupport;

public class TraceeJobListener extends JobListenerSupport {

	private final TraceeBackend backend;

	public TraceeJobListener() {
		backend = Tracee.getBackend();
	}

	TraceeJobListener(final TraceeBackend backend) {
		this.backend = backend;
	}

	@Override
	public String getName() {
		return "TracEE job listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		Utilities.generateRequestIdIfNecessary(backend);
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		backend.clear();
	}
}
