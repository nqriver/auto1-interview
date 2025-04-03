package org.auto1.interview;

import java.util.Optional;

public interface KeyStoreService<K, V> {

    K store(K key, String value);

    Optional<V> retrieve(K key);

    void clearAll();
}
