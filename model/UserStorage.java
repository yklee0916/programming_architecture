package model;

import android.content.Context;
import android.content.SharedPreferences;

public class UserStorage {
  private final Context context;

  public UserStorage(Context context) {
    this.context = context.getApplicationContext();
  }

  public void save(String username) {
    SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(Constants.PREF_KEY_USERNAME, username);
    editor.putBoolean(Constants.PREF_KEY_LOGGED_IN, true);
    editor.apply();
  }
}
