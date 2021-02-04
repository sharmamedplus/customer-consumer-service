package com.prokarma.training.customer.consumer.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.prokarma.training.customer.consumer.entity.ErrorLog;
import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;
import com.prokarma.training.customer.consumer.repository.ErrorLogRepository;

@Aspect
@Component
public class CustomerConsumerServiceAspect {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerConsumerServiceAspect.class);

	@Autowired
	private ErrorLogRepository errorLogRepository;

	@Pointcut(value = "execution(* com.prokarma.training.customer.consumer.service.DefaultCustomerConsumerService.saveConsumedData(..))")
	public void saveConsumedData() {
		// This is pointcut method.
	}

	@AfterThrowing(pointcut = "saveConsumedData()", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		LOG.error("Exception occured while consuming data : {}", ex.getMessage(), ex);

		KafkaCustomerRequest customer = (KafkaCustomerRequest) joinPoint.getArgs()[0];
		ErrorLog errorLog = new ErrorLog();
		errorLog.setPayload(customer);
		errorLog.setErrorType(ex.getClass().getName());
		errorLog.setErrorDescription(ex.getMessage());
		long startTime = System.currentTimeMillis();
		errorLogRepository.save(errorLog);

		LOG.info("Error Log information saved time : {} ms", System.currentTimeMillis() - startTime);
	}
}
