package com.liueq.testdagger.ui.main;

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
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivity;
import com.liueq.testdagger.ui.advancesearch.AdvanceSearchActivity;
import com.liueq.testdagger.ui.settings.SettingsActivity;

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
        TestApplication.getApplication().getAppComponent()
                .plus(new MainActivityModule(this))
                .inject(this);
    }

    private void initView(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mViewPager.setAdapter(mPagerAdapter = new MainPagerAdapter(this, getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Back to Top
                mPagerAdapter.backToTop();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if(id == R.id.action_search){
            mSearchDialogFragment = SearchDialogFragment.newInstance(this);

            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(mSearchDialogFragment, SearchDialogFragment.TAG);
            fragmentTransaction.commit();
        }else if(id == R.id.action_advance_search){
            AdvanceSearchActivity.launchActivity(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.floating)
    public void fabClick(){
        Intent intent = new Intent(MainActivity.this, AccountDetailActivity.class);
        startActivity(intent);
    }

    public Presenter getPresenter() {
        return presenter;
    }
}
