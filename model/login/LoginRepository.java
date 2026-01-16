package model.login;

public interface LoginRepository {
    UserDto login(String username, String password) throws LoginException;
    void saveUser(UserDto user);
}
