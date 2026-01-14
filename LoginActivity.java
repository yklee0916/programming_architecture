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
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private TextView tvError;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LinearLayout layout = new LinearLayout(this);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(50, 50, 50, 50);
    etUsername = new EditText(this);
    etUsername.setHint("사용자명");
    etUsername.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etUsername);
    etPassword = new EditText(this);
    etPassword.setHint("비밀번호");
    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    etPassword.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etPassword);
    btnLogin = new Button(this);
    btnLogin.setText("로그인");
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
    btnLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        tvError.setVisibility(View.GONE);
        btnLogin.setEnabled(false);
        if (username.isEmpty()) {
          tvError.setText("사용자명을 입력하세요");
          tvError.setVisibility(View.VISIBLE);
          btnLogin.setEnabled(true);
          return;
        }
        if (password.length() < 6) {
          tvError.setText("비밀번호는 6자 이상이어야 합니다");
          tvError.setVisibility(View.VISIBLE);
          btnLogin.setEnabled(true);
          return;
        }
        new Thread(new Runnable() {
          @Override
          public void run() {
            HttpURLConnection conn = null;
            OutputStream outputStream = null;
            try {
              URL url = new URL("https://api.example.com/login");
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
              final String finalResponse = response;
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  if (finalResponse.contains("success")) {
                    SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("username", username);
                    editor.putBoolean("logged_in", true);
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                  } else {
                    tvError.setText("로그인 실패");
                    tvError.setVisibility(View.VISIBLE);
                    btnLogin.setEnabled(true);
                  }
                }
              });
            } catch (Exception e) {
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  tvError.setText("네트워크 오류");
                  tvError.setVisibility(View.VISIBLE);
                  btnLogin.setEnabled(true);
                }
              });
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
        }).start();
      }
    });
  }
}
