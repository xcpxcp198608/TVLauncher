package com.smart.tvlauncher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.smart.tvlauncher.R;
import com.smart.tvlauncher.beans.UsbEvent;
import com.smart.tvlauncher.utils.Logger;
import com.smart.tvlauncher.utils.NetUtils;
import com.smart.tvlauncher.utils.RxBus;
import com.smart.tvlauncher.utils.SysUtils;

/**
 * Created by patrick on 2017/3/10.
 */

public class USBStatusReceiver extends BroadcastReceiver {

    public USBStatusReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())){
            String em1 = "/storage/emulated/0";
            String path = intent.getData().getPath();
            if(em1.equals(path)){

            }else {
                RxBus.getDefault().post(new UsbEvent(UsbEvent.USB_MOUNTED, path));
            }
        }else if(Intent.ACTION_MEDIA_EJECT.equals(intent.getAction())){
            String path = intent.getData().getPath();
            RxBus.getDefault().post(new UsbEvent(UsbEvent.USB_UNMOUNTED , path));
        }
    }
}
