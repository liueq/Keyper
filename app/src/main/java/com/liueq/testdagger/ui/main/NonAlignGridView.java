package com.liueq.testdagger.ui.main;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liueq.testdagger.R;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.ui.common.OnItemClickListener;
import com.liueq.testdagger.utils.GoldenHammer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 2/12/2015.
 * 类似于GridView，但是Item并不是对齐的
 */
public class NonAlignGridView extends ViewGroup implements View.OnClickListener{

	int CHILD_MARGIN = GoldenHammer.pixelToDp(10, getContext());

	private OnItemClickListener mOnItemClickListener;

	private List<TextView> mTextViewList = new ArrayList<TextView>();

	public static final int ID_TextView = R.id.tv_grid_item;


	public NonAlignGridView(Context context) {
		super(context);
	}

	public NonAlignGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			measureChild(child, widthMeasureSpec, heightMeasureSpec);
		}

		//测量该View的高度，和onLayout中的算法相同，只是这里需要的是height
		int height = 0;
		if (count != 0) {
			Rect preChildRect = new Rect();
			preChildRect.set(0, CHILD_MARGIN, 0, 0);

			for (int i = 0; i < count; i++) {
				final View child = getChildAt(i);
				if (i == 0) {
					height += child.getMeasuredHeight() + CHILD_MARGIN;
				}

				int left, top, right, bottom;
				if (preChildRect.right + CHILD_MARGIN + child.getMeasuredWidth() + CHILD_MARGIN <= getMeasuredWidth()) {
					//当该行能够容纳新一个Child的情况
					left = preChildRect.right + CHILD_MARGIN;
					top = preChildRect.top;
					right = preChildRect.right + CHILD_MARGIN + child.getMeasuredWidth();
					bottom = preChildRect.top + child.getMeasuredHeight();

					preChildRect.set(left, top, right, bottom);
				} else {
					//换行的情况
					left = getLeft() + CHILD_MARGIN;
					top = preChildRect.bottom + CHILD_MARGIN;
					right = getLeft() + CHILD_MARGIN + child.getMeasuredWidth();
					bottom = preChildRect.bottom + CHILD_MARGIN + child.getMeasuredHeight();

					preChildRect.set(left, top, right, bottom);
					height += child.getMeasuredHeight() + CHILD_MARGIN;
				}
			}
			height += CHILD_MARGIN;
		}
		setMeasuredDimension(getMeasuredWidth(), height);
	}

	@Override
	protected void onLayout(boolean b, int l, int i1, int i2, int i3) {
		int count = getChildCount();
		Rect preChildRect = new Rect();
		preChildRect.set(0, CHILD_MARGIN, 0, 0);

		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);

			int left, top, right, bottom;
			if (preChildRect.right + CHILD_MARGIN + child.getMeasuredWidth() + CHILD_MARGIN <= getMeasuredWidth()) {
				//当该行能够容纳新一个Child的情况
				left = preChildRect.right + CHILD_MARGIN;
				top = preChildRect.top;
				right = preChildRect.right + CHILD_MARGIN + child.getMeasuredWidth();
				bottom = preChildRect.top + child.getMeasuredHeight();
				child.layout(left, top, right, bottom);

				preChildRect.set(left, top, right, bottom);
			} else {
				//换行的情况
				left = getLeft() + CHILD_MARGIN;
				top = preChildRect.bottom + CHILD_MARGIN;
				right = getLeft() + CHILD_MARGIN + child.getMeasuredWidth();
				bottom = preChildRect.bottom + CHILD_MARGIN + child.getMeasuredHeight();
				child.layout(left, top, right, bottom);

				preChildRect.set(left, top, right, bottom);
			}

		}
	}

	/**
	 * 获得所有的childView
	 * @return
	 */
	public List<View> getAllChild(){
		List<View> list = new ArrayList<View>();
		for(int i = 0; i < getChildCount(); i++){
			list.add(getChildAt(i));
		}

		return list;
	}

	/**
	 * Set listener
	 * @param listener
	 */
	public void setOnItemClickListener(OnItemClickListener listener){
		mOnItemClickListener = listener;
	}

	/**
	 * Same as adapter, notfiydatasetchange
	 * @param list
	 */
	public void replaceAll(List<Tag> list){
		int list_size = list.size();

		//First, remove TextView from parent
		for(TextView tv : mTextViewList){
			this.removeView(tv);
		}

		if(mTextViewList.size() < list_size){
			int surplus = list_size - mTextViewList.size();
			for(int i = 0; i < surplus; i++){
				mTextViewList.add(createTextView());
			}
		}else if(mTextViewList.size() > list_size){
			int surplus = mTextViewList.size() - list_size;
			for(int i = 0; i < surplus; i++){
				mTextViewList.remove(0);
			}
		}

		for(int i = 0; i < list_size; i++){
			TextView tv = mTextViewList.get(i);
			Tag tag = list.get(i);

			tv.setText(tag.tag_name);
			tv.setTag(tag);
			tv.setOnClickListener(this);
			this.addView(tv);
		}

		invalidate();
	}

	@Override
	public void onClick(View v) {
		mOnItemClickListener.onItemClick(v, v.getTag(), 0);
	}

	private TextView createTextView(){
		TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_grid_tag, this, false);
		return tv;
	}
}
