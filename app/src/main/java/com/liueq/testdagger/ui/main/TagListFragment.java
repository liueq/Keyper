package com.liueq.testdagger.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
public class TagListFragment extends Fragment implements OnItemClickListener {

	private MainActivity mActivity;
	private MainActivityPresenter mPresenter;

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
		View view = inflater.inflate(R.layout.fragment_tag_list, container, false);
		ButterKnife.bind(this, view);

		initView();
		loadData();
		return view;
	}

	private void initView(){
		mRecycler.setLayoutManager(new LinearLayoutManager(mActivity));
		mRecycler.setAdapter(mAdapter = new RecyclerTagListAdapter(mActivity, this));

//		List<Tag> list_tag = new ArrayList<Tag>();
//		list_tag.add(new Tag("ABC", "1"));
//		list_tag.add(new Tag("EDF", "1"));
//		list_tag.add(new Tag("HIJ", "1"));
//		List<RecyclerTagListAdapter.TagItem> list = new ArrayList<RecyclerTagListAdapter.TagItem>();
//		RecyclerTagListAdapter.TagItem item = new RecyclerTagListAdapter.TagItem("A", list_tag);
//
//		list.add(item);
//		mAdapter.replaceAll(list);
	}

	private void loadData(){
		mPresenter.loadTagListAction();
	}

	public void updateList(List<RecyclerTagListAdapter.TagItem> list){
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
}

