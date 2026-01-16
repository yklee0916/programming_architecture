package presenter;

public interface LoginContract {
  interface View {
    void showError(String message);
    void clearError();
    void setLoginEnabled(boolean enabled);
    void navigateToMain();
    void runOnUiThread(Runnable action);
  }

  interface Presenter {
    void onLoginClicked(String username, String password);
    void onDestroy();
  }
}
