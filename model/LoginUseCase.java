package model;

public interface LoginUseCase {
  interface Callback {
    void onSuccess(String username);
    void onFailure(String errorMessage);
  }

  void login(String username, String password, Callback callback);
}
