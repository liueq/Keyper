package com.liueq.testdagger.ui.launch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.utils.GoldenHammer;

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
    @Bind(R.id.iv_hide_1)
    ImageView mImageViewHide1;
    @Bind(R.id.iv_hide_2)
    ImageView mImageViewHide2;

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
            mToolbar.setElevation(GoldenHammer.pixelToDp(4, this));
            mTextViewSubmit.setElevation(GoldenHammer.pixelToDp(2, this));
        }

        SharedPreferences sp = getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
        if(sp.getString(Constants.SP_PWD, null) != null){
            if(BuildConfig.DEBUG){
                Log.d(TAG, "initData password is not null");
            }
            mEditTextPwd2.setVisibility(View.GONE);
            mImageViewHide2.setVisibility(View.GONE);
            presenter.hasPassword = true;

            mEditTextPwd1.requestFocus();
        }else{
            mEditTextPwd2.setVisibility(View.VISIBLE);
            mImageViewHide2.setVisibility(View.VISIBLE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_info){
            //Show info dialog
            StringBuilder sb = new StringBuilder();
            sb.append(getString(R.string.welcome_tip_1));
            sb.append("\n\n");
            sb.append(getString(R.string.welcome_tip_2));
            sb.append("\n\n");
            sb.append(getString(R.string.welcome_tip_3));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.welcome_dialog_title))
                    .setMessage(sb.toString())
                    .setPositiveButton(R.string.ok, null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new SplashActivityModule(this))
                .inject(this);
    }

    protected Presenter getPresenter() {
        return presenter;
    }

    @OnClick({R.id.submit, R.id.iv_hide_1, R.id.iv_hide_2})
    public void onClick(View view){
        int id = view.getId();
        if(id == R.id.submit){
            String password_1 = mEditTextPwd1.getText().toString();
            String password_2 = mEditTextPwd2.getText().toString();

            presenter.login(password_1, password_2);
        }else if(id == R.id.iv_hide_1){
            if(presenter.checkPassword1Action()){
                //Showing
                mImageViewHide1.setImageResource(R.mipmap.ic_remove_red_eye_grey600_24dp);
                mEditTextPwd1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                //Hiding
                mImageViewHide1.setImageResource(R.mipmap.ic_remove_red_eye_close_grey600_24dp);
                mEditTextPwd1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD); //TODO Can not solve the problem that allowing input chinese, when the password is visible.
            }

            mEditTextPwd1.setSelection(mEditTextPwd1.getText().length());
        }else if(id == R.id.iv_hide_2){
            if(presenter.checkPassword2Action()){
                //Showing
                mImageViewHide2.setImageResource(R.mipmap.ic_remove_red_eye_grey600_24dp);
                mEditTextPwd2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }else{
                //Hiding
                mImageViewHide2.setImageResource(R.mipmap.ic_remove_red_eye_close_grey600_24dp);
                mEditTextPwd2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }

            mEditTextPwd2.setSelection(mEditTextPwd2.getText().length());
        }
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }
}
