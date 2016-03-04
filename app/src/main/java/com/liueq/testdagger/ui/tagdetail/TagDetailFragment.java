package com.liueq.testdagger.ui.tagdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivity;
import com.liueq.testdagger.ui.main.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 29/2/2016.
 * Fragment of tag detail
 */
public class TagDetailFragment extends Fragment implements RecyclerListAdapter.OnItemClickListener{

	@Bind(R.id.recycler)
	RecyclerView mRecycler;

	private RecyclerListAdapter recyclerListAdapter;

	private TagDetailActivity mActivity;
	private TagDetailActivityPresenter mPresneter;

	public static TagDetailFragment newInstance() {
		Bundle args = new Bundle();

		TagDetailFragment fragment = new TagDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (TagDetailActivity) getActivity();
		mPresneter = (TagDetailActivityPresenter) mActivity.getmPresenter();
		mPresneter.attachFragment(TagDetailFragment.class, this);
	}

	@Override
	public void onDetach() {
		mPresneter.detachFragment(TagDetailFragment.class);
		super.onDetach();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, container, false);
		ButterKnife.bind(this, v);

		initView();
		loadData();
		return v;
	}

	private void initView(){

		mRecycler.setHasFixedSize(true);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerListAdapter = new RecyclerListAdapter(getActivity(), new ArrayList<Account>(), this);
		mRecycler.setAdapter(recyclerListAdapter);
	}

	private void loadData(){
		mPresneter.loadListAction();
	}

	public void updateUI(List<Account> list){
		recyclerListAdapter.clear();
        recyclerListAdapter.addAll(list);
        recyclerListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClicked(View view, Object item, int position) {
		int id = view.getId();
		Account account = (Account) item;
		if(id == RecyclerListAdapter.ViewHolder.ID_LienarLayout){
			//Launch detail activity
			AccountDetailActivity.launchActivity(mActivity, account);
		}else if(id == RecyclerListAdapter.ViewHolder.ID_ImageView){
			//Star
			mPresneter.starAction(account);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
}
