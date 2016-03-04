package com.liueq.testdagger.ui.tagdetail;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.BaseActivity;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.ui.main.ListFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Show all account of this tag
 */
public class TagDetailActivity extends BaseActivity implements DialogInterface.OnClickListener{

	@Bind(R.id.toolbar)
	Toolbar toolbar;

	@Inject
	TagDetailActivityPresenter mPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_detail);
		ButterKnife.bind(this);

		getBundle();
		initView();
	}

	public static void launchActivity(Activity activity, Tag tag){
		if(tag == null || TextUtils.isEmpty(tag.id)){
			return;
		}

		Intent intent = new Intent(activity, TagDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("tag", tag);
		intent.putExtras(bundle);

		activity.startActivity(intent);
	}

	private void getBundle(){
		Tag tag = (Tag) getIntent().getExtras().getSerializable("tag");
		mPresenter.setTag(tag);
	}

	private void initView(){
		setSupportActionBar(toolbar);
		getSupportActionBar().setTitle(mPresenter.getTag().tag_name);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
			toolbar.setElevation(6);
		}

		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.fragment, TagDetailFragment.newInstance());
		fragmentTransaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_tag_detail, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == android.R.id.home){
			onBackPressed();
		}else if(id == R.id.action_delete){
			//Show Dialog
			showDialog().show();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Create a Yes or No Dialog
	 * @return
	 */
	private Dialog showDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.dialog_title_del_tag);
		builder.setMessage(R.string.dialog_content_del_tag);
		builder.setPositiveButton(R.string.dialog_yes_del_tag, this);
		builder.setNegativeButton(R.string.dialog_no_del_tag, this);
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == Dialog.BUTTON_POSITIVE){
			mPresenter.deleteTagAction();
		}else{
			dialog.dismiss();
		}
	}

	@Override
	protected void setupActivityComponent() {
		TestApplication.getApplication().getAppComponent().plus(new TagDetailModule(this)).inject(this);
	}

	protected Presenter getmPresenter() {
		return mPresenter;
	}

}
