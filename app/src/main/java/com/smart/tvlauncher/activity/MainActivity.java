package com.smart.tvlauncher.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.storage.StorageManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.tvlauncher.Application;
import com.smart.tvlauncher.F;
import com.smart.tvlauncher.R;
import com.smart.tvlauncher.animator.Rotation;
import com.smart.tvlauncher.animator.Zoom;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.beans.AppInstalledEvent;
import com.smart.tvlauncher.beans.UsbEvent;
import com.smart.tvlauncher.databinding.ActivityMainBinding;
import com.smart.tvlauncher.presenter.MainPresenter;
import com.smart.tvlauncher.receiver.NetworkStatusReceiver;
import com.smart.tvlauncher.receiver.WifiStatusReceiver;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.AppUtils;
import com.smart.tvlauncher.utils.Logger;
import com.smart.tvlauncher.utils.RxBus;
import com.smart.tvlauncher.utils.SysUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity<IMainActivity , MainPresenter>
        implements IMainActivity , View.OnFocusChangeListener  {

    private ActivityMainBinding binding;
    private Intent intent;
    private AppsDao appsDao;
    private ImageView ivNet;
    private ImageView ivUSB;
    private ImageView ivSD;
    private TextView tvTime;
    private NetworkStatusReceiver networkStatusReceiver;
    private WifiStatusReceiver wifiStatusReceiver;
    private Subscription subscription;
    private Subscription appSubscription;
    private LogWriter mLogWriter;
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setOnEvent(new OnEventListener());
        ivNet = (ImageView) binding.head.findViewById(R.id.iv_wifi);
        ivUSB = (ImageView) binding.head.findViewById(R.id.iv_usb);
        ivSD = (ImageView) binding.head.findViewById(R.id.iv_sd);
        tvTime = (TextView) binding.head.findViewById(R.id.tv_time);
        intent = new Intent();
        appsDao  = AppsDao.getInstance(this);
        showTimeAndData();
        registerReceiver();

    }

    @Override
    protected void onStart() {
        super.onStart();
        setFocus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setShortcut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregister();
    }

    public class OnEventListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ibt_manual:
                    intent.setClass(MainActivity.this, ManualActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ibt_google_play:
                    launcherApp(F.packageName.google_play);
                    break;
                case R.id.ibt_game:
                    intent.putExtra("currentItem", 1);
                    intent.setClass(MainActivity.this, AppsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ibt_browser:
                    launcherApp(F.packageName.browser);
                    break;
                case R.id.ibt_videos:
                    intent.putExtra("currentItem", 3);
                    intent.setClass(MainActivity.this, AppsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ibt_apps:
                    intent.putExtra("currentItem", 0);
                    intent.setClass(MainActivity.this, AppsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ibt_youtube:
                    launcherApp(F.packageName.youtube);
                    break;
                case R.id.ibt_music:
                    intent.putExtra("currentItem", 2);
                    intent.setClass(MainActivity.this, AppsActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ibt_settings:
                    Intent intent = new Intent();
                    intent.setClassName("com.android.tv.settings", "com.android.tv.settings.MainSettings");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case R.id.ibt_upgrade:
                    launcherApp(F.packageName.upgrade1);
                    break;
                case R.id.ibt_clean:
                    Rotation.start(binding.ivClean);
                    SysUtils.cleanMemory(MainActivity.this);
                    break;
                default:
                    break;
            }
        }
    }

    private void launcherApp(String packageName) {
        AppUtils.launchApp(this , packageName);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tvTime.setText((String)msg.obj);
                    break;
                case 2:
                    break;
                case 3:
                    binding.tvClean.setText((String)msg.obj + "%");
                    break;
                default:
                    break;
            }
        }
    };

    private void showTimeAndData() {
        Application.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(1000);
                        Date d = new Date(System.currentTimeMillis());
                        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(MainActivity.this);
                        DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(MainActivity.this);
                        String time = timeFormat.format(d);
                        String date = dateFormat.format(d);
                        String memoryRate = SysUtils.getAvailMemory(MainActivity.this)+"";
                        handler.obtainMessage(1, time).sendToTarget();
                        handler.obtainMessage(2, date).sendToTarget();
                        handler.obtainMessage(3, memoryRate).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setShortcut(){
        showShortcut(binding.ibtSc1 , F.app_type.sc1);
        showShortcut(binding.ibtSc2 , F.app_type.sc2);
        showShortcut(binding.ibtSc3 , F.app_type.sc3);
        showShortcut(binding.ibtSc4 , F.app_type.sc4);
        showShortcut(binding.ibtSc5 , F.app_type.sc5);
        showShortcut(binding.ibtSc6 , F.app_type.sc6);
        showShortcut(binding.ibtSc7 , F.app_type.sc7);
        showShortcut(binding.ibtSc8 , F.app_type.sc8);
    }

    public void showShortcut(final ImageButton imageButton , final String shortcut){
        Observable.just(shortcut)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, AppInfo>() {
                    @Override
                    public AppInfo call(String s) {
                        List<AppInfo> list = appsDao.queryDataByShortcut(s);
                        if(list.size() == 1){
                            return list.get(0);
                        }else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppInfo>() {
                    @Override
                    public void call(final AppInfo appInfo) {
                        if (appInfo != null) {
                            imageButton.setImageDrawable(AppUtils.getIcon(MainActivity.this , appInfo.getPackageName()));
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AppUtils.launchApp(MainActivity.this , appInfo.getPackageName());
                                }
                            });
                            imageButton.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    appInfo.setShortcut("1");
                                    appsDao.updateShortcut(appInfo);
                                    Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
                                    intent.putExtra("shortcut" ,shortcut);
                                    startActivity(intent);
                                    return true;
                                }
                            });
                        }else {
                            imageButton.setImageResource(R.drawable.add_72);
                            imageButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(MainActivity.this , ShortcutSelectActivity.class);
                                    intent.putExtra("shortcut" ,shortcut);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setFocus (){
        binding.ibtManual.setOnFocusChangeListener(this);
        binding.ibtGooglePlay.setOnFocusChangeListener(this);
        binding.ibtGame.setOnFocusChangeListener(this);
        binding.ibtBrowser.setOnFocusChangeListener(this);
        binding.ibtVideos.setOnFocusChangeListener(this);
        binding.ibtApps.setOnFocusChangeListener(this);
        binding.ibtYoutube.setOnFocusChangeListener(this);
        binding.ibtMusic.setOnFocusChangeListener(this);
        binding.ibtSc1.setOnFocusChangeListener(this);
        binding.ibtSc2.setOnFocusChangeListener(this);
        binding.ibtSc3.setOnFocusChangeListener(this);
        binding.ibtSc4.setOnFocusChangeListener(this);
        binding.ibtSc5.setOnFocusChangeListener(this);
        binding.ibtSc6.setOnFocusChangeListener(this);
        binding.ibtSc7.setOnFocusChangeListener(this);
        binding.ibtSc8.setOnFocusChangeListener(this);
        binding.ibtSettings.setOnFocusChangeListener(this);
        binding.ibtUpgrade.setOnFocusChangeListener(this);
        binding.ibtClean.setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            Zoom.zoomIn09_10(v);
        }
    }

    private void registerReceiver (){
        networkStatusReceiver = new NetworkStatusReceiver(ivNet);
        registerReceiver(networkStatusReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        wifiStatusReceiver = new WifiStatusReceiver(ivNet);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
        subscription = RxBus.getDefault().toObservable(UsbEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object o) {
                        UsbEvent usbEvent = (UsbEvent) o;
                        if(usbEvent.getStatus() == UsbEvent.USB_MOUNTED){
                            ivUSB.setImageResource(R.drawable.usb);
                        }
                        if(usbEvent.getStatus() == UsbEvent.USB_UNMOUNTED){
                            ivUSB.setImageResource(R.color.colorTransparent);
                        }
                    }
                });
        appSubscription = RxBus.getDefault().toObservable(AppInstalledEvent.class)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AppInstalledEvent>() {
                    @Override
                    public void call(AppInstalledEvent appInstalledEvent) {
                        if(appInstalledEvent.isInstalled()){
                            setShortcut();
                        }
                    }
                });
    }

    private void unregister (){
        unregisterReceiver(networkStatusReceiver);
        unregisterReceiver(wifiStatusReceiver);
        if (subscription != null) subscription.unsubscribe();
        if (appSubscription != null) appSubscription.unsubscribe();
    }

}