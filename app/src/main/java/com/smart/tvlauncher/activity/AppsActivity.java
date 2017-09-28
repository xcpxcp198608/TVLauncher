package com.smart.tvlauncher.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smart.tvlauncher.R;
import com.smart.tvlauncher.adapter.FragmentAdapter;
import com.smart.tvlauncher.animator.Zoom;
import com.smart.tvlauncher.databinding.ActivityAppsBinding;
import com.smart.tvlauncher.fragment.FragmentApps;
import com.smart.tvlauncher.fragment.FragmentGames;
import com.smart.tvlauncher.fragment.FragmentMusic;
import com.smart.tvlauncher.fragment.FragmentVideos;
import com.smart.tvlauncher.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrick on 2017/3/8.
 */

public class AppsActivity extends AppCompatActivity {

    private ActivityAppsBinding binding;
    private List<Fragment> fragmentList;
    private FragmentApps fragmentApps;
    private FragmentGames fragmentGames;
    private FragmentMusic fragmentMusic;
    private FragmentVideos fragmentVideos;
    private FragmentAdapter fragmentAdapter;
    private int currentItem = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_apps);
        currentItem = getIntent().getIntExtra("currentItem",0);
        if(fragmentList == null){
            fragmentList = new ArrayList<>();
        }
        if(fragmentApps == null){
            fragmentApps = new FragmentApps();
        }
        if(fragmentGames == null){
            fragmentGames = new FragmentGames();
        }
        if(fragmentMusic == null){
            fragmentMusic = new FragmentMusic();
        }
        if(fragmentVideos == null){
            fragmentVideos = new FragmentVideos();
        }
        fragmentList.add(fragmentApps);
        fragmentList.add(fragmentGames);
        fragmentList.add(fragmentMusic);
        fragmentList.add(fragmentVideos);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager() , fragmentList);
        binding.viewPager.setAdapter(fragmentAdapter);

        String [] titles = {getString(R.string.apps) ,getString(R.string.games) ,getString(R.string.music)
                ,getString(R.string.videos)};
        binding.viewPagerIndicator.setItem(7,0f,0f);
        binding.viewPagerIndicator.setTextTitle(titles ,R.color.colorTransparent
                ,R.drawable.icon_bg_selected ,40, 0,0);
        binding.viewPagerIndicator.attachViewPager(binding.viewPager ,currentItem);
    }

}
