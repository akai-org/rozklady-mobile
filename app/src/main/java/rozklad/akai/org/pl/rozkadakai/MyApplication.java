package rozklad.akai.org.pl.rozkadakai;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }


}
