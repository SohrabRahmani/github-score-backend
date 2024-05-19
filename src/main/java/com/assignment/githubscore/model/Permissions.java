package com.assignment.githubscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Permissions(
        @JsonProperty("admin") boolean admin,
        @JsonProperty("maintain") boolean maintain,
        @JsonProperty("push") boolean push,
        @JsonProperty("triage") boolean triage,
        @JsonProperty("pull") boolean pull
) {
}