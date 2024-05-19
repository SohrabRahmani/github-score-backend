package com.assignment.githubscore.repository;

import com.assignment.githubscore.entity.RepositoryScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryScoreRepository extends JpaRepository<RepositoryScore, Long> {

    Optional<RepositoryScore> findTopByRepoIdOrderByRecordedAtDesc(long repoId);
}