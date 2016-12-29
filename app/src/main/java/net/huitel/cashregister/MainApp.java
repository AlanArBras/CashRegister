package net.huitel.cashregister;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alan on 10/04/2016.
 * Application object so we can have common resources between Activities (such as DAOs to access tables in SQLite database)
 */
public class MainApp extends Application {
    private static Context mContext;

    public static synchronized Context getContext(){
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }
}
