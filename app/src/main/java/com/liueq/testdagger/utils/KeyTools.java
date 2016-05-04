package com.liueq.testdagger.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.ArrayMap;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Created by liueq on 4/5/2016.
 * KeyTools
 */
public final class KeyTools {

	private final static String PROVIDER_NAME = "AndroidKeyStore";
	private final static String USER_AUTH_KEY_NAME = "com.liueq.testdagger.USER_AUTH_KEY";

	private final static String ALGORITHM = KeyProperties.KEY_ALGORITHM_AES;
	private final static String BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC;
	private final static String PADDING = KeyProperties.ENCRYPTION_PADDING_PKCS7;
	private final static String TRANSFORMATION = ALGORITHM + "/" + BLOCK_MODE + "/" + PADDING;

	private final KeyStore mKeyStore;
	private final Map<String, KeySpecGenerator> mGenerators;

	private KeyTools(KeyStore keyStore, Map<String, KeySpecGenerator> generators){
		mKeyStore = keyStore;
		mGenerators = generators;
	}

	public static KeyTools newInstance() throws KeyToolsException{
		KeyStore keyStore;
		try{
			keyStore = KeyStore.getInstance(PROVIDER_NAME);
			keyStore.load(null);
		} catch (Exception e) {
			throw new KeyToolsException("Error initializing keystore: ", e);
		}

		Map<String, KeySpecGenerator> generators = new HashMap<>();
		generators.put(USER_AUTH_KEY_NAME, new UserAuthKeySpecGenerator(BLOCK_MODE, PADDING));

		return new KeyTools(keyStore, generators);
	}

	public static class KeyToolsException extends Exception{

		public KeyToolsException(String detailMessage, Throwable throwable){
			super(detailMessage, throwable);
		}
	}


	public Cipher getUserAuthCipher(){
		try{
			SecretKey secretKey = createKey();
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);

			return cipher;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}

		return null;
	}

	@TargetApi(Build.VERSION_CODES.M)
	private SecretKey createKey(){
		try {
			if (!mKeyStore.isKeyEntry(USER_AUTH_KEY_NAME)) {
				KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM, PROVIDER_NAME);
				KeyGenParameterSpec spec = mGenerators.get(USER_AUTH_KEY_NAME).generate(USER_AUTH_KEY_NAME);
				keyGenerator.init(spec);
				keyGenerator.generateKey();
			}

			return (SecretKey) mKeyStore.getKey(USER_AUTH_KEY_NAME, null);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		}

		return null;
	}

}

