package com.liueq.keyper.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.liueq.keyper.data.database.DBTables.Password;
import com.liueq.keyper.data.database.SQLCipherOpenHelper;
import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.domain.interactor.SharedPUC;
import com.liueq.keyper.utils.GoldenHammer;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by liueq on 1/2/2016.
 * AccountRepository of Database
 */
public class AccountRepoDBImpl implements AccountRepo {

	public final static String TAG = "DBARI";
	SharedPUC mSharedPUC;

	SQLCipherOpenHelper mDBHelper;

	public AccountRepoDBImpl(Context context, SharedPUC sharedPUC) {
		mSharedPUC = sharedPUC;
		mDBHelper = SQLCipherOpenHelper.getInstance(context);
	}

	@Override
	public List<Account> getAccountList() {
		List<Account> list = new ArrayList<>();
		Account account = null;
		String orderBy = Password.site;
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, null, null, null, null, orderBy);
			if (cursor != null){
				while (cursor.moveToNext()){
					account = new Account(cursor);
					list.add(account);
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch (SQLException e){
			e.printStackTrace();
		}finally{
			db = null;
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}
		}

		Collections.sort(list);
		return list;
	}

	@Override
	public Account getAccountDetail(@Nullable String accountId) {
		Account account = null;
		if (TextUtils.isEmpty(accountId)) {
			return null;
		}

		String selection = "id = ?";
		String []selection_args = {accountId};
		String orderBy = Password.site;

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, orderBy);
			if (cursor != null) {
				if (cursor.moveToNext()) {
					account = new Account(cursor);
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
	public synchronized List<Account> searchAccount(@Nullable String key){
		return searchAccountByField(key, null);
	}

	@Override
	public synchronized List<Account> searchAccountByField(@Nullable String key, @Nullable String field) {
		List<Account> list = new ArrayList<>();
		Account account = null;
		if(TextUtils.isEmpty(key)){
			return new ArrayList<Account>();
		}

		if(TextUtils.isEmpty(field) || !Arrays.asList(Password.ALL_COLUMN).contains(field)){
			//When field is null or field is invalid
			field = "site";
		}

		String selection = field + " LIKE ?";
		String []selection_args = {"%" + key + "%"};
		String orderBy = Password.site;

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(Password.table_name, Password.ALL_COLUMN, selection, selection_args, null, null, orderBy);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					account = new Account(cursor);
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

		Collections.sort(list);
		return list;
	}

	@Override
	public void saveAccountList(List<Account> list) {}

	@Override
	public String insertOrUpdateAccount(@Nullable String id, Account account) {
		if(TextUtils.isEmpty(id)){
			//insert
			return insertAccount(account);
		}else{
			//update
			return updateAccount(id, account);
		}
	}

	public String insertAccount(Account account){
		if(TextUtils.isEmpty(account.site)){
			return null;
		}

		ContentValues values = new ContentValues();
		values.put(Password.site, account.site);
		values.put(Password.username, account.username);
		values.put(Password.password, account.password);
		values.put(Password.email, account.mail);
		values.put(Password.description, account.description);
		values.put(Password.create_time, GoldenHammer.getTimestamp());
		values.put(Password.update_time, GoldenHammer.getTimestamp());

		SQLiteDatabase db = null;
		String id = null;
		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			db.insertOrThrow(Password.table_name, null, values);
			db.setTransactionSuccessful();

			Cursor cursor = db.rawQuery("select last_insert_rowid() from " + Password.table_name, null);
			if(cursor.moveToNext()){
				id = String.valueOf(cursor.getInt(0));
			}
			if(!cursor.isClosed()){
				cursor.close();
			}

			db.endTransaction();
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally {
			db = null;
		}

		return id;
	}

	public String updateAccount(@NonNull String old_id, Account update_item){
		String return_id = null;
		if(TextUtils.isEmpty(old_id) || !old_id.equals(update_item.id)){
			return return_id;
		}

		SQLiteDatabase db = null;
		ContentValues values = new ContentValues();
		values.put(Password.site, update_item.site);
		values.put(Password.username, update_item.username);
		values.put(Password.password, update_item.password);
		values.put(Password.email, update_item.mail);
		values.put(Password.description, update_item.description);
		values.put(Password.create_time, update_item.create_time);
		values.put(Password.update_time, GoldenHammer.getTimestamp());

		String where = "id = ?";
		String []where_args = {old_id};

		try{
			db = mDBHelper.getDatabase();
			db.beginTransaction();
			db.update(Password.table_name, values, where, where_args);
			db.setTransactionSuccessful();

			return_id = old_id;
			db.endTransaction();
		}catch (SQLException exception){
			exception.printStackTrace();
		}finally{
			db = null;
		}

		return return_id;
	}

	@Override
	public boolean deleteAccount(@NonNull String id){
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
