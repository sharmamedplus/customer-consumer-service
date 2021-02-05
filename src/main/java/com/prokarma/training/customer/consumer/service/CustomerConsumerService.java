package com.prokarma.training.customer.consumer.service;

import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

public interface CustomerConsumerService {

	public void saveConsumedData(KafkaCustomerRequest kafkaCustomerRequest);
}
