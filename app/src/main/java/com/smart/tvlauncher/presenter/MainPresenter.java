package com.smart.tvlauncher.presenter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import com.smart.tvlauncher.Application;
import com.smart.tvlauncher.F;
import com.smart.tvlauncher.activity.IMainActivity;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.beans.AppInstalledEvent;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.RxBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by patrick on 2017/3/8.
 */

public class MainPresenter extends BasePresenter<IMainActivity> {
    private IMainActivity iMainActivity;

    public MainPresenter(IMainActivity iMainActivity) {
        this.iMainActivity = iMainActivity;
    }

    public void loadApps (){

    }
}
