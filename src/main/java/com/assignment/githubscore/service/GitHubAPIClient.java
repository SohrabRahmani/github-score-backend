package com.assignment.githubscore.service;

import com.assignment.githubscore.exception.GitHubApiErrorHandler;
import com.assignment.githubscore.model.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class GitHubAPIClient {

    private final WebClient webClient;
    private static final String GITHUB_V3_JSON_MEDIA_TYPE = "application/vnd.github.v3+json";

    public GitHubAPIClient(@Value("${github.api.url}") String gitHubApiUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(gitHubApiUrl)
                .defaultHeader(HttpHeaders.ACCEPT, GITHUB_V3_JSON_MEDIA_TYPE)
                .codecs(configure -> configure.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }

    /**
     * Search GitHub repositories based on language and earliest created date.
     *
     * @param language            The programming language to filter repositories.
     * @param earliestCreatedDate The earliest date when repositories were created.
     * @return The response containing the search results.
     * @throws GitHubApiErrorHandler if there's an error while fetching repositories from GitHub API.
     */
    public SearchResponse searchRepositories(String language, LocalDate earliestCreatedDate) {
        String url = buildSearchUrl(language, earliestCreatedDate);
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> Mono.error(new GitHubApiErrorHandler("Failed to fetch repositories from GitHub API")))
                .bodyToMono(SearchResponse.class)
                .block();
    }

    private String buildSearchUrl(String language, LocalDate earliestCreatedDate) {
        return STR."/search/repositories?q=language:\{language}+created:>=\{earliestCreatedDate}";
    }
}