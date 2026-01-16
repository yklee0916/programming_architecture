package model.login;

public class UserDto {
    private final String username;

    public UserDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
