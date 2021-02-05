package com.prokarma.training.customer.consumer.helper;

import com.prokarma.training.customer.kafka.domain.KafkaCustomerRequest;

public class CustomerDataHelper {

	public static KafkaCustomerRequest getKafkaCustomerRequestData() {

        KafkaCustomerRequest customerRequest = new KafkaCustomerRequest();
        customerRequest.setCustomerNumber("IN04852048");
        customerRequest.setFirstName("VijayKumar");
        customerRequest.setLastName("Sharma1234");
        customerRequest.setBirthdate("20-08-1991");
        customerRequest.setCountry("India");
        customerRequest.setCountryCode("IN");
        customerRequest.setMobileNumber("9398130143");
        customerRequest.setEmail("vijaysharma@gmail.com");
        customerRequest.setCustomerStatus("Open");
        customerRequest.setAddress("address");

		/*
		 * CustomerAddress address = new CustomerAddress();
		 * address.setAddressLine1("Address Line1");
		 * address.setAddressLine2("Address Line2"); address.setStreet("Street Name");
		 * address.setPostalCode("27459");
		 */
        return customerRequest;
    }

}
