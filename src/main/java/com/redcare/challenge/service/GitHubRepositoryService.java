package com.redcare.challenge.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class GitHubRepositoryService implements RepositoryService {
    private final WebClient webClient;
    private final int webClientTimeout;
    private final GitHubQueryBuilder gitHubQueryBuilder;
    private final Logger logger;

    public GitHubRepositoryService(WebClient webClient, int webClientTimeout, GitHubQueryBuilder gitHubQueryBuilder) {
        this.webClient = webClient;
        this.webClientTimeout = webClientTimeout;
        this.gitHubQueryBuilder = gitHubQueryBuilder;
        this.logger = LoggerFactory.getLogger(GitHubRepositoryService.class);
    }

    @Override
    public Mono<List<Repository>> getRepositories(int limit, LocalDate createdAfter, String language) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search/repositories")
                        .queryParam("q", gitHubQueryBuilder.buildQuery(createdAfter, language))
                        .queryParam("sort", "stars")
                        .queryParam("order", "desc")
                        .queryParam("per_page", limit)
                        .build()
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    logger.error(String.format("Received error from GitHub API, got status %s", response.statusCode()));
                    return Mono.error(new RepositoryServiceException("Received error from GitHub API"));
                })
                .bodyToMono(GitHubItems.class)
                .timeout(Duration.ofMillis(webClientTimeout))
                .onErrorMap(e -> {
                    logger.error(String.format("Received timeout from GitHub API, got exception %s", e));
                    return new RepositoryServiceException("Received timeout from GitHub API");
                })
                .map(items -> items.items.stream().map(this::mapToRepository).toList());
    }

    private Repository mapToRepository(GitHubRepository repository) {
        return new Repository(
                repository.fullName,
                repository.url,
                repository.stargazersCount,
                repository.language,
                repository.createdAt
        );
    }

    private record GitHubItems(List<GitHubRepository> items) {
    }

    private record GitHubRepository(
            @JsonProperty("full_name") String fullName,
            @JsonProperty("html_url") URL url,
            @JsonProperty("stargazers_count") int stargazersCount,
            String language,
            @JsonProperty("created_at") LocalDate createdAt) {
    }
}
