package com.prokarma.training.customer.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.prokarma.training.customer.consumer.converter.DefaultCustomerConsumerConverter;
import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

@Service
public class CustomerConsumer {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerConsumer.class);

	@Autowired
	private CustomerConsumerService customerService;

	@Autowired
	private DefaultCustomerConsumerConverter defaultCustomerConsumerConverter;

	@KafkaListener(topics = "${kaafka.topic}", groupId = "${kaafka.group-id}", containerFactory = "kafkaListenerContainerFactory")
	public void getTopicData(KafkaCustomerRequest kafkaCustomerRequest) {
		long startingTime = System.currentTimeMillis();

		defaultCustomerConsumerConverter.convert(kafkaCustomerRequest);

		LOG.info("Consumed Topic Data : {}", kafkaCustomerRequest);

		customerService.saveConsumedData(kafkaCustomerRequest);

		LOG.info("Consumer Service completed in : {} ms", System.currentTimeMillis() - startingTime);
	}
}
