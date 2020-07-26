package com.orfangenes.db.orfanbasedb.service;

import com.orfangenes.db.orfanbasedb.entity.Gene;
import com.orfangenes.db.orfanbasedb.exception.ResourceNotFoundException;
import com.orfangenes.db.orfanbasedb.exception.GeneNotFoundException;
import com.orfangenes.db.orfanbasedb.repository.GeneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Service
public class GeneService {

    @Autowired
    private GeneRepository repository;

    public List<Gene> findAllGenes() {
        return repository.findAll();
    }

    public Gene getGene(long geneId){
        return repository.findById(geneId)
                .orElseThrow(() -> new GeneNotFoundException(geneId));
    }

    public Gene saveGene(Gene gene){
        return repository.save(gene);
    }

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

    public ResponseEntity<?> deleteGene(long geneId){
        return repository.findById(geneId)
                .map(gene -> {
                    repository.delete(gene);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException("Gene not found with id " + geneId));
    }
}
