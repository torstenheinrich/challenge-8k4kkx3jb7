package com.redcare.challenge.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;
import java.time.LocalDate;

record RepositoryResponse(
        String name,
        URL url,
        int stars,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String language,
        @JsonProperty("created_at") LocalDate createdAt
) {
}
