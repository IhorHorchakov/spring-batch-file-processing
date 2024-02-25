package org.example.spring.webflux.service;

import org.example.spring.webflux.repository.ReactiveRecordRepository;
import org.example.spring.webflux.repository.entity.RecordEntity;
import org.example.spring.webflux.service.converter.RecordConverter;
import org.example.spring.webflux.service.dto.RecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
public class RecordService {

    @Autowired
    private ReactiveRecordRepository repository;

    @Autowired
    private RecordConverter converter;


    public Mono<RecordDto> getById(String id) {
        return repository.findById(id).map(converter::toRecordDto);
    }

    public Flux<RecordDto> getAll() {
        return repository.findAll().map(converter::toRecordDto);
    }

    public Mono<RecordEntity> save(RecordDto dto) {
        Objects.requireNonNull(dto);
        Objects.requireNonNull(dto.getId());
        RecordEntity entity = converter.toRecordEntity(dto);
        return repository.save(entity);
    }
}
