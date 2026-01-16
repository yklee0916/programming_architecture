package viewmodel;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import model.LoginUseCase;
import model.UserStorage;

public class LoginViewModel {
  private final LoginUseCase loginUseCase;
  private final UserStorage userStorage;
  private final ObservableField<String> username = new ObservableField<>("");
  private final ObservableField<String> password = new ObservableField<>("");
  private final ObservableField<String> errorMessage = new ObservableField<>("");
  private final ObservableBoolean loginEnabled = new ObservableBoolean(true);
  private final ObservableBoolean navigateToMain = new ObservableBoolean(false);

  public LoginViewModel(LoginUseCase loginUseCase, UserStorage userStorage) {
    this.loginUseCase = loginUseCase;
    this.userStorage = userStorage;
  }

  public ObservableField<String> getUsername() {
    return username;
  }

  public ObservableField<String> getPassword() {
    return password;
  }

  public ObservableField<String> getErrorMessage() {
    return errorMessage;
  }

  public ObservableBoolean getLoginEnabled() {
    return loginEnabled;
  }

  public ObservableBoolean getNavigateToMain() {
    return navigateToMain;
  }

  public void onLoginClicked() {
    String currentUsername = safeValue(username);
    String currentPassword = safeValue(password);
    errorMessage.set("");
    loginEnabled.set(false);
    loginUseCase.login(currentUsername, currentPassword, new LoginUseCase.Callback() {
      @Override
      public void onSuccess(String user) {
        userStorage.save(user);
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
