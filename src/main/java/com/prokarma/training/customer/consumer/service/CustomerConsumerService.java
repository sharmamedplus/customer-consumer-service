package com.prokarma.training.customer.consumer.service;

import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;

public interface CustomerConsumerService {

	public void saveConsumedData(KafkaCustomerRequest kafkaCustomerRequest);
}
