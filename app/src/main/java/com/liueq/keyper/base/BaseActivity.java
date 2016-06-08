package com.liueq.keyper.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import com.liueq.keyper.Constants;
import com.liueq.keyper.VisibleObserver;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.ui.launch.SplashActivity;

import java.util.HashMap;

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

    protected abstract Presenter getPresenter();

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
        String period = shared.getProterties(Constants.SP_AUTO_LOCK_PERIOD);
        period = TextUtils.isEmpty(period) ? "60" : period;

        if(show_time != null && hide_time != null){
            try{
                long surplus = Long.valueOf(show_time) - Long.valueOf(hide_time);
                if(surplus > 0){
                    if(surplus / 1000 > Long.valueOf(period)){
                        SplashActivity.launchActivity(this);
                    }
                }
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        }
    }

	/**
	 * When first launch
     */
    protected void clearAutoLockTime(){
        SharedPreferenceRepo shared = new SharedPreferenceRepoImpl(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.SP_HIDE_TIME, "0");
        map.put(Constants.SP_SHOW_TIME, "0");
        shared.saveProperties(map);
    }

}
