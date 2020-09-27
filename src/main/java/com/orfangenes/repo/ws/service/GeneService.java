package com.orfangenes.repo.ws.service;

import com.orfangenes.repo.ws.entity.Gene;
import com.orfangenes.repo.ws.exception.ResourceNotFoundException;
import com.orfangenes.repo.ws.exception.GeneNotFoundException;
import com.orfangenes.repo.ws.repository.GeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
