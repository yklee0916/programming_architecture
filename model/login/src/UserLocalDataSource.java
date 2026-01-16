package model.login.src;

import android.content.Context;
import android.content.SharedPreferences;

public class UserLocalDataSource {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_USERNAME = "username";
    private final SharedPreferences sharedPreferences;

    public UserLocalDataSource(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(UserDo user) {
        sharedPreferences.edit().putString(KEY_USERNAME, user.getUsername()).apply();
    }
}
