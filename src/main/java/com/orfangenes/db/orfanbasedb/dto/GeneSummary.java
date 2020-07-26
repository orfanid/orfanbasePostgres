package com.orfangenes.db.orfanbasedb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class GeneSummary {

    private String type;
    private int count;
}
