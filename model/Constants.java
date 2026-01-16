package model;

public final class Constants {
  public static final String ERROR_USERNAME_EMPTY = "사용자명을 입력하세요";
  public static final String ERROR_PASSWORD_TOO_SHORT = "비밀번호는 6자 이상이어야 합니다";
  public static final String HINT_USERNAME = "사용자명";
  public static final String HINT_PASSWORD = "비밀번호";
  public static final String BUTTON_TEXT_LOGIN = "로그인";
  public static final int LAYOUT_PADDING = 50;
  public static final int MIN_PASSWORD_LENGTH = 6;
  public static final String PREF_NAME = "user";
  public static final String PREF_KEY_USERNAME = "username";
  public static final String PREF_KEY_LOGGED_IN = "logged_in";

  private Constants() {
  }
}
