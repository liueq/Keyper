package com.liueq.keyper.utils;

import android.security.keystore.KeyGenParameterSpec;

/**
 * Created by liueq on 4/5/2016.
 * KeySpecGenerator
 */
public interface KeySpecGenerator {

	KeyGenParameterSpec generate(String keyName);
}
