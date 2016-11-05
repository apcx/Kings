package apc.kings.common;

import android.app.Application;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    public static final String RES_VERSION = "v1.15.2.25";
    private static SharedPreferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        preferences = getSharedPreferences("app", MODE_PRIVATE);
    }

    public static SharedPreferences preferences() {
        return preferences;
    }
}
