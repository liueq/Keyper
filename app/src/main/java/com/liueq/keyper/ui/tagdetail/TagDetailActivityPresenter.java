package com.liueq.keyper.ui.tagdetail;

import android.widget.Toast;

import com.liueq.keyper.R;
import com.liueq.keyper.base.Presenter;
import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.model.Tag;
import com.liueq.keyper.domain.interactor.AddTagUC;
import com.liueq.keyper.domain.interactor.StarUC;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 29/2/2016.
 * Presenter of TagDetailActivity
 */
public class TagDetailActivityPresenter extends Presenter{

	private Tag mCurrentTag;

	private TagDetailActivity mActivity;
	private AddTagUC mAddTagUC;
	private StarUC mStarUC;

	public TagDetailActivityPresenter(TagDetailActivity activity, StarUC mStarUC, AddTagUC mAddTagUC) {
		this.mActivity = activity;
		this.mStarUC = mStarUC;
		this.mAddTagUC = mAddTagUC;
	}

	public void setTag(Tag tag){
		mCurrentTag = tag;
	}

	public Tag getTag(){
		return mCurrentTag;
	}

	/******************** Action ********************/

	public void loadListAction(){
		loadListOb().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(loadListSub());
	}

	public void starAction(Account account){
		starObj(account).subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(starSub());
	}

	public void deleteTagAction(){
		deleteTagOb().subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(deleteTagSub());
	}

	/******************** RxJava ********************/


	private Observable<List<Account>> loadListOb(){
		return Observable.create(new Observable.OnSubscribe<List<Account>>() {
			@Override
			public void call(Subscriber<? super List<Account>> subscriber) {
				List<Account> list = loadList();
				subscriber.onNext(list);
			}
		});
	}

	private Subscriber<List<Account>> loadListSub(){
		return new Subscriber<List<Account>>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(List<Account> accounts) {
				((TagDetailFragment) getFragment(TagDetailFragment.class)).updateUI(accounts);
			}
		};
	}


	private Observable<Boolean> starObj(final Account account){
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				boolean result = starOrUnStar(account);
				subscriber.onNext(result);
			}
		});
	}

	private Subscriber<Boolean> starSub(){
		return new Subscriber<Boolean>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Boolean aBoolean) {
				if(aBoolean){
					loadListAction();
				}
			}
		};
	}

	private Observable<Boolean> deleteTagOb(){
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				subscriber.onNext(mAddTagUC.deleteUC(mCurrentTag));
			}
		});
	}

	private Action1<Boolean> deleteTagSub(){
		return new Action1<Boolean>() {
			@Override
			public void call(Boolean aBoolean) {
				Toast.makeText(mActivity, R.string.delete_tag_ok, Toast.LENGTH_SHORT).show();
				mActivity.finish();
			}
		};
	}

	/******************** Operation ********************/

	public List<Account> loadList(){
		return mAddTagUC.getAllAccount(mCurrentTag);
	}

	public boolean starOrUnStar(Account account){
		boolean result = false;

		if(account.is_stared){
			result = mStarUC.unStarAccount(account);
		}else{
			result = mStarUC.starAccount(account);
		}

		return result;
	}
}
