package com.redcare.challenge.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GitHubQueryBuilderTest {
    @Test
    void test_uses_default_if_created_after_is_null() {
        var query = new GitHubQueryBuilder().buildQuery(null, null);

        assertEquals("created:>2019-01-10", query);
    }

    @Test
    void test_uses_created_after() {
        var query = new GitHubQueryBuilder().buildQuery(LocalDate.of(2023, 1, 2), null);

        assertEquals("created:>2023-01-02", query);
    }

    @Test
    void test_uses_language() {
        var query = new GitHubQueryBuilder().buildQuery(null, "java");

        assertEquals("created:>2019-01-10 language:java", query);
    }

    @Test
    void test_uses_created_after_and_language() {
        var query = new GitHubQueryBuilder().buildQuery(LocalDate.of(2023, 1, 2), "java");

        assertEquals("created:>2023-01-02 language:java", query);
    }
}
