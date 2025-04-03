package org.auto1.interview;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryIntegerKeyStoreService implements KeyStoreService<Integer> {

    private final Map<Integer, String> keyStoreMap = new ConcurrentHashMap<>();

    @Override
    public Integer store(Integer key, String value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null, nor blank, nor longer than 20 characters");
        } else if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The given value cannot be null or empty");
        } else if (keyStoreMap.containsKey(key)) {
            throw new DuplicatedKeyException(key);
        }
        keyStoreMap.put(key, value);
        return key;
    }

    @Override
    public Optional<String> retrieve(Integer key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null, nor blank");
        }
        return Optional.ofNullable(keyStoreMap.get(key));
    }

    @Override
    public void clearAll() {
        keyStoreMap.clear();
    }
}
