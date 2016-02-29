package com.liueq.testdagger.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 29/2/2016.
 * Adapter for tag list
 */
public class RecyclerTagListAdapter extends RecyclerView.Adapter<RecyclerTagListAdapter.ViewHolder>{

	private Context mContext;
	private List<TagItem> mList = new ArrayList<TagItem>();
	private OnItemClickListener mListener;

	public RecyclerTagListAdapter(Context context, OnItemClickListener listener) {
		mContext = context;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_recycler, parent, false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		TagItem tagItem = mList.get(position);
		holder.mTextViewLabel.setText(tagItem.tag_label);

		holder.mGridView.setOnItemClickListener(mListener);
		holder.mGridView.replaceAll(tagItem.tag_list);
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public void replaceAll(List<TagItem> list){
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder{

		@Bind(R.id.tv_label)
		TextView mTextViewLabel;
		@Bind(R.id.grid_view)
		NonAlignGridView mGridView;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public static class TagItem{
		public String tag_label;
		public List<Tag> tag_list;

		public TagItem() {
		}

		public TagItem(String tag_label, List<Tag> tag_list) {
			this.tag_label = tag_label;
			this.tag_list = tag_list;
		}

	}
}
