package tests.model.login;

import org.junit.Before;
import org.junit.Test;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class LoginUseCaseTest {
    private MockLoginRepository mockRepository;
    private LoginUseCase loginUseCase;

    @Before
    public void setUp() {
        mockRepository = new MockLoginRepository();
    }

    @Test
    public void testLogin_WithValidInput_ShouldSucceed() {
        // Given
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean successCalled = new AtomicBoolean(false);
        AtomicReference<UserDto> resultUser = new AtomicReference<>();

        // When
        loginUseCase.login("testuser", "password123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                successCalled.set(true);
                resultUser.set(user);
            }

            @Override
            public void onFailure(String errorMessage) {
                fail("Should not call onFailure");
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onSuccess should be called", successCalled.get());
        assertNotNull("User should not be null", resultUser.get());
        assertEquals("testuser", resultUser.get().getUsername());
        assertTrue("saveUser should be called", mockRepository.isSaveUserCalled());
    }

    @Test
    public void testLogin_WithEmptyUsername_ShouldFail() {
        // Given
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean failureCalled = new AtomicBoolean(false);
        AtomicReference<String> errorMessage = new AtomicReference<>();

        // When
        loginUseCase.login("", "password123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                fail("Should not call onSuccess");
            }

            @Override
            public void onFailure(String error) {
                failureCalled.set(true);
                errorMessage.set(error);
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onFailure should be called", failureCalled.get());
        assertEquals(LoginErrorMessages.USERNAME_EMPTY, errorMessage.get());
        assertFalse("saveUser should not be called", mockRepository.isSaveUserCalled());
    }

    @Test
    public void testLogin_WithNullUsername_ShouldFail() {
        // Given
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean failureCalled = new AtomicBoolean(false);

        // When
        loginUseCase.login(null, "password123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                fail("Should not call onSuccess");
            }

            @Override
            public void onFailure(String error) {
                failureCalled.set(true);
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onFailure should be called", failureCalled.get());
    }

    @Test
    public void testLogin_WithShortPassword_ShouldFail() {
        // Given
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean failureCalled = new AtomicBoolean(false);
        AtomicReference<String> errorMessage = new AtomicReference<>();

        // When
        loginUseCase.login("testuser", "123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                fail("Should not call onSuccess");
            }

            @Override
            public void onFailure(String error) {
                failureCalled.set(true);
                errorMessage.set(error);
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onFailure should be called", failureCalled.get());
        assertEquals(LoginErrorMessages.PASSWORD_TOO_SHORT, errorMessage.get());
    }

    @Test
    public void testLogin_WithNullPassword_ShouldFail() {
        // Given
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean failureCalled = new AtomicBoolean(false);

        // When
        loginUseCase.login("testuser", null, new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                fail("Should not call onSuccess");
            }

            @Override
            public void onFailure(String error) {
                failureCalled.set(true);
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onFailure should be called", failureCalled.get());
    }

    @Test
    public void testLogin_WithRepositoryException_ShouldFail() {
        // Given
        mockRepository.setShouldThrowException(true);
        loginUseCase = new LoginUseCase(mockRepository);
        AtomicBoolean failureCalled = new AtomicBoolean(false);
        AtomicReference<String> errorMessage = new AtomicReference<>();

        // When
        loginUseCase.login("testuser", "password123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                fail("Should not call onSuccess");
            }

            @Override
            public void onFailure(String error) {
                failureCalled.set(true);
                errorMessage.set(error);
            }
        });

        // Wait for async operation
        waitForAsync();

        // Then
        assertTrue("onFailure should be called", failureCalled.get());
        assertNotNull("Error message should not be null", errorMessage.get());
    }

    @Test
    public void testLogin_WithSynchronousExecutor_ShouldExecuteImmediately() {
        // Given
        Executor synchronousExecutor = Runnable::run; // Execute immediately
        loginUseCase = new LoginUseCase(mockRepository, synchronousExecutor);
        AtomicBoolean successCalled = new AtomicBoolean(false);

        // When
        loginUseCase.login("testuser", "password123", new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                successCalled.set(true);
            }

            @Override
            public void onFailure(String errorMessage) {
                fail("Should not call onFailure");
            }
        });

        // Then - With synchronous executor, should complete immediately
        assertTrue("onSuccess should be called synchronously", successCalled.get());
    }

    private void waitForAsync() {
        try {
            Thread.sleep(100); // Wait for async operations
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Mock Repository for testing
    private static class MockLoginRepository implements LoginRepository {
        private boolean shouldThrowException = false;
        private boolean saveUserCalled = false;

        public void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        public boolean isSaveUserCalled() {
            return saveUserCalled;
        }

        @Override
        public UserDto login(String username, String password) throws LoginException {
            if (shouldThrowException) {
                throw new LoginException("Mock exception");
            }
            return new UserDto(username);
        }

        @Override
        public void saveUser(UserDto user) {
            saveUserCalled = true;
        }
    }
}
