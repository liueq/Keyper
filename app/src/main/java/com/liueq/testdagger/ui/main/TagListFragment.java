package com.liueq.testdagger.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 29/2/2016.
 * TAG tab fragment
 */
public class TagListFragment extends Fragment implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

	private MainActivity mActivity;
	private MainActivityPresenter mPresenter;

	@Bind(R.id.recycler)
	RecyclerView mRecycler;
	@Bind(R.id.refresh_layout)
	SwipeRefreshLayout mRefreshLayout;

	RecyclerTagListAdapter mAdapter;

	public static TagListFragment newInstance() {

		Bundle args = new Bundle();

		TagListFragment fragment = new TagListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (MainActivity) getActivity();
		mPresenter = (MainActivityPresenter) mActivity.getPresenter();
		mPresenter.attachFragment(TagListFragment.class, this);
	}

	@Override
	public void onDetach() {
		mPresenter.detachFragment(TagListFragment.class);
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
		mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
		mRefreshLayout.setOnRefreshListener(this);

		mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecycler.setAdapter(mAdapter = new RecyclerTagListAdapter(mActivity, this));
	}

	private void loadData(){
		mPresenter.loadTagListAction();
	}

	public void updateList(List<RecyclerTagListAdapter.TagItem> list){
		if(mRefreshLayout.isRefreshing()){
			mRefreshLayout.setRefreshing(false);
			Toast.makeText(mActivity, R.string.toast_sync_db, Toast.LENGTH_SHORT).show();
		}

		mAdapter.replaceAll(list);
	}

	@Override
	public void onItemClick(View view, Object item, int position) {
		int id = view.getId();
		if(id == NonAlignGridView.ID_TextView){
			if(item instanceof Tag){
				//TODO 跳转到TAG详情
				Tag t = (Tag) item;
				Toast.makeText(mActivity, t.tag_name, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onRefresh() {
		loadData();
	}
}

