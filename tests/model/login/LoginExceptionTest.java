package tests.model.login;

import org.junit.Test;

import static org.junit.Assert.*;

public class LoginExceptionTest {
    @Test
    public void testConstructor_WithMessage_ShouldCreateException() {
        // When
        LoginException exception = new LoginException("Test error");

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Test error", exception.getMessage());
        assertNull("Cause should be null", exception.getCause());
    }

    @Test
    public void testConstructor_WithMessageAndCause_ShouldCreateExceptionWithCause() {
        // Given
        Throwable cause = new RuntimeException("Root cause");

        // When
        LoginException exception = new LoginException("Test error", cause);

        // Then
        assertNotNull("Exception should not be null", exception);
        assertEquals("Test error", exception.getMessage());
        assertEquals("Cause should be set", cause, exception.getCause());
    }

    @Test
    public void testException_ShouldBeInstanceOfException() {
        // When
        LoginException exception = new LoginException("Test error");

        // Then
        assertTrue("Should be instance of Exception", exception instanceof Exception);
    }
}
