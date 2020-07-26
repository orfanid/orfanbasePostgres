package com.orfangenes.db.orfanbasedb.exception;

/**
 * @author Suresh Hewapathirana
 */
public class GeneNotFoundException extends RuntimeException {

    public GeneNotFoundException(Long id) {
        super("Could not find gene " + id);
    }
}