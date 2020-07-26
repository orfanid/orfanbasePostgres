package com.orfangenes.db.orfanbasedb.repository;

import com.orfangenes.db.orfanbasedb.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Suresh Hewapathirana
 */
@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    Optional<Analysis> findByAnalysisId(String analysisId);
}
