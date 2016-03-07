package com.liueq.testdagger.ui.accountdetail;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
public class AccountDetailFragment extends Fragment implements HorizontalTagAdapter.OnItemClickListener, DialogInterface.OnClickListener{

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
	@Bind(R.id.tv_tag_hint)
	TextView mTextViewTagHint;

	@Bind(R.id.iv_jump)
	ImageView mImageViewJump;
	@Bind(R.id.iv_copy_site)
	ImageView mImageViewCopySite;
	@Bind(R.id.iv_copy_name)
	ImageView mImageViewCopyName;
	@Bind(R.id.iv_copy_password)
	ImageView mImageViewCopyPassword;
	@Bind(R.id.iv_copy_mail)
	ImageView mImageViewCopyMail;
	@Bind(R.id.iv_copy_description)
	ImageView mImageViewCopyDescription;

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

		mEditTextSite.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim().toLowerCase();

				if(TextUtils.isEmpty(text)){
					mImageViewCopySite.setVisibility(View.GONE);
					return;
				}else{
					mImageViewCopySite.setVisibility(View.VISIBLE);
				}

				if(text.startsWith("http://") || text.startsWith("https://") || text.startsWith("www.")){
					mImageViewJump.setVisibility(View.VISIBLE);
				}else{
					mImageViewJump.setVisibility(View.GONE);
				}

			}
		});
		mEditTextName.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim();

				if(TextUtils.isEmpty(text)){
					mImageViewCopyName.setVisibility(View.GONE);
					return;
				}else{
					mImageViewCopyName.setVisibility(View.VISIBLE);
				}

			}

		});
		mEditTextPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim();

				if(TextUtils.isEmpty(text)){
					mImageViewCopyPassword.setVisibility(View.GONE);
					return;
				}else{
					mImageViewCopyPassword.setVisibility(View.VISIBLE);
				}

			}
		});
		mEditTextMail.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim();

				if(TextUtils.isEmpty(text)){
					mImageViewCopyMail.setVisibility(View.GONE);
					return;
				}else{
					mImageViewCopyMail.setVisibility(View.VISIBLE);
				}

			}
		});
		mEditTextDesc.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String text = s.toString().trim();

				if(TextUtils.isEmpty(text)){
					mImageViewCopyDescription.setVisibility(View.GONE);
					return;
				}else{
					mImageViewCopyDescription.setVisibility(View.VISIBLE);
				}

			}
		});
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
		//Toolbar
		mActivity.getSupportActionBar().setTitle(account.site);

		//Detail
        mEditTextSite.setText(account.site);
        mEditTextName.setText(account.username);
        mEditTextPwd.setText(account.password);
        mEditTextMail.setText(account.mail);
        mEditTextDesc.setText(account.description);

		//Tag adapter
		if(account.tag_list == null || account.tag_list.size() == 0){
			mTextViewTagHint.setVisibility(View.VISIBLE);
			mRecyclerTag.setVisibility(View.INVISIBLE);
		}else{
			mTextViewTagHint.setVisibility(View.GONE);
			mRecyclerTag.setVisibility(View.VISIBLE);
		}
		mHorizontalTagAdapter.replaceAll(account.tag_list);
		mHorizontalTagAdapter.notifyDataSetChanged();
    }

	@OnClick({R.id.tv_delete, R.id.iv_add, R.id.iv_jump, R.id.iv_copy_site, R.id.iv_copy_name, R.id.iv_copy_password, R.id.iv_copy_mail, R.id.iv_copy_description})
    public void onClick(View view){
		int id = view.getId();
		if(id == R.id.tv_delete) {
			showDialog().show();
		}else if(id == R.id.iv_add){
			//Before show tag, need save change to memory
			syncData();
			//Show tag dialog
			FragmentManager manager = mActivity.getSupportFragmentManager();
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.add(ChooseTagDialog.newInstance(), null);
			transaction.commit();
		} else if (id == R.id.iv_jump) {
			//Open URL
			String url = mEditTextSite.getText().toString().trim();
			if(!url.startsWith("http://") || url.startsWith("https://")){
				url = "http://" + url;
			}
			try{
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
			}catch(ActivityNotFoundException e){
				e.printStackTrace();
				Log.e(TAG, "Error!! Jump activity no found" );
			}

		} else if (id == R.id.iv_copy_site){
			//Copy
			String text = mEditTextSite.getText().toString().trim();
			mPresenter.clipAction(text);
		} else if (id == R.id.iv_copy_name){
			String text = mEditTextName.getText().toString().trim();
			mPresenter.clipAction(text);

		} else if (id == R.id.iv_copy_password){
			String text = mEditTextPwd.getText().toString().trim();
			mPresenter.clipAction(text);

		} else if (id == R.id.iv_copy_mail){
			String text = mEditTextMail.getText().toString().trim();
			mPresenter.clipAction(text);

		} else if (id == R.id.iv_copy_description){
			String text = mEditTextDesc.getText().toString().trim();
			mPresenter.clipAction(text);
		}
	}

	/**
	 * Create a Yes or No Dialog
	 * @return
	 */
	private Dialog showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(R.string.dialog_title_delete);
		builder.setMessage(R.string.dialog_content_delete);
		builder.setPositiveButton(R.string.dialog_yes_delete, this);
		builder.setNegativeButton(R.string.dialog_no_delete, this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == Dialog.BUTTON_POSITIVE){
			mPresenter.deleteAccountAction();
		}else{
			dialog.dismiss();
		}
	}

	/**
	 * Save UI change to mCurrentAccount
	 * @return
	 */
	public Account syncData(){
		Account account = mPresenter.getCurrentAccount();
		if (account == null) {
			return null;
		}

		account.site = mEditTextSite.getText().toString();
		account.username = mEditTextName.getText().toString();
		account.password = mEditTextPwd.getText().toString();
		account.mail = mEditTextMail.getText().toString();
		account.description = mEditTextDesc.getText().toString();

		return account;
	}

	/**
	 * Call mPresenter save to db
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
			//TODO Open Tag detail, not sure is this action needed?
		}else if(id == HorizontalTagAdapter.ViewHolder.ID_ImageViewDel){
			//Delete tag
			if(item instanceof Tag){
				mPresenter.removeTagAction((Tag) item);
			}
		}
	}
}
