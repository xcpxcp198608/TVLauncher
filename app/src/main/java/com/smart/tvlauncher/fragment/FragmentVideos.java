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
import com.smart.tvlauncher.activity.AppSelectActivity;
import com.smart.tvlauncher.activity.AppsActivity;
import com.smart.tvlauncher.adapter.AppsCustomAdapter;
import com.smart.tvlauncher.animator.Zoom;
import com.smart.tvlauncher.beans.AppInfo;
import com.smart.tvlauncher.databinding.FragmentGamesBinding;
import com.smart.tvlauncher.databinding.FragmentVideosBinding;
import com.smart.tvlauncher.sql.AppsDao;
import com.smart.tvlauncher.utils.AppUtils;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by patrick on 2017/3/8.
 */

public class FragmentVideos extends Fragment {

    private FragmentVideosBinding binding;
    private AppsCustomAdapter appsCustomAdapter;
    private AppsDao appsDao;
    private AppsActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater , R.layout.fragment_videos , container ,false);
        activity = (AppsActivity) getContext();
        appsDao = AppsDao.getInstance(activity);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.just(F.app_type.videos)
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, List<AppInfo>>() {
                    @Override
                    public List<AppInfo> call(String s) {
                        return appsDao.queryDataByType(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AppInfo>>() {
                    @Override
                    public void call(final List<AppInfo> appInfos) {
                        appsCustomAdapter = new AppsCustomAdapter(activity, appInfos);
                        binding.gvVideos.setAdapter(appsCustomAdapter);
                        binding.gvVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position <appInfos.size()){
                                    String packageName = appInfos.get(position).getPackageName();
                                    AppUtils.launchApp(getContext() , packageName);
                                }else {
                                    Intent intent = new Intent(getActivity() , AppSelectActivity.class);
                                    intent.putExtra("app_type" ,F.app_type.videos);
                                    startActivity(intent);
                                }
                            }
                        });
                        binding.gvVideos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

}