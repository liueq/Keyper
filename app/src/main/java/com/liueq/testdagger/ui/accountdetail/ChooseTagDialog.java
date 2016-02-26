package com.liueq.testdagger.ui.accountdetail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 26/2/2016.
 * Choose tag
 */
public class ChooseTagDialog extends DialogFragment implements OnItemClickListener {

	@Bind(R.id.et_tag)
	EditText mEditTextTag;
	@Bind(R.id.tv_add_tag)
	TextView mTextViewAdd;
	@Bind(R.id.recycler_tag)
	RecyclerView mRecyclerTag;

	TagRecyclerAdapter mRecyclerAdapter;

	AccountDetailActivity mActivity;
	AccountDetailActivityPresenter mPresenter;

	public static ChooseTagDialog newInstance() {

		Bundle args = new Bundle();

		ChooseTagDialog fragment = new ChooseTagDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mActivity = (AccountDetailActivity) getActivity();
		mPresenter = (AccountDetailActivityPresenter) mActivity.getPresenter();
		mPresenter.attachFragment(ChooseTagDialog.class, this);
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

	@Override
	public void onDetach() {
		mPresenter.detachFragment(ChooseTagDialog.class);
		super.onDetach();
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
				String search = s.toString().trim();
				if(TextUtils.isEmpty(search)){
					mRecyclerAdapter.replaceAll(new ArrayList<Tag>());
				}else{
					//Search
					mPresenter.searchAvailableTag(search);
				}

			}
		});

		mTextViewAdd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Create a new tag
				String tag_name = mEditTextTag.getText().toString().trim();
				Tag tag = new Tag(tag_name, "1");

				mPresenter.createNewTag(tag);
			}
		});

		//Init recycler
		mRecyclerTag.setHasFixedSize(false);
		mRecyclerTag.setLayoutManager(new LinearLayoutManager(getActivity()));
		mRecyclerTag.setAdapter(mRecyclerAdapter = new TagRecyclerAdapter(getActivity(), this));
	}

	public void updateUI(List<Tag> list){
		mRecyclerAdapter.replaceAll(list);

		/** When the list size is 0, show create tag button**/
		if(list.size() == 0){
			//Show create new tag button
			mTextViewAdd.setVisibility(View.VISIBLE);
		}else{
			mTextViewAdd.setVisibility(View.GONE);
		}
	}

	@Override
	public void onItemClick(View view, Object obj, int position) {

	}

	public class TagRecyclerAdapter extends RecyclerView.Adapter<TagRecyclerAdapter.ViewHolder>{

		private List<Tag> mList = new ArrayList<Tag>();
		private Context mContext;
		private OnItemClickListener mListener;

		public TagRecyclerAdapter(Context context, OnItemClickListener listener) {
			mContext = context;
			mListener = listener;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);

			return viewHolder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, final int position) {
			final Tag tag = mList.get(position);

			holder.mTextView.setText(tag.tag_name);
			holder.mTextView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mListener.onItemClick(v, tag, position);
				}
			});
		}

		@Override
		public int getItemCount() {
			return mList.size();
		}

		public void replaceAll(List<Tag> list){
			mList.clear();
			mList.addAll(list);
		}

		public class ViewHolder extends RecyclerView.ViewHolder{

			@Bind(android.R.id.text1)
			public TextView mTextView;

			public ViewHolder(View itemView) {
				super(itemView);
				ButterKnife.bind(this, itemView);
			}
		}

	}
}
