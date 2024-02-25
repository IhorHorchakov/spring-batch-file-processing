package org.example.spring.webflux.service.converter;

import org.example.spring.webflux.repository.entity.RecordEntity;
import org.example.spring.webflux.service.dto.RecordDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class RecordConverter {

    public RecordDto toRecordDto(RecordEntity entity) {
        Objects.requireNonNull(entity);
        RecordDto dto = new RecordDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }

    public RecordEntity toRecordEntity(RecordDto dto) {
        Objects.requireNonNull(dto);
        RecordEntity entity = new RecordEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        return entity;
    }
}
