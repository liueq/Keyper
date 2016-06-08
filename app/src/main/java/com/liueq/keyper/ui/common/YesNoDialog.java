package com.liueq.keyper.ui.common;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liueq.keyper.utils.GoldenHammer;

/**
 * Created by liueq on 1/3/2016.
 * Common Yes or No dialog
 * 简单的Dialog 有必要自定义？？ 暂时中断
 */
public class YesNoDialog extends DialogFragment{

	private String mTitle;
	private String mContent;
	private String mYes;
	private String mNo;

	public static YesNoDialog newInstance(@Nullable int title_id, @NonNull int content_id, @Nullable int yes_id, @Nullable int no_id){
		String title = GoldenHammer.getString(title_id);
		String content = GoldenHammer.getString(content_id);
		String yes = GoldenHammer.getString(yes_id);
		String no = GoldenHammer.getString(no_id);

		return newInstance(title, content, yes, no);
	}

	public static YesNoDialog newInstance(@Nullable String title, @NonNull String content, @Nullable String yes, @Nullable String no) {
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("content", content);
		args.putString("yes", yes);
		args.putString("no", no);

		YesNoDialog fragment = new YesNoDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		mTitle = bundle.getString("title");
		mContent = bundle.getString("content");
		mYes = bundle.getString("yes");
		mNo = bundle.getString("no");

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
}
