package apc.kings.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    public static final String GAME_VERSION = "v1.15.2.6";
    public static final String RES_VERSION = "v1.15.2.10";

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        context = this;
    }

    public static Context context() {
        return context;
    }
}
