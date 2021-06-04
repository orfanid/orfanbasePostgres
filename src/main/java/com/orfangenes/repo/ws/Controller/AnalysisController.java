package com.orfangenes.repo.ws.Controller;

import com.orfangenes.repo.ws.entity.User;
import com.orfangenes.repo.ws.dto.*;
import com.orfangenes.repo.ws.entity.Analysis;
import com.orfangenes.repo.ws.entity.Gene;
import com.orfangenes.repo.ws.service.AnalysisService;
import com.orfangenes.repo.ws.service.UserService;
import com.orfangenes.repo.ws.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author Suresh Hewapathirana
 */
@RestController
@Validated
@RequestMapping("/analysis")
@Slf4j
@CrossOrigin
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

    @GetMapping("/analyses/all-analysis-table")
    public @ResponseBody
    List<AnalysisResultsTableRaw> findAllAnalysissForTable(){
        return  analysisService.findAllAnalysissForTable();
    }

    @GetMapping("/analysis/{id}")
    public Analysis one(@PathVariable @Valid Long id) {
        Analysis analysis = analysisService.getAnalysis(id);
       return  analysis;
    }

    @GetMapping("/cancel/{analysisId}")
    public void cancel(@PathVariable @Valid String analysisId) {
        analysisService.cancel(analysisId);
    }

    @GetMapping("/analysis")
    public Analysis getAnalysisByAnalysisId(@RequestParam(value="analysisId") String analysisId) {
        Analysis analysis = analysisService.getAnalysisByAnalysisId(analysisId);
        return analysis;
    }

    @PostMapping("/pending")
    public void savePendingAnalysis(@RequestBody Analysis analysis) {
        analysisService.savePendingAnalysis(analysis);
    }

    @PostMapping("/update")
    public Analysis update(@Valid @RequestBody Analysis analysis) {
        return analysisService.updateByAnalysisId(analysis);
    }

    @PostMapping("/analysis")
    public Analysis save(@Valid @RequestBody Analysis analysis) {
        System.out.println(analysis.toString());
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

        Analysis analysis = analysisService.getAnalysisByAnalysisId(analysisId);
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

        Analysis analysis = analysisService.getAnalysisByAnalysisId(analysisId);
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

        Analysis analysis = analysisService.getAnalysisByAnalysisId(analysisId);
        return analysis.getBlastResults();
    }

    /**
     * Replacement for the ORFanGenesSummaryChart.json
     * @param analysisId
     * @return
     */
    @GetMapping("/data/summary-chart/{analysisId}")
    public SummaryChart getSummaryChart(@PathVariable @Valid String analysisId) {

        Analysis analysis = analysisService.getAnalysisByAnalysisId(analysisId);
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

    @GetMapping("/orfanbase-table")
    public @ResponseBody List<Genes> getAnalysedGenes() {
        List<Genes> genes = new ArrayList<>();
        List<Analysis> analyses = analysisService.findAllAnalysiss();
        analyses.forEach(analysis -> {
            Genes genes1 = new Genes();
            analysis.getGeneList().forEach(gene -> {
                genes1.setGeneId(gene.getGeneId());
                genes1.setDescription(gene.getDescription());
                genes1.setSequence(gene.getSequence());
                genes1.setGccontent((gene.getGccontent()));
                genes1.setLength(gene.getLength());
                genes1.setOrfanLevel(gene.getOrfanLevel());

            });
            genes1.setAnalysisDate(analysis.getAnalysisDate());
            genes1.setOrganism(analysis.getOrganism());
            genes1.setTaxonomyId(analysis.getTaxonomyId());
            genes1.setAnalysisId(analysis.getAnalysisId());
            genes.add(genes1);
        });
        return genes;
    }

    @PostMapping("/test")
    public void populateTestData() throws ParseException {
        Random rand = new Random();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        User user = User.builder()
                .firstName("orfanid")
                .lastName("orfanid")
                .email("orfanid@gmail.com").build();
//
        Gene gene = Gene.builder()
                .geneId("NP_415100.1")
                .description("bacteriophage N4 receptor, outer membrane protein [Escherichia coli str. K-12 substr. MG1655]")
                .orfanLevel("genus restricted gene")
                .build();

        Analysis analysis = new Analysis();
        analysis.setAnalysisId("1595716461278_VMS");
        analysis.setOrganism("Escherichia coli str. K-12 substr. MG1655");
        analysis.setTaxonomyId(562);
        analysis.setAnalysisDate(simpleDateFormat.parse("31/08/2020"));
        analysis.setSaved(true);
        analysis.setBlastResults("[{\"tree\":{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"name\":\"Shigella dysenteriae(1)\"},{\"name\":\"Shigella sonnei(1)\"}],\"name\":\"Shigella(2)\"},{\"children\":[{\"name\":\"Escherichia coli B185(1)\"},{\"name\":\"Escherichia coli(1)\"},{\"name\":\"Escherichia coli O43:H14(1)\"},{\"name\":\"Escherichia coli K-12(1)\"}],\"name\":\"Escherichia(4)\"},{\"children\":[{\"name\":\"Enterobacteriaceae bacterium(1)\"}],\"name\":\"(1)\"}],\"name\":\"Enterobacteriaceae(7)\"}],\"name\":\"Enterobacterales(7)\"}],\"name\":\"Gammaproteobacteria(7)\"}],\"name\":\"Proteobacteria(7)\"}],\"name\":\"(7)\"}],\"name\":\"Bacteria(1)\"}],\"name\":\"LUCA(1)\"},\"id\":\"NP_415100.1\"}]\\n");
        analysis.setUser(user);

        List<Gene> genes = new ArrayList<>();
        gene.setAnalysis(analysis);
        genes.add(gene);

        analysis.setGeneList(genes);
        analysis.setUser(user);

        analysisService.saveAnalysis(analysis);


        Gene gene2 = Gene.builder()
                .geneId("NP_524859.2")
                .description("male-specific opa containing gene, isoform A [Drosophila melanogaster]")
                .orfanLevel("Strict ORFan")
                .build();

        Analysis analysis2 = new Analysis();
        analysis2.setAnalysisId("1598083731654_WiP");
        analysis2.setOrganism("Drosophila melanogaster");
        analysis2.setTaxonomyId(7227);
        analysis2.setAnalysisDate(simpleDateFormat.parse("30/08/2020"));
        analysis2.setSaved(true);
        analysis2.setBlastResults("[{\"tree\":{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"name\":\"Drosophila melanogaster(1)\"},{\"name\":\"Drosophila simulans(1)\"},{\"name\":\"Drosophila mauritiana(1)\"},{\"name\":\"Drosophila sechellia(1)\"}],\"name\":\"Drosophila(4)\"}],\"name\":\"Drosophilidae(4)\"}],\"name\":\"Diptera(4)\"}],\"name\":\"Insecta(4)\"}],\"name\":\"Arthropoda(4)\"}],\"name\":\"Metazoa(4)\"}],\"name\":\"Eukaryota(1)\"}],\"name\":\"LUCA(1)\"},\"id\":\"NP_524859.2\"}]");
        analysis2.setUser(user);

        List<Gene> genes2 = new ArrayList<>();
        gene2.setAnalysis(analysis2);
        genes2.add(gene2);

        analysis2.setGeneList(genes2);
        analysis2.setUser(user);

        analysisService.saveAnalysis(analysis2);
        System.out.println("Data saved!");
    }
}