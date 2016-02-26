package com.liueq.testdagger.ui.main;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Account;

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

    public RecyclerListAdapter(Context context, List<Account> list, OnItemClickListener listener){
        this.mContext = context;
        this.mList.addAll(list);
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

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(holder.mLinearLayout, account, position);
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

                mListener.onItemClicked(holder.mImageView, account, position);
            }
        });
    }

    private void starImage(ViewHolder holder){
        Drawable drawable = mContext.getDrawable(R.mipmap.ic_star_black_24dp);
        drawable.setTint(mContext.getResources().getColor(R.color.yellow));
        holder.mImageView.setImageDrawable(drawable);
    }

    private void unStarImage(ViewHolder holder){
        Drawable drawable = mContext.getDrawable(R.mipmap.ic_star_outline_black_24dp);
        drawable.setTint(mContext.getResources().getColor(R.color.grey));
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

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ll_container)
        LinearLayout mLinearLayout;
        @Bind(R.id.tv_item)
        TextView mTextView;
        @Bind(R.id.iv_star)
        ImageView mImageView;

        public static int ID_LienarLayout = R.id.ll_container;
        public static int ID_TextView = R.id.tv_item;
        public static int ID_ImageView = R.id.iv_star;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener{
        void onItemClicked(View view, Object item, int position);
    }
}
