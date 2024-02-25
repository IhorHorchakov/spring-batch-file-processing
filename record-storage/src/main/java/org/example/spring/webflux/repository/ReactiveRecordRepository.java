package org.example.spring.webflux.repository;

import org.example.spring.webflux.repository.entity.RecordEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ReactiveRecordRepository extends ReactiveMongoRepository<RecordEntity, String> {

}
