package com.redcare.challenge.service;

import java.net.URL;
import java.time.LocalDate;

public record Repository(String name, URL url, int stars, String language, LocalDate createdAt) {
}
