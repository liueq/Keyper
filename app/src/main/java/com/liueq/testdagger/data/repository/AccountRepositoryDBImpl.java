package com.liueq.testdagger.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;

import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import com.liueq.testdagger.data.database.DBTables.Password;

/**
 * Created by liueq on 1/2/2016.
 * AccountRepository of Database
 */
public class AccountRepositoryDBImpl implements AccountRepository{

	public final static String TAG = "DBARI";

	List<Account> mAccountList;
	List<Account> mFilteredList;
	GetSpUseCase mGetSpUseCase;

	SQLCipherOpenHelper mDBHelper;

	public AccountRepositoryDBImpl(Context context, GetSpUseCase getSpUseCase) {
		mGetSpUseCase = getSpUseCase;
		mDBHelper = new SQLCipherOpenHelper(context);
	}

	@Override
	public List<Account> getAccountList() {
		List<Account> list = new ArrayList<>();
		Account account = null;

		SQLiteDatabase db = mDBHelper.getReadableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		Cursor cursor = db.query(Password.table_name, Password.ALL_COLUMN, null, null, null, null, null);
		if (cursor != null){
			while (cursor.moveToNext()){
				account = new Account();
				account.id = String.valueOf(cursor.getInt(0));
				account.site = cursor.getString(1);
				account.username = cursor.getString(2);
				account.password = cursor.getString(3);
				account.mail = cursor.getString(4);
				account.description = cursor.getString(5);

				list.add(account);
			}
		}
		return list;
	}

	@Override
	public Account getAccountDetail(String accountId) {
		Account account = null;

		String selection = "id = '?'";
		String []selection_args = {accountId};

		SQLiteDatabase db = mDBHelper.getReadableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		Cursor cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, null);
		if(cursor != null){
			if(cursor.moveToNext()){
				account = new Account();
				account.id = String.valueOf(cursor.getInt(0));
				account.site = cursor.getString(1);
				account.username = cursor.getString(2);
				account.password = cursor.getString(3);
				account.mail = cursor.getString(4);
				account.description = cursor.getString(5);
			}
		}
		return account;
	}

	@Override
	public List<Account> searchAccount(String key) {
		List<Account> list = new ArrayList<>();
		Account account = null;

		String selection = "site LIKE '%?%'";
		String []selection_args = {Password.site};

		SQLiteDatabase db = mDBHelper.getReadableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		Cursor cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, null);
		if(cursor != null){
			while (cursor.moveToNext()){
				account = new Account();
				account.id = String.valueOf(cursor.getInt(0));
				account.site = cursor.getString(1);
				account.username = cursor.getString(2);
				account.password = cursor.getString(3);
				account.mail = cursor.getString(4);
				account.description = cursor.getString(5);

				list.add(account);
			}
		}

		return list;
	}

	@Override
	public void saveAccountList(List<Account> list) {}

	public boolean insertAccount(Account account){
		boolean is_successful = false;
		if(TextUtils.isEmpty(account.site)){
			return is_successful;
		}

		ContentValues values = new ContentValues();
		values.put(Password.site, account.site);
		values.put(Password.username, account.username);
		values.put(Password.password, account.password);
		values.put(Password.email, account.mail);
		values.put(Password.description, account.description);

		SQLiteDatabase db = mDBHelper.getWritableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		try{
			db.beginTransaction();
			db.insertOrThrow(Password.table_name, null, values);
			db.setTransactionSuccessful();

			is_successful = true;
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally {
			db.endTransaction();
		}

		return is_successful;
	}

	public boolean updateAccount(Account old_item, Account update_item){
		boolean is_successful = false;
		if(TextUtils.isEmpty(old_item.id) || !old_item.id.equals(update_item.id)){
			return is_successful;
		}

		SQLiteDatabase db = mDBHelper.getWritableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		ContentValues values = new ContentValues();
		values.put(Password.site, update_item.site);
		values.put(Password.username, update_item.username);
		values.put(Password.password, update_item.password);
		values.put(Password.email, update_item.mail);
		values.put(Password.description, update_item.description);
		String where = "id = '?'";
		String []where_args = {old_item.id};

		try{
			db.beginTransaction();
			db.update(Password.table_name, values, where, where_args);
			db.setTransactionSuccessful();

			is_successful = true;
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally{
			db.endTransaction();
		}

		return is_successful;
	}

	public boolean deleteAccount(Account delete_item){
		boolean is_successful = false;
		if(TextUtils.isEmpty(delete_item.id)){
			return is_successful;
		}

		SQLiteDatabase db = mDBHelper.getWritableDatabase(SQLCipherOpenHelper.DATABASE_PASSWORD);
		String where = "id = '?'";
		String []where_args = {delete_item.id};
		try{
			db.beginTransaction();
			db.delete(Password.table_name, where, where_args);
			db.setTransactionSuccessful();

			is_successful = true;
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally {
			db.endTransaction();
		}

		return is_successful;
	}
}
