package com.liueq.keyper.ui.main;

import com.liueq.keyper.base.Presenter;
import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.model.Tag;
import com.liueq.keyper.domain.interactor.AddTagUC;
import com.liueq.keyper.domain.interactor.GetAccountListUC;
import com.liueq.keyper.domain.interactor.GetStarListUC;
import com.liueq.keyper.domain.interactor.SearchAccountUC;
import com.liueq.keyper.domain.interactor.StarUC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 13/7/15.
 * MainActivity Presenter
 *      include: ListFragment, SearchDialogFragment
 */
public class MainActivityPresenter extends Presenter {

    public final static String TAG = "MainActivityPresenter";

    private MainActivity mainActivity;

    GetAccountListUC getAccountListUC;
    SearchAccountUC searchAccountUC;
    StarUC mStarUC;
    GetStarListUC getStarListUC;
    AddTagUC addTagUC;

    public MainActivityPresenter(MainActivity mainActivity, GetAccountListUC getAccountListUC, SearchAccountUC searchAccountUC, StarUC starUC, GetStarListUC starListUC, AddTagUC addTagUC) {
        this.mainActivity = mainActivity;

        this.getAccountListUC = getAccountListUC;
        this.searchAccountUC = searchAccountUC;
        this.mStarUC = starUC;
        this.getStarListUC = starListUC;
        this.addTagUC = addTagUC;
    }


    /******************** Action ********************/
	/**
     * Load star list - StarListFragment
     */
    public void loadStarListAction(){
        loadStarListOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadStarListSub());
    }

	/**
     * Load list - ListFragment
     */
    public void loadListAction(){
        loadListOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loadListSub());
    }

	/**
     * Load tag list - TagListFragment
     */
    public void loadTagListAction(){
        getAllTagOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(divideListFun())
                .subscribe(getAllTagSub());

    }

	/**
     * Do star
     * @param account
     */
    public void starAction(Account account){
        starObj(account).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(starSub());
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
                ((ListFragment) getFragment(ListFragment.class)).updateUI(accounts);
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
                    loadStarListAction();
                }
            }
        };
    }

    private Observable<List<Tag>> getAllTagOb(){
        return Observable.create(new Observable.OnSubscribe<List<Tag>>() {
            @Override
            public void call(Subscriber<? super List<Tag>> subscriber) {
                subscriber.onNext(addTagUC.getAllTag());
            }
        });
    }

    private Func1<List<Tag>, List<RecyclerTagListAdapter.TagItem>> divideListFun(){
        return new Func1<List<Tag>, List<RecyclerTagListAdapter.TagItem>>() {
            @Override
            public List<RecyclerTagListAdapter.TagItem> call(List<Tag> tags) {
                HashMap<String, List<Tag>> mapCharToTags = new HashMap<>();
                final String WIDE = "*";
                for(Tag t : tags){
                    String c = t.tag_name.substring(0, 1);

                    if(Character.isLetter(c.charAt(0))){
                        c = c.toUpperCase();
                    }

                    if(!Character.isLetter(c.charAt(0))){
                        //当不是字母的情况，用一个字符标示
                        if(mapCharToTags.get(WIDE) == null){
                            List<Tag> map_list = new ArrayList<Tag>();
                            map_list.add(t);
                            mapCharToTags.put(WIDE, map_list);
                        }else{
                            List<Tag> map_list = mapCharToTags.get(c);
                            map_list.add(t);
                            mapCharToTags.put(WIDE, map_list);
                        }
                    }else if(mapCharToTags.get(c) == null){
                        //没有该字母
                        List<Tag> map_list = new ArrayList<Tag>();
                        map_list.add(t);
                        mapCharToTags.put(c, map_list);
                    }else{
                        //已经有了该字母
                        List<Tag> map_list = mapCharToTags.get(c);
                        map_list.add(t);
                        mapCharToTags.put(c, map_list);
                    }
                }

                List<RecyclerTagListAdapter.TagItem> item_list = new ArrayList<>();
                for(String key : mapCharToTags.keySet()){
                    RecyclerTagListAdapter.TagItem item = new RecyclerTagListAdapter.TagItem(key, mapCharToTags.get(key));
                    item_list.add(item);
                }

                Collections.sort(item_list);
                return item_list;
            }
        };
    }

    private Action1<List<RecyclerTagListAdapter.TagItem>> getAllTagSub(){
        return new Action1<List<RecyclerTagListAdapter.TagItem>>() {
            @Override
            public void call(List<RecyclerTagListAdapter.TagItem> tagItems) {
                ((TagListFragment) getFragment(TagListFragment.class)).updateList(tagItems);
            }
        };
    }

    private Observable<List<Account>> loadStarListOb(){
        return Observable.create(new Observable.OnSubscribe<List<Account>>() {
            @Override
            public void call(Subscriber<? super List<Account>> subscriber) {
                List<Account> list = loadStarList();
                subscriber.onNext(list);
            }
        });
    }

    private Subscriber<List<Account>> loadStarListSub(){
        return new Subscriber<List<Account>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<Account> accounts) {
                ((StarListFragment) getFragment(StarListFragment.class)).updateUI(accounts);
            }
        };
    }


    public List<Account> loadList(){
        return getAccountListUC.executeDB();
    }

    public List<Account> loadStarList(){
        return getStarListUC.executeDB();
    }


    public List<Account> search(String searchKey){
        return searchAccountUC.execute(searchKey);
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
