package com.orfangenes.repo.ws.Controller;

import com.orfangenes.repo.entity.User;
import com.orfangenes.repo.ws.dto.AnalysisResultsTableRaw;
import com.orfangenes.repo.ws.dto.GeneSummary;
import com.orfangenes.repo.ws.dto.ORFanGenes;
import com.orfangenes.repo.ws.dto.SummaryChart;
import com.orfangenes.repo.entity.Analysis;
import com.orfangenes.repo.entity.Gene;
import com.orfangenes.repo.ws.service.AnalysisService;
import com.orfangenes.repo.ws.service.UserService;
import com.orfangenes.repo.ws.util.Constants;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @author Suresh Hewapathirana
 */
@RestController
@Validated
@RequestMapping("/analysis")
@Slf4j
@Tag(name="Analysis")
public class AnalysisController {

    @Autowired
    private AnalysisService analysisService;

    @Autowired
    private UserService userService;

    @GetMapping("/analyses")
    public @ResponseBody
    List<Analysis> all(){
        return  analysisService.findAllAnalysiss();
    }

    @GetMapping("/analyses/table")
    public @ResponseBody
    List<AnalysisResultsTableRaw> findAllAnalysissForTable(){
        return  analysisService.findAllAnalysissForTable();
    }

    @GetMapping("/analysis/{id}")
    public EntityModel<Analysis> one(@PathVariable @Valid Long id) {
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
    public ResponseEntity<?> delete(@PathVariable @Valid Long analysisId) {
        return analysisService.deleteAnalysis(analysisId);
    }

    // data support endpoints

    /**
     * Replacement for the ORFanGenesSummary.json
     * @param analysisId
     * @return
     */
    @GetMapping("/data/summary/{analysisId}")
    public List<GeneSummary> getSummary(@PathVariable @Valid String analysisId) {

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

    /**
     * Replacement for the ORFanGenes.json
     * @param analysisId
     * @return
     */
    @GetMapping("/data/genes/{analysisId}")
    public List<ORFanGenes> getORFanGenes(@PathVariable @Valid String analysisId) {

        List<ORFanGenes> ORFanGeneList = new ArrayList<>();

        Analysis analysis = analysisService.getAnalysisById(analysisId);
        List<Gene> genes = analysis.getGeneList();

        for (Gene gene:genes) {
            ORFanGeneList.add(new ORFanGenes(gene.getGeneId(), gene.getDescription(),gene.getOrfanLevel()));
        }
        return ORFanGeneList;
    }

    /**
     * Replacement for the blastresults.json
     * @param analysisId
     * @return
     */
    @GetMapping("/data/blastresults/{analysisId}")
    public String getblastResults(@PathVariable @Valid String analysisId) {

        Analysis analysis = analysisService.getAnalysisById(analysisId);
        return analysis.getBlastResults();
    }

    /**
     * Replacement for the ORFanGenesSummaryChart.json
     * @param analysisId
     * @return
     */
    @GetMapping("/data/summary/chart/{analysisId}")
    public SummaryChart getSummaryChart(@PathVariable @Valid String analysisId) {

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

    @PostMapping("/test")
    public void populateTestData() throws ParseException {
        Random rand = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        User user = User.builder()
                .firstName("Suresh")
                .lastName("Hewapathirana")
                .email("sureshhewabi@gmail.com").build();
        user = userService.saveUser(user);

        Gene gene = Gene.builder()
                .geneId("NP_415100.1")
                .description("bacteriophage N4 receptor, outer membrane protein [Escherichia coli str. K-12 substr. MG1655]")
                .orfanLevel("genus restricted gene")
                .build();

        Analysis analysis = new Analysis();
        analysis.setAnalysisId("1595716461278_VMO");
        analysis.setOrganism("Escherichia coli str. K-12 substr. MG1655");
        analysis.setTaxonomyId(562);
        analysis.setAnalysisDate(simpleDateFormat.parse("22/07/2020"));
        analysis.setSaved(true);
        analysis.setBlastResults("[{\"tree\":{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"name\":\"Shigella dysenteriae(1)\"},{\"name\":\"Shigella sonnei(1)\"}],\"name\":\"Shigella(2)\"},{\"children\":[{\"name\":\"Escherichia coli B185(1)\"},{\"name\":\"Escherichia coli(1)\"},{\"name\":\"Escherichia coli O43:H14(1)\"},{\"name\":\"Escherichia coli K-12(1)\"}],\"name\":\"Escherichia(4)\"},{\"children\":[{\"name\":\"Enterobacteriaceae bacterium(1)\"}],\"name\":\"(1)\"}],\"name\":\"Enterobacteriaceae(7)\"}],\"name\":\"Enterobacterales(7)\"}],\"name\":\"Gammaproteobacteria(7)\"}],\"name\":\"Proteobacteria(7)\"}],\"name\":\"(7)\"}],\"name\":\"Bacteria(1)\"}],\"name\":\"LUCA(1)\"},\"id\":\"NP_415100.1\"}]\n");
        analysis.setUser(user);

        List<Gene> genes = new ArrayList<>();
        gene.setGeneParent(analysis);
        genes.add(gene);

        analysis.setGeneList(genes);

        analysisService.saveAnalysis(analysis);
        System.out.println("Data saved!");
    }
}