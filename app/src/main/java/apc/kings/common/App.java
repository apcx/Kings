package apc.kings.common;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;

import apc.kings.R;

public class App extends Application {

    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
    }

    public static SharedPreferences preferences() {
        return preferences;
    }
}
