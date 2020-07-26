package com.orfangenes.db.orfanbasedb.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
public class Gene extends AuditModel implements Serializable {

    @Id
    @SequenceGenerator(name = "GENES_SEQ", sequenceName = "GENES_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "GENES_SEQ", strategy = GenerationType.AUTO)
    private Long id;
    private String geneId;
    private String sequence;
    private String description;
    private double gcContent;
    private int length;
    private String orfanLevel;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne(optional = false)
    @JoinColumn(name = "analysis_id", nullable = false, updatable = false)
    private Analysis geneParent;


}
