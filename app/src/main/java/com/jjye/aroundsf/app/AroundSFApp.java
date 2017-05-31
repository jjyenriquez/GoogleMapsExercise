package com.jjye.aroundsf.app;

import android.app.Application;

/**
 * Created by jjye on 5/30/17.
 */

public class AroundSFApp extends Application {
    private static AroundSFApp mContext;

    @Override
    public void onCreate() {
        mContext = this;
    }

    public static AroundSFApp getInstance() {
        return mContext;
    }
}
