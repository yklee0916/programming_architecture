package presenter.mock;

import presenter.LoginContract;

public class MockLoginView implements LoginContract.View {
  private String lastError;
  private boolean loginEnabled = true;
  private boolean navigated;
  private int runOnUiThreadCalls;

  @Override
  public void showError(String message) {
    lastError = message;
  }

  @Override
  public void clearError() {
    lastError = null;
  }

  @Override
  public void setLoginEnabled(boolean enabled) {
    loginEnabled = enabled;
  }

  @Override
  public void navigateToMain() {
    navigated = true;
  }

  @Override
  public void runOnUiThread(Runnable action) {
    runOnUiThreadCalls += 1;
    action.run();
  }

  public String getLastError() {
    return lastError;
  }

  public boolean isLoginEnabled() {
    return loginEnabled;
  }

  public boolean isNavigated() {
    return navigated;
  }

  public int getRunOnUiThreadCalls() {
    return runOnUiThreadCalls;
  }
}
