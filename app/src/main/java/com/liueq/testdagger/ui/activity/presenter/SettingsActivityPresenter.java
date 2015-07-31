package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.CheckPasswordUseCase;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;
import com.liueq.testdagger.domain.interactor.SaveAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SetSpUseCase;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 */
public class SettingsActivityPresenter {

    private SettingsActivity mSettingsActivity;
    private List<Account> mList;
    private SetSpUseCase mSetSpUseCase;
    private GetSpUseCase mGetSpUseCase;
    private CheckPasswordUseCase checkPasswordUseCase;
    private SaveAccountListUseCase mSaveAccountListUseCase;

    public HashMap<String, Boolean> mFilePathState;

    public SettingsActivityPresenter(SettingsActivity settingsActivity, List<Account> list, SetSpUseCase setSpUseCase, GetSpUseCase getSpUseCase, CheckPasswordUseCase checkPasswordUseCase, SaveAccountListUseCase saveAccountListUseCase){
        this.mSettingsActivity = settingsActivity;
        this.mList = list;
        this.mSetSpUseCase = setSpUseCase;
        this.mGetSpUseCase = getSpUseCase;
        this.checkPasswordUseCase = checkPasswordUseCase;
        this.mSaveAccountListUseCase = saveAccountListUseCase;
    }

    public void initialSwitch(){
        //从UseCase层获取设定
        HashMap<String, String> status = mGetSpUseCase.getEncStatus();
        String pwd_enc_status = status.get(Constants.SP_IS_PWD_ENC);
        String desc_enc_status = status.get(Constants.SP_IS_DESC_ENC);

        //第一次启动的时候初始化，默认是加密
        if(pwd_enc_status == null && desc_enc_status == null){
            mSettingsActivity.checkSwitchPwd(true);
            mSettingsActivity.checkSwitchDesc(true);
            return ;
        }

        if(pwd_enc_status.equals(Constants.YES)){
            mSettingsActivity.checkSwitchPwd(true);
        }else{
            mSettingsActivity.checkSwitchPwd(false);
        }

        if(desc_enc_status.equals(Constants.YES)){
            mSettingsActivity.checkSwitchDesc(true);
        }else{
            mSettingsActivity.checkSwitchDesc(false);
        }
    }

    public void retrieveUIData() {
        //从SP获取AES密码和文件的保存路径
        HashMap<String, String> aes_pwd_map = mGetSpUseCase.getAESPassword();
        mFilePathState = mGetSpUseCase.getFileSavePath();
        String aes = aes_pwd_map.get(Constants.SP_AES);

        //设定到activity
        mSettingsActivity.setShowAES(aes);

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

        mSettingsActivity.setShowPath(sb.toString());
    }
    public void encryptPwd(boolean encrypt){
        mSetSpUseCase.savePwdEncStatus(encrypt);
    }

    public void encryptDesc(boolean encrypt){
        mSetSpUseCase.saveDescEncStatus(encrypt);
    }

    public boolean checkPassword(String password){
        return checkPasswordUseCase.execute(password);
    }

    public boolean savePassword(String password){
        return mSetSpUseCase.savePassword(password);
    }

    public boolean saveAes(String aes_pwd){
        return mSetSpUseCase.saveAES(aes_pwd);
    }

    public boolean savePath(HashMap<String, Boolean> state){
        return mSetSpUseCase.saveFilePathState(state);
    }

    public void saveData(){
        mSaveAccountListUseCase.execute(mList, null);
    }
}
