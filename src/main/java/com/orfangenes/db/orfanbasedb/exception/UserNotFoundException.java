package com.orfangenes.db.orfanbasedb.exception;

/**
 * @author Suresh Hewapathirana
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super("Could not find user " + id);
    }
}