package com.liueq.testdagger.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.database.DBTables.Password;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.domain.interactor.GetSpUseCase;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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
		mDBHelper = SQLCipherOpenHelper.getInstance(context);
	}

	@Override
	public List<Account> getAccountList() {
		List<Account> list = new ArrayList<>();
		Account account = null;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, null, null, null, null, null);
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
			db.setTransactionSuccessful();
			db.endTransaction();
		}finally{
			db = null;
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return list;
	}

	@Override
	public Account getAccountDetail(String accountId) {
		Account account = null;

		String selection = "id = ?";
		String []selection_args = {accountId};

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, null);
			if (cursor != null) {
				if (cursor.moveToNext()) {
					account = new Account();
					account.id = String.valueOf(cursor.getInt(0));
					account.site = cursor.getString(1);
					account.username = cursor.getString(2);
					account.password = cursor.getString(3);
					account.mail = cursor.getString(4);
					account.description = cursor.getString(5);
				}
			}

			db.setTransactionSuccessful();
			db.endTransaction();
		}finally {
			db = null;
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}
		return account;
	}

	@Override
	public synchronized List<Account> searchAccount(String key) {
		List<Account> list = new ArrayList<>();
		Account account = null;

		String selection = "site LIKE ?";
		String []selection_args = {"%" + key + "%"};

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
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

			db.setTransactionSuccessful();
			db.endTransaction();
		}finally{
			db = null;
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}

		return list;
	}

	@Override
	public void saveAccountList(List<Account> list) {}

	@Override
	public boolean insertOrUpdateAccount(@Nullable String id, Account account) {
		if(TextUtils.isEmpty(id)){
			//insert
			return insertAccount(account);
		}else{
			//update
			return updateAccount(id, account);
		}
	}

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

		SQLiteDatabase db = null;
		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			db.insertOrThrow(Password.table_name, null, values);
			db.setTransactionSuccessful();

			is_successful = true;
			db.endTransaction();
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally {
			db = null;
		}

		return is_successful;
	}

	public boolean updateAccount(String old_id, Account update_item){
		boolean is_successful = false;
		if(TextUtils.isEmpty(old_id) || !old_id.equals(update_item.id)){
			return is_successful;
		}

		SQLiteDatabase db = null;
		ContentValues values = new ContentValues();
		values.put(Password.site, update_item.site);
		values.put(Password.username, update_item.username);
		values.put(Password.password, update_item.password);
		values.put(Password.email, update_item.mail);
		values.put(Password.description, update_item.description);
		String where = "id = ?";
		String []where_args = {old_id};

		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			db.update(Password.table_name, values, where, where_args);
			db.setTransactionSuccessful();

			db.endTransaction();
			is_successful = true;
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally{
			db = null;
		}

		return is_successful;
	}

	@Override
	public boolean deleteAccount(String id){
		boolean is_successful = false;
		if(TextUtils.isEmpty(id)){
			return is_successful;
		}

		SQLiteDatabase db = null;
		String where = "id = ?";
		String []where_args = {id};

		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			db.delete(Password.table_name, where, where_args);
			db.setTransactionSuccessful();

			db.endTransaction();
			is_successful = true;
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally {
			db = null;
		}

		return is_successful;
	}
}
