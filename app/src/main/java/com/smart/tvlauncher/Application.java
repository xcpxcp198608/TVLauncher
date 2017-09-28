package com.smart.tvlauncher;

import android.content.Context;

import com.smart.tvlauncher.task.InstalledApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by patrick on 2017/3/3.
 */

public class Application extends android.app.Application {
    private static Context context;
    private static ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        executorService = Executors.newCachedThreadPool();
        executorService.execute(new InstalledApp());
    }

    public static Context getContext (){
        return context;
    }

    public static ExecutorService getThreadPool (){
        return executorService;
    }
}
