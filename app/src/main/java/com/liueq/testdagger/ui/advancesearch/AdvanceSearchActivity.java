package com.liueq.testdagger.ui.advancesearch;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdvanceSearchActivity extends BaseActivity {

	@Bind(R.id.rl_searchbox)
	RelativeLayout mSearchBox;
	@Bind(R.id.iv_back)
	ImageView mIvBack;
	@Bind(R.id.iv_clear)
	ImageView mIvClear;
	@Bind(R.id.et_search)
	EditText mEtSearch;
	@Bind(R.id.ll_container)
	LinearLayout mLinearLayoutContainer;
	@Bind(R.id.rl_field)
	RecyclerView mRecyclerField;
	@Bind(R.id.rl_result)
	RecyclerView mRecyclerResult;

	@Inject
	AdvanceSearchPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_search);
		ButterKnife.bind(this);
	}

	@Override
	protected void setupActivityComponent() {
		TestApplication.getApplication().getAppComponent().plus(new AdvanceSearchModule(this)).inject(this);
	}

	@Override
	protected Presenter getPresenter() {
		return mPresenter;
	}
}
