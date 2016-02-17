package com.liueq.testdagger.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.liueq.testdagger.R;
import com.liueq.testdagger.ui.fragment.ListFragment;

/**
 * Created by liueq on 17/2/2016.
 * 主界面的PagerAdapter
 */
public class MainPagerAdapter extends FragmentPagerAdapter{

	private final static int COUNT = 3;

	private static String [] TITLE_ARRAY = {"全部", "标星", "标签"};

	private Context mContext;

	public MainPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;

		TITLE_ARRAY[0] = context.getString(R.string.tab_all);
		TITLE_ARRAY[1] = context.getString(R.string.tab_star);
		TITLE_ARRAY[2] = context.getString(R.string.tab_tag);
	}

	@Override
	public Fragment getItem(int position) {
		return ListFragment.newInstance();
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE_ARRAY[position];
	}
}
