package com.bootcamp.project.bootcoinoperation.service;

import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationDTO;
import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class Consumer {

	@Autowired
	private MongoTemplate mongoTemplate;

	@KafkaListener(topics="mybootcointopic", groupId="mygroup")
	public void consumeFromTopic(BootcoinOperationDTO operationDTO) {
		BootcoinOperationEntity entity = addOperation(operationDTO);
		System.out.println("Operation created.");

	}
	/*
	public BootcoinOperationEntity initialValidationsF(BootcoinOperationEntity entity)
	{
		String validationResult;
		Boolean validated = false;
		Query query = new Query();
		Update update = new Update();
		query.addCriteria(Criteria.where("operationNumber").is(entity.getOperationNumber()));
		if(entity.getPaymentMethod().toUpperCase() != null && (entity.getPaymentMethod().equals("YANKI") ||entity.getPaymentMethod().equals("TRANSFER"))
		&& entity.getAmount() > 0)
		{
			validationResult = "INITIAL VALIDATIONS COMPLETED";
		}
		else
		{
			validated = true;
			validationResult = "REJECTED - INVALID PAYMENT METHOD OR AMOUNT REQUESTED (MUST BE > 0).";
		}
		update.set("validated",validated);
		update.set("status",validationResult);
		return mongoTemplate.findAndModify(query, update, BootcoinOperationEntity.class);
	}
	 */
	public BootcoinOperationEntity addOperation(BootcoinOperationDTO operationDTO) {
		BootcoinOperationEntity entity = new BootcoinOperationEntity();
		entity.setPetitionerDocumentNumber(operationDTO.getPetitionerDocumentNumber());
		entity.setPaymentMethod(operationDTO.getPaymentMethod());
		entity.setPetitionerAccountNumber(operationDTO.getAccountNumber());
		entity.setPetitionerMobileNumber(operationDTO.getMobileNumber());
		entity.setAmount(operationDTO.getAmount());
		entity.setStatus(operationDTO.getStatus());
		entity.setValidated(false);
		entity.setCreateDate(new Date());
		return mongoTemplate.insert(entity);
	}
}
