package com.prokarma.training.customer.consumer.repository;

import org.springframework.data.repository.CrudRepository;

import com.prokarma.training.customer.consumer.entity.ErrorLog;

public interface ErrorLogRepository extends CrudRepository<ErrorLog, Long> {

}
