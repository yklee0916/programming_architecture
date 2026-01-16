package tests.viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import model.login.LoginUseCase;
import model.login.UserDto;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class LoginViewModelTest {
    private MockLoginUseCase mockUseCase;
    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        mockUseCase = new MockLoginUseCase();
        viewModel = new LoginViewModel(mockUseCase);
    }

    @Test
    public void testInitialState_ShouldHaveEmptyFields() {
        // Then
        assertEquals("", viewModel.getUsername().get());
        assertEquals("", viewModel.getPassword().get());
        assertEquals("", viewModel.getErrorMessage().get());
        assertTrue("Login should be enabled initially", viewModel.getLoginEnabled().get());
        assertFalse("Should not navigate initially", viewModel.getNavigateToMain().get());
    }

    @Test
    public void testOnLoginClicked_WithSuccess_ShouldNavigate() {
        // Given
        mockUseCase.setShouldSucceed(true);
        viewModel.getUsername().set("testuser");
        viewModel.getPassword().set("password123");

        // When
        viewModel.onLoginClicked();

        // Wait for async callback
        waitForAsync();

        // Then
        assertTrue("Should navigate to main", viewModel.getNavigateToMain().get());
        assertEquals("", viewModel.getErrorMessage().get());
        assertFalse("Login should be disabled during login", viewModel.getLoginEnabled().get());
    }

    @Test
    public void testOnLoginClicked_WithFailure_ShouldShowError() {
        // Given
        mockUseCase.setShouldSucceed(false);
        mockUseCase.setErrorMessage("Login failed");
        viewModel.getUsername().set("testuser");
        viewModel.getPassword().set("password123");

        // When
        viewModel.onLoginClicked();

        // Wait for async callback
        waitForAsync();

        // Then
        assertFalse("Should not navigate", viewModel.getNavigateToMain().get());
        assertEquals("Login failed", viewModel.getErrorMessage().get());
        assertTrue("Login should be re-enabled after failure", viewModel.getLoginEnabled().get());
    }

    @Test
    public void testOnLoginClicked_ShouldClearErrorMessage() {
        // Given
        viewModel.getErrorMessage().set("Previous error");
        mockUseCase.setShouldSucceed(true);
        viewModel.getUsername().set("testuser");
        viewModel.getPassword().set("password123");

        // When
        viewModel.onLoginClicked();

        // Then - Error message should be cleared immediately
        assertEquals("", viewModel.getErrorMessage().get());
    }

    @Test
    public void testOnLoginClicked_ShouldDisableLoginDuringRequest() {
        // Given
        mockUseCase.setShouldSucceed(true);
        mockUseCase.setDelay(50); // Add delay to test intermediate state
        viewModel.getUsername().set("testuser");
        viewModel.getPassword().set("password123");

        // When
        viewModel.onLoginClicked();

        // Then - Login should be disabled immediately
        assertFalse("Login should be disabled", viewModel.getLoginEnabled().get());

        // Wait for completion
        waitForAsync();
    }

    @Test
    public void testOnNavigateHandled_ShouldResetNavigateFlag() {
        // Given
        viewModel.getNavigateToMain().set(true);

        // When
        viewModel.onNavigateHandled();

        // Then
        assertFalse("Navigate flag should be reset", viewModel.getNavigateToMain().get());
    }

    @Test
    public void testOnLoginClicked_WithNullUsername_ShouldHandleGracefully() {
        // Given
        viewModel.getUsername().set(null);
        viewModel.getPassword().set("password123");

        // When
        viewModel.onLoginClicked();

        // Wait for async callback
        waitForAsync();

        // Then - Should handle null gracefully (safeValue converts to empty string)
        assertTrue("UseCase should be called", mockUseCase.isLoginCalled());
    }

    @Test
    public void testOnLoginClicked_WithNullPassword_ShouldHandleGracefully() {
        // Given
        viewModel.getUsername().set("testuser");
        viewModel.getPassword().set(null);

        // When
        viewModel.onLoginClicked();

        // Wait for async callback
        waitForAsync();

        // Then - Should handle null gracefully
        assertTrue("UseCase should be called", mockUseCase.isLoginCalled());
    }

    private void waitForAsync() {
        try {
            Thread.sleep(100); // Wait for async operations
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Mock UseCase for testing
    private static class MockLoginUseCase extends LoginUseCase {
        private boolean shouldSucceed = true;
        private String errorMessage = "Test error";
        private boolean loginCalled = false;
        private int delay = 0;

        public MockLoginUseCase() {
            super(new MockRepository()); // Repository needed for constructor
        }

        public void setShouldSucceed(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public void setDelay(int delay) {
            this.delay = delay;
        }

        public boolean isLoginCalled() {
            return loginCalled;
        }

        @Override
        public void login(String username, String password, Callback callback) {
            loginCalled = true;
            
            // Simulate async operation
            new Thread(() -> {
                try {
                    if (delay > 0) {
                        Thread.sleep(delay);
                    }
                    if (shouldSucceed) {
                        callback.onSuccess(new UserDto(username));
                    } else {
                        callback.onFailure(errorMessage);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        // Dummy repository for constructor
        private static class MockRepository implements model.login.LoginRepository {
            @Override
            public UserDto login(String username, String password) throws model.login.LoginException {
                return new UserDto(username);
            }

            @Override
            public void saveUser(UserDto user) {
                // Do nothing
            }
        }
    }
}
