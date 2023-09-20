package com.redcare.challenge.controller;

import com.redcare.challenge.service.Repository;
import org.springframework.stereotype.Component;

@Component
class RepositoryResponseMapper {
    public RepositoryResponse map(Repository repository) {
        return new RepositoryResponse(
                repository.name(),
                repository.url(),
                repository.stars(),
                repository.language(),
                repository.createdAt()
        );
    }
}
