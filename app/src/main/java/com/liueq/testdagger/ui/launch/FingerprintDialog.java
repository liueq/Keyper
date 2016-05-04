package com.liueq.testdagger.ui.launch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.AuthenticationCallback;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat.CryptoObject;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.liueq.testdagger.R;
import com.liueq.testdagger.utils.KeyTools;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by liueq on 3/5/2016.
 * Fingerprint dialog
 */
public class FingerprintDialog extends DialogFragment{

	private FingerprintManagerCompat mFingerprintManager;
	private AuthenticationCallback mAuthenticationCallback = new AuthenticationCallback();
	private CancellationSignal mCancellationSingal;

	private KeyTools mKeyTools;
	private static byte[] SECRET_BYTES = new byte[]{1, 2, 3, 4, 5, 6, 7, 8};

	public static FingerprintDialog newInstance() {

		Bundle args = new Bundle();

		FingerprintDialog fragment = new FingerprintDialog();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mFingerprintManager = FingerprintManagerCompat.from(getContext());
		try {
			mKeyTools = KeyTools.newInstance();
		} catch (KeyTools.KeyToolsException e) {
			e.printStackTrace();
		}
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.welcome_dialog_fingerprint, container, false);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getDialog().setTitle(R.string.welcome_dialog_fingerprint_title);

		authenticate();
	}

	private void authenticate(){
		Cipher cipher;
		cipher = mKeyTools.getUserAuthCipher();

		CryptoObject crypto = new CryptoObject(cipher);
		mCancellationSingal = new CancellationSignal();
		mFingerprintManager.authenticate(crypto, 0, mCancellationSingal, mAuthenticationCallback, null);
	}

	private class AuthenticationCallback extends FingerprintManagerCompat.AuthenticationCallback{

		@Override
		public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
			Cipher cipher = result.getCryptoObject().getCipher();
			try {
				cipher.doFinal(SECRET_BYTES);
			} catch (Exception e) {
				Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
			}

			Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onAuthenticationFailed() {
			super.onAuthenticationFailed();
		}

		@Override
		public void onAuthenticationError(int errMsgId, CharSequence errString) {
			super.onAuthenticationError(errMsgId, errString);
		}
	}
}
