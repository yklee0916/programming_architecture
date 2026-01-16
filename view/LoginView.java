package view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import model.Constants;

public class LoginView {
  private final Context context;
  private EditText etUsername;
  private EditText etPassword;
  private Button btnLogin;
  private TextView tvError;

  public LoginView(Context context) {
    this.context = context;
  }

  public View createRootView() {
    LinearLayout layout = new LinearLayout(context);
    layout.setOrientation(LinearLayout.VERTICAL);
    layout.setPadding(
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING,
      Constants.LAYOUT_PADDING
    );

    etUsername = new EditText(context);
    etUsername.setHint(Constants.HINT_USERNAME);
    etUsername.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etUsername);

    etPassword = new EditText(context);
    etPassword.setHint(Constants.HINT_PASSWORD);
    etPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
      | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
    etPassword.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(etPassword);

    btnLogin = new Button(context);
    btnLogin.setText(Constants.BUTTON_TEXT_LOGIN);
    btnLogin.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(btnLogin);

    tvError = new TextView(context);
    tvError.setText("");
    tvError.setTextColor(android.graphics.Color.RED);
    tvError.setVisibility(View.GONE);
    tvError.setLayoutParams(new LinearLayout.LayoutParams(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.WRAP_CONTENT
    ));
    layout.addView(tvError);

    return layout;
  }

  public void setOnLoginClickListener(View.OnClickListener listener) {
    btnLogin.setOnClickListener(listener);
  }

  public String getUsername() {
    return etUsername.getText().toString();
  }

  public String getPassword() {
    return etPassword.getText().toString();
  }

  public void showError(String message) {
    tvError.setText(message);
    tvError.setVisibility(View.VISIBLE);
  }

  public void clearError() {
    tvError.setVisibility(View.GONE);
  }

  public void setLoginEnabled(boolean enabled) {
    btnLogin.setEnabled(enabled);
  }
}
