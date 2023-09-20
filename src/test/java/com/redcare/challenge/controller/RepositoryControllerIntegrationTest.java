package com.redcare.challenge.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RepositoryControllerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void test_get_request_returns_bad_request_if_limit_is_invalid() {
        this.webTestClient
                .get()
                .uri("/repositories?limit=1000")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void test_get_request_returns_bad_request_if_from_date_is_invalid() {
        this.webTestClient
                .get()
                .uri("/repositories?created_after=22-01-01")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void test_get_request_returns_repositories_ordered_by_stars_desc() {
        var responseBody = this.webTestClient
                .get()
                .uri("/repositories")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);

        var stars = responseBody.stream().map(RepositoryResponse::stars).toList();

        assertEquals(stars.stream().sorted(Comparator.reverseOrder()).toList(), stars);
    }

    @Test
    void test_get_request_returns_top_10_repositories() {
        this.webTestClient
                .get()
                .uri("/repositories?limit=10")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.size()")
                .value(Matchers.is(10));
    }

    @Test
    void test_get_request_returns_top_50_repositories() {
        this.webTestClient
                .get()
                .uri("/repositories?limit=50")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.size()")
                .value(Matchers.is(50));
    }

    @Test
    void test_get_request_returns_top_100_repositories() {
        this.webTestClient
                .get()
                .uri("/repositories?limit=100")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.size()")
                .value(Matchers.is(100));
    }

    @Test
    void test_get_request_returns_repositories_created_after_date_onward() {
        var firstDayOfLastMonth = LocalDate.now().withDayOfMonth(1).minusMonths(1);
        var responseBody = this.webTestClient
                .get()
                .uri(String.format("/repositories?created_after=%s", firstDayOfLastMonth.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);

        var minCreatedAt = responseBody
                .stream()
                .min(Comparator.comparing(RepositoryResponse::createdAt))
                .orElseThrow()
                .createdAt();

        assertTrue(minCreatedAt.isEqual(firstDayOfLastMonth) || minCreatedAt.isAfter(firstDayOfLastMonth));
    }

    @Test
    void test_get_request_returns_repositories_filtered_by_language() {
        var responseBody = this.webTestClient
                .get()
                .uri("/repositories?language=java")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(RepositoryResponse.class)
                .returnResult()
                .getResponseBody();

        assertNotNull(responseBody);

        var languages = responseBody.stream().map(RepositoryResponse::language).distinct().toList();

        assertEquals(1, languages.size());
        assertEquals("Java", languages.get(0));
    }
}
