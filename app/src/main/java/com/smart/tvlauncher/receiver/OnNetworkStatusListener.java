package com.smart.tvlauncher.receiver;

/**
 * Created by PX on 2016-11-12.
 */

public interface OnNetworkStatusListener {
    void onConnected(boolean isConnected);
    void onDisconnect(boolean disConnected);
}
