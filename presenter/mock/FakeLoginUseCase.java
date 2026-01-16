package presenter.mock;

import model.LoginUseCase;

public class FakeLoginUseCase implements LoginUseCase {
  private final boolean shouldSucceed;

  public FakeLoginUseCase(boolean shouldSucceed) {
    this.shouldSucceed = shouldSucceed;
  }

  @Override
  public void login(String username, String password, Callback callback) {
    if (shouldSucceed) {
      callback.onSuccess(username);
    } else {
      callback.onFailure("테스트 실패");
    }
  }
}
