package view;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import model.LoginModel;
import model.SharedPreferencesUserStorage;
import model.UserStorage;
import viewmodel.LoginViewModel;

public class LoginActivity extends Activity {

  private LoginModel loginModel;
  private UserStorage userStorage;
  private LoginView loginView;
  private LoginViewModel viewModel;
  private LoginDataBinding dataBinding;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupViewModel();
    setupDataBinding();
    setupLoginButton();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (dataBinding != null) {
      dataBinding.unbind();
    }
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

  private void setupDataBinding() {
    dataBinding = new LoginDataBinding(
      viewModel,
      loginView,
      this
    );
    dataBinding.bind();
  }
  
  private void setupLoginButton() {
    loginView.setOnLoginClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        viewModel.onLoginClicked(loginView.getUsername(), loginView.getPassword());
      }
    });
  }

}
