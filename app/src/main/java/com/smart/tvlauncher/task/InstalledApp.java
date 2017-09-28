package com.smart.tvlauncher.task;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.smart.tvlauncher.Application;
import com.smart.tvlauncher.F;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.beans.AppInstalledEvent;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.Logger;
import com.smart.tvlauncher.utils.RxBus;

import java.util.Iterator;
import java.util.List;

/**
 * Created by patrick on 02/07/2017.
 * create time : 10:26 AM
 */

public class InstalledApp implements Runnable {
    @Override
    public void run() {
        getApps();
    }

    private void getApps (){
        AppsDao appsDao = AppsDao.getInstance(Application.getContext());
        PackageManager packageManager = Application.getContext().getPackageManager();
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> localList = packageManager.queryIntentActivities(intent ,0);
        Iterator<ResolveInfo> iterator = null;
        if(localList != null) {
            iterator = localList.iterator();
        }
        while (true) {
            if (!iterator.hasNext()) {
                break;
            }
            ResolveInfo resolveInfo = iterator.next();
            String packageName = resolveInfo.activityInfo.packageName;
            if(!F.packageName.setting.equals(packageName) && !F.packageName.app.equals(packageName)
                    && !F.packageName.upgrade1.equals(packageName)){
                AppInfo appInfo = new AppInfo();
                appInfo.setLabel(resolveInfo.loadLabel(packageManager).toString());
                appInfo.setPackageName(packageName);
                appInfo.setType(F.app_type.apps);
                Logger.d(packageName);
                if(!appsDao.isExists(appInfo)){
                    Logger.d("! exists" + packageName);
//                    if(F.packageName.netflix.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc1);
//                    }else if(F.packageName.dangbei.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc2);
//                    }else if(F.packageName.coolbox.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc3);
//                    }else if(F.packageName.multimedia.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc4);
//                    }else if(F.packageName.apt.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc5);
//                    }else if(F.packageName.mxplayer.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc6);
//                    }else if(F.packageName.rssplayer.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc7);
//                    }else if(F.packageName.vplayer.equals(packageName)){
//                        appInfo.setShortcut(F.app_type.sc8);
//                    }else {
                        appInfo.setShortcut("1");
//                    }
                    appsDao.insertData(appInfo);
                }
            }

        }
        RxBus.getDefault().post(new AppInstalledEvent(true));
    }
}
