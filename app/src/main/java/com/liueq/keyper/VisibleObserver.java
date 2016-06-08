package com.liueq.keyper;

import com.liueq.keyper.data.database.SQLCipherOpenHelper;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 23/2/2016.
 * Observe when application is pause or invisible
 */
public class VisibleObserver {

	private static VisibleObserver mInstance;
	private final HideLock mHideLock;

	public static VisibleObserver getInstance(){
		if(mInstance == null){
			mInstance = new VisibleObserver();
		}

		return mInstance;
	}

	private VisibleObserver(){
		mHideLock = new HideLock();
	}
	/**
	 * Trigger when app from visible to hide.
	 */
	public void onApplicationHide(){
		synchronized (mHideLock){
			if(mHideLock.mHided){
				return;
			}
			mHideLock.mHided = true;
//			Log.d(Constants.DEFAULT_TAG, "onApplicationHide: ");

			//RxJava do close delay
			closeDBAction();

			recordHideTime();
		}
	}

	/**
	 * Trigger when app from hide to visible.
	 */
	public void onApplicationShow(){
		synchronized (mHideLock){
			if(!mHideLock.mHided){
				return ;
			}
			mHideLock.mHided = false;
//			Log.d(Constants.DEFAULT_TAG, "onApplicationShow: ");

			//RxJava do open db.
			openDBAction();

			recordShowTime();
		}

	}

	/******************** Action ********************/

	private void closeDBAction(){
		closeDBOb().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.delaySubscription(500, TimeUnit.MILLISECONDS)
				.subscribe();
	}

	private void openDBAction(){
		openDBOb().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe();
	}

	private void recordHideTime(){
		SharedPreferenceRepo Shared = new SharedPreferenceRepoImpl(TestApplication.getApplication());
		HashMap<String, String> time = new HashMap<String, String>();
		time.put(Constants.SP_HIDE_TIME, String.valueOf(System.currentTimeMillis()));
		Shared.saveProperties(time);
	}

	private void recordShowTime(){
		SharedPreferenceRepo Shared = new SharedPreferenceRepoImpl(TestApplication.getApplication());
		HashMap<String, String> time = new HashMap<String, String>();
		time.put(Constants.SP_SHOW_TIME, String.valueOf(System.currentTimeMillis()));
		Shared.saveProperties(time);
	}

	/******************** RxJava ********************/

	private Observable<Void> closeDBOb(){
		return Observable.create(new Observable.OnSubscribe<Void>() {
			@Override
			public void call(Subscriber<? super Void> subscriber) {
				synchronized (mHideLock){
					if(mHideLock.mHided){
//						Log.d(Constants.DEFAULT_TAG, "call: close database");
						SQLCipherOpenHelper.getInstance(TestApplication.getApplication()).closeDatabase();
					}
				}
			}
		});
	}

	private Observable<Void> openDBOb(){
		return Observable.create(new Observable.OnSubscribe<Void>() {
			@Override
			public void call(Subscriber<? super Void> subscriber) {
				SQLCipherOpenHelper.getInstance(TestApplication.getApplication());
			}
		});
	}

	/**
	 * final object for synchronized
	 * boolean object for tell visibility
	 */
	private class HideLock{
		public boolean mHided = false;
	}

}
