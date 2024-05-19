package com.assignment.githubscore.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TextMatch(
        @JsonProperty("text") String text,
        @JsonProperty("indices") List<Integer> indices
) {}
