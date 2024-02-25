package org.example.spring.webflux.controller;

import org.example.spring.webflux.service.RecordService;
import org.example.spring.webflux.service.dto.RecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService service;

    @GetMapping("/{id}")
    public Mono<RecordDto> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    public Flux<RecordDto> getAll() {
        return service.getAll();
    }
}
