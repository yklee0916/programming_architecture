package tests.model.login;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserDtoTest {
    @Test
    public void testConstructor_WithValidUsername_ShouldCreateUserDto() {
        // When
        UserDto userDto = new UserDto("testuser");

        // Then
        assertNotNull("UserDto should not be null", userDto);
        assertEquals("testuser", userDto.getUsername());
    }

    @Test
    public void testGetUsername_ShouldReturnCorrectUsername() {
        // Given
        UserDto userDto = new UserDto("testuser");

        // When
        String username = userDto.getUsername();

        // Then
        assertEquals("testuser", username);
    }

    @Test
    public void testConstructor_WithDifferentUsernames_ShouldCreateDifferentInstances() {
        // When
        UserDto user1 = new UserDto("user1");
        UserDto user2 = new UserDto("user2");

        // Then
        assertNotEquals("Different usernames should create different instances", 
            user1.getUsername(), user2.getUsername());
    }
}
