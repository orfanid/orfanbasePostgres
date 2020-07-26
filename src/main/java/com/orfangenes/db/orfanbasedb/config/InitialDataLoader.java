package com.orfangenes.db.orfanbasedb.config;

import com.orfangenes.db.orfanbasedb.entity.Analysis;
import com.orfangenes.db.orfanbasedb.entity.Gene;
import com.orfangenes.db.orfanbasedb.entity.User;
import com.orfangenes.db.orfanbasedb.service.AnalysisService;
import com.orfangenes.db.orfanbasedb.service.GeneService;
import com.orfangenes.db.orfanbasedb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Suresh Hewapathirana
 */
@Configuration
@EnableJpaAuditing
public class InitialDataLoader {

    @Autowired
    GeneService geneService;

    @Autowired
    AnalysisService analysisService;

    @Autowired
    UserService userService;

    @Bean
    public CommandLineRunner loadData() {
        return (args) -> {

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
            analysis.setUser(user);

            List<Gene> genes = new ArrayList<>();
            gene.setGeneParent(analysis);
            genes.add(gene);

            analysis.setGeneList(genes);

            analysisService.saveAnalysis(analysis);
        };
    }
}
