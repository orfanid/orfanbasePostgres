package com.orfangenes.repo.ws.Controller;

import com.orfangenes.repo.entity.Gene;
import com.orfangenes.repo.ws.service.GeneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Suresh Hewapathirana
 */
@RestController
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
    public EntityModel<Gene> one(@PathVariable Long id) {
        Gene gene = geneService.getGene(id);
        EntityModel<Gene> resource=new EntityModel<>(gene);
        resource.add(
                linkTo(methodOn(this.getClass()).one(id)).withSelfRel(),
                linkTo(methodOn(this.getClass()).all()).withRel("genes"));
        return resource;
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