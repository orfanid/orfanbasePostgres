package com.orfangenes.repo.ws.service;

import com.orfangenes.repo.ws.dto.Genes;
import com.orfangenes.repo.ws.dto.PagedResults;
import com.orfangenes.repo.ws.entity.Analysis;
import com.orfangenes.repo.ws.entity.Gene;
import com.orfangenes.repo.ws.exception.ResourceNotFoundException;
import com.orfangenes.repo.ws.exception.GeneNotFoundException;
import com.orfangenes.repo.ws.repository.GeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Transactional(readOnly = true)
@Service
public class GeneService {

    private final GeneRepository repository;

    @Autowired
    public GeneService(GeneRepository repository) {
        this.repository = repository;
    }

    public List<Gene> findAllGenes() {
        return repository.findAll();
    }

    public List<Gene> findGenesByOrfanLevel(String orfanLevel) {
        return repository.findByOrfanLevel(orfanLevel);
    }

    public Gene getGene(long geneId){
        return repository.findById(geneId)
                .orElseThrow(() -> new GeneNotFoundException(geneId));
    }

    @Transactional(readOnly = false)
    public Gene saveGene(Gene gene){
        return repository.save(gene);
    }

    @Transactional(readOnly = false)
    public Gene updateGene(long geneId, Gene geneRequest){
        return  repository.findById(geneId)
                .map(gene -> {
                    gene.setId(geneRequest.getId());
                    gene.setDescription(geneRequest.getDescription());
                    gene.setOrfanLevel(geneRequest.getOrfanLevel());
                    gene.setSequence(geneRequest.getSequence());
                    return repository.save(gene);
                }).orElseThrow(() -> new ResourceNotFoundException("Gene not found with id " + geneId));
    }

    @Transactional(readOnly = false)
    public ResponseEntity<?> deleteGene(long geneId){
        return repository.findById(geneId)
                .map(gene -> {
                    repository.delete(gene);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Gene not found with id " + geneId));
    }

    public PagedResults<Genes> findGenesPage(int page, int size, String sortByDate) {
        Page<Gene> all = repository.findAll(PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortByDate), "analysis.analysisDate")));
        PagedResults<Genes> genesPagedResults = new PagedResults<>();
        genesPagedResults.setTotal(all.getTotalElements());
        List<Genes> genes = new ArrayList<>();
        for (Gene gene : all.getContent()) {
            Analysis analysis = gene.getAnalysis();
            Genes genes1 = new Genes();
            genes1.setGeneId(gene.getGeneId());
            genes1.setDescription(gene.getDescription());
            genes1.setSequence(gene.getSequence());
            genes1.setGccontent((gene.getGccontent()));
            genes1.setLength(gene.getLength());
            genes1.setOrfanLevel(gene.getOrfanLevel());
            genes1.setAnalysisDate(analysis.getAnalysisDate());
            genes1.setOrganism(analysis.getOrganism());
            genes1.setTaxonomyId(analysis.getTaxonomyId());
            genes1.setAnalysisId(analysis.getAnalysisId());
            genes.add(genes1);
        }
        genesPagedResults.setData(genes);
        return genesPagedResults;
    }
}
