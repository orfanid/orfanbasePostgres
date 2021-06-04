package com.orfangenes.repo.ws.service;


import com.orfangenes.repo.ws.entity.Analysis;
import com.orfangenes.repo.ws.dto.AnalysisResultsTableRaw;
import com.orfangenes.repo.ws.entity.Gene;
import com.orfangenes.repo.ws.exception.AnalysisNotFoundException;
import com.orfangenes.repo.ws.exception.ResourceNotFoundException;
import com.orfangenes.repo.ws.repository.AnalysisRepository;
import com.orfangenes.repo.ws.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Suresh Hewapathirana
 */
@Slf4j
@Transactional
@Service
public class AnalysisService {

    @Autowired
    private AnalysisRepository analysisRepository;

    public List<Analysis> findAllAnalysiss() {
        return analysisRepository.findAll();
    }

    public List<AnalysisResultsTableRaw> findAllAnalysissForTable() {
        List<AnalysisResultsTableRaw> analysisResultsTableRaws = new ArrayList<>();

        analysisRepository.findAll().forEach(analysis -> {
            analysisResultsTableRaws.add(
                    new AnalysisResultsTableRaw(
                        analysis.getAnalysisId(),
                        analysis.getAnalysisDate(),
                        analysis.getOrganism(),
                        analysis.getUser().getEmail(),
                        analysis.getGeneList().size())
            );
        });
        return analysisResultsTableRaws;
    }

    public Analysis getAnalysis(long analysisId){
        return analysisRepository.findById(analysisId)
                .orElseThrow(() -> new AnalysisNotFoundException(analysisId));
    }

    public Analysis getAnalysisByAnalysisId(String analysisId){
        Assert.notNull(analysisId, "analysisId cannot be null");
        return analysisRepository.findAnalysesByAnalysisId(analysisId)
                .orElseThrow(() -> new AnalysisNotFoundException(analysisId));
    }

    public Analysis saveAnalysis(Analysis analysis){
        return analysisRepository.save(analysis);
    }

    public Analysis updateAnalysis(long analysisId, Analysis analysisRequest){
        return  analysisRepository.findById(analysisId)
                .map(analysis -> {
                    analysis.setId(analysisRequest.getId());
                    analysis.setAnalysisId(analysisRequest.getAnalysisId());
                    analysis.setAnalysisDate(analysisRequest.getAnalysisDate());
                    analysis.setOrganism(analysisRequest.getOrganism());
                    analysis.setTaxonomyId(analysisRequest.getTaxonomyId());
                    analysis.setSaved(analysisRequest.isSaved());
                    analysis.setBlastResults(analysisRequest.getBlastResults());
                    analysis.setGeneList(analysisRequest.getGeneList());
                    analysis.setUser(analysisRequest.getUser());
                    return analysisRepository.save(analysis);
                }).orElseThrow(() -> new ResourceNotFoundException("Analysis not found with id " + analysisId));
    }

    public ResponseEntity<?> deleteAnalysis(long analysisId){
        return analysisRepository.findById(analysisId)
                .map(analysis -> {
                    analysisRepository.delete(analysis);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Analysis not found with id " + analysisId));
    }

    public void savePendingAnalysis(Analysis analysis) {
        analysis.setStatus(Constants.AnalysisStatus.PENDING);
        analysisRepository.save(analysis);
    }

    public Analysis updateByAnalysisId(Analysis analysis) {
        Analysis savedAnalysis = analysisRepository.findAnalysesByAnalysisId(analysis.getAnalysisId()).orElseThrow(() -> new RuntimeException("No analysis found"));
        savedAnalysis.setAnalysisId(analysis.getAnalysisId());
        savedAnalysis.setAnalysisDate(analysis.getAnalysisDate());
        savedAnalysis.setOrganism(analysis.getOrganism());
        savedAnalysis.setTaxonomyId(analysis.getTaxonomyId());
        savedAnalysis.setSaved(analysis.isSaved());
        savedAnalysis.setBlastResults(analysis.getBlastResults());
        savedAnalysis.setEvalue(analysis.getEvalue());
        savedAnalysis.setMaximumTargetSequences(analysis.getMaximumTargetSequences());
        savedAnalysis.setIdentity(analysis.getIdentity());
        savedAnalysis.setSequenceType(analysis.getSequenceType());
        savedAnalysis.getGeneList().clear();
        savedAnalysis.getGeneList().addAll(analysis.getGeneList());
        savedAnalysis.setUser(analysis.getUser());
        savedAnalysis.setStatus(analysis.getStatus());
        for (Gene gene : savedAnalysis.getGeneList()) {
            gene.setAnalysis(savedAnalysis);
        }
        return analysisRepository.save(savedAnalysis);
    }

    @Transactional
    public void cancel(String analysisId) {
        Analysis analysis = analysisRepository.findAnalysesByAnalysisId(analysisId)
                .orElseThrow(() -> new ResourceNotFoundException("Analysis not found with id " + analysisId));

        if (analysis.getStatus().equals(Constants.AnalysisStatus.PENDING)) {
            analysis.setStatus(Constants.AnalysisStatus.CANCELLED);
        } else {
            throw new RuntimeException("Can not cancel");
        }
    }
}
