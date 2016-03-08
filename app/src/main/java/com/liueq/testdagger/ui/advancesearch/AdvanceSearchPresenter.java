package com.liueq.testdagger.ui.advancesearch;

import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.SearchAccountUC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 7/3/2016.
 * Advance search presenter
 */
public class AdvanceSearchPresenter extends Presenter{

	public final static String [] SEARCH_FIELDS = {"SITE", "NAME", "PASSWORD", "MAIL", "DESCRIPTION"};
	public HashMap<String, String> mSearchFieldToColumn = new HashMap<String, String>();
	List<RecyclerFieldAdapter.SearchField> mSearchFields = new ArrayList<RecyclerFieldAdapter.SearchField>();
	String mCurrentSearch;
	String mCurrentField;

	private AdvanceSearchActivity mActivity;

	SearchAccountUC mSearchAccontUC;

	public AdvanceSearchPresenter(AdvanceSearchActivity activity, SearchAccountUC searchAccountUC) {
		mActivity = activity;
		mSearchAccontUC = searchAccountUC;

		initFields();
	}

	private void initFields(){
		for(int i = 0; i < SEARCH_FIELDS.length; i++){
			if(i == 0){
				mSearchFields.add(new RecyclerFieldAdapter.SearchField(SEARCH_FIELDS[i], true));
			}else{
				mSearchFields.add(new RecyclerFieldAdapter.SearchField(SEARCH_FIELDS[i], false));
			}

			mSearchFieldToColumn.put(SEARCH_FIELDS[i], DBTables.Password.ALL_COLUMN[i + 1]);
		}

	}

	/******************** Action ********************/
	public void searchAction(){
		searchAction(mCurrentSearch, mSearchFieldToColumn.get(mCurrentField));
	}
	public void searchAction(String key, String field){
		searchOb(key, field).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(searchSub());
	}

	/******************** RxJava ********************/
	Observable<List<Account>> searchOb(final String key, final String field){
		return Observable.create(new Observable.OnSubscribe<List<Account>>() {
			@Override
			public void call(Subscriber<? super List<Account>> subscriber) {
				subscriber.onNext(mSearchAccontUC.searchByField(key, field));
			}
		});
	}

	Action1<List<Account>> searchSub(){
		return new Action1<List<Account>>() {
			@Override
			public void call(List<Account> accounts) {
				 mActivity.updateUI(accounts);
			}
		};
	}


}
