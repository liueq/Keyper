package com.liueq.testdagger.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.R;
import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.activity.presenter.AccountDetailActivityPresenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 18/2/2016.
 * 详情页面的Fragment
 */
public class AccountDetailFragment extends Fragment{

	public final static String TAG = "Detail";

	@Bind(R.id.tv_site)
	TextView mTextViewSite;
	@Bind(R.id.et_site)
	EditText mEditTextSite;
	@Bind(R.id.et_name)
	EditText mEditTextName;
	@Bind(R.id.et_password)
	EditText mEditTextPwd;
	@Bind(R.id.et_mail)
	EditText mEditTextMail;
	@Bind(R.id.et_desc)
	EditText mEditTextDesc;

	@Bind(R.id.linear)
	LinearLayout mLinearLayout;

	AccountDetailActivity mActivity;
	AccountDetailActivityPresenter mPresenter;


	public static AccountDetailFragment newInstance() {

		Bundle args = new Bundle();

		AccountDetailFragment fragment = new AccountDetailFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (AccountDetailActivity) getActivity();
		mPresenter = (AccountDetailActivityPresenter) mActivity.getPresenter();
		initData();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_account_detail, null);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * TODO 从Bundle 中获取ID，Presenter 在DB中查找数据
	 */
	private void initData(){
		getDetailOb(mPresenter.mId)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(getDetailSub());
	}

	public void updateUI(Account account){
        mEditTextSite.setText(account.site);
        mEditTextName.setText(account.username);
        mEditTextPwd.setText(account.password);
        mEditTextMail.setText(account.mail);
        mEditTextDesc.setText(account.description);
    }

	@OnClick(R.id.tv_commit)
    public void saveData(){
        Account account = mPresenter.getCurrentAccount();
        if(account == null){
            account = new Account();
        }

        account.site = mEditTextSite.getText().toString();
        account.username = mEditTextName.getText().toString();
        account.password = mEditTextPwd.getText().toString();
        account.mail = mEditTextMail.getText().toString();
        account.description = mEditTextDesc.getText().toString();

        if(BuildConfig.DEBUG){
            Log.d(TAG, "saveData account is " + account.toString());
        }

		saveDetailOb(account)
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeOn(Schedulers.io())
				.subscribe(saveDetailSub());
    }

	private void showResult(boolean result){
		if(result){
            Snackbar snackbar = Snackbar.make(mLinearLayout, "SAVED", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar.make(mLinearLayout, "Site can't be NULL", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
	}

	private Observable<Account> getDetailOb(final String id){
		return Observable.create(new Observable.OnSubscribe<Account>() {
			@Override
			public void call(Subscriber<? super Account> subscriber) {
				Account account = mPresenter.loadData(id);
				subscriber.onNext(account);
			}
		});
	}

	private Subscriber<Account> getDetailSub(){
		return new Subscriber<Account>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Account account) {
				updateUI(account);
			}
		};
	}

	private Observable<Boolean> saveDetailOb(final Account account){
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				boolean result = mPresenter.saveData(account);
				subscriber.onNext(result);
			}
		});
	}

	private Subscriber<Boolean> saveDetailSub(){
		return new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				showResult(aBoolean);
			}
		};
	}
}
