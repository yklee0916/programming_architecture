package tests.model.login.src;

import model.login.LoginException;
import model.login.LoginErrorMessages;
import model.login.UserDto;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginRepositoryImplTest {
    private MockLoginRemoteDataSource mockRemoteDataSource;
    private MockUserLocalDataSource mockLocalDataSource;
    private LoginRepositoryImpl repository;

    @Before
    public void setUp() {
        mockRemoteDataSource = new MockLoginRemoteDataSource();
        mockLocalDataSource = new MockUserLocalDataSource();
        // Create repository with mock data sources using reflection or create a test-friendly constructor
        // For now, we'll need to create a test version or use a different approach
        // Since LoginRepositoryImpl requires specific types, we'll create a testable version
        repository = createTestRepository();
    }

    private LoginRepositoryImpl createTestRepository() {
        // Create a test repository that uses our mocks
        // We need to create a wrapper or use a test-friendly approach
        return new LoginRepositoryImpl(
            new LoginRemoteDataSource() {
                @Override
                public UserVo login(String username, String password) throws Exception {
                    return mockRemoteDataSource.login(username, password);
                }
            },
            new UserLocalDataSource(null) {
                @Override
                public void saveUser(UserDo user) {
                    mockLocalDataSource.saveUser(user);
                }
            }
        );
    }

    @Test
    public void testLogin_WithValidCredentials_ShouldReturnUserDto() throws LoginException {
        // Given
        String username = "testuser";
        String password = "password123";
        mockRemoteDataSource.setUsername(username);

        // When
        UserDto result = repository.login(username, password);

        // Then
        assertNotNull("UserDto should not be null", result);
        assertEquals(username, result.getUsername());
        assertTrue("Remote data source should be called", mockRemoteDataSource.isLoginCalled());
    }

    @Test
    public void testLogin_WithRemoteException_ShouldThrowLoginException() {
        // Given
        mockRemoteDataSource.setShouldThrowException(true);
        String username = "testuser";
        String password = "password123";

        // When & Then
        try {
            repository.login(username, password);
            fail("Should throw LoginException");
        } catch (LoginException e) {
            assertTrue("Error message should contain LOGIN_FAILED", 
                e.getMessage().contains(LoginErrorMessages.LOGIN_FAILED));
            assertNotNull("Cause should not be null", e.getCause());
        }
    }

    @Test
    public void testSaveUser_ShouldCallLocalDataSource() {
        // Given
        UserDto user = new UserDto("testuser");

        // When
        repository.saveUser(user);

        // Then
        assertTrue("Local data source should be called", mockLocalDataSource.isSaveUserCalled());
        assertEquals("testuser", mockLocalDataSource.getSavedUsername());
    }

    @Test
    public void testSaveUser_WithDifferentUsername_ShouldSaveCorrectly() {
        // Given
        UserDto user = new UserDto("anotheruser");

        // When
        repository.saveUser(user);

        // Then
        assertEquals("anotheruser", mockLocalDataSource.getSavedUsername());
    }

    // Mock Remote Data Source
    private static class MockLoginRemoteDataSource {
        private boolean loginCalled = false;
        private boolean shouldThrowException = false;
        private String username = "testuser";

        public void setShouldThrowException(boolean shouldThrow) {
            this.shouldThrowException = shouldThrow;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isLoginCalled() {
            return loginCalled;
        }

        public UserVo login(String username, String password) throws Exception {
            loginCalled = true;
            if (shouldThrowException) {
                throw new Exception("Network error");
            }
            return new UserVo(this.username);
        }
    }

    // Mock Local Data Source
    private static class MockUserLocalDataSource {
        private boolean saveUserCalled = false;
        private String savedUsername = null;

        public boolean isSaveUserCalled() {
            return saveUserCalled;
        }

        public String getSavedUsername() {
            return savedUsername;
        }

        public void saveUser(UserDo user) {
            saveUserCalled = true;
            savedUsername = user.getUsername();
        }
    }
}
