package com.liueq.testdagger.ui.launch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    public final static String TAG = "splash_A";

    @Bind(R.id.pwd_1)
    EditText mEditTextPwd1;
    @Bind(R.id.pwd_2)
    EditText mEditTextPwd2;
    @Bind(R.id.submit)
    TextView mTextViewSubmit;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    SplashActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupActivityComponent();
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initView(){

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if(Build.VERSION.SDK_INT >= 21){
            mToolbar.setElevation(10f);
        }

        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        if(sp.getString(Constants.SP_PWD, null) != null){
            if(BuildConfig.DEBUG){
                Log.d(TAG, "initData password is not null");
            }
            mEditTextPwd2.setVisibility(View.GONE);
            presenter.hasPassword = true;

            mEditTextPwd1.requestFocus();
        }else{
            mEditTextPwd2.setVisibility(View.VISIBLE);
            presenter.hasPassword = false;
        }
    }

    private void initData(){
        if(getIntent() == null){
            clearAutoLockTime();//When launch app, clear autolock time
            presenter.mMode = SplashActivityPresenter.MODE_LAUNCH;
        }else{
            Bundle bundle = getIntent().getExtras();
            if(bundle != null){
                //Lock mode
                String mode = bundle.getString("mode");
                if(!TextUtils.isEmpty(mode) && mode.equals(SplashActivityPresenter.MODE_LOCK)){
                    presenter.mMode = SplashActivityPresenter.MODE_LOCK;
                }else{
                    presenter.mMode = SplashActivityPresenter.MODE_LAUNCH;
                }
            }else{
                //Launch mode
                presenter.mMode = SplashActivityPresenter.MODE_LAUNCH;
            }
        }

    }

    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new SplashActivityModule(this))
                .inject(this);
    }

    protected Presenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.submit)
    public void submit(){
        String password_1 = mEditTextPwd1.getText().toString();
        String password_2 = mEditTextPwd2.getText().toString();

        presenter.login(password_1, password_2);
    }

    @Override
    public void onBackPressed() {
        if(presenter.mMode.equals(SplashActivityPresenter.MODE_LAUNCH)){
            super.onBackPressed();
        }else if(presenter.mMode.equals(SplashActivityPresenter.MODE_LOCK)){
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    protected void clearAutoLockTime(){
        SharedPreferenceRepo shared = new SharedPreferenceRepoImpl(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.SP_HIDE_TIME, "0");
        map.put(Constants.SP_SHOW_TIME, "0");
        shared.saveProperties(map);
    }

	/**
	 * launch for lock
     * @param activity
     */
    public static void launchActivity(Activity activity){
        Intent intent = new Intent(activity, SplashActivity.class);
        intent.putExtra("mode", SplashActivityPresenter.MODE_LOCK);
        activity.startActivity(intent);
    }

}
