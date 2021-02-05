package com.prokarma.training.customer.consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prokarma.training.customer.consumer.entity.AuditLog;
import com.prokarma.training.customer.consumer.helper.CustomerDataHelper;
import com.prokarma.training.customer.consumer.repository.AuditLogRepository;
import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

@ExtendWith(MockitoExtension.class)
class DefaultCustomerServiceTest {

	@InjectMocks
	private DefaultCustomerConsumerService defaultCustomerService;

	@Mock
	private AuditLogRepository auditLogRepository;

	@Test
	void testInvokeCustomerService() throws JsonProcessingException {
		DefaultCustomerConsumerService mockDefaultCustomerService = mock(DefaultCustomerConsumerService.class);
		when(auditLogRepository.save(Mockito.any(AuditLog.class))).thenReturn(getAuditLog());
		defaultCustomerService.saveConsumedData(CustomerDataHelper.getKafkaCustomerRequestData());

		doAnswer((i) -> {
			KafkaCustomerRequest customerRequest = i.getArgument(0);
			assertEquals("IN04852048", customerRequest.getCustomerNumber());
			return null;
		}).when(mockDefaultCustomerService).saveConsumedData(CustomerDataHelper.getKafkaCustomerRequestData());

		mockDefaultCustomerService.saveConsumedData(CustomerDataHelper.getKafkaCustomerRequestData());
		verify(mockDefaultCustomerService, times(1)).saveConsumedData(CustomerDataHelper.getKafkaCustomerRequestData());

	}

	private AuditLog getAuditLog() {
		AuditLog auditLog = new AuditLog();
		auditLog.setCustomerNumber("IN04852048");
		auditLog.setPayload(CustomerDataHelper.getKafkaCustomerRequestData());
		return auditLog;
	}

}
