package com.smart.tvlauncher.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.smart.tvlauncher.presenter.BasePresenter;
import com.smart.tvlauncher.utils.Logger;

/**
 * Created by patrick on 2017/3/3.
 */

public abstract class BaseActivity<V ,T extends BasePresenter> extends AppCompatActivity {

    protected T presenter;

    public abstract T createPresenter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
        presenter = createPresenter();
        presenter.attachView(this);
        Logger.init("smart_tv");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
