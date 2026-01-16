package presenter.mock;

import model.UserStorage;

public class FakeUserStorage implements UserStorage {
  private String savedUsername;

  @Override
  public void save(String username) {
    savedUsername = username;
  }

  public String getSavedUsername() {
    return savedUsername;
  }
}
