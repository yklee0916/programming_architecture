package presenter;

import model.Constants;
import model.LoginUseCase;
import model.UserStorage;

public class LoginPresenter implements LoginContract.Presenter {
  private final LoginUseCase loginModel;
  private final UserStorage userStorage;
  private LoginContract.View view;

  public LoginPresenter(LoginUseCase loginModel, UserStorage userStorage, LoginContract.View view) {
    this.loginModel = loginModel;
    this.userStorage = userStorage;
    this.view = view;
  }

  @Override
  public void onLoginClicked(String username, String password) {
    if (view == null) {
      return;
    }
    view.clearError();
    view.setLoginEnabled(false);
    if (!validateInput(username, password)) {
      view.setLoginEnabled(true);
      return;
    }
    loginModel.login(username, password, new LoginUseCase.Callback() {
      @Override
      public void onSuccess(String user) {
        if (view == null) {
          return;
        }
        view.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            userStorage.save(user);
            view.navigateToMain();
          }
        });
      }

      @Override
      public void onFailure(String errorMessage) {
        if (view == null) {
          return;
        }
        view.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            view.showError(errorMessage);
            view.setLoginEnabled(true);
          }
        });
      }
    });
  }

  @Override
  public void onDestroy() {
    view = null;
  }

  private boolean validateInput(String username, String password) {
    if (username.isEmpty()) {
      view.showError(Constants.ERROR_USERNAME_EMPTY);
      return false;
    }
    if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
      view.showError(Constants.ERROR_PASSWORD_TOO_SHORT);
      return false;
    }
    return true;
  }
}
