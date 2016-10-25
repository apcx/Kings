package apc.kings.common;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

    public static final String RES_VERSION = "v1.15.2.22";

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
