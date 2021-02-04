package com.prokarma.training.customer.consumer.config;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import com.prokarma.training.customer.consumer.exception.ApplicationRuntimeException;
import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;

@Configuration
public class CustomerConsumerConfig {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerConsumerConfig.class);
	
	@Value("${kafka.bootstrap-servers}")
	private String bootstrapServers;
	
	@Value("${kafka.auto.offset.reset}")
	private String autoOffsetReset;

	@Bean
	public ConsumerFactory<String, KafkaCustomerRequest> consumerFactory(){

		JsonDeserializer<KafkaCustomerRequest> deserializer = new JsonDeserializer<>(KafkaCustomerRequest.class);
		deserializer.setRemoveTypeHeaders(false);
		deserializer.addTrustedPackages("*");
		deserializer.setUseTypeMapperForKey(true);
		
	    Map<String, Object> config = new HashMap<>();

	    config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
	    config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
	    config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
	    config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
	    
	    return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), deserializer);
	}

	@Bean
	ConcurrentKafkaListenerContainerFactory<String, KafkaCustomerRequest> kafkaListenerContainerFactory() {
		
		ConcurrentKafkaListenerContainerFactory<String, KafkaCustomerRequest> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory()); 
	    
		factory.setRetryTemplate(retryTemplate());

		factory.setRecoveryCallback((context -> {

			if (!(context.getLastThrowable().getCause() instanceof RecoverableDataAccessException)) {
				
				// here you can log things and throw some custom exception that Error handler
				// will take care of ..
				throw new ApplicationRuntimeException(context.getLastThrowable().getMessage());
			}
			// here you can do your recovery mechanism where you can put back on to the
			// topic using a Kafka producer

			return null;

		}));

		factory.setErrorHandler(((exception, data) -> {
			/*
			 * here you can do you custom handling, I am just logging it same as default
			 * Error handler does If you just want to log. you need not configure the error
			 * handler here. The default handler does it for you. Generally, you will
			 * persist the failed records to DB for tracking the failed records.
			 */
			LOG.error("Error in process with Exception {} and the record is {}", exception, data);
		}));

		return factory;
	}

	private RetryTemplate retryTemplate() {

		RetryTemplate retryTemplate = new RetryTemplate();
		/*
		 * here retry policy is used to set the number of attempts to retry and what
		 * exceptions you wanted to try and what you don't want to retry.
		 */
		retryTemplate.setRetryPolicy(getSimpleRetryPolicy());

		return retryTemplate;
	}

	private SimpleRetryPolicy getSimpleRetryPolicy() {
		Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
		exceptionMap.put(IllegalArgumentException.class, false);
		exceptionMap.put(TimeoutException.class, true);
		exceptionMap.put(ApplicationRuntimeException.class, true);
		return new SimpleRetryPolicy(1, exceptionMap, true);
	}

}