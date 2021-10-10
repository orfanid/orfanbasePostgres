package com.orfangenes.repo.ws.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.orfangenes.repo.ws.util.Constants;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@SequenceGenerator(name = "ANALYSIS_SEQ", sequenceName = "ANALYSIS_SEQ", allocationSize = 1)
public class Analysis extends AuditModel {

    @Id
    @GeneratedValue(generator = "ANALYSIS_SEQ", strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Column(unique = true)
    private String analysisId;
//    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date analysisDate;
    @NotBlank
    @Size(min = 3, max = 100)
    private String organism;
    private int taxonomyId;
    private boolean isSaved = false;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private String blastResults;
    private int evalue;
    private int maximumTargetSequences;
    private int identity;
    private String sequenceType;
    @Enumerated(EnumType.STRING)
    private Constants.AnalysisStatus status;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "analysis_id")
    @ToString.Exclude
    private List<Gene> geneList = new ArrayList<>();

    @JsonManagedReference
    public List<Gene> getGeneList() {
        return geneList;
    }
}
