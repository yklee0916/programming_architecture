import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends Activity {
  private static final String API_LOGIN_URL = "https://api.example.com/login";
  private static final String PREF_NAME = "user";
  private static final String PREF_KEY_USERNAME = "username";
  private static final String PREF_KEY_LOGGED_IN = "logged_in";
  private static final String ERROR_USERNAME_EMPTY = "사용자명을 입력하세요";
  private static final String ERROR_PASSWORD_TOO_SHORT = "비밀번호는 6자 이상이어야 합니다";
  private static final String ERROR_LOGIN_FAILED = "로그인 실패";
  private static final String ERROR_NETWORK = "네트워크 오류";
  private static final String HINT_USERNAME = "사용자명";
  private static final String HINT_PASSWORD = "비밀번호";
  private static final String BUTTON_TEXT_LOGIN = "로그인";
  private static final int LAYOUT_PADDING = 50;
  private static final int MIN_PASSWORD_LENGTH = 6;
  private static final String SUCCESS_RESPONSE = "success";
  
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
    layout.setPadding(LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING, LAYOUT_PADDING);
    etUsername = new EditText(this);
    etUsername.setHint(HINT_USERNAME);
    etUsername.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etUsername);
    etPassword = new EditText(this);
    etPassword.setHint(HINT_PASSWORD);
    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    etPassword.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etPassword);
    btnLogin = new Button(this);
    btnLogin.setText(BUTTON_TEXT_LOGIN);
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
      showError(ERROR_USERNAME_EMPTY);
      enableLoginButton();
      return false;
    }
    if (password.length() < MIN_PASSWORD_LENGTH) {
      showError(ERROR_PASSWORD_TOO_SHORT);
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
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          String response = sendLoginRequest(username, password);
          handleLoginResponse(response, username);
        } catch (Exception e) {
          handleLoginError();
        }
      }
    }).start();
  }
  
  private String sendLoginRequest(String username, String password) throws Exception {
    HttpURLConnection conn = null;
    OutputStream outputStream = null;
    try {
      URL url = new URL(API_LOGIN_URL);
      conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json");
      String jsonData = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
      outputStream = conn.getOutputStream();
      outputStream.write(jsonData.getBytes());
      outputStream.flush();
      String response;
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
        response = reader.readLine();
      }
      return response;
    } finally {
      if (outputStream != null) {
        try {
          outputStream.close();
        } catch (Exception e) {
        }
      }
      if (conn != null) {
        conn.disconnect();
      }
    }
  }
  
  private void handleLoginResponse(String response, String username) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (response.contains(SUCCESS_RESPONSE)) {
          saveUserData(username);
          navigateToMain();
        } else {
          showError(ERROR_LOGIN_FAILED);
          enableLoginButton();
        }
      }
    });
  }
  
  private void handleLoginError() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        showError(ERROR_NETWORK);
        enableLoginButton();
      }
    });
  }
  
  private void saveUserData(String username) {
    SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(PREF_KEY_USERNAME, username);
    editor.putBoolean(PREF_KEY_LOGGED_IN, true);
    editor.apply();
  }
  
  private void navigateToMain() {
    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }
}
