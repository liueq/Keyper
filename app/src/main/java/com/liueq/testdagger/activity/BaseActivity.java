package com.liueq.testdagger.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.activity.presenter.Presenter;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
    }

    protected abstract void setupActivityComponent();

    protected abstract Presenter getPresenter();
}
