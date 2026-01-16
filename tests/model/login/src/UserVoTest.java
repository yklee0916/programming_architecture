package tests.model.login.src;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserVoTest {
    @Test
    public void testConstructor_WithValidUsername_ShouldCreateUserVo() {
        // When
        UserVo userVo = new UserVo("testuser");

        // Then
        assertNotNull("UserVo should not be null", userVo);
        assertEquals("testuser", userVo.getUsername());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_WithNullUsername_ShouldThrowException() {
        // When & Then
        new UserVo(null);
    }

    @Test
    public void testEquals_WithSameUsername_ShouldReturnTrue() {
        // Given
        UserVo user1 = new UserVo("testuser");
        UserVo user2 = new UserVo("testuser");

        // When & Then
        assertEquals("Users with same username should be equal", user1, user2);
    }

    @Test
    public void testEquals_WithDifferentUsername_ShouldReturnFalse() {
        // Given
        UserVo user1 = new UserVo("testuser");
        UserVo user2 = new UserVo("otheruser");

        // When & Then
        assertNotEquals("Users with different username should not be equal", user1, user2);
    }

    @Test
    public void testEquals_WithNull_ShouldReturnFalse() {
        // Given
        UserVo user = new UserVo("testuser");

        // When & Then
        assertNotEquals("User should not equal null", user, null);
    }

    @Test
    public void testHashCode_WithSameUsername_ShouldReturnSameHash() {
        // Given
        UserVo user1 = new UserVo("testuser");
        UserVo user2 = new UserVo("testuser");

        // When & Then
        assertEquals("Users with same username should have same hash code", 
            user1.hashCode(), user2.hashCode());
    }

    @Test
    public void testHashCode_WithDifferentUsername_ShouldReturnDifferentHash() {
        // Given
        UserVo user1 = new UserVo("testuser");
        UserVo user2 = new UserVo("otheruser");

        // When & Then
        assertNotEquals("Users with different username should have different hash code", 
            user1.hashCode(), user2.hashCode());
    }
}
