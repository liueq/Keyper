package com.liueq.testdagger.activity;

import android.os.Build;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.activity.module.AccountDetailActivityModule;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.activity.presenter.AccountDetailActivityPresenter;
import com.liueq.testdagger.ui.activity.presenter.Presenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountDetailActivity extends BaseActivity {

    public final static String TAG = "AccountDA";

    @Bind(R.id.tv_site)
    TextView mTextViewSite;
    @Bind(R.id.et_site)
    EditText mEditTextSite;
    @Bind(R.id.et_name)
    EditText mEditTextName;
    @Bind(R.id.et_password)
    EditText mEditTextPwd;
    @Bind(R.id.et_mail)
    EditText mEditTextMail;
    @Bind(R.id.et_desc)
    EditText mEditTextDesc;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.linear)
    LinearLayout mLinearLayout;

    @Inject
    AccountDetailActivityPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);

        modifyFromData();
        initView();
        initData();

    }

    private void modifyFromData(){
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null){
            presenter.loadData(bundle);
        }
    }

    private void initView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(mEditTextSite.getText().toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT > 21){
            toolbar.setElevation(8);
        }

        mTextViewSite.requestFocus();
    }

    private void initData(){

    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.get(this).getAppComponent()
                .plus(new AccountDetailActivityModule(this))
                .inject(this);
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            if(presenter.deleteAccount()){
                Toast.makeText(AccountDetailActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(AccountDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateUI(Account account){
        mEditTextSite.setText(account.site);
        mEditTextName.setText(account.username);
        mEditTextPwd.setText(account.password);
        mEditTextMail.setText(account.mail);
        mEditTextDesc.setText(account.description);
    }

    @OnClick(R.id.btn_commit)
    public void saveData(){
        Account account;
        if(presenter.getCurrentAccount() == null){
            account = new Account();
        }else{
            account = presenter.getCurrentAccount();
        }

        account.site = mEditTextSite.getText().toString();
        account.username = mEditTextName.getText().toString();
        account.password = mEditTextPwd.getText().toString();
        account.mail = mEditTextMail.getText().toString();
        account.description = mEditTextDesc.getText().toString();

        if(BuildConfig.DEBUG){
            Log.d(TAG, "saveData account is " + account.toString());
        }

        if(presenter.saveData(account)){
            Snackbar snackbar = Snackbar.make(mLinearLayout, "SAVED", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar.make(mLinearLayout, "Site can't be NULL", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
}
