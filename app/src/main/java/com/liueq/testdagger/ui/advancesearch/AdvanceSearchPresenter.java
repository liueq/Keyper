package com.liueq.testdagger.ui.advancesearch;

import com.liueq.testdagger.base.Presenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 7/3/2016.
 * Advance search presenter
 */
public class AdvanceSearchPresenter extends Presenter{

	private final String [] SEARCH_FIELDS = {"SITE", "NAME", "PASSWORD", "MAIL", "DESCRIPTION"};
	List<RecyclerFieldAdapter.SearchField> mSearchFields = new ArrayList<RecyclerFieldAdapter.SearchField>();
	private AdvanceSearchActivity mActivity;

	public AdvanceSearchPresenter(AdvanceSearchActivity activity) {
		mActivity = activity;

		initFields();
	}

	private void initFields(){
		for(int i = 0; i < SEARCH_FIELDS.length; i++){
			if(i == 0){
				mSearchFields.add(new RecyclerFieldAdapter.SearchField(SEARCH_FIELDS[i], true));
			}else{
				mSearchFields.add(new RecyclerFieldAdapter.SearchField(SEARCH_FIELDS[i], false));
			}
		}
	}

}
