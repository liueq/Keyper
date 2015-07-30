package com.liueq.testdagger;

import android.os.Environment;

/**
 * Created by liueq on 14/7/15.
 */
public class Constants {
    public final static String STORAGE_FILE = "password.json";

    public final static String AES_KEY = "MGMCAQACEQDAP3HO";

    public final static String SP_NAME = "properties";

    public final static String SP_PWD = "sp_password";

    public final static String SP_AES = "sp_aes";

    public final static String STORAGE_PATH = "sp_path";

    public final static String SP_IS_PWD_ENC = "is_pwd_enc";

    public final static String SP_IS_DESC_ENC = "is_desc_enc";

    public final static String YES = "YES";

    public final static String NO = "NO";

    public final static String INTERNAL_STORAGE_PATH = Environment.getDataDirectory().toString() + "/" + STORAGE_FILE;

    public final static String EXTERNAL_STORAGE_PATH = Environment.getExternalStorageDirectory().toString() + "/IPassword/" + STORAGE_FILE;

    public final static String SP_IS_SAVE_INTERNAL = "is_save_internal";

    public final static String SP_IS_SAVE_EXTERNAL = "is_save_external";

}
