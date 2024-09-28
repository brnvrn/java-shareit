package ru.practicum.shareit.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotUniqueEmailExceptionTest {

    @Test
    void testNotUniqueEmailExceptionMessage() {
        String expectedMessage = "Эмейл уже существует";

        NotUniqueEmailException exception = new NotUniqueEmailException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testNotUniqueEmailExceptionIsRuntimeException() {
        NotUniqueEmailException exception = new NotUniqueEmailException("Эмейл уже существует");

        assertTrue(exception instanceof RuntimeException);
    }
}
