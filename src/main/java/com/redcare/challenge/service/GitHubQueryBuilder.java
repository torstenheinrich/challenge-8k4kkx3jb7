package com.redcare.challenge.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GitHubQueryBuilder {
    String buildQuery(LocalDate createdAfter, String language) {
        var qualifiers = new ArrayList<String>();

        // the query cannot be empty, so we have to set sane defaults in case qualifiers are missing
        String created;
        if (createdAfter == null) {
            created = "2019-01-10"; // taken from the sample URL in the implementation details
        } else {
            created = createdAfter.format(DateTimeFormatter.ISO_LOCAL_DATE);
        }
        qualifiers.add(String.format("created:>%s", created));

        if (language != null) {
            qualifiers.add(String.format("language:%s", language));
        }

        return String.join(" ", qualifiers);
    }
}
