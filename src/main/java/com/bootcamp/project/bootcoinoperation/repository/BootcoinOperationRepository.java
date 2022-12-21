package com.bootcamp.project.bootcoinoperation.repository;

import com.bootcamp.project.bootcoinoperation.entity.BootcoinOperationEntity;
import org.bson.types.ObjectId;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BootcoinOperationRepository extends ReactiveCrudRepository<BootcoinOperationEntity, ObjectId> {
}
