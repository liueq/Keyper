package com.liueq.keyper;

/**
 * Created by liueq on 14/7/15.
 * Constant Values
 */
public class Constants {

    public final static String DEFAULT_TAG = "LIUEQ";

    public final static String STORAGE_FILE = "password.json";

    public final static String AES_KEY = "MGMCAQACEQDAP3HO";

    public final static String SP_NAME = "properties";

    /*sp properties*/
    public final static String SP_PWD = "sp_password";

    public final static String SP_DB_PWD = "sp_db_password";

    public final static String SP_HIDE_TIME = "sp_hide_time";

    public final static String SP_SHOW_TIME = "sp_show_time";

    public final static String SP_AUTO_LOCK_PERIOD = "sp_auto_lock_period";

    public final static String SP_FINGERPRINT = "sp_fingerprint";

    public final static String SP_FINGERPRINT_PASSWORD = "sp_fingerprint_pass";//To use fingerprint, must save DB password to local filesystem

}
