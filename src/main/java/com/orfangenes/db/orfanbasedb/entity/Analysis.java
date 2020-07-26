package com.orfangenes.db.orfanbasedb.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Suresh Hewapathirana
 */
@Entity
@Table(name="analysis")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Analysis extends AuditModel {

    @Id
    @SequenceGenerator(name = "ANALYSIS_SEQ", sequenceName = "ANALYSIS_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ANALYSIS_SEQ", strategy = GenerationType.AUTO)
    private Long id;
    private String analysisId;
    private Date analysisDate;
    private String organism;
    private int taxonomyId;
    private boolean isSaved = false;
    private String blastResults;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ToString.Exclude
    @OneToMany(mappedBy = "geneParent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Gene> geneList = new ArrayList<>();

}
