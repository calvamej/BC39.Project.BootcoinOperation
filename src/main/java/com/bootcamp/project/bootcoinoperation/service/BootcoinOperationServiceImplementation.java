package com.bootcamp.project.bootcoinoperation.service;

import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationDTO;
import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationEntity;
import com.bootcamp.project.bootcoinoperation.exception.CustomNotFoundException;
import com.bootcamp.project.bootcoinoperation.repository.BootcoinOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Service
public class BootcoinOperationServiceImplementation implements BootcoinOperationService{
    public static final String topic = "BootcoinOperationTopic";

    @Autowired
    private KafkaTemplate<String, BootcoinOperationDTO> kafkaTemp;
    @Autowired
    private BootcoinOperationRepository bootcoinRepository;

    @Override
    public Flux<BootcoinOperationEntity> getAll() {
        return bootcoinRepository.findAll().switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operations not found")));
    }
    @Override
    public Mono<BootcoinOperationEntity> getOne(String id) {
        return bootcoinRepository.findAll().filter(x -> x.getId() != null && x.getId().equals(id)).next();
    }

    @Override
    public Mono<BootcoinOperationEntity> save(BootcoinOperationEntity colEnt) {
        colEnt.setCreateDate(new Date());
        return bootcoinRepository.save(colEnt);
    }

    @Override
    public Mono<BootcoinOperationEntity> update(String id, String status) {
        return getOne(id).flatMap(c -> {
            c.setStatus(status);
            c.setModifyDate(new Date());
            return bootcoinRepository.save(c);
        }).switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operation not found")));
    }

    @Override
    public Mono<Void> delete(String id) {
        return getOne(id)
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operation not found")))
                .flatMap(c -> {
                    return bootcoinRepository.delete(c);
                });
    }
    @Override
    public Flux<BootcoinOperationEntity> getPurchaseRequests() {
        Flux<BootcoinOperationEntity> entity = bootcoinRepository.findAll().filter(x -> x.getStatus() != null && x.getStatus().toUpperCase().equals("REQUESTED"));
        return entity.map(person -> {
            person.setPetitionerMobileNumber("********");
            return person;
        });
    }
    @Override
    public Mono<BootcoinOperationEntity> acceptPurchaseRequest(String id, String documentNumber) {
        return bootcoinRepository.findAll().filter(x -> x.getId() != null && x.getId().equals(id)
        && x.getStatus() != null && x.getStatus().toUpperCase().equals("REQUESTED")).next().flatMap(c -> {
            c.setStatus("VALIDATION IN PROGRESS");
            c.setSellerDocumentNumber(documentNumber);
            c.setModifyDate(new Date());
            c.setOperationNumber(UUID.randomUUID().toString());
            return bootcoinRepository.save(c).then(initialValidations(c.getOperationNumber()));
        }).switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operation not found or already accepted")));
    }
    @Override
    public Mono<BootcoinOperationEntity> initialValidations(String operationNumber) {
        return bootcoinRepository.findAll().filter(x -> x.getOperationNumber() != null && x.getOperationNumber().equals(operationNumber)).next()
        .flatMap(c -> {
            if(c.getPaymentMethod().toUpperCase() != null && (c.getPaymentMethod().toUpperCase().equals("YANKI") ||c.getPaymentMethod().toUpperCase().equals("TRANSFER"))
                    && c.getAmount() > 0)
            {
                if(c.getPaymentMethod().toUpperCase().equals("YANKI") && (c.getPetitionerMobileNumber() == null || c.getPetitionerMobileNumber().equals("")))
                {
                    c.setValidated(true);
                    c.setStatus("REJECTED - MOBILE NUMBER IS REQUIRED WHEN YANKI IS SELECTED AS THE PAYMENT METHOD.");
                }
                else if (c.getPaymentMethod().toUpperCase().equals("TRANSFER") && (c.getPetitionerAccountNumber() == null || c.getPetitionerAccountNumber().equals(""))){
                    c.setValidated(true);
                    c.setStatus("REJECTED - ACCOUNT NUMBER IS REQUIRED WHEN TRANSFER IS SELECTED AS THE PAYMENT METHOD.");
                }else
                {
                    c.setValidated(false);
                    c.setStatus("INITIAL VALIDATIONS COMPLETED");
                }
            }
            else
            {
                c.setValidated(true);
                c.setStatus("REJECTED - INVALID PAYMENT METHOD (MUST BE YANKI OR TRANSFER) OR AMOUNT REQUESTED (MUST BE > 0).");
            }
            c.setInitialValidations(true);
            c.setModifyDate(new Date());
            if(!c.isValidated())
            {
                publishToTopic(new BootcoinOperationDTO(c.getPetitionerDocumentNumber(),c.getPaymentMethod(), c.getPetitionerMobileNumber(), c.getPetitionerAccountNumber(),c.getSellerDocumentNumber(),c.getAmount(),c.getStatus()));
            }

            return bootcoinRepository.save(c);
        }).switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operation not found")));
    }
    @Override
    public Mono<BootcoinOperationEntity> getByOperationNumber(String operationNumber) {
        return bootcoinRepository.findAll().filter(x -> x.getOperationNumber() != null && x.getOperationNumber().equals(operationNumber))
                .next()
                .switchIfEmpty(Mono.error(new CustomNotFoundException("Bootcoin operation not found")));
    }
    @Override
    public void publishToTopic(BootcoinOperationDTO entity) {
        this.kafkaTemp.send(topic, entity);
    }
}
