package io.tracee.spi;

import java.util.Map;

/**
 * TracEE SPI Encoder that is capable of encoding a Tracee-
 *
 * @since 2.0
 */
public interface TraceeHttpHeaderEncoder {


	/**
	 * Parses a TracEE context from a given sequence of HTTP-Headers.
	 * @param httpHeaders A sequence of HTTP header keys and values.
	 * @return a TracEE context map.
	 */
	Map<String,String> parse(Iterable<Map.Entry<String,String>> httpHeaders);

	/**
	 * Serialize an context as sequence of HTTP-Header values.
	 * The returned sequence of tuples MUST only contain entries with correctly encoded HTTP-Header keys and values.
	 *
	 * @param context an existing TracEE context. This map must not be modified.
	 * @return a sequence of http headers.
	 */
	Iterable<Map.Entry<String,String>> render(Map<String,String> context);

}
