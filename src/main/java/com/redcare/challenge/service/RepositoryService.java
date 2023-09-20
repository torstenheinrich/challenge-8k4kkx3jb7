package com.redcare.challenge.service;

import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface RepositoryService {
    Mono<List<Repository>> getRepositories(int limit, LocalDate createdAfter, String language);
}
