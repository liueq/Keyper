package com.liueq.testdagger.ui.accountdetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 25/2/2016.
 * Horizontal tag adapter in AccountDetail
 */
public class HorizontalTagAdapter extends RecyclerView.Adapter<HorizontalTagAdapter.ViewHolder>{

	private Context mContext;
	private List<Tag> mList = new ArrayList<Tag>();
	private OnItemClickListener mListener;

	public HorizontalTagAdapter(Context mContext, List<Tag> mList, OnItemClickListener mListener) {
		this.mContext = mContext;
		this.mList = mList;
		this.mListener = mListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_horizontal_tag, parent, false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		final Tag tag = mList.get(position);

		holder.mTextViewTag.setText(tag.tag_name);

		/** Set listener **/

		holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onItemClicked(v, tag, position);
			}
		});

		holder.mImageViewDel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onItemClicked(v, tag, position);
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

	public static class ViewHolder extends RecyclerView.ViewHolder{


		@Bind(R.id.ll_container)
		LinearLayout mLinearLayout;
		@Bind(R.id.tv_tag)
		TextView mTextViewTag;
		@Bind(R.id.iv_del)
		ImageView mImageViewDel;

		public final static int ID_LinearLayout = R.id.ll_container;
		public final static int ID_TextViewTag = R.id.tv_tag;
		public final static int ID_ImageViewDel = R.id.iv_del;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public interface OnItemClickListener{
		void onItemClicked(View view, Object item, int position);
	}
}
