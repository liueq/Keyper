package com.liueq.testdagger.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.utils.BackUpTool;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rl_change_pwd)
    RelativeLayout mRelativePwd;
    @Bind(R.id.rl_import)
    RelativeLayout mRelativeImport;
    @Bind(R.id.rl_export)
    RelativeLayout mRelativeExport;
    @Bind(R.id.rl_change_db)
    RelativeLayout mRelativeDb;
    @Bind(R.id.tv_show_db)
    TextView mTextViewDb;

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
        getSupportActionBar().setTitle(R.string.settings_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT > 21){
            mToolbar.setElevation(8);
        }
    }

    private void initData(){
        presenter.loadDataAction();

    }

    public void updateDBPassword(String password){
        mTextViewDb.setText(password);
    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new SettingsActivityModule(this))
                .inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }


    @OnClick({R.id.rl_change_pwd, R.id.rl_import, R.id.rl_export, R.id.rl_change_db})
    public void click(View v){
        int id = v.getId();
        switch (id){
            case R.id.rl_change_pwd:
                createChangePasswordDialog();
                break;
            case R.id.rl_change_db:
                createChangeDBDialog();
                break;
            case R.id.rl_import:
                //Import DB
                BackUpTool.importDB(this);
                break;
            case R.id.rl_export:
                //Export DB
                BackUpTool.exportDB(this);
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
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String old_pwd_str = old_pwd.getText().toString();
                String new_pwd_str = new_pwd.getText().toString();
                //Check old password
                if(presenter.checkPassAction(old_pwd_str)){
                    //Save to SP
                    if(presenter.savePassAction(new_pwd_str)) {
                        Toast.makeText(SettingsActivity.this, R.string.change_pwd_succeed, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SettingsActivity.this, R.string.change_pwd_not_null, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SettingsActivity.this, R.string.change_pwd_wrong, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void createChangeDBDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.change_db);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_db, null);
        final EditText new_db = (EditText) view.findViewById(R.id.et_new_db);

        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_aes_str = new_db.getText().toString();
                if(presenter.saveDBPassAction(new_aes_str)) {
                    Toast.makeText(SettingsActivity.this, R.string.change_db_succeed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SettingsActivity.this, R.string.change_db_not_null, Toast.LENGTH_SHORT).show();
                }
                presenter.loadDataAction();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
