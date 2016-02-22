package com.liueq.testdagger.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 22/2/2016.
 * Star ListView
 */
public class RecyclerStarListAdapter extends RecyclerView.Adapter<RecyclerStarListAdapter.ViewHolder>{

	private Context mContext;
	private List<Account> mList = new ArrayList<Account>();
	private OnItemClickListener mListener;

	public RecyclerStarListAdapter(Context mContext, List<Account> mList, OnItemClickListener mListener) {
		this.mContext = mContext;
		this.mList = mList;
		this.mListener = mListener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext).inflate(R.layout.star_list_item, parent, false);
		ViewHolder viewHolder = new ViewHolder(view);

		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		final Account account = mList.get(position);
		holder.mTextViewSite.setText(account.site);
		holder.mTextViewName.setText(account.username);
		holder.mTextViewPassword.setText(account.password);

		holder.mCardView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onItemClicked(v, account, position);
			}
		});

	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public void clear(){
		mList.clear();
	}

	public void addAll(List<Account> list){
		mList.addAll(list);
	}

	public static class ViewHolder extends RecyclerView.ViewHolder{

		@Bind(R.id.cardview)
		CardView mCardView;
		@Bind(R.id.tv_site)
		TextView mTextViewSite;
		@Bind(R.id.tv_name)
		TextView mTextViewName;
		@Bind(R.id.tv_password)
		TextView mTextViewPassword;

		public static int ID_LinearLayout = R.id.cardview;
		public static int ID_TextViewSite = R.id.tv_site;
		public static int ID_TextViewName = R.id.tv_name;
		public static int ID_TextViewPassword = R.id.tv_password;

		public ViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}

	public interface OnItemClickListener{

		void onItemClicked(View view, Object item, int position);
	}
}
