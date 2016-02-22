package com.liueq.testdagger.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.activity.presenter.MainActivityPresenter;
import com.liueq.testdagger.ui.adapter.RecyclerListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 17/2/2016.
 * 主界面，ALL tab
 */
public class ListFragment extends Fragment implements RecyclerListAdapter.OnItemClickListener{

	@Bind(R.id.recycler)
	RecyclerView mRecycler;

	private RecyclerListAdapter recyclerListAdapter;

	private MainActivity mActivity;
	private MainActivityPresenter mPresneter;

	public static ListFragment newInstance() {
		Bundle args = new Bundle();
		ListFragment fragment = new ListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (MainActivity) getActivity();
		mPresneter = (MainActivityPresenter) mActivity.getPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_list, null);
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

		mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private boolean scrollUp = true;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
				//TODO 这里会和FAB互动，估计得使用接口回调
//                if(BuildConfig.DEBUG)
//                    Log.i("liueq", "onScrolled dy = " + dy);
//
//                if(dy > 0 && scrollUp){
//                    Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_appear);
//                    anim.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            fab.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    fab.startAnimation(anim);
//
//                    scrollUp = false;
//                }else if(dy < 0 && !scrollUp){
//                    Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fab_disappear);
//                    anim.setAnimationListener(new Animation.AnimationListener() {
//                        @Override
//                        public void onAnimationStart(Animation animation) {
//
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animation animation) {
//                            fab.setVisibility(View.VISIBLE);
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animation animation) {
//
//                        }
//                    });
//                    fab.startAnimation(anim);
//
//                    scrollUp = true;
//                }
            }
        });
	}

	private void loadData(){
		loadListOb().subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(loadListSub());
	}

	private void updateUI(List<Account> list){
        recyclerListAdapter.clear();
        recyclerListAdapter.addAll(list);
        recyclerListAdapter.notifyDataSetChanged();
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
				List<Account> list = mPresneter.loadList();
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

	private Observable<Boolean> starObj(final Account account){
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				boolean result = mPresneter.starOrUnStar(account);
				subscriber.onNext(result);
			}
		});
	}

	private Subscriber<Boolean> starSub(){
		return new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				if(aBoolean){
					loadData();
				}
			}
		};
	}


	@Override
	public void onItemClicked(View view, Object item, int position) {
		int id = view.getId();
		Account account = (Account) item;
		if(id == RecyclerListAdapter.ViewHolder.ID_LienarLayout){
			//Launch detail activity
			Bundle bundle = new Bundle();
			bundle.putSerializable("account", account);

			Intent intent = new Intent(mActivity, AccountDetailActivity.class);
			intent.putExtras(bundle);
			mActivity.startActivity(intent);
		}else if(id == RecyclerListAdapter.ViewHolder.ID_ImageView){
			//Star
			starObj(account).subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe(starSub());
		}
	}
}
