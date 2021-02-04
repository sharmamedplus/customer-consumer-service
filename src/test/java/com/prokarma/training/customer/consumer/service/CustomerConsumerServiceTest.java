package com.prokarma.training.customer.consumer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.prokarma.training.customer.consumer.converter.DefaultCustomerConsumerConverter;
import com.prokarma.training.customer.consumer.helper.CustomerDataHelper;
import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;

@EmbeddedKafka
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerConsumerServiceTest {
	private static final String TOPIC = "topic-customer-consumer-test";

	@InjectMocks
	private CustomerConsumer customerConsumer;

	@Mock
	private CustomerConsumerService customerConsumerService;

	@Mock
	private DefaultCustomerConsumerConverter defaultCustomerConsumerConverter;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	BlockingQueue<ConsumerRecord<String, String>> records;

	KafkaMessageListenerContainer<String, String> container;

	@BeforeAll
	void setUp() {
		Map<String, Object> configs = new HashMap<>(
				KafkaTestUtils.consumerProps("customer-consumer", "false", embeddedKafkaBroker));
		DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(configs,
				new StringDeserializer(), new StringDeserializer());
		ContainerProperties containerProperties = new ContainerProperties(TOPIC);
		container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
		records = new LinkedBlockingQueue<>();
		container.setupMessageListener((MessageListener<String, String>) records::add);
		container.start();
		ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
	}

	@AfterAll
	void tearDown() {
		container.stop();
	}

	@Test
	void testGetTopicsWhenEnsureSendMessageIsReceived() throws Exception {
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
		Producer<String, String> producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(),
				new StringSerializer()).createProducer();

		producer.send(new ProducerRecord<>(TOPIC, "customer-consumer", "{\"event\":\"Test Event\"}"));
		producer.flush();

		ConsumerRecord<String, String> singleRecord = records.poll(100, TimeUnit.MILLISECONDS);
		assertThat(singleRecord).isNotNull();
		assertThat(singleRecord.key()).isEqualTo("customer-consumer");
		assertThat(singleRecord.value()).isEqualTo("{\"event\":\"Test Event\"}");
	}

	@Test
	void testGetTopics() {

		CustomerConsumer mockCustomerConsumerService = mock(CustomerConsumer.class);

		doAnswer((i) -> {
			KafkaCustomerRequest customerRequest = i.getArgument(0);
			assertEquals("IN04852048", customerRequest.getCustomerNumber());
			return null;
		}).when(mockCustomerConsumerService).getTopicData(CustomerDataHelper.getKafkaCustomerRequestData());
		mockCustomerConsumerService.getTopicData(CustomerDataHelper.getKafkaCustomerRequestData());
		customerConsumer.getTopicData(CustomerDataHelper.getKafkaCustomerRequestData());
		verify(mockCustomerConsumerService, times(1)).getTopicData(CustomerDataHelper.getKafkaCustomerRequestData());

	}
}
