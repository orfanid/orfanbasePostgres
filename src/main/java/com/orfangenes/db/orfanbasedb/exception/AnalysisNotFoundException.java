package com.orfangenes.db.orfanbasedb.exception;

/**
 * @author Suresh Hewapathirana
 */
public class AnalysisNotFoundException extends RuntimeException {

    public AnalysisNotFoundException(Long id) {
        super("Could not find Analysis " + id);
    }

    public AnalysisNotFoundException(String analysisId) {
        super("Could not find Analysis " + analysisId);
    }
}