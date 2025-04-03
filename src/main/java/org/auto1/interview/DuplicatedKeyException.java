package org.auto1.interview;

public class DuplicatedKeyException extends RuntimeException {

    private static final String MESSAGE_FORMAT = "The given key already exists {key={%s}}";

    public DuplicatedKeyException(Object key) {
        super(MESSAGE_FORMAT.formatted(key));
    }
}
