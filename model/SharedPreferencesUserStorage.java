package model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUserStorage implements UserStorage {
  private final Context context;

  public SharedPreferencesUserStorage(Context context) {
    this.context = context.getApplicationContext();
  }

  @Override
  public void save(String username) {
    SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putString(Constants.PREF_KEY_USERNAME, username);
    editor.putBoolean(Constants.PREF_KEY_LOGGED_IN, true);
    editor.apply();
  }
}
