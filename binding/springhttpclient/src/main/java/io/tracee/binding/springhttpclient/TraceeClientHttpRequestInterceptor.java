package io.tracee.binding.springhttpclient;

import io.tracee.Tracee;
import io.tracee.TraceeBackend;
import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Profile;
import io.tracee.transport.HttpHeaderTransport;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.tracee.configuration.TraceeFilterConfiguration.Channel.IncomingResponse;
import static io.tracee.configuration.TraceeFilterConfiguration.Channel.OutgoingRequest;

public final class TraceeClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

	private final TraceeBackend backend;
	private final HttpHeaderTransport transportSerialization;
	private final String profile;

	public TraceeClientHttpRequestInterceptor() {
		this(Tracee.getBackend(), new HttpHeaderTransport(), Profile.DEFAULT);
	}

	public TraceeClientHttpRequestInterceptor(String profile) {
		this(Tracee.getBackend(), new HttpHeaderTransport(), profile);
	}

	public TraceeClientHttpRequestInterceptor(TraceeBackend backend, HttpHeaderTransport transportSerialization, String profile) {
		this.backend = backend;
		this.transportSerialization = transportSerialization;
		this.profile = profile;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		preRequest(request);
		final ClientHttpResponse response = execution.execute(request, body);
		postResponse(response);
		return response;
	}

	private void preRequest(final HttpRequest request) {
		final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
		if (!backend.isEmpty() && filterConfiguration.shouldProcessContext(OutgoingRequest)) {
			final Map<String, String> filteredParams = filterConfiguration.filterDeniedParams(backend.copyToMap(), OutgoingRequest);
			for (Map.Entry<String, String> headerEntry : transportSerialization.render(filteredParams)) {
				request.getHeaders().add(headerEntry.getKey(), headerEntry.getValue());
			}
		}
	}

	private void postResponse(ClientHttpResponse response) {

		final List<Map.Entry<String,String>> headers = new ArrayList<>();
		for (Map.Entry<String, List<String>> headerEntry : response.getHeaders().entrySet()) {
			for (String headerValue : headerEntry.getValue()) {
				headers.add(new AbstractMap.SimpleImmutableEntry<>(headerEntry.getKey(), headerValue));
			}
		}

		if (headers != null) {
			final TraceeFilterConfiguration filterConfiguration = backend.getConfiguration(profile);
			if (filterConfiguration.shouldProcessContext(IncomingResponse)) {
				backend.putAll(filterConfiguration.filterDeniedParams(transportSerialization.parse(headers), IncomingResponse));
			}
		}
	}
}
