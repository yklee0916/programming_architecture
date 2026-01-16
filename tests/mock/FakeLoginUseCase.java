package tests.mock;

import model.login.LoginUseCase;
import model.login.UserDto;

public class FakeLoginUseCase extends LoginUseCase {
  private final boolean shouldSucceed;

  public FakeLoginUseCase(boolean shouldSucceed) {
    super(null);
    this.shouldSucceed = shouldSucceed;
  }

  @Override
  public void login(String username, String password, LoginUseCase.Callback callback) {
    if (shouldSucceed) {
      callback.onSuccess(new UserDto(username));
    } else {
      callback.onFailure("테스트 실패");
    }
  }
}
