package com.liueq.keyper.ui.main;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.liueq.keyper.R;

/**
 * Created by liueq on 17/2/2016.
 * 主界面的PagerAdapter
 */
public class MainPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{

	private final static int COUNT = 3;

	private static String [] TITLE_ARRAY = {"全部", "标星", "标签"};

	private Context mContext;

	private int mCurrntPageNum = 0;

	private ListFragment mListFragment = null;
	private StarListFragment mStarListFragment = null;
	private TagListFragment mTagListFragment = null;


	public MainPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
		mContext = context;

		TITLE_ARRAY[0] = context.getString(R.string.tab_all);
		TITLE_ARRAY[1] = context.getString(R.string.tab_star);
		TITLE_ARRAY[2] = context.getString(R.string.tab_tag);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position){
			case 0:
				if(mListFragment == null){
					mListFragment = ListFragment.newInstance();
				}
				return mListFragment;
			case 1:
				if(mStarListFragment == null){
					mStarListFragment = StarListFragment.newInstance();
				}
				return mStarListFragment;
			case 2:
				if(mTagListFragment == null){
					mTagListFragment = TagListFragment.newInstance();
				}
				return mTagListFragment;
		}

		return null;
	}

	@Override
	public int getCount() {
		return COUNT;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE_ARRAY[position];
	}

	public void backToTop(){
		if(mCurrntPageNum == 0){
			mListFragment.backToTop();
		}else if(mCurrntPageNum == 1){
			mStarListFragment.backToTop();
		}else if(mCurrntPageNum == 2){
			mTagListFragment.backToTop();
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		mCurrntPageNum = position;

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public interface BackToTop{
		void backToTop();
	}
}
