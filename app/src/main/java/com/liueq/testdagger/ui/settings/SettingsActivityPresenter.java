package com.liueq.testdagger.ui.settings;

import android.text.TextUtils;
import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.repository.SharedPreferenceRepo;
import com.liueq.testdagger.data.repository.SharedPreferenceRepoImpl;
import com.liueq.testdagger.domain.interactor.SharedPUC;
import com.liueq.testdagger.utils.BackUpTool;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liueq on 29/7/15.
 * Settings Presenter
 */
public class SettingsActivityPresenter extends Presenter {

    public final static String TAG = "SettingsP";

    private SettingsActivity mActivity;
    private SharedPUC mSharedPUC;

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SharedPUC sharedPUC){
        this.mActivity = settingsActivity;
        this.mSharedPUC = sharedPUC;
    }

    /******************** Action ********************/
	/**
     * Load DB password to UI
     */
    public void loadDataAction() {
        //Get Database password from SharedPreference
        HashMap<String, String> db_pwd_map = mSharedPUC.getDBPassword();
        String db_password = db_pwd_map.get(Constants.SP_DB_PWD);

        mActivity.updateDBPassword(db_password);
    }

	/**
     * Check login password
     * @param password
     * @return
     */
    public boolean checkPassAction(String password){
        return mSharedPUC.checkPassword(password);
    }

	/**
     * Save login password
     * @param password
     * @return
     */
    public boolean savePassAction(String password){
        return mSharedPUC.savePassword(password);
    }

	/**
     * Save DB password
     * @param db_pwd
     * @return
     */
    public void saveDBPassAction(String db_pwd){
        if(mSharedPUC.saveDBPassword(db_pwd)){
            saveDBPassOb(db_pwd)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(saveDBPassSub());
        }
    }

	/**
	 * Export DB Action
     */
    public void exportDBAction(){
        exportDBOb().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(exportDBSub());
    }

	/**
	 * Import DB action
     * @param path import db path
     * @param password import db password
     */
    public void importDBAction(String path, String password){
        importDBOb(path, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(importDBFun())
                .subscribe(importDBSub());
    }

    public int getAutoLockPeriod(){
        SharedPreferenceRepo shared = new SharedPreferenceRepoImpl(mActivity);
        String period = shared.getProterties(Constants.SP_AUTO_LOCK_PERIOD);
        if(TextUtils.isEmpty(period)){
           period = "0";
        }

        int period_int = 0;
        try{
            period_int = Integer.valueOf(period);
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return period_int;
    }

    public void setAutoLockPeriod(int period){
        SharedPreferenceRepo shared = new SharedPreferenceRepoImpl(mActivity);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Constants.SP_AUTO_LOCK_PERIOD, String.valueOf(period));
        shared.saveProperties(map);
    }

    /******************** RxJava ********************/
    private Observable<Boolean> saveDBPassOb(final String password){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(SQLCipherOpenHelper.getInstance(mActivity).setPassword(password));
            }
        });
    }

    private Action1<Boolean> saveDBPassSub(){
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(mActivity, R.string.change_db_succeed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, R.string.change_db_failed, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private Observable<String> exportDBOb(){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String path = BackUpTool.exportDB(mActivity);
                subscriber.onNext(path);
            }
        });
    }

    private Action1<String> exportDBSub(){
        return new Action1<String>() {
            @Override
            public void call(String s) {
                if(TextUtils.isEmpty(s)){
                    Toast.makeText(mActivity, R.string.export_failed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, mActivity.getString(R.string.export_path, s), Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private Observable<String> importDBOb(final String path, final String password){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if(BackUpTool.canOpenDB(password, path)){
                    BackUpTool.importDB(mActivity, path);
                    subscriber.onNext(password);
                }else{
                    subscriber.onNext(null);
                }
            }
        });
    }

    private Func1<String, Boolean> importDBFun(){
        return new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                if(!TextUtils.isEmpty(s)){
                    mSharedPUC.saveDBPassword(s);
                    return true;
                }else{
                    return false;
                }
            }
        };
    }

    private Action1<Boolean> importDBSub(){
        return new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if(aBoolean){
                    loadDataAction();
                    Toast.makeText(mActivity, R.string.import_data_succeed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, R.string.import_db_error, Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

}
