package com.liueq.testdagger;

import com.liueq.testdagger.data.database.SQLCipherOpenHelper;

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
			closeDBOb().subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.delaySubscription(500, TimeUnit.MILLISECONDS)
					.subscribe();
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

			openDBOb().subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.subscribe();
		}

	}

	private void openDatabase(){
		//Operation: Open DB
		SQLCipherOpenHelper.getInstance(TestApplication.getApplication());
	}

	private void closeDatabase(){
		//Operation: Close DB
		SQLCipherOpenHelper.getInstance(TestApplication.getApplication()).closeDatabase();
	}

	private Observable<Void> closeDBOb(){
		return Observable.create(new Observable.OnSubscribe<Void>() {
			@Override
			public void call(Subscriber<? super Void> subscriber) {
				synchronized (mHideLock){
					if(mHideLock.mHided){
//						Log.d(Constants.DEFAULT_TAG, "call: close database");
						closeDatabase();
					}
				}
			}
		});
	}

	private Observable<Void> openDBOb(){
		return Observable.create(new Observable.OnSubscribe<Void>() {
			@Override
			public void call(Subscriber<? super Void> subscriber) {
				openDatabase();
			}
		});
	}

	private class HideLock{
		public boolean mHided = false;
	}

}
