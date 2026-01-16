package model.login;

import java.util.Optional;
import java.util.concurrent.Executor;

public class LoginUseCase {
    public interface Callback {
        void onSuccess(UserDto user);
        void onFailure(String errorMessage);
    }

    private final LoginRepository repository;
    private final Optional<Executor> executor;

    public LoginUseCase(LoginRepository repository) {
        this(repository, null);
    }

    public LoginUseCase(LoginRepository repository, Executor executor) {
        this.repository = repository;
        this.executor = Optional.ofNullable(executor);
    }

    public void login(String username, String password, Callback callback) {
        String validationError = validateInput(username, password);
        if (validationError != null) {
            callback.onFailure(validationError);
            return;
        }

        Runnable loginTask = () -> {
            try {
                UserDto user = repository.login(username, password);
                repository.saveUser(user);
                callback.onSuccess(user);
            } catch (LoginException e) {
                callback.onFailure(e.getMessage());
            }
        };

        if (executor.isPresent()) {
            executor.get().execute(loginTask);
        } else {
            Thread loginThread = new Thread(loginTask, "LoginUseCase-Thread");
            loginThread.start();
        }
    }

    private String validateInput(String username, String password) {
        if (username == null || username.isEmpty()) {
            return LoginErrorMessages.USERNAME_EMPTY;
        }
        if (password == null || password.length() < 4) {
            return LoginErrorMessages.PASSWORD_TOO_SHORT;
        }
        return null;
    }
}
