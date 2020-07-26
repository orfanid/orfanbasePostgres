package com.orfangenes.db.orfanbasedb.Controller;

import com.orfangenes.db.orfanbasedb.dto.GeneSummary;
import com.orfangenes.db.orfanbasedb.dto.ORFanGenes;
import com.orfangenes.db.orfanbasedb.dto.SummaryChart;
import com.orfangenes.db.orfanbasedb.entity.Analysis;
import com.orfangenes.db.orfanbasedb.entity.Gene;
import com.orfangenes.db.orfanbasedb.service.AnalysisService;
import com.orfangenes.db.orfanbasedb.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Suresh Hewapathirana
 */
@RestController
public class AnalysisController {

    private final AnalysisService analysisService;

    @Autowired
    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    @GetMapping("/analyses")
    public @ResponseBody
    List<Analysis> all(){
        return  analysisService.findAllAnalysiss();
    }

    @GetMapping("/analysis/{id}")
    public EntityModel<Analysis> one(@PathVariable Long id) {
        Analysis analysis = analysisService.getAnalysis(id);
        EntityModel<Analysis> resource=new EntityModel<>(analysis);
        resource.add(
                linkTo(methodOn(this.getClass()).one(id)).withSelfRel(),
                linkTo(methodOn(this.getClass()).all()).withRel("analyses"));
        return resource;
    }

    @PostMapping("/analysis")
    public Analysis save(@Valid @RequestBody Analysis analysis) {
        return analysisService.saveAnalysis(analysis);
    }

    @PutMapping("/analysis/{analysisId}")
    public Analysis update(@PathVariable Long analysisId,
                          @Valid @RequestBody Analysis analysisRequest) {
        return analysisService.updateAnalysis(analysisId, analysisRequest);
    }

    @DeleteMapping("/analysis/{analysisId}")
    public ResponseEntity<?> delete(@PathVariable Long analysisId) {
        return analysisService.deleteAnalysis(analysisId);
    }

    // data support endpoints

    @GetMapping("/data/summary/{analysisId}")
    public List<GeneSummary> getSummary(@PathVariable String analysisId) {

        Analysis analysis = analysisService.getAnalysisById(analysisId);
        List<Gene> genes = analysis.getGeneList();

        List<String> orfanLevels = genes.stream().map(Gene::getOrfanLevel).collect(Collectors.toList());
        List<GeneSummary> geneSummaries = new ArrayList<>();

        for (Constants.TRGS trg :Constants.TRGS.values()) {
            System.out.println(trg.getName());
            geneSummaries.add(new GeneSummary(trg.getName(), Collections.frequency(orfanLevels, trg.getName())));
        }
        return geneSummaries;
    }

    @GetMapping("/data/genes/{analysisId}")
    public List<ORFanGenes> getORFanGenes(@PathVariable String analysisId) {

        List<ORFanGenes> ORFanGeneList = new ArrayList<>();

        Analysis analysis = analysisService.getAnalysisById(analysisId);
        List<Gene> genes = analysis.getGeneList();

        for (Gene gene:genes) {
            ORFanGeneList.add(new ORFanGenes(gene.getGeneId(), gene.getDescription(),gene.getOrfanLevel()));
        }
        return ORFanGeneList;
    }

    @GetMapping("/data/summary/chart/{analysisId}")
    public SummaryChart getSummaryChart(@PathVariable String analysisId) {

        Analysis analysis = analysisService.getAnalysisById(analysisId);
        List<Gene> genes = analysis.getGeneList();
        List<String> orfanLevels = genes.stream().map(Gene::getOrfanLevel).collect(Collectors.toList());
        List<GeneSummary> geneSummaries = new ArrayList<>();

        for (Constants.TRGS trg :Constants.TRGS.values()) {
            System.out.println(trg.getName());
            geneSummaries.add(new GeneSummary(trg.getName(), Collections.frequency(orfanLevels, trg.getName())));
        }


        List<String> types = geneSummaries.stream().map(GeneSummary::getType).collect(Collectors.toList());
        String[] typeArray = new String[types.size()];
        typeArray = types.toArray(typeArray);

        List<Integer> counts = geneSummaries.stream().map(GeneSummary::getCount).collect(Collectors.toList());
        Integer[] countArray = new Integer[counts.size()];
        countArray = counts.toArray(countArray);

        SummaryChart summaryChart = new SummaryChart(typeArray, countArray);

        return summaryChart;
    }
}