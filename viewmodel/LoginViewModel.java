package viewmodel;

import model.Constants;
import model.LoginUseCase;
import model.UserStorage;

public class LoginViewModel {
  private final LoginUseCase loginUseCase;
  private final UserStorage userStorage;
  private final ObservableValue<String> errorMessage = new ObservableValue<>();
  private final ObservableValue<Boolean> loginEnabled = new ObservableValue<>();
  private final ObservableValue<Boolean> navigateToMain = new ObservableValue<>();

  public LoginViewModel(LoginUseCase loginUseCase, UserStorage userStorage) {
    this.loginUseCase = loginUseCase;
    this.userStorage = userStorage;
    loginEnabled.setValue(true);
  }

  public ObservableValue<String> getErrorMessage() {
    return errorMessage;
  }

  public ObservableValue<Boolean> getLoginEnabled() {
    return loginEnabled;
  }

  public ObservableValue<Boolean> getNavigateToMain() {
    return navigateToMain;
  }

  public void onLoginClicked(String username, String password) {
    errorMessage.setValue(null);
    loginEnabled.setValue(false);
    if (!validateInput(username, password)) {
      loginEnabled.setValue(true);
      return;
    }
    loginUseCase.login(username, password, new LoginUseCase.Callback() {
      @Override
      public void onSuccess(String user) {
        userStorage.save(user);
        navigateToMain.setValue(true);
      }

      @Override
      public void onFailure(String error) {
        errorMessage.setValue(error);
        loginEnabled.setValue(true);
      }
    });
  }

  public void onNavigateHandled() {
    navigateToMain.setValue(false);
  }

  private boolean validateInput(String username, String password) {
    if (username.isEmpty()) {
      errorMessage.setValue(Constants.ERROR_USERNAME_EMPTY);
      return false;
    }
    if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
      errorMessage.setValue(Constants.ERROR_PASSWORD_TOO_SHORT);
      return false;
    }
    return true;
  }
}
