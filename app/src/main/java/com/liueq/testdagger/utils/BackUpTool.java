package com.liueq.testdagger.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 17/2/2016.
 */
public class BackUpTool {

	public static boolean importDB(Context context){
		String file = Environment.getExternalStorageDirectory() + "/encrypted3.db";
		SQLiteDatabase database = SQLiteDatabase.openDatabase(file, "29Jan10:25", null, 0);
		List<Account> accounts = new ArrayList<>();
		if(database != null){
			Cursor cursor = database.query("password", DBTables.Password.ALL_COLUMN, null, null, null, null, null);
			while(cursor.moveToNext()){
				Account account = new Account();
				account.id = String.valueOf(cursor.getInt(0));
				account.site = cursor.getString(1);
				account.username = cursor.getString(2);
				account.password = cursor.getString(3);
				account.mail = cursor.getString(4);
				account.description = cursor.getString(5);

				accounts.add(account);
			}
		}else{
			Log.e("liueq", "importDB: database is null");
		}

		database.close();

		AccountRepoDBImpl ARI = new AccountRepoDBImpl(context, null);
		for(Account a : accounts){
			ARI.insertAccount(a);
		}

		return true;
	}
}
