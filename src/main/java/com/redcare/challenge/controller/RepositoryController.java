package com.redcare.challenge.controller;

import com.redcare.challenge.service.RepositoryService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/repositories")
@Validated
public class RepositoryController {
    private final RepositoryService repositoryService;
    private final RepositoryResponseMapper repositoryResponseMapper;

    public RepositoryController(
            RepositoryService repositoryService,
            RepositoryResponseMapper repositoryResponseMapper
    ) {
        this.repositoryService = repositoryService;
        this.repositoryResponseMapper = repositoryResponseMapper;
    }

    @GetMapping
    Mono<List<RepositoryResponse>> getRepositories(
            @RequestParam(defaultValue = "10") @Min(10) @Max(100) int limit,
            @RequestParam(name = "created_after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PastOrPresent LocalDate createdAfter,
            @RequestParam(required = false) String language
    ) {
        return repositoryService
                .getRepositories(limit, createdAfter, language)
                .map(repositories -> repositories.stream().map(repositoryResponseMapper::map).toList());
    }
}
