package org.auto1.interview;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InMemoryIntegerKeyStoreServiceTest {

    private final KeyStoreService<Integer, String> keyStoreService = new InMemoryIntegerKeyStoreService();

    @Test
    void shouldStoreKeyValuePairWhenKeyIsValid() {
        // given
        var givenValue = "value";
        var givenKey = 1;

        // when
        var actualKey = keyStoreService.store(givenKey, givenValue);

        // then
        Assertions.assertThat(actualKey).isEqualTo(givenKey);
        Assertions.assertThat(keyStoreService.retrieve(givenKey)).isPresent()
                .get()
                .isEqualTo("value");
    }

}
