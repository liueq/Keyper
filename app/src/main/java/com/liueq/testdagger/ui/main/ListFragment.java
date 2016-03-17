package com.liueq.testdagger.ui.main;

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

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.accountdetail.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 17/2/2016.
 * 主界面，ALL tab
 */
public class ListFragment extends Fragment implements OnItemClickListener, MainPagerAdapter.BackToTop {

	@Bind(R.id.iv_hint)
	ImageView mImageViewHint;
	@Bind(R.id.tv_hint)
	TextView mTextViewHint;
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
		mPresneter.attachFragment(ListFragment.class, this);
	}

	@Override
	public void onDetach() {
		mPresneter.detachFragment(ListFragment.class);
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
        recyclerListAdapter = new RecyclerListAdapter(getActivity(), this);
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

		mTextViewHint.setText(R.string.tab_all_hint);
		mImageViewHint.setImageResource(R.mipmap.ic_account_balance_wallet_grey600_48dp);
	}

	private void loadData(){
		mPresneter.loadListAction();
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

		recyclerListAdapter.clear();
        recyclerListAdapter.addAll(list);
        recyclerListAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(View view, Object item, int position) {
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

	@Override
	public void backToTop() {
		mRecycler.smoothScrollToPosition(0);
	}
}
