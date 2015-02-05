package io.tracee.camel;

import org.apache.camel.AsyncCallback;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.camel.spi.InterceptStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceeTracer implements InterceptStrategy {

	private static final Logger LOG = LoggerFactory.getLogger(TraceeTracer.class);

	@Override
	public Processor wrapProcessorInInterceptors(final CamelContext context, final ProcessorDefinition<?> definition, final Processor target, final Processor nextTarget) throws Exception {
		return new DelegateAsyncProcessor(target) {
			@Override
			public boolean process(Exchange exchange, AsyncCallback callback) {
				LOG.info("enter process {}", definition.toString());
				final boolean r = super.process(exchange, callback);
				LOG.info("exit process {}", definition.toString());
				return r;
			}
		};
	}
}
