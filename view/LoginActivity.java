package view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import model.Constants;
import model.LoginModel;
import model.UserStorage;

public class LoginActivity extends Activity {

  private LoginModel loginModel;
  private UserStorage userStorage;
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private TextView tvError;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setupUI();
    setupLoginButton();
  }
  
  private void setupUI() {
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING
    );

    etUsername = new EditText(this);
    etUsername.setHint(Constants.HINT_USERNAME);
    etUsername.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etUsername);

    etPassword = new EditText(this);
    etPassword.setHint(Constants.HINT_PASSWORD);
    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
      | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    etPassword.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etPassword);

    btnLogin = new Button(this);
    btnLogin.setText(Constants.BUTTON_TEXT_LOGIN);
    btnLogin.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(btnLogin);

    tvError = new TextView(this);
    tvError.setText("");
    tvError.setTextColor(android.graphics.Color.RED);
    tvError.setVisibility(View.GONE);
    tvError.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(tvError);

    setContentView(layout);
    loginModel = new LoginModel();
    userStorage = new UserStorage(getApplicationContext());
  }
  
  private void setupLoginButton() {
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
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
    tvError.setVisibility(View.GONE);
  }
  
  private void disableLoginButton() {
    btnLogin.setEnabled(false);
  }
  
  private void enableLoginButton() {
    btnLogin.setEnabled(true);
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
    tvError.setText(message);
    tvError.setVisibility(View.VISIBLE);
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
