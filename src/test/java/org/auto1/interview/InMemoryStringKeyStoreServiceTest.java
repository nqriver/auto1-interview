package org.auto1.interview;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.auto1.interview.InMemoryKeyStoreService.ALLOWED_KEY_LENGTH;

class InMemoryStringKeyStoreServiceTest {

    private final KeyStoreService<String> keyStoreService = new InMemoryKeyStoreService();

    @ParameterizedTest
    @ValueSource(strings = {"KEY", "key"})
    void shouldStoreSecretWithCaseInsensitiveKeyWhenKeyIsValid(String validKey) {
        // given
        var givenValue = "value";

        // when
        var actualKey = keyStoreService.store(validKey, givenValue);

        // then
        Assertions.assertThat(actualKey)
                .isEqualTo(validKey.toLowerCase());

        Assertions.assertThat(keyStoreService.retrieve(validKey)).isPresent()
                .get()
                .isEqualTo("value");
    }

    @Test
    void shouldRejectStoringSecretWhenTheKeyIsTooLong() {
        // given
        var tooLongKey = "a".repeat(ALLOWED_KEY_LENGTH + 1);
        var validValue = "value";
        // when and then
        Assertions.assertThatThrownBy(() -> keyStoreService.store(tooLongKey, validValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null, nor blank, nor longer than %s characters".formatted(ALLOWED_KEY_LENGTH));
    }

    @Test
    void shouldRejectStoringSecretWhenTheKeyIsBlank() {
        // given
        var blankKey = " ".repeat(ALLOWED_KEY_LENGTH - 1);
        var validValue = "value";
        // when and then
        Assertions.assertThatThrownBy(() -> keyStoreService.store(blankKey, validValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Key cannot be null, nor blank, nor longer than %s characters".formatted(ALLOWED_KEY_LENGTH));
    }

    @Test
    void shouldGenerateNewRandomKeyAndStoreSecretWhenGivenKeyIsNull() {
        // given
        String nullKey = null;
        var validValue = "value";

        // when
        var storedKey = keyStoreService.store(nullKey, validValue);

        // then
        Assertions.assertThat(storedKey)
                .isNotBlank();

        Assertions.assertThat(keyStoreService.retrieve(storedKey)).isPresent()
                .get()
                .isEqualTo("value");
    }

    @Test
    void shouldRejectStoringSecretWhenTheValueIsNull() {
        // given
        String invalidValue = null;
        var validKey = "validKey";

        // when and then
        Assertions.assertThatThrownBy(() -> keyStoreService.store(validKey, invalidValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectStoringSecretWhenTheValueIsEmpty() {
        // given
        String invalidValue = "";
        var validKey = "validKey";

        // when and then
        Assertions.assertThatThrownBy(() -> keyStoreService.store(validKey, invalidValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectStoringSecretWhenTheKeyIsAlreadyPresent() {
        // given
        var invalidKey = "key";
        keyStoreService.store(invalidKey, "value");

        // when and then
        Assertions.assertThatThrownBy(() -> keyStoreService.store(invalidKey, "value2"))
                .isInstanceOf(DuplicatedKeyException.class)
                .hasMessageContaining("The given key already exists {key={key}}");
    }

    @Test
    void shouldReturnSecretCaseInsensitivelyByKeyIfItsPresent() {
        // given
        var validKey = "key";
        var validKeyUpperCase = validKey.toUpperCase();
        keyStoreService.store(validKey, "value");

        // when
        var key = keyStoreService.retrieve(validKeyUpperCase);

        // then
        Assertions.assertThat(key).isPresent()
                .get()
                .isEqualTo("value");
    }

    @Test
    void shouldNotReturnSecretIfItIsNotPresent() {
        // when
        var key = keyStoreService.retrieve("nonexistent");

        // then
        Assertions.assertThat(key).isNotPresent();
    }
}
