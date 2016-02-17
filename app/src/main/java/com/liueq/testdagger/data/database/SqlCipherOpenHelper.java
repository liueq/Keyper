package com.liueq.testdagger.data.database;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

/**
 * Created by liueq on 1/2/2016.
 *
 */
public class SQLCipherOpenHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "encrypted3.db";

	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_PASSWORD = "29Jan10:25";

	private static SQLCipherOpenHelper mInstance;

	public static SQLCipherOpenHelper getInstance(Context context){
		if(mInstance == null){
			mInstance = new SQLCipherOpenHelper(context);
		}

		return mInstance;
	}

	public SQLCipherOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String sql = DBTables.Password.SQL_CREATE;
		sqLiteDatabase.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

	}
}
