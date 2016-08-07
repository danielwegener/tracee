package io.tracee.spring.autoconfigure;

import io.tracee.Tracee;
import io.tracee.spring.autoconfigure.jms.TraceeSpringJmsConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;

import javax.jms.ConnectionFactory;

/**
 * @since 2.0
 */
@Configuration
@ConditionalOnClass({Tracee.class, EnableJms.class})
@ConditionalOnBean(ConnectionFactory.class)
@AutoConfigureBefore(TraceeContextAutoConfiguration.class)
@AutoConfigureAfter(JmsAutoConfiguration.class)
@Import(TraceeSpringJmsConfiguration.class)
public class TraceeSpringJmsAutoConfiguration {}
