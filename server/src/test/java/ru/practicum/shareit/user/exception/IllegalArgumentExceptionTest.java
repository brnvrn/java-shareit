package ru.practicum.shareit.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IllegalArgumentExceptionTest {

    @Test
    void testIllegalArgumentExceptionMessage() {
        String expectedMessage = "Illegal argument exception";

        IllegalArgumentException exception = new IllegalArgumentException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testIllegalArgumentExceptionIsRuntimeException() {
        IllegalArgumentException exception = new IllegalArgumentException("Ошибка");

        assertTrue(exception instanceof RuntimeException);
    }
}
