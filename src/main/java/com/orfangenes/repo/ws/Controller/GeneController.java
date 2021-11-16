package com.orfangenes.repo.ws.Controller;

import com.orfangenes.repo.ws.entity.Gene;
import com.orfangenes.repo.ws.service.GeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
/**
 * @author Suresh Hewapathirana
 */
@Validated
@RestController
@CrossOrigin
public class GeneController {

    private final GeneService geneService;

    @Autowired
    public GeneController(GeneService geneService) {
        this.geneService = geneService;
    }

    @GetMapping("/genes")
    public @ResponseBody
    List<Gene> all(){
        return  geneService.findAllGenes();
    }

    @GetMapping("/gene/{id}")
    public Gene one(@PathVariable Long id) {
        Gene gene = geneService.getGene(id);
        return gene;
    }

    @PostMapping("/gene")
    public Gene save(@Valid @RequestBody Gene gene) {
        return geneService.saveGene(gene);
    }

    @PutMapping("/gene/{geneId}")
    public Gene update(@PathVariable Long geneId,
                          @Valid @RequestBody Gene geneRequest) {
        return geneService.updateGene(geneId, geneRequest);
    }

    @DeleteMapping("/gene/{geneId}")
    public ResponseEntity<?> delete(@PathVariable Long geneId) {
        return geneService.deleteGene(geneId);
    }
}