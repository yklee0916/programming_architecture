package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import model.LoginModel;
import model.SharedPreferencesUserStorage;
import model.UserStorage;
import viewmodel.LoginViewModel;
import viewmodel.ObservableValue;

public class LoginActivity extends Activity {

  private LoginModel loginModel;
  private UserStorage userStorage;
  private LoginView loginView;
  private LoginViewModel viewModel;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupViewModel();
    bindViewModel();
    setupLoginButton();
  }

  private void setupUI() {
    loginView = new LoginView(this);
    setContentView(loginView.createRootView());
    loginModel = new LoginModel();
    userStorage = new SharedPreferencesUserStorage(getApplicationContext());
  }

  private void setupViewModel() {
    viewModel = new LoginViewModel(loginModel, userStorage);
  }

  private void bindViewModel() {
    viewModel.getErrorMessage().observe(new ObservableValue.Observer<String>() {
      @Override
      public void onChanged(String message) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if (message == null || message.isEmpty()) {
              loginView.clearError();
            } else {
              loginView.showError(message);
            }
          }
        });
      }
    });

    viewModel.getLoginEnabled().observe(new ObservableValue.Observer<Boolean>() {
      @Override
      public void onChanged(Boolean enabled) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            loginView.setLoginEnabled(enabled != null && enabled);
          }
        });
      }
    });

    viewModel.getNavigateToMain().observe(new ObservableValue.Observer<Boolean>() {
      @Override
      public void onChanged(Boolean shouldNavigate) {
        if (shouldNavigate == null || !shouldNavigate) {
          return;
        }
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            navigateToMain();
            viewModel.onNavigateHandled();
          }
        });
      }
    });
  }
  
  private void setupLoginButton() {
    loginView.setOnLoginClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        viewModel.onLoginClicked(loginView.getUsername(), loginView.getPassword());
      }
    });
  }

  private void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

}
