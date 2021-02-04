package com.prokarma.training.customer.consumer.converter;

public interface CustomerConsumerConverter<I,O> {
	O convert(I input);
}
