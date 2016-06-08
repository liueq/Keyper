package com.liueq.keyper.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liueq.keyper.R;
import com.liueq.keyper.ui.accountdetail.AccountDetailActivity;
import com.liueq.keyper.data.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 22/2/2016.
 * 首页的StarList
 */
public class StarListFragment extends Fragment implements RecyclerStarListAdapter.OnItemClickListener, MainPagerAdapter.BackToTop{

	@Bind(R.id.iv_hint)
	ImageView mImageViewHint;
	@Bind(R.id.tv_hint)
	TextView mTextViewHint;
	@Bind(R.id.recycler)
	RecyclerView mRecycler;

	private RecyclerStarListAdapter mAdapter;

	private MainActivity mActivity;
	private MainActivityPresenter mPresenter;

	public static StarListFragment newInstance() {

		Bundle args = new Bundle();

		StarListFragment fragment = new StarListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (MainActivity) getActivity();
		mPresenter = (MainActivityPresenter) mActivity.getPresenter();
		mPresenter.attachFragment(StarListFragment.class, this);
	}

	@Override
	public void onDetach() {
		mPresenter.detachFragment(StarListFragment.class);
		super.onDetach();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, view);

		initView();
		loadData();
		return view;
	}

	private void initView(){

		mRecycler.setHasFixedSize(true);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAdapter = new RecyclerStarListAdapter(getActivity(), new ArrayList<Account>(), this);
		mRecycler.setAdapter(mAdapter);

		mTextViewHint.setText(R.string.tab_star_hint);
		mImageViewHint.setImageResource(R.mipmap.ic_star_outline_grey600_48dp);
	}

	private void loadData(){
		mPresenter.loadStarListAction();
	}

	public void updateUI(List<Account> list){

		if(list.size() > 0){
			mTextViewHint.setVisibility(View.INVISIBLE);
			mImageViewHint.setVisibility(View.INVISIBLE);
			mRecycler.setVisibility(View.VISIBLE);
		}else{
			mTextViewHint.setVisibility(View.VISIBLE);
			mImageViewHint.setVisibility(View.VISIBLE);
			mRecycler.setVisibility(View.INVISIBLE);
		}

		mAdapter.clear();
		mAdapter.addAll(list);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	public void onItemClicked(View view, Object item, int position) {
		int id = view.getId();
		Account account = (Account) item;
		if(id == RecyclerStarListAdapter.ViewHolder.ID_LinearLayout || id == RecyclerStarListAdapter.ViewHolder.ID_TextViewPassword){
			AccountDetailActivity.launchActivity(mActivity, account);
		}
	}

	@Override
	public void backToTop() {
		mRecycler.scrollToPosition(0);
	}
}
