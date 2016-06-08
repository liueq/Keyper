package com.liueq.keyper.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 26/2/2016.
 * Command Recycler Adapter, just show text
 */
public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleRecyclerAdapter.ViewHolder>{

	private List<String> mList = new ArrayList<String>();
	private Context mContext;
	private OnItemClickListener mListener;

	public SimpleRecyclerAdapter(Context context, OnItemClickListener listener) {
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
		final String str = mList.get(position);

		holder.mTextView.setText(str);
		holder.mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onItemClick(v, str, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public void replaceAll(List<String > list){
		mList.clear();
		mList.addAll(list);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder{

		@Bind(android.R.id.text1)
		public TextView mTextView;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public interface OnItemClickListener{
		void onItemClick(View view, Object obj, int position);
	}
}
