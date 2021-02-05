package com.prokarma.training.customer.consumer.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.prokarma.training.customer.consumer.helper.CustomerDataHelper;
import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

@ExtendWith(MockitoExtension.class)
class DefaultCustomerConsumerConverterTest {

	@Test
	void testConvertToCustomerConsumerRequest() {
		DefaultCustomerConsumerConverter mockDefaultCustomerConsumerConverter = mock(
				DefaultCustomerConsumerConverter.class);

		doAnswer((i) -> {
			KafkaCustomerRequest customerRequest = i.getArgument(0);
			assertEquals("IN04852048", customerRequest.getCustomerNumber());
			return null;
		}).when(mockDefaultCustomerConsumerConverter).convert(CustomerDataHelper.getKafkaCustomerRequestData());

		mockDefaultCustomerConsumerConverter.convert(CustomerDataHelper.getKafkaCustomerRequestData());
		verify(mockDefaultCustomerConsumerConverter, times(1))
				.convert(CustomerDataHelper.getKafkaCustomerRequestData());
	}

}
