//package com.orfangenes.db.orfanbasedb.Controller;
//
//
//import com.orfangenes.repo.entity.Analysis;
//import com.orfangenes.repo.entity.Gene;
//import com.orfangenes.repo.entity.User;
//import com.orfangenes.repo.ws.service.AnalysisService;
//import com.orfangenes.repo.ws.service.GeneService;
//import com.orfangenes.repo.ws.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
///**
// * @author Suresh Hewapathirana
// */
//class AnalysisControllerTest {
//
//    @Autowired
//    GeneService geneService;
//
//    @Autowired
//    AnalysisService analysisService;
//
//    @Autowired
//    UserService userService;
//
//    @BeforeEach
//    void setUp() throws ParseException {
//
//        Random rand = new Random();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//        User user = User.builder()
//                .firstName("Suresh")
//                .lastName("Hewapathirana")
//                .email("sureshhewabi@gmail.com").build();
//        user = userService.saveUser(user);
//
//        Gene gene = Gene.builder()
//                .geneId("NP_415100.1")
//                .description("bacteriophage N4 receptor, outer membrane protein [Escherichia coli str. K-12 substr. MG1655]")
//                .orfanLevel("genus restricted gene")
//                .build();
//
//
//
//        Analysis analysis = new Analysis();
//        analysis.setAnalysisId("1595716461278_VMO");
//        analysis.setOrganism("Escherichia coli str. K-12 substr. MG1655");
//        analysis.setTaxonomyId(562);
//        analysis.setAnalysisDate(simpleDateFormat.parse("22/07/2020"));
//        analysis.setSaved(true);
//        analysis.setBlastResults("[{\"tree\":{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"children\":[{\"name\":\"Shigella dysenteriae(1)\"},{\"name\":\"Shigella sonnei(1)\"}],\"name\":\"Shigella(2)\"},{\"children\":[{\"name\":\"Escherichia coli B185(1)\"},{\"name\":\"Escherichia coli(1)\"},{\"name\":\"Escherichia coli O43:H14(1)\"},{\"name\":\"Escherichia coli K-12(1)\"}],\"name\":\"Escherichia(4)\"},{\"children\":[{\"name\":\"Enterobacteriaceae bacterium(1)\"}],\"name\":\"(1)\"}],\"name\":\"Enterobacteriaceae(7)\"}],\"name\":\"Enterobacterales(7)\"}],\"name\":\"Gammaproteobacteria(7)\"}],\"name\":\"Proteobacteria(7)\"}],\"name\":\"(7)\"}],\"name\":\"Bacteria(1)\"}],\"name\":\"LUCA(1)\"},\"id\":\"NP_415100.1\"}]\n");
//        analysis.setUser(user);
//
//        List<Gene> genes = new ArrayList<>();
//        gene.setGeneParent(analysis);
//        genes.add(gene);
//
//        analysis.setGeneList(genes);
//
//        analysisService.saveAnalysis(analysis);
//        System.out.println("Data saved!");
//
//    }
//
//    @Test
//    void getSummaryChart() {
//
//    }
//}