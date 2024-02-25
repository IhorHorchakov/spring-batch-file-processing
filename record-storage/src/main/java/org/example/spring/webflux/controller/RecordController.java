package org.example.spring.webflux.controller;

import org.example.spring.webflux.service.RecordService;
import org.example.spring.webflux.service.dto.RecordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/records")
public class RecordController {

    @Autowired
    private RecordService service;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<RecordDto> getById(@PathVariable String id) {
        return service.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<RecordDto> getAll() {
        return service.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<RecordDto> create(@RequestBody RecordDto recordDto) {
        return service.save(recordDto);
    }
}
