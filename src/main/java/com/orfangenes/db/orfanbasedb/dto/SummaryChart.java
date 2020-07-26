package com.orfangenes.db.orfanbasedb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class SummaryChart {
    String[] x;
    Integer[] y;
}
