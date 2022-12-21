package com.bootcamp.project.bootcoinoperation.service;

import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationDTO;
import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BootcoinOperationService {
    public Flux<BootcoinOperationEntity> getAll();
    public Mono<BootcoinOperationEntity> getOne(String id);
    public Mono<BootcoinOperationEntity> save(BootcoinOperationEntity colEnt);
    public Mono<BootcoinOperationEntity> update(String id, String status);
    public Mono<Void> delete(String id);
    public Flux<BootcoinOperationEntity> getPurchaseRequests();
    public Mono<BootcoinOperationEntity> acceptPurchaseRequest(String id, String documentNumber);
    public Mono<BootcoinOperationEntity> initialValidations(String operationNumber);
    public Mono<BootcoinOperationEntity> getByOperationNumber(String operationNumber);
    public void publishToTopic(BootcoinOperationDTO entity);
}
