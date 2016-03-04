package com.liueq.testdagger.base;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.VisibleObserver;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.ui.launch.SplashActivity;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected VisibleObserver mVisibleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

    }

    protected abstract void setupActivityComponent();

    protected abstract Presenter getmPresenter();

    @Override
    protected void onResume() {
        mVisibleObserver.onApplicationShow();

        checkIsLaunchLock();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVisibleObserver.onApplicationHide();
    }

    private void checkIsLaunchLock(){
        SharedPreferenceRepo shared = new SharedPreferenceRepoImpl(this);
        String hide_time = shared.getProterties(Constants.SP_HIDE_TIME);
        String show_time = shared.getProterties(Constants.SP_SHOW_TIME);

        if(show_time != null && hide_time != null){
            try{
                long surplus = Long.valueOf(show_time) - Long.valueOf(hide_time);
                if(surplus > 0){
                    if(surplus / 1000 > 20){
                        SplashActivity.launchActivity(this);
                    }
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        }
    }
}
