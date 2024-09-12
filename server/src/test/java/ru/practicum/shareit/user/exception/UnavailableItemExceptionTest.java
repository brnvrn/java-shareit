package ru.practicum.shareit.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnavailableItemExceptionTest {

    @Test
    void testUnavailableItemExceptionMessage() {
        String expectedMessage = "Вещь недоступна";

        UnavailableItemException exception = new UnavailableItemException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testUnavailableItemExceptionIsRuntimeException() {
        UnavailableItemException exception = new UnavailableItemException("Вещь недоступна");

        assertTrue(exception instanceof RuntimeException);
    }
}