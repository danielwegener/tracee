package io.tracee.spring.autoconfigure.jms;


import io.tracee.binding.jms.TraceeConnectionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import javax.jms.ConnectionFactory;

/**
 * Proxies jms ConnectionFactories with {@link TraceeConnectionFactory} to get a deep proxy of the JMS api in
 * order to inject send, receive and listener hooks.
 * <p>
 * //TODO move to its own tracee-springjms package so it can be used without spring-boot.
 *
 * @since 2.0
 */
@Configuration
public class TraceeSpringJmsConfiguration {

	@Bean
	@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
	BeanPostProcessor traceeJmsBeanPostProcessor() {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
				return bean;
			}

			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				if (bean instanceof TraceeConnectionFactory) {
					return bean;
				} else if (bean instanceof ConnectionFactory) {
					//TODO: pass backend and filterConfiguration to TraceeConnectionFactory
					return new TraceeConnectionFactory((ConnectionFactory) bean);
				} else {
					return bean;
				}
			}
		};
	}

}
