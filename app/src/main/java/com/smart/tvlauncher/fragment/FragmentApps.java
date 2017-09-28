package com.smart.tvlauncher.fragment;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.smart.tvlauncher.Application;
import com.smart.tvlauncher.F;
import com.smart.tvlauncher.R;
import com.smart.tvlauncher.activity.AppsActivity;
import com.smart.tvlauncher.adapter.AppsAdapter;
import com.smart.tvlauncher.animator.Zoom;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.databinding.FragmentAppsBinding;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.AppUtils;
import com.smart.tvlauncher.utils.Logger;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by patrick on 2017/3/8.
 */

public class FragmentApps extends Fragment {

    private FragmentAppsBinding binding;
    private AppsAdapter appsAdapter;
    private AppsDao appsDao;
    private AppsActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_apps,container ,false);
        activity = (AppsActivity) getContext();
        appsDao = AppsDao.getInstance(activity);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Observable.just(F.app_type.apps)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return appsDao.queryData();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(final List<AppInfo> appInfos) {
                        appsAdapter = new AppsAdapter(activity , appInfos);
                        binding.gvApps.setAdapter(appsAdapter);
                        binding.gvApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String packageName = appInfos.get(position).getPackageName();
                                Logger.d(packageName);
                                AppUtils.launchApp(getContext() , packageName);
                            }
                        });
                        binding.gvApps.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Zoom.zoomIn09_10(view);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(appsAdapter != null){
            appsAdapter.notifyDataSetChanged();
        }
    }
}
