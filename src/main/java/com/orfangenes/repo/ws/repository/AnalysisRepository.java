package com.orfangenes.repo.ws.repository;

import com.orfangenes.repo.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Suresh Hewapathirana
 */
//@Transactional
@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    Optional<Analysis> findByAnalysisId(String analysisId);
}
