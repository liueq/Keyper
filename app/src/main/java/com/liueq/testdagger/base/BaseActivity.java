package com.liueq.testdagger.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liueq.testdagger.VisibleObserver;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    protected VisibleObserver mVisibleObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();

    }

    protected abstract void setupActivityComponent();

    protected abstract Presenter getPresenter();

    @Override
    protected void onResume() {
        super.onResume();
        mVisibleObserver.onApplicationShow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVisibleObserver.onApplicationHide();
    }
}
