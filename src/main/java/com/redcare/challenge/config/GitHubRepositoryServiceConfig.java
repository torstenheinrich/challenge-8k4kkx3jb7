package com.redcare.challenge.config;

import com.redcare.challenge.service.GitHubQueryBuilder;
import com.redcare.challenge.service.GitHubRepositoryService;
import com.redcare.challenge.service.RepositoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GitHubRepositoryServiceConfig {
    @Bean
    RepositoryService repositoryService(
            WebClient.Builder webClientBuilder,
            @Value("${repository.service.github.base_url}") String baseUrl,
            @Value("${repository.service.github.timeout}") int timeout
    ) {
        var webClient = webClientBuilder.baseUrl(baseUrl).build();

        return new GitHubRepositoryService(webClient, timeout, new GitHubQueryBuilder());
    }
}
