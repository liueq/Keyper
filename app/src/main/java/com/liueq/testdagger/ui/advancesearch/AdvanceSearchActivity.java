package com.liueq.testdagger.ui.advancesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivity;
import com.liueq.testdagger.ui.accountdetail.AccountDetailFragment;
import com.liueq.testdagger.ui.common.OnItemClickListener;
import com.liueq.testdagger.ui.main.RecyclerListAdapter;
import com.liueq.testdagger.utils.GoldenHammer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
	@Bind(R.id.progress)
	ProgressBar mProgressBar;

	@Inject
	AdvanceSearchPresenter mPresenter;

	private RecyclerFieldAdapter mFieldAdapter;
	private RecyclerListAdapter mListAdapter;

	/**
	 * Launch empty activity
	 * @param activity
	 */
	public static void launchActivity(Activity activity){
		Intent intent = new Intent(activity, AdvanceSearchActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * Launch activity with search_key
	 * @param activity
	 * @param search
	 */
	public static void launchActivity(Activity activity, String search){
		Intent intent = new Intent(activity, AdvanceSearchActivity.class);
		intent.putExtra("search", search);
		intent.putExtra("field", AdvanceSearchPresenter.SEARCH_FIELDS[0]);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advance_search);
		ButterKnife.bind(this);

		getBundle();
		initView();
		doSearch();
	}

	private void getBundle(){
		String search = getIntent().getStringExtra("search");
		String field = getIntent().getStringExtra("field");
		if(!TextUtils.isEmpty(search) && !TextUtils.isEmpty(field)){
			mPresenter.mCurrentSearch = search;
			mPresenter.mCurrentField = field;
		}
	}

	private void initView(){
		mRecyclerField.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		mRecyclerField.setAdapter(mFieldAdapter = new RecyclerFieldAdapter(this, this));
		mFieldAdapter.replaceAll(mPresenter.mSearchFields);

		mRecyclerResult.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerResult.setAdapter(mListAdapter = new RecyclerListAdapter(this, this, false));

		mEtSearch.setText(mPresenter.mCurrentSearch);
		mEtSearch.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				boolean isValidKey = event != null && keyCode == KeyEvent.KEYCODE_ENTER;
				if(isValidKey){
					doSearch();
				}
				return false;
			}
		});

		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim();
				if(TextUtils.isEmpty(text)){
					mIvClear.setVisibility(View.INVISIBLE);
				}else{
					mIvClear.setVisibility(View.VISIBLE);
				}
				mPresenter.mCurrentSearch = text;
			}
		});
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
				if(item instanceof RecyclerFieldAdapter.SearchField){
					RecyclerFieldAdapter.SearchField searchField = (RecyclerFieldAdapter.SearchField) item;
					searchField.mSelected = true;
					mFieldAdapter.reverseOther(searchField);

					mPresenter.mCurrentField = searchField.mField;
					doSearch();
				}
				break;
			case RecyclerListAdapter.ViewHolder.ID_LienarLayout:
				if(item instanceof Account){
					Account account = (Account) item;
					AccountDetailActivity.launchActivity(this, account);
				}
				break;
		}
	}

	@OnClick({R.id.iv_back, R.id.iv_clear})
	public void onClick(View view){
		int id = view.getId();
		switch (id){
			case R.id.iv_back:
				onBackPressed();
				break;
			case R.id.iv_clear:
				mEtSearch.setText("");
				break;
		}
	}

	private void doSearch(){
		mRecyclerResult.setVisibility(View.INVISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		mPresenter.searchAction();
	}

	public void updateUI(List<Account> list){
		GoldenHammer.hideInputMethod(this, mEtSearch);
		mProgressBar.setVisibility(View.INVISIBLE);
		mRecyclerResult.setVisibility(View.VISIBLE);
		mListAdapter.replaceAll(list);
	}
}
