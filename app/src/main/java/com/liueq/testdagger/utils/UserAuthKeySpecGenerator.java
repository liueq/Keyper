package com.liueq.testdagger.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

/**
 * Created by liueq on 4/5/2016.
 * UserAuthKeySpecGenerator
 */
public class UserAuthKeySpecGenerator implements KeySpecGenerator{

	private final String mBlockMode;
	private final String mPadding;


	public UserAuthKeySpecGenerator(String mBlockMode, String mPadding) {
		this.mBlockMode = mBlockMode;
		this.mPadding = mPadding;
	}

	@TargetApi(Build.VERSION_CODES.M)
	@Override
	public KeyGenParameterSpec generate(String keyName) {
		return new KeyGenParameterSpec.Builder(keyName, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
				.setBlockModes(mBlockMode)
				.setEncryptionPaddings(mPadding)
				.setUserAuthenticationRequired(true)
				.build();
	}
}
