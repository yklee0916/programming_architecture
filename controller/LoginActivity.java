package controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import model.Constants;
import model.LoginModel;
import model.UserStorage;
import view.LoginView;

public class LoginActivity extends Activity {

  private LoginModel loginModel;
  private UserStorage userStorage;
  private LoginView loginView;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupLoginButton();
  }
  
  private void setupUI() {
    loginView = new LoginView(this);
    setContentView(loginView.createRootView());
    loginModel = new LoginModel();
    userStorage = new UserStorage(getApplicationContext());
  }
  
  private void setupLoginButton() {
    loginView.setOnLoginClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = loginView.getUsername();
        String password = loginView.getPassword();
        resetError();
        disableLoginButton();
        if (!validateInput(username, password)) {
          return;
        }
        performLogin(username, password);
      }
    });
  }
  
  private void resetError() {
    loginView.clearError();
  }
  
  private void disableLoginButton() {
    loginView.setLoginEnabled(false);
  }
  
  private void enableLoginButton() {
    loginView.setLoginEnabled(true);
  }
  
  private boolean validateInput(String username, String password) {
    if (username.isEmpty()) {
      showError(Constants.ERROR_USERNAME_EMPTY);
      enableLoginButton();
      return false;
    }
    if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
      showError(Constants.ERROR_PASSWORD_TOO_SHORT);
      enableLoginButton();
      return false;
    }
    return true;
  }
  
  private void showError(String message) {
    loginView.showError(message);
  }
  
  private void performLogin(String username, String password) {
    loginModel.login(username, password, new LoginModel.LoginCallback() {
      @Override
      public void onSuccess(String user) {
        LoginActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            userStorage.save(user);
            navigateToMain();
          }
        });
      }

      @Override
      public void onFailure(String errorMessage) {
        LoginActivity.this.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            showError(errorMessage);
            enableLoginButton();
          }
        });
      }
    });
  }
  
  private void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

}
