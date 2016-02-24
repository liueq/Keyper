package com.liueq.testdagger.data.database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by liueq on 1/2/2016.
 * SQLCipher Open Helper
 */
public class SQLCipherOpenHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "encrypted3.db";

	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_PASSWORD = "29Jan10:25";

	private static SQLCipherOpenHelper mInstance;

	private static SQLiteDatabase mDatabase;

	public static SQLCipherOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new SQLCipherOpenHelper(context);
			mDatabase = mInstance.getWritableDatabase(DATABASE_PASSWORD);
		}

		return mInstance;
	}

	public SQLCipherOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.beginTransaction();

		sqLiteDatabase.execSQL(DBTables.Password.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Free.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Star.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.Tag.SQL_CREATE);
		sqLiteDatabase.execSQL(DBTables.TagAndPassword.SQL_CREATE);

		sqLiteDatabase.setTransactionSuccessful();
		sqLiteDatabase.endTransaction();
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}

	public SQLiteDatabase getDatabase(){
		return mDatabase;
	}

	public void closeDatabase(){
		if(mDatabase != null && mDatabase.isOpen()){
			mDatabase.close();
			mInstance = null;
		}
	}
}
