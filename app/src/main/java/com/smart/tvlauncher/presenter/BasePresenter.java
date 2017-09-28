package com.smart.tvlauncher.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by patrick on 2017/3/8.
 */

public abstract class BasePresenter<V> {

    private WeakReference<V> weakReference;

    public void attachView (V view){
        weakReference = new WeakReference<V>(view);
    }

    public void detachView (){
        if(weakReference != null){
            weakReference.clear();
            weakReference = null;
        }
    }
}
