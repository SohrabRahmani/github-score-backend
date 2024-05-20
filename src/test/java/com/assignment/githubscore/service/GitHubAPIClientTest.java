package com.assignment.githubscore.service;

import com.assignment.githubscore.model.SearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GitHubAPIClientTest {
    private GitHubAPIClient gitHubAPIClient;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json"))
                .thenReturn(webClientBuilder);
        when(webClientBuilder.codecs(any())).thenReturn(webClientBuilder);  // Using any() to avoid lambda mismatch
        when(webClientBuilder.build()).thenReturn(webClient);

        gitHubAPIClient = new GitHubAPIClient("https://api.github.com", webClientBuilder);
    }

    @Test
    void testSearchRepositories_success() {
        String language = "Java";
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(30);
        SearchResponse mockResponse = new SearchResponse(1, false, emptyList());

        doReturn(requestHeadersUriSpec).when(webClient).get();
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString());

        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(SearchResponse.class)).thenReturn(Mono.just(mockResponse));

        SearchResponse response = gitHubAPIClient.searchRepositories(language, earliestCreatedDate);

        assertEquals(mockResponse, response);
    }
}