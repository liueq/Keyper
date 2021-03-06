package com.liueq.keyper.ui.accountdetail;

import android.app.Activity;
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

import com.liueq.keyper.R;
import com.liueq.keyper.TestApplication;
import com.liueq.keyper.base.BaseActivity;
import com.liueq.keyper.base.Presenter;
import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.utils.GoldenHammer;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AccountDetailActivity extends BaseActivity{

    public final static String TAG = "AccountDA";

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fragment)
    FrameLayout mContainer;

    @Inject
    AccountDetailActivityPresenter mPresenter;

    MenuItem mMenuItemSave;

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
        mPresenter.init(bundle);
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

        if(TextUtils.isEmpty(mPresenter.mId)){
            //Write mode
            mPresenter.intoWriteMode();
        }else{
            //Read mode
            mPresenter.intoReadMode();
        }
    }

    private void initData() {
    }

    @Override
    protected void setupActivityComponent() {
        TestApplication.getApplication().getAppComponent()
                .plus(new AccountDetailActivityModule(this))
                .inject(this);
    }

    public Presenter getPresenter() {
        return mPresenter;
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
        mMenuItemSave = menu.findItem(R.id.action_save);
        Account account = mPresenter.getCurrentAccount();
        if(account == null){
            return true;
        }

        item.setIcon(getStarIcon(account.is_stared));
        if(mPresenter.getMode().equals(AccountDetailActivityPresenter.MODE_READ)){
            mMenuItemSave.setIcon(R.mipmap.ic_mode_edit_white_24dp);
        }else if(mPresenter.getMode().equals(AccountDetailActivityPresenter.MODE_WRITE)){
            mMenuItemSave.setIcon(R.mipmap.ic_save_white_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_star){
            Account account = mPresenter.getCurrentAccount();
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

        }else if (id == R.id.action_save) {
            //Save
            if(mPresenter.getMode().equals(AccountDetailActivityPresenter.MODE_READ)){
                //Current is read mode, into write mode
                mPresenter.intoWriteMode();
                item.setIcon(R.mipmap.ic_save_white_24dp);
            }else if(mPresenter.getMode().equals(AccountDetailActivityPresenter.MODE_WRITE)){
                //Current is write mode, save data and into read mode
                mPresenter.saveDataToDB(mPresenter.getCurrentAccount());
                mPresenter.intoReadMode();
                item.setIcon(R.mipmap.ic_mode_edit_white_24dp);
            }
            return true;
        }else if (id == R.id.action_info){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            String create_time = getString(R.string.dialog_info_create, GoldenHammer.timeFormat(mPresenter.getCurrentAccount().create_time));
            String update_time = getString(R.string.dialog_info_update, GoldenHammer.timeFormat(mPresenter.getCurrentAccount().update_time));

            builder.setTitle(R.string.dialog_title_info);
            builder.setMessage(create_time + "\n" + update_time);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    private Drawable getStarIcon(boolean state){
        Drawable drawable = null;
        if(state){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getResources().getDrawable(R.mipmap.ic_star_black_24dp);
                drawable.setTint(getResources().getColor(R.color.colorAccent));
            }else{
                //Compat
                drawable = getResources().getDrawable(R.mipmap.ic_star_white_24dp);
            }
        }else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                drawable = getResources().getDrawable(R.mipmap.ic_star_outline_black_24dp);
                drawable.setTint(getResources().getColor(R.color.white));
            }else {
                //Compat
                drawable = getResources().getDrawable(R.mipmap.ic_star_outline_white_24dp);
            }
        }

        return drawable;
    }

	/**
     * when mPresenter delete finish, show result
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

    @Override
    public void onBackPressed() {
        if(!mPresenter.checkChangeSavedAction()){
            //Show save tips
            showSaveDialog();
        }else{
            super.onBackPressed();
        }
    }

    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.detail_quit_title);
        builder.setMessage(R.string.detail_quit_msg);
        builder.setPositiveButton(R.string.positive_quit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void changeMenuItemRead(){
        if(mMenuItemSave != null){
            mMenuItemSave.setIcon(R.mipmap.ic_mode_edit_white_24dp);
        }
    }

    public void changeMenuItemWrite(){
        if(mMenuItemSave != null){
            mMenuItemSave.setIcon(R.mipmap.ic_save_white_24dp);
        }
    }
}
