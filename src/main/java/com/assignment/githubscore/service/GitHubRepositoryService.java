package com.assignment.githubscore.service;

import com.assignment.githubscore.dto.RepoItemDTO;
import com.assignment.githubscore.entity.RepositoryScore;
import com.assignment.githubscore.mapper.GitHubDtoMapper;
import com.assignment.githubscore.model.RepoItem;
import com.assignment.githubscore.model.SearchResponse;
import com.assignment.githubscore.repository.RepositoryScoreRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class GitHubRepositoryService {

    private final GitHubAPIClient gitHubAPIClient;
    private final GitHubDtoMapper gitHubDtoMapper;
    private final ScoringService scoringService;
    private final RepositoryScoreRepository repositoryScoreRepository;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public GitHubRepositoryService(GitHubAPIClient gitHubAPIClient, GitHubDtoMapper gitHubDtoMapper,
                                   ScoringService scoringService, RepositoryScoreRepository repositoryScoreRepository) {
        this.gitHubAPIClient = gitHubAPIClient;
        this.gitHubDtoMapper = gitHubDtoMapper;
        this.scoringService = scoringService;
        this.repositoryScoreRepository = repositoryScoreRepository;
    }

    /**
     * Retrieves filtered repositories from the GitHub API based on language and earliest created date.
     *
     * @param language            The programming language to filter repositories.
     * @param earliestCreatedDate The earliest date when repositories were created.
     * @param userId              The ID of the user requesting the repositories.
     * @return A list of repository DTOs representing the filtered repositories.
     */

    public List<RepoItemDTO> getFilteredRepositories(String language, LocalDate earliestCreatedDate, long userId) {
        SearchResponse searchResponse = fetchRepositories(language, earliestCreatedDate);
        int maxStars = findMaxStarsCount(searchResponse);
        int maxForks = findMaxForksCount(searchResponse);
        return mapToRepoItemDTOs(searchResponse, maxStars, maxForks, earliestCreatedDate, userId);
    }

    /**
     * Fetches repositories from the GitHub API based on language and earliest created date.
     *
     * @param language            The programming language to filter repositories.
     * @param earliestCreatedDate The earliest date when repositories were created.
     * @return The response containing the fetched repositories.
     * @throws RuntimeException if there is an error while fetching repositories from GitHub API.
     */
    private SearchResponse fetchRepositories(String language, LocalDate earliestCreatedDate) {
        try {
            return gitHubAPIClient.searchRepositories(language, earliestCreatedDate);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch repositories from GitHub API", e);
        }
    }

    /**
     * Maps search response items to repository DTOs with popularity scores and trends.
     *
     * @param searchResponse      The search response from the GitHub API.
     * @param maxStars            The max stars for a repository
     * @param maxForks            The max fork for a repository
     * @param earliestCreatedDate The earliest date when repositories were created.
     * @param userId              The ID of the user requesting the repositories.
     * @return A list of repository DTOs with popularity scores and trends.
     */
    private List<RepoItemDTO> mapToRepoItemDTOs(SearchResponse searchResponse, int maxStars, int maxForks, LocalDate earliestCreatedDate, long userId) {
        return searchResponse.items().stream()
                .map(item -> {
                    Optional<RepositoryScore> previousScoreOpt = getPreviousScore(item.id());
                    double previousScore = previousScoreOpt.map(RepositoryScore::getPopularityScore).orElse(0.0);
                    double popularityScore = scoringService.calculatePopularityScore(
                            item.stargazersCount(),
                            item.forksCount(),
                            LocalDate.parse(item.updatedAt(), DATE_TIME_FORMATTER),
                            maxStars,
                            maxForks,
                            earliestCreatedDate,
                            userId
                    );
                    saveCurrentScore(item.id(), item.name(), popularityScore);

                    String trend = determineTrend(popularityScore, previousScore);
                    return gitHubDtoMapper.toRepoItemDTO(item, popularityScore, trend);
                })
                .toList();
    }

    /**
     * Saves the current popularity score of a repository to the database.
     *
     * @param repoId          The ID of the repository.
     * @param repoName        The name of the repository.
     * @param popularityScore The popularity score of the repository.
     */
    private void saveCurrentScore(int repoId, String repoName, double popularityScore) {
        RepositoryScore repositoryScore = new RepositoryScore();
        repositoryScore.setRepoId(repoId);
        repositoryScore.setRepoName(repoName);
        repositoryScore.setPopularityScore(popularityScore);
        repositoryScore.setRecordedAt(LocalDateTime.now());
        repositoryScoreRepository.save(repositoryScore);
    }

    /**
     * Determines the trend of popularity score change between the current and previous scores.
     *
     * @param currentScore  The current popularity score of the repository.
     * @param previousScore The previous popularity score of the repository.
     * @return A string indicating the trend of score change ("Up", "Down", "Same", or "N/A").
     */
    public String determineTrend(double currentScore, double previousScore) {
        if (previousScore == 0.0) return "N/A";
        if (currentScore > previousScore) return "Up";
        if (currentScore < previousScore) return "Down";
        return "Same";
    }

    /**
     * Retrieves the previous popularity score of a repository from the database.
     *
     * @param repoId The ID of the repository.
     * @return An optional containing the previous repository score, if available.
     */
    private Optional<RepositoryScore> getPreviousScore(int repoId) {
        return repositoryScoreRepository.findTopByRepoIdOrderByRecordedAtDesc(repoId);
    }

    public int findMaxStarsCount(SearchResponse searchResponse) {
        if (searchResponse != null && searchResponse.items() != null && !searchResponse.items().isEmpty()) {
            return searchResponse.items().stream()
                    .map(RepoItem::stargazersCount)
                    .max(Integer::compareTo)
                    .orElse(0);
        } else {
            return 0;
        }
    }

    public int findMaxForksCount(SearchResponse searchResponse) {
        if (searchResponse != null && searchResponse.items() != null && !searchResponse.items().isEmpty()) {
            return searchResponse.items().stream()
                    .map(RepoItem::forksCount)
                    .max(Integer::compareTo)
                    .orElse(0);
        } else {
            return 0;
        }
    }
}
