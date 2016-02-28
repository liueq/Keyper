package com.liueq.testdagger.ui.accountdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liueq on 18/2/2016.
 * 详情页面的Fragment
 */
public class AccountDetailFragment extends Fragment implements HorizontalTagAdapter.OnItemClickListener {

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
	@Bind(R.id.iv_add)
	ImageView mImageViewAdd;

	@Bind(R.id.linear)
	LinearLayout mLinearLayout;
	@Bind(R.id.recycler_tag)
	RecyclerView mRecyclerTag;

	AccountDetailActivity mActivity;
	AccountDetailActivityPresenter mPresenter;

	HorizontalTagAdapter mHorizontalTagAdapter;


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
		mPresenter.attachFragment(this.getClass(), this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_account_detail, null);
		ButterKnife.bind(this, view);

		initView();
		loadData();
		return view;
	}

	/**
	 * Init the UI
	 */
	private void initView(){
		mRecyclerTag.setHasFixedSize(true);
		mRecyclerTag.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
		mRecyclerTag.setAdapter(mHorizontalTagAdapter = new HorizontalTagAdapter(mActivity, new ArrayList<Tag>(), this));
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		mPresenter.detachFragment(this.getClass());
		super.onDetach();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 从Bundle 中获取ID，Presenter 在DB中查找数据
	 */
	private void loadData(){
		mPresenter.loadDataFromDB(this);
	}

	public void updateUI(Account account){
        mEditTextSite.setText(account.site);
        mEditTextName.setText(account.username);
        mEditTextPwd.setText(account.password);
        mEditTextMail.setText(account.mail);
        mEditTextDesc.setText(account.description);

		mHorizontalTagAdapter.replaceAll(account.tag_list);
		mHorizontalTagAdapter.notifyDataSetChanged();
    }

	@OnClick({R.id.tv_commit, R.id.iv_add})
    public void onClick(View view){
		int id = view.getId();
		if(id == R.id.tv_commit) {
			saveData();
		}else if(id == R.id.iv_add){
			//Before show tag, need save change to memory
			syncData();
			//Show tag dialog
			FragmentManager manager = mActivity.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(ChooseTagDialog.newInstance(), null);
			transaction.commit();
		}
    }

	/**
	 * Save UI change to mCurrentAccount
	 * @return
	 */
	public Account syncData(){
		Account account = mPresenter.getCurrentAccount();
		if (account == null) {
			account = new Account();
		}

		account.site = mEditTextSite.getText().toString();
		account.username = mEditTextName.getText().toString();
		account.password = mEditTextPwd.getText().toString();
		account.mail = mEditTextMail.getText().toString();
		account.description = mEditTextDesc.getText().toString();

		return account;
	}

	/**
	 * Call presenter save to db
	 */
	public void saveData(){
		Account account = syncData();

		if (BuildConfig.DEBUG) {
			Log.d(TAG, "saveDataToDB account is " + account.toString());
		}

		mPresenter.saveDataToDB(account);
	}

	public void showResult(boolean result){
		if(result){
            Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.status_saved, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else{
            Snackbar snackbar = Snackbar.make(mLinearLayout, R.string.warning_site_not_null, Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
	}

	@Override
	public void onItemClicked(View view, Object item, int position) {
		int id = view.getId();
		if(id == HorizontalTagAdapter.ViewHolder.ID_LinearLayout){
			//TODO Open Tag detail
		}else if(id == HorizontalTagAdapter.ViewHolder.ID_ImageViewDel){
			//TODO Del tag
		}
	}
}
