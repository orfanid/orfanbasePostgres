package com.orfangenes.repo.ws.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.orfangenes.repo.ws.util.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Suresh Hewapathirana
 */
@Data
@AllArgsConstructor
public class AnalysisResultsTableRaw {

    private String analysisId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm")
    private Date analysisDate;
    private String organism;
    private String email;
    private int numberOfGenes;
    private Constants.AnalysisStatus status;
}
