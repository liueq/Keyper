package com.liueq.testdagger.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.liueq.testdagger.data.database.DBTables;
import com.liueq.testdagger.data.database.SQLCipherOpenHelper;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 24/2/2016.
 * Tag Repository, db implements
 */
public class TagRepoDBImpl implements TagRepo{


	SQLCipherOpenHelper mOpenHelper;

	public TagRepoDBImpl(Context context) {
		mOpenHelper = SQLCipherOpenHelper.getInstance(context);
	}

	@Override
	public List<Tag> getAllTags() {
		List<Tag> tag_list = new ArrayList<Tag>();
		SQLiteDatabase db = null;
		Cursor cursor = null;

		try{
			db = mOpenHelper.getDatabase();
			cursor = db.query(DBTables.Tag.table_name, DBTables.Tag.ALL_COLUMN, null, null, null, null, null);

			while (cursor.moveToNext()){
				Tag tag = new Tag();
				tag.id = String.valueOf(cursor.getInt(0));
				tag.tag_name = cursor.getString(1);
				tag.tag_num = cursor.getString(2);

				tag_list.add(tag);
			}
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}

			db = null;
		}

		return tag_list;
	}

	@Override
	public String insertOrUpdateTag(@NonNull Tag tag) {
		if(TextUtils.isEmpty(tag.id)){
			return insertTag(tag);
		}else{
			return updateTag(tag);
		}
	}

	private String insertTag(Tag tag){
		SQLiteDatabase db = null;
		Cursor cursor = null;
		String insert_id = null;
		ContentValues values = new ContentValues();
		values.put(DBTables.Tag.tag_name, tag.tag_name);
		values.put(DBTables.Tag.tag_num, tag.tag_num);

		try{
			db = mOpenHelper.getDatabase();
			db.beginTransaction();
			{
				db.insertOrThrow(DBTables.Tag.table_name, null, values);

				cursor = db.rawQuery("select last_insert_rowid() from " + DBTables.Tag.table_name, null);
				if(cursor.moveToNext()){
					insert_id = String.valueOf(cursor.getInt(0));
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			if(cursor != null && !cursor.isClosed()){
				cursor.close();
			}

			db = null;
		}

		return insert_id;
	}

	private String updateTag(Tag tag){
		String update_id = null;
		SQLiteDatabase db = null;

		ContentValues values = new ContentValues();
		values.put(DBTables.Tag.tag_name, tag.tag_name);
		values.put(DBTables.Tag.tag_num, tag.tag_num);

		String where = "id = ?";
		String []whereArgs = {tag.id};

		try{
			db = mOpenHelper.getDatabase();
			db.beginTransaction();
			{
				db.update(DBTables.Tag.table_name, values, where, whereArgs);
				update_id = tag.id;
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			db = null;
		}

		return update_id;
	}

	@Override
	public boolean deleteTag(Tag tag) {
		boolean is_successful = false;
		if(TextUtils.isEmpty(tag.id)){
			return is_successful;
		}

		SQLiteDatabase db = null;
		String where = "id = ?";
		String []where_args = {tag.id};

		try{
			db = mOpenHelper.getDatabase();
			db.beginTransaction();
			db.delete(DBTables.Tag.table_name, where, where_args);
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

	@Override
	public List<Tag> searchTag(String tag_name) {
		List<Tag> list = new ArrayList<>();
		Tag tag = null;
		if(TextUtils.isEmpty(tag_name)){
			return new ArrayList<Tag>();
		}

		String selection = DBTables.Tag.tag_name + " LIKE ?";
		String []selection_args = {"%" + tag_name + "%"};

		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = mOpenHelper.getDatabase();
			db.beginTransaction();
			cursor = db.query(DBTables.Tag.table_name, DBTables.Tag.ALL_COLUMN, selection, selection_args, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					tag = new Tag();
					tag.id = String.valueOf(cursor.getInt(0));
					tag.tag_name = cursor.getString(1);
					tag.tag_num = cursor.getString(2);

					list.add(tag);
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
}
