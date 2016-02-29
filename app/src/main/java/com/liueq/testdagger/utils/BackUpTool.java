package com.liueq.testdagger.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepoDBImpl;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

	public static void exportDB(Context context){
		String db_path = context.getFilesDir().getAbsolutePath() + "/databases" + "/encrypted3.db";
		String output = Environment.getExternalStorageDirectory() + "/encrypted3.db";
		File file = new File(db_path);
		File file_out = new File(output);
		if(file.exists()){
			try {
				FileInputStream fis = new FileInputStream(file);
				FileOutputStream fos = new FileOutputStream(file_out);

				byte [] tmp = new byte[1024];

				while(fis.read(tmp) > 0){
					fos.write(tmp);
				}

				fos.flush();
				fis.close();
				fos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
