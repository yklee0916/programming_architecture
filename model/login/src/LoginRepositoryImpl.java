package model.login.src;

import model.login.LoginRepository;
import model.login.LoginException;
import model.login.LoginErrorMessages;
import model.login.UserDto;

public class LoginRepositoryImpl implements LoginRepository {
    private final LoginRemoteDataSource remoteDataSource;
    private final UserLocalDataSource localDataSource;

    public LoginRepositoryImpl(LoginRemoteDataSource remote, UserLocalDataSource local) {
        this.remoteDataSource = remote;
        this.localDataSource = local;
    }

    @Override
    public UserDto login(String username, String password) throws LoginException {
        try {
            UserVo userVo = remoteDataSource.login(username, password);
            return new UserDto(userVo.getUsername());
        } catch (Exception e) {
            throw new LoginException(LoginErrorMessages.LOGIN_FAILED + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void saveUser(UserDto user) {
        UserDo userDo = new UserDo(user.getUsername());
        localDataSource.saveUser(userDo);
    }
}
