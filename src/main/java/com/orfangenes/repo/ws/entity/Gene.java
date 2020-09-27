package com.orfangenes.repo.ws.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Suresh Hewapathirana
 */
@Entity
@Table(name="genes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "GENES_SEQ", sequenceName = "GENES_SEQ", allocationSize = 1)
public class Gene extends AuditModel implements Serializable {

    @Id
    @GeneratedValue(generator = "GENES_SEQ", strategy = GenerationType.AUTO)
    private Long id;
    private String geneId;
    @Column(columnDefinition="TEXT")
    private String sequence;
    private String description;
    private String orfanLevel;
    @Column(nullable = true)
    private int taxonomyId;
    @Column(nullable = true)
    private double gccontent = 0.00;
    @Column(nullable = true)
    private int length;
    @Column(nullable = true)
    private int startLocation;
    @Column(nullable = true)
    private int endLocation;

    @ManyToOne
    @JoinColumn(name = "analysis_id")
    private Analysis analysis;

    @JsonBackReference
    public Analysis getAnalysis() {
        return analysis;
    }
}
