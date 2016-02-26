package com.liueq.testdagger.ui.accountdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.common.SimpleRecyclerAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 26/2/2016.
 * Choose tag
 */
public class ChooseTagDialog extends DialogFragment implements SimpleRecyclerAdapter.OnItemClickListener {

	@Bind(R.id.et_tag)
	EditText mEditTextTag;
	@Bind(R.id.iv_add_tag)
	ImageView mImageViewTag;
	@Bind(R.id.recycler_tag)
	RecyclerView mRecyclerTag;

	SimpleRecyclerAdapter mRecyclerAdapter;

	public static ChooseTagDialog newInstance() {

		Bundle args = new Bundle();

		ChooseTagDialog fragment = new ChooseTagDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_choose_tag, container, false);
		ButterKnife.bind(this, view);

		getDialog().setTitle(R.string.choose_tag_dialog_title);
		initView();
		return view;
	}

	private void initView(){
		mEditTextTag.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		mImageViewTag.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		//Init recycler
		mRecyclerTag.setHasFixedSize(false);
		mRecyclerTag.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerTag.setAdapter(mRecyclerAdapter = new SimpleRecyclerAdapter(getActivity(), this));
	}

	@Override
	public void onItemClick(View view, Object obj, int position) {

	}
}
