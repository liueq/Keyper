package com.liueq.testdagger.utils;

import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;

import junit.framework.TestCase;
import junit.framework.TestResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by liueq on 27/7/15.
 */
public class EncrypterTest extends AndroidTestCase {


    @Before
    public void setUp(){
    }

    @After
    public void tearDown(){
    }

    @Test
    public void testAESEncryptDecrypt(){
        String test_plaint = "hello world";
        String AES_KEY = "1111111111111111";

        String test_aes = Encrypter.encryptByAes(AES_KEY, test_plaint);

        String test_aes_plaint = Encrypter.decryptByAes(AES_KEY, test_aes);
        assertEquals("hello world", test_aes_plaint);
    }

}
