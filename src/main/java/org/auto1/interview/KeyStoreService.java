package org.auto1.interview;

import java.util.Optional;

public interface KeyStoreService<T> {

    T store(T key, String value);

    Optional<String> retrieve(T key);

    void clearAll();
}
