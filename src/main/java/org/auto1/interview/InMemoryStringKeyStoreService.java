package org.auto1.interview;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStringKeyStoreService implements KeyStoreService<String, String> {

    static final int ALLOWED_KEY_LENGTH = 20;

    private final Map<String, String> keyStoreMap = new ConcurrentHashMap<>();

    @Override
    public String store(String key, String value) {
        if (key == null) {
            key = generateRandomKey();
        }
        var lowerCaseKey = key.toLowerCase();
        if (lowerCaseKey.length() > ALLOWED_KEY_LENGTH || lowerCaseKey.isBlank()) {
            throw new IllegalArgumentException("Key cannot be null, nor blank, nor longer than %s characters".formatted(ALLOWED_KEY_LENGTH));
        } else if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("The given value cannot be null or empty");
        }
        else if (keyStoreMap.containsKey(lowerCaseKey)) {
            throw new DuplicatedKeyException(lowerCaseKey);
        }
        keyStoreMap.put(lowerCaseKey, value);
        return lowerCaseKey;
    }

    @Override
    public Optional<String> retrieve(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key cannot be null, nor blank");
        }
        return Optional.ofNullable(keyStoreMap.get(key.toLowerCase()));
    }

    @Override
    public void clearAll() {
        keyStoreMap.clear();
    }

    private String generateRandomKey() {
        return UUID.randomUUID().toString()
                .substring(0, ALLOWED_KEY_LENGTH);
    }
}
