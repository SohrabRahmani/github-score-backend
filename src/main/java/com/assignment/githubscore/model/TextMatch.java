package com.assignment.githubscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TextMatch(
        @JsonProperty("text") String text,
        @JsonProperty("indices") List<Integer> indices
) {
}
