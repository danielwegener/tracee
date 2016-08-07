package io.tracee.spring.autoconfigure;

import io.tracee.configuration.TraceeFilterConfiguration;
import io.tracee.configuration.TraceeFilterConfiguration.Channel;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link ConfigurationProperties properties} for Tracee
 *
 * @since 2.0
 */
@ConfigurationProperties(prefix = "tracee")
public class TraceeProperties {

	/**
	 * The length of autogenerated TPIC session id.
	 */
	private int sessionIdLength = 32;

	/**
	 * The length of autogenerated TPIC invocation id.
	 */
	private int invocationIdLength = 32;

	/**
	 * Default configuration for allowed patterns for each Channel.
	 */
	private Map<Channel, Pattern> filter = new HashMap<>();

	/**
	 * Filter configuration for individual profiles.
	 */
	private Map<String, Profile> profile = new HashMap<>();

	public int getSessionIdLength() {
		return sessionIdLength;
	}

	public void setSessionIdLength(int sessionIdLength) {
		this.sessionIdLength = sessionIdLength;
	}

	public int getInvocationIdLength() {
		return invocationIdLength;
	}

	public void setInvocationIdLength(int invocationIdLength) {
		this.invocationIdLength = invocationIdLength;
	}

	public Map<Channel, Pattern> getFilter() {
		return filter;
	}

	public void setFilter(Map<Channel, Pattern> filter) {
		this.filter = filter;
	}

	public Map<String, Profile> getProfile() {
		return profile;
	}

	public void setProfile(Map<String, Profile> profile) {
		this.profile = profile;
	}

	private TraceeFilterConfiguration delegate = new TraceeFilterConfiguration() {
		@Override
		public boolean shouldProcessParam(String paramName, Channel channel) {
			return true;
		}

		@Override
		public Map<String, String> filterDeniedParams(Map<String, String> unfiltered, Channel channel) {
			return unfiltered;
		}

		@Override
		public boolean shouldProcessContext(Channel channel) {
			return true;
		}

		@Override
		public boolean shouldGenerateInvocationId() {
			return invocationIdLength > 0;
		}

		@Override
		public int generatedInvocationIdLength() {
			return invocationIdLength;
		}

		@Override
		public boolean shouldGenerateSessionId() {
			return sessionIdLength > 0;
		}

		@Override
		public int generatedSessionIdLength() {
			return sessionIdLength;
		}
	};

	public TraceeFilterConfiguration getAsFilterConfiguration() {
		return delegate;
	}


	public static class Profile {

		/**
		 * Filter configuration for allowed patterns for each Channel.
		 */
		private Map<Channel, Pattern> filter;

		public Map<Channel, Pattern> getFilter() {
			return filter;
		}

		public void setFilter(Map<Channel, Pattern> filter) {
			this.filter = filter;
		}
	}

}
