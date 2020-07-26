package com.orfangenes.db.orfanbasedb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class ORFanGenes {
    String geneid;
    String description;
    String orfanLevel;
}
