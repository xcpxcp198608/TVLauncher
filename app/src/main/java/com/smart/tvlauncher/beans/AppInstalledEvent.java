package com.smart.tvlauncher.beans;

/**
 * Created by patrick on 21/06/2017.
 * create time : 10:18 PM
 */

public class AppInstalledEvent {

    private boolean isInstalled = false;

    public AppInstalledEvent(boolean isInstalled) {
        this.isInstalled = isInstalled;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }
}
