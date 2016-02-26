package com.liueq.testdagger.ui.launch;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends BaseActivity {

    public final static String TAG = "splash_A";

    @Bind(R.id.pwd_1)
    EditText mEditTextPwd1;
    @Bind(R.id.pwd_2)
    EditText mEditTextPwd2;
    @Bind(R.id.submit)
    Button mButtonSubmit;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Inject
    SplashActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
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

    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new SplashActivityModule(this))
                .inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.submit)
    public void submit(){
        String password_1 = mEditTextPwd1.getText().toString();
        String password_2 = mEditTextPwd2.getText().toString();

        presenter.login(password_1, password_2);
    }

}