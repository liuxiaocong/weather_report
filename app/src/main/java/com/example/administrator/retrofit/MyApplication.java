package com.example.administrator.retrofit;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by LiuXiaocong on 8/12/2016.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
