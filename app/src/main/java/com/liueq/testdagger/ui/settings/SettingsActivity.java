package com.liueq.testdagger.ui.settings;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.AlteredCharSequence;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Settings
 */
public class SettingsActivity extends BaseActivity {

    public final static int REQUEST_CODE = 1234;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.rl_change_pwd)
    RelativeLayout mRelativePwd;
    @Bind(R.id.rl_import)
    RelativeLayout mRelativeImport;
    @Bind(R.id.rl_export)
    RelativeLayout mRelativeExport;
    @Bind(R.id.rl_about)
    RelativeLayout mRelativeAbout;
//    @Bind(R.id.tv_show_db)
//    TextView mTextViewDb;
    @Bind(R.id.rl_set_timeout)
    RelativeLayout mRelativeLayoutTimeout;

    @Inject
    SettingsActivityPresenter mPresenter;

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
        mPresenter.loadDataAction();

    }

//    public void updateDBPassword(String password){
//        mTextViewDb.setText(password);
//    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new SettingsActivityModule(this))
                .inject(this);
    }

    protected Presenter getPresenter() {
        return mPresenter;
    }


    @OnClick({R.id.rl_change_pwd, R.id.rl_import, R.id.rl_export, R.id.rl_set_timeout, R.id.rl_about})
    public void click(View v){
        int id = v.getId();
        switch (id){
            case R.id.rl_change_pwd:
                createChangePasswordDialog();
                break;
//            case R.id.rl_change_db:
//                createChangeDBDialog();
//                break;
            case R.id.rl_set_timeout:
                createChooseTimeDialog();
                break;
            case R.id.rl_import:
                //Import DB
                String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".db");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.rl_export:
                //Export DB: Show dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.export_data)
                        .setMessage(R.string.export_dialog_msg)
                        .setPositiveButton(R.string.export_dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.exportDBAction();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
                break;
            case R.id.rl_about:
                Toast.makeText(SettingsActivity.this, "ABOUT", Toast.LENGTH_SHORT).show();
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
                if(mPresenter.checkPassAction(old_pwd_str)){
                    //Save to SP
                    mPresenter.savePassAction(new_pwd_str);
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
                String new_db_str = new_db.getText().toString();
                if(TextUtils.isEmpty(new_db_str)) {
                    Toast.makeText(SettingsActivity.this, R.string.change_db_not_null, Toast.LENGTH_SHORT).show();
                }else if(new_db_str.length() < 6) {
                    Toast.makeText(SettingsActivity.this, R.string.change_db_short, Toast.LENGTH_SHORT).show();
                }else{
                    mPresenter.saveDBPassAction(new_db_str);
                    mPresenter.loadDataAction();
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void createChooseTimeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.set_time_dialog_title);
        String [] period = {"1 min", "5 min", "15 min", "1 hour", "No auto lock"};

        builder.setSingleChoiceItems(period, mPresenter.mMapPeriodToPos.get(mPresenter.getAutoLockPeriod()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        mPresenter.setAutoLockPeriod(60);
                        break;
                    case 1:
                        mPresenter.setAutoLockPeriod(5 * 60);
                        break;
                    case 2:
                        mPresenter.setAutoLockPeriod(15 * 60);
                        break;
                    case 3:
                        mPresenter.setAutoLockPeriod(60 * 60);
                        break;
                    case 4:
                        mPresenter.setAutoLockPeriod(3600 * 24);
                        break;
                }
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

    private void createImportDBDialog(final String path){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_db_password);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_change_db, null);
        final EditText new_db = (EditText) view.findViewById(R.id.et_new_db);

        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String new_db_str = new_db.getText().toString();
                if(TextUtils.isEmpty(new_db_str)) {
                    Toast.makeText(SettingsActivity.this, R.string.change_db_not_null, Toast.LENGTH_SHORT).show();
                }else{
                    mPresenter.importDBAction(path, new_db_str);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create().show();
    }

    private void createAboutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_dialog_title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            if(data != null){
                data.getExtras();
                String path = data.getDataString();
                Uri uri = data.getData();
                String prefix_file = "file://";
                String prefix_content = "content://";
                if(path.startsWith(prefix_file)){
                    //When file
                    path = path.substring(prefix_file.length());
                    createImportDBDialog(path);
                }else if(path.startsWith(prefix_content)){
                    //When content provider
                    String selection[] = new String[]{"_data"};
                    Cursor cursor = getContentResolver().query(uri, selection, null, null, null);
                    if(cursor.moveToNext()){
                        createImportDBDialog(cursor.getString(0));
                    }
                }else{
                    Toast.makeText(SettingsActivity.this, R.string.import_file_uri_error, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
