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
import com.liueq.keyper.data.model.Tag;
import com.liueq.keyper.ui.common.OnItemClickListener;
import com.liueq.keyper.ui.tagdetail.TagDetailActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 29/2/2016.
 * TAG tab fragment
 */
public class TagListFragment extends Fragment implements OnItemClickListener, MainPagerAdapter.BackToTop{

	private MainActivity mActivity;
	private MainActivityPresenter mPresenter;

	@Bind(R.id.iv_hint)
	ImageView mImageViewHint;
	@Bind(R.id.tv_hint)
	TextView mTextViewHint;
	@Bind(R.id.recycler)
	RecyclerView mRecycler;

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

		mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecycler.setAdapter(mAdapter = new RecyclerTagListAdapter(mActivity, this));

		mTextViewHint.setText(R.string.tab_tag_hint);
		mImageViewHint.setImageResource(R.mipmap.ic_local_offer_grey600_48dp);
	}

	private void loadData(){
		mPresenter.loadTagListAction();
	}

	public void updateList(List<RecyclerTagListAdapter.TagItem> list){

		if(list.size() > 0){
			mTextViewHint.setVisibility(View.INVISIBLE);
			mImageViewHint.setVisibility(View.INVISIBLE);
			mRecycler.setVisibility(View.VISIBLE);
		}else{
			mTextViewHint.setVisibility(View.VISIBLE);
			mImageViewHint.setVisibility(View.VISIBLE);
			mRecycler.setVisibility(View.INVISIBLE);
		}

		mAdapter.replaceAll(list);
	}

	@Override
	public void onItemClick(View view, Object item, int position) {
		int id = view.getId();
		if(id == NonAlignGridView.ID_TextView){
			if(item instanceof Tag){
				Tag t = (Tag) item;
				TagDetailActivity.launchActivity(mActivity, t);
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	public void backToTop() {
		mRecycler.scrollToPosition(0);
	}
}

