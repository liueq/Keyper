package com.liueq.keyper.ui.settings;

import android.text.TextUtils;
import android.widget.Toast;

import com.liueq.keyper.Constants;
import com.liueq.keyper.R;
import com.liueq.keyper.TestApplication;
import com.liueq.keyper.base.Presenter;
import com.liueq.keyper.data.database.SQLCipherOpenHelper;
import com.liueq.keyper.data.repository.SharedPreferenceRepo;
import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;
import com.liueq.keyper.domain.interactor.SharedPUC;
import com.liueq.keyper.utils.BackUpTool;
import com.liueq.keyper.utils.SharedPreferencesUtils;

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
    public HashMap<Integer, Integer> mMapPeriodToPos = new HashMap<Integer, Integer>();

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SharedPUC sharedPUC){
        this.mActivity = settingsActivity;
        this.mSharedPUC = sharedPUC;

        mMapPeriodToPos.put(60, 0);
        mMapPeriodToPos.put(5 * 60, 1);
        mMapPeriodToPos.put(15 * 60, 2);
        mMapPeriodToPos.put(60 * 60, 3);
        mMapPeriodToPos.put(3600 * 24, 4);
    }

    /******************** Action ********************/
	/**
     * Load DB password to UI
     */
    public void loadDataAction() {
        //Get Database password from SharedPreference
//        HashMap<String, String> db_pwd_map = mSharedPUC.getDBPassword();
//        String db_password = db_pwd_map.get(Constants.SP_DB_PWD);

//        mActivity.updateDBPassword(db_password);
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
    public void savePassAction(String password){
        if(mSharedPUC.savePassword(password)){
            saveDBPassAction(password);
        }
    }

	/**
     * Save DB password
     * @param db_pwd
     * @return
     */
    public void saveDBPassAction(String db_pwd){
        saveDBPassOb(db_pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(saveDBPassSub());
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
           period = "60";
        }

        int period_int = 60;
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

    public boolean isFingerprintEnable(){
        String isEnable = SharedPreferencesUtils.get(Constants.SP_NAME, Constants.SP_FINGERPRINT);
        if(TextUtils.isEmpty(isEnable)){
            SharedPreferencesUtils.set(Constants.SP_NAME, Constants.SP_FINGERPRINT, "0");
            return false;
        }else {
            return isEnable.equals("1");
        }
    }

    public void setFingerprint(boolean enable){
        SharedPreferencesUtils.set(Constants.SP_NAME, Constants.SP_FINGERPRINT, enable ? "1" : "0");
        if(enable){
            SharedPreferencesUtils.set(Constants.SP_NAME, Constants.SP_FINGERPRINT_PASSWORD, TestApplication.getDBPassword());//To use fingerprint, must save password in local
        }else{
            SharedPreferencesUtils.set(Constants.SP_NAME, Constants.SP_FINGERPRINT_PASSWORD, "");
        }
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
                    Toast.makeText(mActivity, R.string.change_pwd_succeed, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mActivity, R.string.change_pwd_failed, Toast.LENGTH_SHORT).show();
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
                    BackUpTool.importDB(mActivity, path, password);
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
