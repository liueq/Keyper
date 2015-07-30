package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.SettingsActivity;
import com.liueq.testdagger.domain.interactor.CheckPasswordUseCase;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;
import com.liueq.testdagger.domain.interactor.SetSpUseCase;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 */
public class SettingsActivityPresenter {

    private SettingsActivity mSettingsActivity;
    private SetSpUseCase mSetSpUseCase;
    private GetSpUseCase mGetSpUseCase;
    private CheckPasswordUseCase checkPasswordUseCase;

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SetSpUseCase setSpUseCase, GetSpUseCase getSpUseCase, CheckPasswordUseCase checkPasswordUseCase){
        this.mSettingsActivity = settingsActivity;
        this.mSetSpUseCase = setSpUseCase;
        this.mGetSpUseCase = getSpUseCase;
        this.checkPasswordUseCase = checkPasswordUseCase;
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
        HashMap<String, String> file_path_map = mGetSpUseCase.getFileSavePath();
        String aes = aes_pwd_map.get(Constants.SP_AES);
        String path = file_path_map.get(Constants.STORAGE_PATH);

        //设定到activity
        mSettingsActivity.setShowAES(aes);
        mSettingsActivity.setShowPath(path);
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
}
