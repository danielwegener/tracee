package io.tracee.camel;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.spi.RouteContext;

public class TraceeContextPropagationPolicy implements ContextPropagationPolicy {

	private final TraceeBackend backend;

	TraceeContextPropagationPolicy(TraceeBackend backend) {
		this.backend = backend;
	}

	@SuppressWarnings("unused")
	public TraceeContextPropagationPolicy() {
		this(Tracee.getBackend());
	}

	@Override
	public final void beforeWrap(RouteContext routeContext, ProcessorDefinition<?> definition) {

	}

	@Override
	public final Processor wrap(RouteContext routeContext, Processor processor) {
		return null;
	}
}
