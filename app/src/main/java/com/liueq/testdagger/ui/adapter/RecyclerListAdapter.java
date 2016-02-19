package com.liueq.testdagger.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.activity.AccountDetailActivity;
import com.liueq.testdagger.data.model.Account;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by liueq on 13/7/15.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ViewHolder> {

    private Context mContext;
    private List<Account> mList = new ArrayList<Account>();
    private View.OnClickListener mListener;

    private static final int star_resource = R.mipmap.ic_star_black_24dp;
    private static final int unstar_resource = R.mipmap.ic_star_outline_black_24dp;
    private static final int star_tint_color = R.color.yellow;
    private static final int unstar_tint_color = R.color.grey;

    public RecyclerListAdapter(Context context, List<Account> list){
        this.mContext = context;
        this.mList.addAll(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Account account = mList.get(position);
        holder.mTextView.setText(account.site);
        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("account", account);

                Intent intent = new Intent(mContext, AccountDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO star or unstar

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ll_container)
        LinearLayout mLinearLayout;
        @Bind(R.id.tv_item)
        TextView mTextView;
        @Bind(R.id.iv_star)
        ImageView mImageView;

        public ViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void clear(){
        mList.clear();
    }

    public void addAll(List<Account> list){
        mList.addAll(list);
    }
}
