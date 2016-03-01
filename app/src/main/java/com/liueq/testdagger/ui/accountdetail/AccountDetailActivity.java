package com.liueq.testdagger.ui.accountdetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.model.Account;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountDetailActivity extends BaseActivity implements DialogInterface.OnClickListener{

    public final static String TAG = "AccountDA";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fragment)
    FrameLayout mContainer;

    @Inject
    AccountDetailActivityPresenter presenter;

	/**
     * Launch this activity
     * @param activity from activity
     * @param account Account Object
     */
    public static void launchActivity(Activity activity, Account account){
        Bundle bundle = new Bundle();
        bundle.putSerializable("account", account);

        Intent intent = new Intent(activity, AccountDetailActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);

        receiveIntent();
        initView();
        initData();
    }

    private void receiveIntent() {
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            presenter.init(bundle);
        }
    }

    private void initView() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT > 21) {
            toolbar.setElevation(8);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, AccountDetailFragment.newInstance());
        transaction.commit();
    }

    private void initData() {
    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new AccountDetailActivityModule(this))
                .inject(this);
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_account_detail, menu);
        //init
        MenuItem item = menu.findItem(R.id.action_star);
        Account account = presenter.getCurrentAccount();
        if(account == null){
            return true;
        }

        item.setIcon(getStarIcon(account.is_stared));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_star){
            Account account = presenter.getCurrentAccount();
            if(account == null){
                return true;
            }

            if(TextUtils.isEmpty(account.id)){
                //When create, just change the icon and account data
                account.is_stared = !account.is_stared;
                item.setIcon(getStarIcon(account.is_stared));
            }else{
                //When update, save data to db
                account.is_stared = !account.is_stared;
                item.setIcon(getStarIcon(account.is_stared));

                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment);
                if(fragment instanceof AccountDetailFragment){
                    ((AccountDetailFragment) fragment).saveData();
                }
            }

        }else if (id == R.id.action_delete) {
            //Show dialog
            showDialog().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Drawable getStarIcon(boolean state){
        Drawable drawable = null;
        if(state){
            drawable = getDrawable(R.mipmap.ic_star_black_24dp);
            drawable.setTint(getResources().getColor(R.color.yellow));
        }else {
            drawable = getDrawable(R.mipmap.ic_star_outline_black_24dp);
            drawable.setTint(getResources().getColor(R.color.white));
        }

        return drawable;
    }

	/**
     * Create a Yes or No Dialog
     * @return
     */
    private Dialog showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_title_delete);
        builder.setMessage(R.string.dialog_content_delete);
        builder.setPositiveButton(R.string.dialog_yes_delete, this);
        builder.setNegativeButton(R.string.dialog_no_delete, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == Dialog.BUTTON_POSITIVE){
            presenter.deleteAccountAction();
        }else{
            dialog.dismiss();
        }
    }

	/**
     * when presenter delete finish, show result
     * @param ok
     */
    public void deleteToast(boolean ok){
        if (ok) {
            Toast.makeText(AccountDetailActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(AccountDetailActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    }

}
