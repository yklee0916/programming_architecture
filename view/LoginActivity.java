package view;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import view.databinding.ActivityLoginBinding;

import model.LoginModel;
import model.LoginUseCase;
import model.SharedPreferencesUserStorage;
import model.UserStorage;
import viewmodel.LoginViewModel;

public class LoginActivity extends Activity {

  private LoginViewModel viewModel;
  private ActivityLoginBinding binding;
  private final Observable.OnPropertyChangedCallback navigateCallback =
    new Observable.OnPropertyChangedCallback() {
      @Override
      public void onPropertyChanged(Observable sender, int propertyId) {
        if (viewModel.getNavigateToMain().get()) {
          navigateToMain();
          viewModel.onNavigateHandled();
        }
      }
    };
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupViewModel();
    setupDataBinding();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (viewModel != null) {
      viewModel.getNavigateToMain().removeOnPropertyChangedCallback(navigateCallback);
    }
  }

  private void setupViewModel() {
    LoginUseCase loginUseCase = new LoginModel();
    UserStorage userStorage = new SharedPreferencesUserStorage(getApplicationContext());
    viewModel = new LoginViewModel(loginUseCase, userStorage);
  }

  private void setupDataBinding() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
    binding.setViewModel(viewModel);
    viewModel.getNavigateToMain().addOnPropertyChangedCallback(navigateCallback);
  }

  private void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }
  
}
