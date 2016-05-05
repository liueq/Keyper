package com.liueq.testdagger.ui.launch;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.utils.GoldenHammer;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 3/5/2016.
 * Fingerprint dialog
 */
public class FingerprintDialog extends DialogFragment {

	@Bind(R.id.tv_title)
	TextView mTextViewTitle;
	@Bind(R.id.iv_finger)
	ImageView mImageViewFinger;

	private SplashActivity mActivity;
	private SplashActivityPresenter mPresenter;

	public static FingerprintDialog newInstance() {

		Bundle args = new Bundle();

		FingerprintDialog fragment = new FingerprintDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = (SplashActivity) getActivity();
		mPresenter = (SplashActivityPresenter) mActivity.getPresenter();
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welcome_dialog_fingerprint, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if (GoldenHammer.isAboveLollipop()) {
			mTextViewTitle.setVisibility(View.VISIBLE);
		} else {
			getDialog().setTitle(R.string.welcome_dialog_fingerprint_title);
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		mPresenter.cancelAuthAction();
	}

	@Override
	public void onPause() {
		super.onPause();
		mPresenter.cancelAuthAction();
	}

	@Override
	public void onResume() {
		super.onResume();
		mPresenter.authenticateAction();
	}
}
