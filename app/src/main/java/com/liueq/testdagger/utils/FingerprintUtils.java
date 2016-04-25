package com.liueq.testdagger.utils;

import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.liueq.testdagger.R;
import com.liueq.testdagger.TestApplication;

/**
 * Created by liueq on 25/4/2016.
 * FingerprintUtils
 */
public class FingerprintUtils {

	public boolean hasSensor(){
		FingerprintManagerCompat manager = FingerprintManagerCompat.from(TestApplication.getApplication());
		return manager.isHardwareDetected();
	}

	public boolean hasEnrolled(){
		FingerprintManagerCompat manager = FingerprintManagerCompat.from(TestApplication.getApplication());
		return manager.hasEnrolledFingerprints();
	}

	public static String canUseFingerprint(){
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
			return TestApplication.getApplication().getString(R.string.fingerprint_version_too_low);
		}
		FingerprintManagerCompat manager = FingerprintManagerCompat.from(TestApplication.getApplication());
		if(!manager.isHardwareDetected()){
			return TestApplication.getApplication().getString(R.string.fingerprint_no_sensor);
		}else if(!manager.hasEnrolledFingerprints()){
			return TestApplication.getApplication().getString(R.string.fingerprint_no_enroll);
		}else {
			return "";
		}
	}
}
