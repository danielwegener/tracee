package io.tracee.spi;

import io.tracee.TraceeBackend;

/**
 * A TraceeBackendProvider is an
 * @since 1.0
 */
public interface TraceeBackendProvider {

	TraceeBackend provideBackend();

}
