package com.bootcamp.project.bootcoinoperation.controller;

import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationEntity;
import com.bootcamp.project.bootcoinoperation.service.BootcoinOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value="/BootcoinOperation")
public class BootcoinOperationController {
    @Autowired
    BootcoinOperationService bootcoinService;

    @GetMapping(value = "/FindOne/{id}")
    public Mono<BootcoinOperationEntity> Get_One(@PathVariable("documentNumber") String id){
        return bootcoinService.getOne(id);
    }
    @GetMapping(value = "/FindAll")
    public Flux<BootcoinOperationEntity> Get_All(){

        return bootcoinService.getAll();
    }
    @PostMapping(value = "/Save")
    public Mono<BootcoinOperationEntity> Save(@RequestBody BootcoinOperationEntity col){

        return bootcoinService.save(col);
    }
    @PutMapping(value = "/Update/{id}/{status}")
    public Mono<BootcoinOperationEntity> Update(@PathVariable("id") String id,@PathVariable("status") String status){
        return bootcoinService.update(id, status);
    }
    @DeleteMapping  (value = "/Delete/{id}")
    public Mono<Void> Delete(@PathVariable("id") String id){
        return bootcoinService.delete(id);
    }
    @GetMapping(value = "/GetPurchaseRequests")
    public Flux<BootcoinOperationEntity> getPurchaseRequests(){

        return bootcoinService.getPurchaseRequests();
    }
    @PutMapping(value = "/AcceptPurchaseRequest/{id}/{documentNumber}")
    public Mono<BootcoinOperationEntity> acceptPurchaseRequest(@PathVariable("id") String id,@PathVariable("documentNumber") String documentNumber){
        return bootcoinService.acceptPurchaseRequest(id, documentNumber);
    }
    @GetMapping(value = "/GetByOperationNumber/{operationNumber}")
    public Mono<BootcoinOperationEntity> getByOperationNumber(@PathVariable("operationNumber") String operationNumber){
        return bootcoinService.getByOperationNumber(operationNumber);
    }
}
