package com.liueq.testdagger.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;

import com.liueq.testdagger.data.database.DBTables.Star;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.List;

/**
 * Created by liueq on 19/2/2016.
 * StarRepository 的DB实现
 */
public class StarRepoDBImpl implements StarRepo{

	SQLCipherOpenHelper mDBHelper;

	public StarRepoDBImpl(Context context) {
		mDBHelper = SQLCipherOpenHelper.getInstance(context);
	}

	@Override
	public boolean starAccount(@NonNull Account account) {
		boolean result = false;
		SQLiteDatabase db = mDBHelper.getDatabase();
		ContentValues valuse = new ContentValues();
		valuse.put(Star.type, Star.TYPE_ACCOUNT);
		valuse.put(Star.link_id, account.id);

		try {
			db.beginTransaction();
			{
				db.insertOrThrow(Star.table_name, null, valuse);
			}
			db.setTransactionSuccessful();
			result = true;
			db.endTransaction();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			db = null;
		}

		return result;
	}

	@Override
	public boolean unStarAccount(@NonNull Account account) {
		boolean result = false;
		SQLiteDatabase db = mDBHelper.getDatabase();
		String where = "id = ?";
		String []whereArgs = {account.id};

		try {
			db.beginTransaction();
			{
				db.delete(Star.table_name, where, whereArgs);
			}
			db.setTransactionSuccessful();
			result = true;
			db.endTransaction();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			db = null;
		}
		return result;
	}

	@Override
	public Account getStarStatus(Account account) {
		boolean stared = false;
		SQLiteDatabase db = mDBHelper.getDatabase();
		String selection = Star.link_id + " = ?";
		String [] selectionArgs = {account.id};

		Cursor cursor = db.query(Star.table_name, Star.ALL_COLUMN, selection, selectionArgs, null, null, null);
		if(cursor.moveToNext()){
			cursor.getInt(0);
			String type = cursor.getString(1);

			if(type.equals(Star.TYPE_ACCOUNT)){
				stared = true;
			}
		}

		account.is_stared = stared;
		return account;
	}

	@Override
	public List<Account> getStarStatusList(List<Account> list) {
		for(Account account : list){
			getStarStatus(account);
		}
		return list;
	}
}
