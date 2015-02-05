package io.tracee.camel;

import io.tracee.Tracee;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.impl.converter.AsyncProcessorTypeConverter;
import org.junit.Test;

import static org.hamcrest.Matchers.hasEntry;

public class AsynchronousRouteTest extends AbstractRouteTest {



	public class TraceeContextSetterProcessor implements Processor {
		@Override
		public void process(Exchange exchange) throws Exception {
			Tracee.getBackend().put("foo","bar");
		}
	}

	public class TraceeContextGetterProcessor implements Processor {
		@Override
		public void process(Exchange exchange) throws Exception {

			checker.checkThat(Tracee.getBackend().copyToMap(), hasEntry("foo", "bar"));
		}
	}

	@Override
	RoutesBuilder buildRoutes() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("direct:start")
						.inOnly()
						.threads(2)
						.process(AsyncProcessorTypeConverter.convert(new TraceeContextSetterProcessor()))
						.threads(2)
						.process(AsyncProcessorTypeConverter.convert(new TraceeContextSetterProcessor()))
						.to("mock:end")
						.end();
			}
		};
	}

	@Test
	public void testStartToEnd() throws Exception {
		final MockEndpoint end = MockEndpoint.resolve(camelContext, "mock:end");

		final Exchange exchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		template.send("direct:start", exchange);

		end.expectedMessageCount(1);
		end.assertIsSatisfied();




	}
}
