package com.liueq.testdagger.ui.activity.presenter;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.SettingsActivity;
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

    public SettingsActivityPresenter(SettingsActivity settingsActivity, SetSpUseCase setSpUseCase, GetSpUseCase getSpUseCase){
        this.mSettingsActivity = settingsActivity;
        this.mSetSpUseCase = setSpUseCase;
        this.mGetSpUseCase = getSpUseCase;
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

    public void encryptPwd(boolean encrypt){
        mSetSpUseCase.savePwdEncStatus(encrypt);
    }

    public void encryptDesc(boolean encrypt){
        mSetSpUseCase.saveDescEncStatus(encrypt);
    }
}
