package apc.kings.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences preferences() {
        return preferences;
    }
}
