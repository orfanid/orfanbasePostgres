package com.orfangenes.db.orfanbasedb.service;

import com.orfangenes.db.orfanbasedb.entity.Analysis;
import com.orfangenes.db.orfanbasedb.exception.AnalysisNotFoundException;
import com.orfangenes.db.orfanbasedb.exception.ResourceNotFoundException;
import com.orfangenes.db.orfanbasedb.repository.AnalysisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository repository;

    public List<Analysis> findAllAnalysiss() {
        return repository.findAll();
    }

    public Analysis getAnalysis(long analysisId){
        return repository.findById(analysisId)
                .orElseThrow(() -> new AnalysisNotFoundException(analysisId));
    }

    public Analysis getAnalysisById(String analysisId){
        Assert.notNull(analysisId, "analysisId cannot be null");
        return repository.findByAnalysisId(analysisId)
                .orElseThrow(() -> new AnalysisNotFoundException(analysisId));
    }

    public Analysis saveAnalysis(Analysis analysis){
        return repository.save(analysis);
    }

    public Analysis updateAnalysis(long analysisId, Analysis analysisRequest){
        return  repository.findById(analysisId)
                .map(analysis -> {
                    analysis.setId(analysisRequest.getId());
                    analysis.setAnalysisId(analysisRequest.getAnalysisId());
                    analysis.setAnalysisDate(analysisRequest.getAnalysisDate());
                    analysis.setOrganism(analysisRequest.getOrganism());
                    analysis.setTaxonomyId(analysisRequest.getTaxonomyId());
                    analysis.setSaved(analysisRequest.isSaved());
                    analysis.setGeneList(analysisRequest.getGeneList());
                    analysis.setUser(analysisRequest.getUser());
                    return repository.save(analysis);
                }).orElseThrow(() -> new ResourceNotFoundException("Analysis not found with id " + analysisId));
    }

    public ResponseEntity<?> deleteAnalysis(long analysisId){
        return repository.findById(analysisId)
                .map(analysis -> {
                    repository.delete(analysis);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Analysis not found with id " + analysisId));
    }
}
