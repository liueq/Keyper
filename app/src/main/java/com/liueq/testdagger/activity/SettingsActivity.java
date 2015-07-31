package com.liueq.testdagger.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.activity.module.SettingsActivityModule;
import com.liueq.testdagger.ui.activity.presenter.SettingsActivityPresenter;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rl_change_pwd)
    RelativeLayout mRelativePwd;
    @Bind(R.id.rl_change_aes)
    RelativeLayout mRelativeAES;
    @Bind(R.id.rl_change_path)
    RelativeLayout mRelativePath;
    @Bind(R.id.rl_encrypt_pwd)
    RelativeLayout mRelativeEncPwd;
    @Bind(R.id.rl_encrypt_desc)
    RelativeLayout mRelativeEncDesc;
    @Bind(R.id.switch_pwd)
    Switch mSwitchPwd;
    @Bind(R.id.switch_desc)
    Switch mSwitchDesc;
    @Bind(R.id.tv_show_aes)
    TextView mTextViewAES;
    @Bind(R.id.tv_show_path)
    TextView mTextViewPath;

    @Inject
    SettingsActivityPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initView();
        initData();

    }

    private void initView(){
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT > 21){
            mToolbar.setElevation(8);
        }

        mSwitchPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.encryptPwd(isChecked);
            }
        });

        mSwitchDesc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.encryptDesc(isChecked);
            }
        });
    }

    private void initData(){
        presenter.initialSwitch();
        presenter.retrieveUIData();
    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.get(this).getAppComponent()
                .plus(new SettingsActivityModule(this))
                .inject(this);
    }

    public void checkSwitchPwd(boolean check){
        mSwitchPwd.setChecked(check);
    }

    public void checkSwitchDesc(boolean check){
        mSwitchDesc.setChecked(check);
    }

    @OnClick({R.id.rl_change_pwd, R.id.rl_change_aes, R.id.rl_change_path, R.id.rl_encrypt_pwd, R.id.rl_encrypt_desc})
    public void click(View v){
        int id = v.getId();
        switch (id){
            case R.id.rl_change_pwd:
                createChangePasswordDialog();
                break;
            case R.id.rl_change_aes:
                createChangeAESDialog();
                break;
            case R.id.rl_change_path:
                createChooseSavePathDialog();
                break;
            case R.id.rl_encrypt_pwd:
                mSwitchPwd.setChecked(!mSwitchPwd.isChecked());
                break;
            case R.id.rl_encrypt_desc:
                mSwitchDesc.setChecked(!mSwitchDesc.isChecked());
                break;
        }
    }

    private void createChangePasswordDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_pwd);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_pwd, null);
        final EditText old_pwd = (EditText) view.findViewById(R.id.et_old_pwd);
        final EditText new_pwd = (EditText) view.findViewById(R.id.et_new_pwd);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String old_pwd_str = old_pwd.getText().toString();
                String new_pwd_str = new_pwd.getText().toString();
                //Check old 是否和SP中相同
                if(presenter.checkPassword(old_pwd_str)){
                    //将新密码保存到SP
                    if(presenter.savePassword(new_pwd_str)) {
                        Toast.makeText(SettingsActivity.this, "Password saved", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, "Password can not null", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SettingsActivity.this, "Old password wrong", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create().show();
    }

    private void createChangeAESDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_aes);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_aes, null);
        final EditText new_aes = (EditText) view.findViewById(R.id.et_new_aes);

        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_aes_str = new_aes.getText().toString();
                if(presenter.saveAes(new_aes_str)) {
                    Toast.makeText(SettingsActivity.this, "AES password saved", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SettingsActivity.this, "AES password can not null", Toast.LENGTH_SHORT).show();
                }
                presenter.saveData();
                presenter.retrieveUIData();
            }
        });
        builder.setNegativeButton("CANCEL", null);
        builder.create().show();
    }

    private void createChooseSavePathDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_path);
        String [] paths = {"External Storage", "Internal Storage"};

        boolean [] paths_state = new boolean[2];
        if(presenter.mFilePathState != null){
            if(presenter.mFilePathState.get(Constants.SP_IS_SAVE_EXTERNAL)){
                paths_state[0] = true;
            }else{
                paths_state[0] = false;
            }

            if(presenter.mFilePathState.get(Constants.SP_IS_SAVE_INTERNAL)){
                paths_state[1] = true;
            }else{
                paths_state[1] = false;
            }
        }else{
            paths_state[0] = true;
            paths_state[1] = false;
        }

        builder.setMultiChoiceItems(paths, paths_state, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                switch (which){
                    case 0:
                        if(isChecked)
                            presenter.mFilePathState.put(Constants.SP_IS_SAVE_EXTERNAL, true);
                        else
                            presenter.mFilePathState.put(Constants.SP_IS_SAVE_EXTERNAL, false);
                        break;
                    case 1:
                        if(isChecked)
                            presenter.mFilePathState.put(Constants.SP_IS_SAVE_INTERNAL, true);
                        else
                            presenter.mFilePathState.put(Constants.SP_IS_SAVE_INTERNAL, false);
                        break;
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //将状态保存到SP
                if(presenter.savePath(presenter.mFilePathState)){
                    Toast.makeText(SettingsActivity.this, "Save path changed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SettingsActivity.this, "Must choose one path", Toast.LENGTH_SHORT).show();
                }
                presenter.retrieveUIData();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.retrieveUIData();
            }
        });
        builder.create().show();
    }

    public void setShowAES(String aes_pwd){
        mTextViewAES.setText(aes_pwd);
    }

    public void setShowPath(String file_path){
        mTextViewPath.setText(file_path);
    }
}
