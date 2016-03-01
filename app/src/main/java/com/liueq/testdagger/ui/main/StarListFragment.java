package com.liueq.testdagger.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 22/2/2016.
 * 首页的StarList
 */
public class StarListFragment extends Fragment implements RecyclerStarListAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

	@Bind(R.id.recycler)
	RecyclerView mRecycler;
	@Bind(R.id.refresh_layout)
	SwipeRefreshLayout mRefreshLayout;

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

		mRecycler.setHasFixedSize(true);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAdapter = new RecyclerStarListAdapter(getActivity(), new ArrayList<Account>(), this);
		mRecycler.setAdapter(mAdapter);
	}

	private void loadData(){
		loadListOb().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(loadListSub());
	}

	private void updateUI(List<Account> list){
		if(mRefreshLayout.isRefreshing()){
			mRefreshLayout.setRefreshing(false);
			Toast.makeText(mActivity, R.string.toast_sync_db, Toast.LENGTH_SHORT).show();
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

	private Observable<List<Account>> loadListOb(){
		return Observable.create(new Observable.OnSubscribe<List<Account>>() {
			@Override
			public void call(Subscriber<? super List<Account>> subscriber) {
				List<Account> list = mPresenter.loadStarList();
				subscriber.onNext(list);
			}
		});
	}

	private Subscriber<List<Account>> loadListSub(){
		return new Subscriber<List<Account>>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(List<Account> accounts) {
				updateUI(accounts);
			}
		};
	}

	@Override
	public void onItemClicked(View view, Object item, int position) {
		int id = view.getId();
		Account account = (Account) item;
		if(id == RecyclerStarListAdapter.ViewHolder.ID_LinearLayout){
			AccountDetailActivity.launchActivity(mActivity, account);
		}
	}

	@Override
	public void onRefresh() {
		loadData();
	}

}
