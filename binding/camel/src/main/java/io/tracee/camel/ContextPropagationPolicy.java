package io.tracee.camel;

import org.apache.camel.spi.Policy;

/**
 * A marker for defining the policy to be used for applying Tracee context propagation to routes.
 */
public interface ContextPropagationPolicy extends Policy {


}
