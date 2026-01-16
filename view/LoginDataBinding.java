package view;

import android.app.Activity;
import android.content.Intent;
import viewmodel.LoginViewModel;
import viewmodel.ObservableValue;

public class LoginDataBinding {
  private final LoginViewModel viewModel;
  private final LoginView loginView;
  private final Activity activity;

  public LoginDataBinding(
    LoginViewModel viewModel,
    LoginView loginView,
    Activity activity
  ) {
    this.viewModel = viewModel;
    this.loginView = loginView;
    this.activity = activity;
  }

  public void bind() {
    viewModel.getErrorMessage().observe(new ObservableValue.Observer<String>() {
      @Override
      public void onChanged(String message) {
        activity.runOnUiThread(new Runnable() {
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
        activity.runOnUiThread(new Runnable() {
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
        activity.runOnUiThread(new Runnable() {
          @Override
          public void run() {
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
            viewModel.onNavigateHandled();
          }
        });
      }
    });
  }

  public void unbind() {
    viewModel.getErrorMessage().clearObserver();
    viewModel.getLoginEnabled().clearObserver();
    viewModel.getNavigateToMain().clearObserver();
  }
}
