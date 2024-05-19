package com.assignment.githubscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SearchResultTextMatch(
        @JsonProperty("object_url") String objectUrl,
        @JsonProperty("object_type") String objectType,
        @JsonProperty("property") String property,
        @JsonProperty("fragment") String fragment,
        @JsonProperty("matches") List<TextMatch> matches
) {
}
