package com.prokarma.training.customer.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prokarma.training.customer.consumer.entity.AuditLog;
import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;
import com.prokarma.training.customer.consumer.repository.AuditLogRepository;

@Service
public class DefaultCustomerConsumerService implements CustomerConsumerService {

	private static final Logger LOG = LoggerFactory.getLogger(DefaultCustomerConsumerService.class);

	@Autowired
	private AuditLogRepository auditLogRepository;

	@Override
	public void saveConsumedData(KafkaCustomerRequest kafkaCustomerRequest) {

		long startingTime = System.currentTimeMillis();

		AuditLog auditLog = new AuditLog();
		auditLog.setCustomerNumber(kafkaCustomerRequest.getCustomerNumber());
		auditLog.setPayload(kafkaCustomerRequest);
		auditLogRepository.save(auditLog);
		
		LOG.info("Audit data saved time : {} ms", System.currentTimeMillis() - startingTime);
	}
}
