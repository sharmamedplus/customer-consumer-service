package com.prokarma.training.customer.consumer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.prokarma.training.customer.consumer.kafka.domain.KafkaCustomerRequest;
import com.vladmihalcea.hibernate.type.json.JsonStringType;

@Entity
@Table(name = "ERROR_LOG")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class ErrorLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "error_type")
	private String errorType;

	@Column(name = "error_description")
	private String errorDescription;

	@Type(type = "json")
	@Column(columnDefinition = "json")
	private KafkaCustomerRequest payload;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public KafkaCustomerRequest getPayload() {
		return payload;
	}

	public void setPayload(KafkaCustomerRequest payload) {
		this.payload = payload;
	}

}
