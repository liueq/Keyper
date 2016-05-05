package com.liueq.testdagger.ui.launch;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject;
import android.support.v4.os.CancellationSignal;
import android.widget.Toast;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;
import com.liueq.testdagger.base.Presenter;
import com.liueq.testdagger.ui.main.MainActivity;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.KeyTools;
import com.liueq.testdagger.utils.SharedPreferencesUtils;

import javax.crypto.Cipher;

/**
 * Created by liueq on 16/7/15.
 * Presenter of SplashActivity
 */
public class SplashActivityPresenter extends Presenter {

    public final static String TAG = "Splash P";

    public boolean hasPassword = false;

    //Visibility of password
    public boolean mPasswordStatus1 = false;
    public boolean mPasswordStatus2 = false;

    public String mMode;

    public final static String MODE_LAUNCH = "mode_launch";
    public final static String MODE_LOCK = "mode_lock";

    private FingerprintManagerCompat mFingerprintManager;
    private AuthenticationCallback mAuthenticationCallback = new AuthenticationCallback();
    private CancellationSignal mCancellationSingal;

    private KeyTools mKeyTools;
    public static byte[] SECRET_BYTES = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};

    private SplashActivity activity;

    public SplashActivityPresenter(SplashActivity activity){
        this.activity = activity;

        mFingerprintManager = FingerprintManagerCompat.from(activity);
        try {
            mKeyTools = KeyTools.newInstance();
        } catch (KeyTools.KeyToolsException e) {
            e.printStackTrace();
        }
    }

    public void login(String pwd1, String pwd2){
        SharedPreferences sp = activity.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        String saved_pwd = sp.getString(Constants.SP_PWD, null);

        String pwd_1_md5 = Encrypter.encryptByMD5(pwd1);
        String pwd_2_md5 = Encrypter.encryptByMD5(pwd2);

        if(hasPassword){
            if(pwd_1_md5.equals(saved_pwd)){
                //Save as DB password in memory
                TestApplication.setDBPassword(pwd1);

                if(mMode.equals(MODE_LAUNCH)){
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                    activity.finish();
                }else if(mMode.equals(MODE_LOCK)){
                    activity.finish();
                }
            }else{
                Toast.makeText(activity, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(pwd_1_md5.equals(pwd_2_md5)){
                //Save as DB password
                TestApplication.setDBPassword(pwd1);

                SharedPreferences.Editor editor = sp.edit();
                editor.putString(Constants.SP_PWD, pwd_1_md5);
                editor.commit();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else{
                Toast.makeText(activity, "Password Mismatch", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * return the  visibility of password 1
     * @return the status after click
     */
    public boolean checkPassword1Action(){
        mPasswordStatus1 = !mPasswordStatus1;
        return mPasswordStatus1;
    }

    /**
     * Check visibility of password 2
     * @return the status after click
     */
    public boolean checkPassword2Action(){
        mPasswordStatus2 = !mPasswordStatus2;
        return mPasswordStatus2;
    }


     public void authenticateAction(){
		Cipher cipher;
		cipher = mKeyTools.getUserAuthCipher();

		CryptoObject crypto = new CryptoObject(cipher);
		mCancellationSingal = new CancellationSignal();
		mFingerprintManager.authenticate(crypto, 0, mCancellationSingal, mAuthenticationCallback, null);
	}

    public void cancelAuthAction(){
        if(!mCancellationSingal.isCanceled()){
            mCancellationSingal.cancel();
        }
    }

    public class AuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback{
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            Cipher cipher = result.getCryptoObject().getCipher();
            try {
                cipher.doFinal(SplashActivityPresenter.SECRET_BYTES);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String password = SharedPreferencesUtils.get(Constants.SP_NAME, Constants.SP_FINGERPRINT_PASSWORD);
            login(password, null);
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            Toast.makeText(activity, R.string.welcome_dialog_fingerprint_mismatch, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
        }
    }
}
