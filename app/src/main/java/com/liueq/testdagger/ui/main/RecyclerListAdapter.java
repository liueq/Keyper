package com.liueq.testdagger.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.ui.common.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 13/7/15.
 * 普通的ListView
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private Context mContext;
    private List<Account> mList = new ArrayList<Account>();
    private OnItemClickListener mListener;
    private boolean mShowStar = true;

    public RecyclerListAdapter(Context context, OnItemClickListener listener, boolean showStar){
        this.mContext = context;
        this.mListener = listener;
        this.mShowStar = showStar;
    }

    public RecyclerListAdapter(Context context, OnItemClickListener listener){
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Account account = mList.get(position);
        holder.mTextView.setText(account.site);
        if(account.is_stared){
            starImage(holder);
        }else {
            unStarImage(holder);
        }

        if(mShowStar){
            holder.mImageView.setVisibility(View.VISIBLE);
        }else{
            holder.mImageView.setVisibility(View.GONE);
        }

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.mLinearLayout, account, position);
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO star or unstar
                if(account.is_stared){
                    //unstar
                    unStarImage(holder);
                }else{
                    //star
                    starImage(holder);
                }

                mListener.onItemClick(holder.mImageView, account, position);
            }
        });
    }

    private void starImage(ViewHolder holder){
        Drawable drawable = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable = mContext.getDrawable(R.mipmap.ic_star_black_24dp);
            drawable.setTint(mContext.getResources().getColor(R.color.colorAccent));
        }else{
        	drawable = mContext.getResources().getDrawable(R.mipmap.ic_star_grey600_24dp);
        }
        holder.mImageView.setImageDrawable(drawable);
    }

    private void unStarImage(ViewHolder holder){
        Drawable drawable = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            drawable = mContext.getDrawable(R.mipmap.ic_star_outline_black_24dp);
            drawable.setTint(mContext.getResources().getColor(R.color.grey));
        }else{
        	drawable = mContext.getResources().getDrawable(R.mipmap.ic_star_outline_grey600_24dp);
        }
        holder.mImageView.setImageDrawable(drawable);
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

    public void replaceAll(List<Account> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ll_container)
        LinearLayout mLinearLayout;
        @Bind(R.id.tv_item)
        TextView mTextView;
        @Bind(R.id.iv_star)
        ImageView mImageView;

        public static final int ID_LienarLayout = R.id.ll_container;
        public static final int ID_TextView = R.id.tv_item;
        public static final int ID_ImageView = R.id.iv_star;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
