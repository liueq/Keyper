package com.liueq.testdagger.ui.settings;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.domain.interactor.CheckPasswordUC;
import com.liueq.testdagger.domain.interactor.SharedPUC;
import com.liueq.testdagger.domain.interactor.SetSpUC;

import java.util.HashMap;

/**
 * Created by liueq on 29/7/15.
 * Settings Presenter
 */
public class SettingsActivityPresenter extends Presenter {

    public final static String TAG = "settingsP";

    private SettingsActivity mSettingsActivity;
    private SharedPUC mSharedPUC;

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SharedPUC sharedPUC){
        this.mSettingsActivity = settingsActivity;
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

        mSettingsActivity.updateDBPassword(db_password);
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
     * @param aes_pwd
     * @return
     */
    public boolean saveDBPassAction(String aes_pwd){
        return mSharedPUC.saveDBPassword(aes_pwd);
    }

}
