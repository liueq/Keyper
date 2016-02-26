package com.liueq.testdagger.ui.common;

import android.view.View;

/**
 * Created by liueq on 26/2/2016.
 * ListView or RecyclerView, onItemClick
 */
public interface OnItemClickListener {
	/**
	 * Item click, include all child view onclick
	 * @param view
	 * @param item
	 * @param position
	 */
	void onItemClick(View view, Object item, int position);
}
