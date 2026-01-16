package viewmodel;

import android.app.Activity;
import android.content.Intent;

public class NavigationViewModel {
    private final Activity activity;

    public NavigationViewModel(Activity activity) {
        this.activity = activity;
    }

    public void navigate(Class<? extends Activity> targetActivity) {
        Intent intent = new Intent(activity, targetActivity);
        activity.startActivity(intent);
        activity.finish();
    }
}
