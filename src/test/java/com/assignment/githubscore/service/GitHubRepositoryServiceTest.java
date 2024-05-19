package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.RepoItemDTO;
import com.assignment.githubscore.entity.RepositoryScore;
import com.assignment.githubscore.exception.GitHubApiErrorHandler;
import com.assignment.githubscore.mapper.GitHubDtoMapper;
import com.assignment.githubscore.model.RepoItem;
import com.assignment.githubscore.model.SearchResponse;
import com.assignment.githubscore.repository.RepositoryScoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GitHubRepositoryServiceTest {

    @Mock
    private GitHubAPIClient gitHubAPIClient;

    @Mock
    private GitHubDtoMapper gitHubDtoMapper;

    @Mock
    private ScoringService scoringService;

    @Mock
    private RepositoryScoreRepository repositoryScoreRepository;

    @InjectMocks
    private GitHubRepositoryService gitHubRepositoryService;

    @Test
    void getFilteredRepositories_ShouldFetchRepositoriesAndMapToDTO() {
        String language = "java";
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(30);
        long userId = 1L;
        String updateAt = LocalDateTime.now().minusDays(1).toString();

        RepoItem item = createRepoItem(1, 100, 50, LocalDateTime.now().minusDays(1));
        SearchResponse searchResponse = new SearchResponse(1, false, List.of(item));

        when(gitHubAPIClient.searchRepositories(language, earliestCreatedDate)).thenReturn(searchResponse);
        when(scoringService.calculatePopularityScore(100, 50, updateAt, userId)).thenReturn(150.0);
        when(gitHubDtoMapper.toRepoItemDTO(any(), eq(150.0), eq("N/A"))).thenReturn(createItemDto(150.0, 100, 50, updateAt));

        List<RepoItemDTO> result = gitHubRepositoryService.getFilteredRepositories(language, earliestCreatedDate, userId);

        assertEquals(1, result.size());
        verify(repositoryScoreRepository).save(any(RepositoryScore.class));
    }

    @Test
    void getFilteredRepositories_ShouldHandleEmptyResponse() {
        String language = "java";
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(30);
        long userId = 1L;

        SearchResponse searchResponse = new SearchResponse(1, false, emptyList());

        when(gitHubAPIClient.searchRepositories(language, earliestCreatedDate)).thenReturn(searchResponse);

        List<RepoItemDTO> result = gitHubRepositoryService.getFilteredRepositories(language, earliestCreatedDate, userId);

        assertEquals(0, result.size());
        verify(repositoryScoreRepository, never()).save(any(RepositoryScore.class));
    }

    @Test
    void fetchRepositories_ShouldThrowRuntimeExceptionOnFailure() {
        String language = "java";
        LocalDate earliestCreatedDate = LocalDate.now().minusDays(30);

        when(gitHubAPIClient.searchRepositories(language, earliestCreatedDate)).thenThrow(new GitHubApiErrorHandler("API error"));

        assertThrows(RuntimeException.class, () -> gitHubRepositoryService.getFilteredRepositories(language, earliestCreatedDate, 1L));
    }

    @Test
    void determineTrend_ShouldReturnCorrectTrend() {
        assertEquals("Up", gitHubRepositoryService.determineTrend(200.0d, 150.0d));
        assertEquals("Down", gitHubRepositoryService.determineTrend(150.0d, 200.0d));
        assertEquals("Same", gitHubRepositoryService.determineTrend(150.0d, 150.0d));
        assertEquals("N/A", gitHubRepositoryService.determineTrend(150.0d, 0.0d));
    }

    private RepoItemDTO createItemDto(double popularityScore, int stargazersCount, int forksCount, String updatedAt) {
        return new RepoItemDTO(
                1,
                popularityScore,
                "same",
                "1",
                "test-repo",
                "test-repo-full-name",
                false,
                "url",
                false,
                LocalDateTime.now().minusDays(30).toString(),
                updatedAt,
                stargazersCount,
                "java",
                forksCount,
                false,
                false
        );
    }

    private RepoItem createRepoItem(int id, int stargazersCount, int forksCount, LocalDateTime updatedAt) {
        return new RepoItem(
                id, "", "Test Repo", "", null, false, "", "", false, "",
                "", updatedAt.toString(), "", "", 0, stargazersCount, 0, "", forksCount, 0,
                "", "", 0.0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", 0, 0, 0, null, "", false, false, false, false, false, false, false, false, "", null, null,
                null, "", false, false, false, false, false, false,
                false, false
        );
    }
}
