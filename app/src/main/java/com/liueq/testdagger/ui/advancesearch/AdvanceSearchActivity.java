package com.liueq.testdagger.ui.advancesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.ui.common.OnItemClickListener;
import com.liueq.testdagger.ui.main.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AdvanceSearchActivity extends BaseActivity implements OnItemClickListener {

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

	private RecyclerFieldAdapter mFieldAdapter;
	private RecyclerListAdapter mListAdapter;

	public static void launchActivity(Activity activity){
		Intent intent = new Intent(activity, AdvanceSearchActivity.class);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_search);
		ButterKnife.bind(this);

		initView();
	}

	private void initView(){
		mRecyclerField.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		mRecyclerField.setAdapter(mFieldAdapter = new RecyclerFieldAdapter(this, this));
		mFieldAdapter.replaceAll(mPresenter.mSearchFields);

		mRecyclerResult.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerResult.setAdapter(mListAdapter = new RecyclerListAdapter(this, this));
	}

	@Override
	protected void setupActivityComponent() {
		TestApplication.getApplication().getAppComponent().plus(new AdvanceSearchModule(this)).inject(this);
	}

	@Override
	protected Presenter getPresenter() {
		return mPresenter;
	}

	@Override
	public void onItemClick(View view, Object item, int position) {
		int id = view.getId();
		switch (id){
			case RecyclerFieldAdapter.ViewHolder.ID_RelativeLayout:
				//TODO Change search field
				if(item instanceof RecyclerFieldAdapter.SearchField){
					RecyclerFieldAdapter.SearchField searchField = (RecyclerFieldAdapter.SearchField) item;
					searchField.mSelected = true;
					mFieldAdapter.reverseOther(searchField);
				}
				break;
			case RecyclerListAdapter.ViewHolder.ID_LienarLayout:
				//TODO Go to detail
				break;
		}
	}
}
