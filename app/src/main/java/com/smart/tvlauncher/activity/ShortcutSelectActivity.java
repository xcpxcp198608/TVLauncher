package com.smart.tvlauncher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.GridView;

import com.smart.tvlauncher.F;
import com.smart.tvlauncher.R;
import com.smart.tvlauncher.adapter.AppsSelectAdapter;
import com.smart.tvlauncher.adapter.ShortcutAdapter;
import com.smart.tvlauncher.animator.Zoom;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.Logger;

import java.util.List;

/**
 * Created by patrick on 2017/3/9.
 */

public class ShortcutSelectActivity extends AppCompatActivity {

    private String shortcut;
    private ShortcutAdapter shortcutAdapter;
    private List<AppInfo> list;
    private GridView gvShortcut;
    private AppsDao appsDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shortcut);
        gvShortcut = (GridView) findViewById(R.id.gv_shortcut);
        shortcut = getIntent().getStringExtra("shortcut");
        appsDao = AppsDao.getInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        list = appsDao.showShortcutData();
        shortcutAdapter = new ShortcutAdapter(this , list, shortcut);
        gvShortcut.setAdapter(shortcutAdapter);
        gvShortcut.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = list.get(position);
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
                if(checkBox.isChecked()){
                    checkBox.setChecked(false);
                    appInfo.setShortcut("1");
                    appsDao.updateShortcut(appInfo);
                }else {
                    checkBox.setChecked(true);
                    if(shortcut.equals(F.app_type.sc1) || shortcut.equals(F.app_type.sc2) || shortcut.equals(F.app_type.sc3) ||
                            shortcut.equals(F.app_type.sc4) || shortcut.equals(F.app_type.sc5) || shortcut.equals(F.app_type.sc6)){
                        appsDao.resetShortcut(shortcut);
                    }
                    appInfo.setShortcut(shortcut);
                    appsDao.updateShortcut(appInfo);
                    finish();
                }
            }
        });
        gvShortcut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Zoom.zoomIn09_10(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
