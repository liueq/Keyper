package com.liueq.testdagger.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.activity.module.MainActivityModule;
import com.liueq.testdagger.ui.activity.presenter.MainActivityPresenter;
import com.liueq.testdagger.ui.activity.presenter.Presenter;
import com.liueq.testdagger.ui.adapter.MainPagerAdapter;
import com.liueq.testdagger.ui.fragment.SearchDialogFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.floating)
    FloatingActionButton fab;
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.pager)
    ViewPager mViewPager;

    @Inject
    MainActivityPresenter presenter;

    private MainPagerAdapter mPagerAdapter;
    SearchDialogFragment mSearchDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    /**
     * 依赖注入必须手动获取，从AppComponent到当前Module
     */
    @Override
    protected void setupActivityComponent() {
        TestApplication.get(this).getAppComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    private void initView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_activity_title);

        mViewPager.setAdapter(mPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_search){
            mSearchDialogFragment = SearchDialogFragment.newInstance(this);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mSearchDialogFragment, SearchDialogFragment.TAG);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.floating)
    public void fabClick(){
        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public Presenter getPresenter() {
        return presenter;
    }
}
