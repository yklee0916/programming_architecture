package viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import model.login.UserDto;
import model.login.LoginUseCase;

public class LoginViewModel {
    private final LoginUseCase loginUseCase;
    private final ObservableField<String> username = new ObservableField<>("");
    private final ObservableField<String> password = new ObservableField<>("");
    private final ObservableField<String> errorMessage = new ObservableField<>("");
    private final ObservableBoolean loginEnabled = new ObservableBoolean(true);
    private final ObservableBoolean navigateToMain = new ObservableBoolean(false);

    public LoginViewModel(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    public ObservableField<String> getUsername() { return username; }
    public ObservableField<String> getPassword() { return password; }
    public ObservableField<String> getErrorMessage() { return errorMessage; }
    public ObservableBoolean getLoginEnabled() { return loginEnabled; }
    public ObservableBoolean getNavigateToMain() { return navigateToMain; }

    public void onLoginClicked() {
        String currentUsername = safeValue(username);
        String currentPassword = safeValue(password);
        errorMessage.set("");
        loginEnabled.set(false);

        loginUseCase.login(currentUsername, currentPassword, new LoginUseCase.Callback() {
            @Override
            public void onSuccess(UserDto user) {
                navigateToMain.set(true);
            }

            @Override
            public void onFailure(String error) {
                errorMessage.set(error);
                loginEnabled.set(true);
            }
        });
    }

    public void onNavigateHandled() {
        navigateToMain.set(false);
    }

    private String safeValue(ObservableField<String> field) {
        String value = field.get();
        return value == null ? "" : value;
    }
}
