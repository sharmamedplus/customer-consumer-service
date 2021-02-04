package com.prokarma.training.customer.consumer.repository;

import org.springframework.data.repository.CrudRepository;

import com.prokarma.training.customer.consumer.entity.AuditLog;

public interface AuditLogRepository extends CrudRepository<AuditLog, Long> {

}
