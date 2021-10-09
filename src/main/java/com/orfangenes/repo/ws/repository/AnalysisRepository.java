package com.orfangenes.repo.ws.repository;

import com.orfangenes.repo.ws.entity.Analysis;
import com.orfangenes.repo.ws.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Suresh Hewapathirana
 */
//@Transactional
@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {
    Optional<Analysis> findAnalysesByAnalysisId(String analysisId);

    List<Analysis> findByAnalysisDateGreaterThanEqualAndAnalysisDateLessThanAndStatus(Date startDate, Date endDate, Constants.AnalysisStatus status);
}
