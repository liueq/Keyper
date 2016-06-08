package com.liueq.keyper.ui.main;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liueq.keyper.R;
import com.liueq.keyper.ui.accountdetail.AccountDetailActivity;
import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.ui.advancesearch.AdvanceSearchActivity;
import com.liueq.keyper.ui.common.OnItemClickListener;
import com.liueq.keyper.utils.GoldenHammer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 27/10/2015.
 * Search Dialog Fragment
 */
public class SearchDialogFragment extends AppCompatDialogFragment implements OnItemClickListener {

	public final static String TAG = "SearchDialogFragment";

	@Bind(R.id.rl_searchbox)
	RelativeLayout mSearchBox;
	@Bind(R.id.iv_back)
	ImageView mIvBack;
	@Bind(R.id.iv_clear)
	ImageView mIvClear;
	@Bind(R.id.et_search)
	EditText mEtSearch;
	@Bind(R.id.rl_root)
	RelativeLayout mRlRoot;
	@Bind(R.id.recycler)
	RecyclerView mRecycler;

	private boolean is_exiting = false;

	private MainActivity mActivity;
	private MainActivityPresenter mPresneter;
	private RecyclerListAdapter recyclerListAdapter;


	public static SearchDialogFragment newInstance(Context context){
		SearchDialogFragment dialogFragment = new SearchDialogFragment();
		dialogFragment.setStyle(STYLE_NO_TITLE, R.style.DialogFragmentTheme);

		return dialogFragment;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		is_exiting = false;

		mActivity = (MainActivity) getActivity();
		mPresneter = (MainActivityPresenter) mActivity.getPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_search_dialog, container);
		ButterKnife.bind(this, v);

		initView();
		return v;
	}

	private void initView(){
		mRecycler.setHasFixedSize(false);
		mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
		recyclerListAdapter = new RecyclerListAdapter(getActivity(), this, false);
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

	private void searchData(String key){
		searchListOb(key).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(searchListSub());
	}

	private void updateUI(List<Account> list){
		recyclerListAdapter.clear();
		recyclerListAdapter.addAll(list);
		recyclerListAdapter.notifyDataSetChanged();
	}

	/***** Observable and Subscriber*****/

	private Observable<List<Account>> searchListOb(final String key){
		return Observable.create(new Observable.OnSubscribe<List<Account>>() {
			@Override
			public void call(Subscriber<? super List<Account>> subscriber) {
				List<Account> list = mPresneter.search(key);
				subscriber.onNext(list);
			}
		});
	}

	private Subscriber<List<Account>> searchListSub(){
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
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mRlRoot.post(new Runnable() {
			@Override
			public void run() {
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
					searchBoxInAnim();
				}else{
					searchBoxInNoAnim();
				}
			}
		});

		mEtSearch.setHint(R.string.hint_quick_search);
		mEtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void afterTextChanged(Editable editable) {
				if (mEtSearch.getText().toString().trim().length() > 0) {
					mIvClear.setVisibility(View.VISIBLE);

					searchData(mEtSearch.getText().toString().trim());
				} else {
					updateUI(new ArrayList<Account>()); //Clear
					mIvClear.setVisibility(View.GONE);
				}
			}
		});

		mEtSearch.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				boolean isValidKey = event != null && keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN;
				if(isValidKey){
					AdvanceSearchActivity.launchActivity(mActivity, mEtSearch.getText().toString().trim());
				}
				return false;
			}
		});
	}

	@Override
	public void setupDialog(Dialog dialog, int style) {
		super.setupDialog(dialog, style);

		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
				if (i == KeyEvent.KEYCODE_BACK) {
					if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
						return true;
					} else if (keyEvent.getAction() == KeyEvent.ACTION_UP && !is_exiting) {
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
							searchBoxOutAnim();
						}else{
							searchBoxOutNoAnim();
						}
						return true;
					}
				}
				return false;
			}
		});
	}

	private void searchBoxInNoAnim(){
		mEtSearch.requestFocus();
		GoldenHammer.showInputMethod(getActivity(), mEtSearch);
	}


	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private void searchBoxInAnim(){
		int cx = mSearchBox.getRight();
		int cy = (mSearchBox.getTop() + mSearchBox.getBottom())/2;
		int finalRadius = Math.max(mSearchBox.getWidth(), mSearchBox.getHeight());

		Animator anim = ViewAnimationUtils.createCircularReveal(mSearchBox, cx, cy, 0, finalRadius);
		mSearchBox.setVisibility(View.VISIBLE);
		anim.setDuration(200);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {

			}

			@Override
			public void onAnimationEnd(Animator animator) {
				mEtSearch.requestFocus();
				GoldenHammer.showInputMethod(getActivity(), mEtSearch);
			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		anim.start();
	}

	public void searchBoxOutNoAnim(){
		GoldenHammer.hideInputMethod(getActivity(), mEtSearch);
		mSearchBox.setVisibility(View.GONE);
		FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(SearchDialogFragment.this);
		fragmentTransaction.commit();
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void searchBoxOutAnim(){
		is_exiting = true;
		int cx = mSearchBox.getRight();
		int cy = (mSearchBox.getBottom() + mSearchBox.getTop())/2;
		int finalRadius = Math.max(mSearchBox.getWidth(), mSearchBox.getHeight());

		Animator anim = ViewAnimationUtils.createCircularReveal(mSearchBox, cx, cy, finalRadius, 0);
		anim.setDuration(200);
		anim.setInterpolator(new AccelerateDecelerateInterpolator());
		anim.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animator) {
				mRecycler.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animator) {
				GoldenHammer.hideInputMethod(getActivity(), mEtSearch);
				mSearchBox.setVisibility(View.GONE);
				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.remove(SearchDialogFragment.this);
				fragmentTransaction.commit();
			}

			@Override
			public void onAnimationCancel(Animator animator) {

			}

			@Override
			public void onAnimationRepeat(Animator animator) {

			}
		});
		anim.start();
	}

	@OnClick({R.id.rl_root, R.id.iv_back, R.id.iv_clear})
	public void onClick(View v){
		int id = v.getId();
		if((id == R.id.rl_root || id == R.id.iv_back) &&  !is_exiting) {
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
				searchBoxOutAnim();
			}else{
				searchBoxOutNoAnim();
			}
		}else if(id == R.id.iv_clear){
			mEtSearch.setText("");
		}
	}

	@Override
	public void onItemClick(View view, Object item, int position) {
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
			mPresneter.starOrUnStar(account);
		}
	}

//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if(requestCode == REQUEST_CODE){
//			searchBoxOutAnim();
//		}
//	}

}
