package com.prokarma.training.customer.consumer.converter;

import org.springframework.stereotype.Component;

import com.prokarma.training.customer.consumer.constant.CustomerMaskConstant;
import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

@Component
public class DefaultCustomerConsumerConverter
		implements CustomerConsumerConverter<KafkaCustomerRequest, KafkaCustomerRequest> {

	@Override
	public KafkaCustomerRequest convert(KafkaCustomerRequest kafkaCustomerRequest) {

		String replacementCharacter = CustomerMaskConstant.REPLACEMENT_CHARACTER.getValue();

		kafkaCustomerRequest.setCustomerNumber(kafkaCustomerRequest.getCustomerNumber()
				.replaceAll(CustomerMaskConstant.CUSTOMER_NUMBER_REGEX.getValue(), replacementCharacter));

		kafkaCustomerRequest
				.setEmail(kafkaCustomerRequest.getEmail().replaceFirst(CustomerMaskConstant.EMAIL_REGEX.getValue(),
						CustomerMaskConstant.EMAIL_REPLACEMENT_CHARACTER.getValue()));

		kafkaCustomerRequest.setBirthdate(kafkaCustomerRequest.getBirthdate()
				.replaceAll(CustomerMaskConstant.BIRTHDATE_REGEX.getValue(), replacementCharacter));

		return kafkaCustomerRequest;
	}

}
