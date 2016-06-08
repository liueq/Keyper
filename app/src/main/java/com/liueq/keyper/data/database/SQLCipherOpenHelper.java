package com.liueq.keyper.data.database;

import android.content.Context;
import android.text.TextUtils;

import com.liueq.keyper.TestApplication;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by liueq on 1/2/2016.
 * SQLCipher Open Helper
 * 是否需要再封装一层??
 */
public class SQLCipherOpenHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "encrypted3.db";

	public static final int DATABASE_VERSION = 2;

	public static String INITITAL_DATABASE_PASSWORD = "29Jan10:25";

	private static SQLCipherOpenHelper mInstance;

	private static SQLiteDatabase mDatabase;

	public static SQLCipherOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new SQLCipherOpenHelper(context);
			if(TextUtils.isEmpty(getPassword())){
				mDatabase = mInstance.getWritableDatabase(INITITAL_DATABASE_PASSWORD);
			}else{
				mDatabase = mInstance.getWritableDatabase(getPassword());
			}
		}

		return mInstance;
	}

	public SQLCipherOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.beginTransaction();

		sqLiteDatabase.rawExecSQL("PRAGMA key=\"" + getPassword() + "\";");
		sqLiteDatabase.execSQL(DBTables.Password.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Free.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Star.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Tag.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.TagAndPassword.SQL_CREATE);

		sqLiteDatabase.setTransactionSuccessful();
		sqLiteDatabase.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_ver, int new_ver) {
		if(old_ver == 1 && new_ver == 2) {
			sqLiteDatabase.execSQL(DBTables.Tag.SQL_CREATE);
			sqLiteDatabase.execSQL(DBTables.TagAndPassword.SQL_CREATE);
		}
	}

	public SQLiteDatabase getDatabase(){
		if(mDatabase == null || !mDatabase.isOpen()) {
			mDatabase = mInstance.getWritableDatabase(getPassword());
		}
		return mDatabase;
	}

	public void closeDatabase(){
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.close();
			mInstance = null;
		}
	}

	/**
	 * Change DB password
	 * @param password
	 */
	public boolean setPassword(String password){
		boolean return_flag = false;
		getDatabase().beginTransaction();
		{
			getDatabase().rawExecSQL("pragma rekey=\"" + password + "\";");
		}
		return_flag = true;
		TestApplication.setDBPassword(password);
		getDatabase().setTransactionSuccessful();
		getDatabase().endTransaction();
		return return_flag;
	}

	/**
	 * Get DB password
	 */
	private static String getPassword(){
		return TestApplication.getDBPassword();
	}
}
