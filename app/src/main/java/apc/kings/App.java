package apc.kings;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {

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
