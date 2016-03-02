package com.liueq.testdagger.ui.settings;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.CheckPasswordUC;
import com.liueq.testdagger.domain.interactor.GetAccountListUC;
import com.liueq.testdagger.domain.interactor.GetSpUC;
import com.liueq.testdagger.domain.interactor.SaveAccountListUC;
import com.liueq.testdagger.domain.interactor.SetSpUC;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liueq on 29/7/15.
 * Settings Presenter
 */
public class SettingsActivityPresenter extends Presenter {

    public final static String TAG = "settingsP";

    private SettingsActivity mSettingsActivity;
    private SetSpUC mSetSpUC;
    private GetSpUC mGetSpUC;
    private CheckPasswordUC checkPasswordUC;

    public HashMap<String, Boolean> mFilePathState;

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SetSpUC setSpUC, GetSpUC getSpUC, CheckPasswordUC checkPasswordUC){
        this.mSettingsActivity = settingsActivity;
        this.mSetSpUC = setSpUC;
        this.mGetSpUC = getSpUC;
        this.checkPasswordUC = checkPasswordUC;
    }

    public void retrieveUIData() {
        //从SP获取AES密码和文件的保存路径
        HashMap<String, String> aes_pwd_map = mGetSpUC.getAESPassword();
        mFilePathState = mGetSpUC.getFileSavePath();
        String aes = aes_pwd_map.get(Constants.SP_AES);

        //设定到activity
//        mSettingsActivity.setShowAES(aes);

        StringBuffer sb = new StringBuffer();
        if (mFilePathState.get(Constants.SP_IS_SAVE_EXTERNAL)) {
            sb.append(mSettingsActivity.getString(R.string.external_path));
            if (mFilePathState.get(Constants.SP_IS_SAVE_INTERNAL)) {
                sb.append(", ");
                sb.append(mSettingsActivity.getString(R.string.internal_path));
            }
        }else if (mFilePathState.get(Constants.SP_IS_SAVE_INTERNAL)){
            sb.append(mSettingsActivity.getString(R.string.internal_path));
        }

//        mSettingsActivity.setShowPath(sb.toString());
    }
    public void encryptPwd(boolean encrypt){
        mSetSpUC.savePwdEncStatus(encrypt);
    }

    public void encryptDesc(boolean encrypt){
        mSetSpUC.saveDescEncStatus(encrypt);
    }

    public boolean checkPassword(String password){
        return checkPasswordUC.execute(password);
    }

    public boolean savePassword(String password){
        return mSetSpUC.savePassword(password);
    }

    public boolean saveAes(String aes_pwd){
        return mSetSpUC.saveAES(aes_pwd);
    }

    public boolean savePath(HashMap<String, Boolean> state){
        return mSetSpUC.saveFilePathState(state);
    }

}
