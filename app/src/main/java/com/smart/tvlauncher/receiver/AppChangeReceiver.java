package com.smart.tvlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.smart.tvlauncher.Application;
import com.smart.tvlauncher.F;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.AppUtils;
import com.smart.tvlauncher.utils.Logger;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by patrick on 2017/3/8.
 */

public class AppChangeReceiver extends BroadcastReceiver {
    private AppsDao appsDao;

    public AppChangeReceiver() {
        appsDao = AppsDao.getInstance(Application.getContext());
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        if(Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())){
            String packageName = intent.getData().getSchemeSpecificPart();
            Logger.d("receiver" + packageName);
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            AppInfo appInfo = new AppInfo();
                            appInfo.setPackageName(s);
                            appInfo.setLabel(AppUtils.getLabelName(Application.getContext() , s));
                            appInfo.setType(F.app_type.apps);
                            if(F.packageName.netflix.equals(s)){
                                appInfo.setShortcut(F.app_type.sc1);
                            }else if(F.packageName.dangbei.equals(s)){
                                appInfo.setShortcut(F.app_type.sc2);
                            }else if(F.packageName.multimedia.equals(s)){
                                appInfo.setShortcut(F.app_type.sc3);
                            }else if(F.packageName.apt.equals(s)){
                                appInfo.setShortcut(F.app_type.sc4);
                            }else if(F.packageName.mxplayer.equals(s)){
                                appInfo.setShortcut(F.app_type.sc5);
                            }else if(F.packageName.vplayer.equals(s)){
                                appInfo.setShortcut(F.app_type.sc6);
                            }else {
                                appInfo.setShortcut("1");
                            }
                            if(!appsDao.isExists(appInfo)){
                                appsDao.insertData(appInfo);
                            }
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });
        }else if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
            String packageName = intent.getData().getSchemeSpecificPart();
            Observable.just(packageName)
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<String, Object>() {
                        @Override
                        public Object call(String s) {
                            appsDao.deleteByPackageName(s);
                            return null;
                        }
                    })
                    .subscribe(new Action1<Object>() {
                        @Override
                        public void call(Object o) {

                        }
                    });

        }else if(Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())){

        }
    }
}
