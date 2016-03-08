package com.liueq.testdagger.ui.advancesearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 7/3/2016.
 * Recycler adapter for search field
 */
public class RecyclerFieldAdapter extends RecyclerView.Adapter<RecyclerFieldAdapter.ViewHolder>{

	private List<SearchField> mList = new ArrayList<SearchField>();
	private Context mContext;
	private OnItemClickListener mListener;

	public RecyclerFieldAdapter(Context context, OnItemClickListener listener) {
		mContext = context;
		mListener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.item_search_field, parent, false);
		ViewHolder holder = new ViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		final SearchField searchField = mList.get(position);
		if(searchField.mSelected){
			holder.mImageView.setVisibility(View.VISIBLE);
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
		}else{
			holder.mImageView.setVisibility(View.INVISIBLE);
			holder.mTextView.setTextColor(mContext.getResources().getColor(R.color.secondary_text));
		}

		holder.mTextView.setText(searchField.mField);

		holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onItemClick(v, searchField, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public void replaceAll(List<SearchField> list){
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	/**
	 * Keep radio
	 * @param searchField
	 */
	public void reverseOther(SearchField searchField){
		for(SearchField sf : mList){
			if(!sf.mField.equals(searchField.mField)){
				sf.mSelected = false;
			}
		}

		notifyDataSetChanged();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder{

		@Bind(R.id.tv_field)
		TextView mTextView;
		@Bind(R.id.iv_indicator)
		ImageView mImageView;
		@Bind(R.id.rl_container)
		RelativeLayout mRelativeLayout;

		public final static int ID_RelativeLayout = R.id.rl_container;
		public final static int ID_TextView = R.id.tv_field;
		public final static int ID_ImageView = R.id.iv_indicator;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public static class SearchField{

		public String mField;
		public boolean mSelected;

		public SearchField(){}

		public SearchField(String field, boolean selected){
			mField = field;
			mSelected = selected;
		}
	}
}
