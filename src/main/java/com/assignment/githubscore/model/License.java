package com.assignment.githubscore.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record License(
        @JsonProperty("key") String key,
        @JsonProperty("name") String name,
        @JsonProperty("url") String url,
        @JsonProperty("spdx_id") String spdxId,
        @JsonProperty("node_id") String nodeId,
        @JsonProperty("html_url") String htmlUrl
) {
}