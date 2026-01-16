package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import model.LoginModel;
import model.LoginUseCase;
import model.SharedPreferencesUserStorage;
import model.UserStorage;
import presenter.LoginContract;
import presenter.LoginPresenter;

public class LoginActivity extends Activity implements LoginContract.View {

  private LoginUseCase loginModel;
  private UserStorage userStorage;
  private LoginView loginView;
  private LoginContract.Presenter presenter;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupPresenter();
    setupLoginButton();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    presenter.onDestroy();
  }
  
  private void setupUI() {
    loginView = new LoginView(this);
    setContentView(loginView.createRootView());
    loginModel = new LoginModel();
    userStorage = new SharedPreferencesUserStorage(getApplicationContext());
  }

  private void setupPresenter() {
    presenter = new LoginPresenter(loginModel, userStorage, this);
  }
  
  private void setupLoginButton() {
    loginView.setOnLoginClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.onLoginClicked(loginView.getUsername(), loginView.getPassword());
      }
    });
  }

  @Override
  public void showError(String message) {
    loginView.showError(message);
  }

  @Override
  public void clearError() {
    loginView.clearError();
  }

  @Override
  public void setLoginEnabled(boolean enabled) {
    loginView.setLoginEnabled(enabled);
  }

  @Override
  public void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void runOnUiThread(Runnable action) {
    LoginActivity.this.runOnUiThread(action);
  }

}
