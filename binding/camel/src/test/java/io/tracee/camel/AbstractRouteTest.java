package io.tracee.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ErrorCollector;

public abstract class AbstractRouteTest {

	@Rule
	public final ErrorCollector checker = new ErrorCollector();

	protected CamelContext camelContext;
	protected ProducerTemplate template;

	abstract RoutesBuilder buildRoutes();

	@Before
	public void startServices() throws Exception {
		camelContext = new DefaultCamelContext();
		camelContext.disableJMX();
		camelContext.addRoutes(buildRoutes());
		camelContext.addInterceptStrategy(new TraceeTracer());
		template = camelContext.createProducerTemplate();
		camelContext.start();
	}

	@After
	public void stopServices() throws Exception {
		if (camelContext != null) {
			camelContext.stop();
		}
	}


}
