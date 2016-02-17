package com.liueq.testdagger.activity;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.activity.module.MainActivityModule;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.activity.presenter.MainActivityPresenter;
import com.liueq.testdagger.ui.activity.presenter.Presenter;
import com.liueq.testdagger.ui.adapter.MainPagerAdapter;
import com.liueq.testdagger.ui.adapter.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.v7.widget.RecyclerView.*;

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

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = null;
        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, MainActivity.class)));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    presenter.search(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    //进行Search
                    presenter.search(newText);
                    return false;
                }
            });

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    presenter.loadData();
                    return false;
                }
            });

        }
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
