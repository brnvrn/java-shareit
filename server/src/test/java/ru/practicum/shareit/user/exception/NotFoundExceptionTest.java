package ru.practicum.shareit.user.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void testNotFoundExceptionMessage() {
        String expectedMessage = "Not found";

        NotFoundException exception = new NotFoundException(expectedMessage);

        assertNotNull(exception);
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testNotFoundExceptionIsRuntimeException() {
        NotFoundException exception = new NotFoundException("Не найдено");

        assertTrue(exception instanceof RuntimeException);
    }
}
