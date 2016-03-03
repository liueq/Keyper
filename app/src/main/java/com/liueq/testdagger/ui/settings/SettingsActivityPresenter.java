package com.liueq.testdagger.ui.settings;

import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.domain.interactor.SharedPUC;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
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

}
