package com.example.jobfinder.exceptions;

import java.text.MessageFormat;
import java.util.function.Supplier;

import static java.text.MessageFormat.*;

public class ValueNotFoundException extends RuntimeException {

    public ValueNotFoundException(String id) {
        super(format("Object with id {0} not found", id));
    }
}
