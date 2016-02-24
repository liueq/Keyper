package com.liueq.testdagger.data.model;

import net.sqlcipher.Cursor;

/**
 * Created by liueq on 24/2/2016.
 * Tag obj
 */
public class Tag {

	public String id;
	public String tag_name;
	public String tag_num;

	public Tag(){}

	public Tag(Cursor cursor){
		id = String.valueOf(cursor.getInt(0));
		tag_name = cursor.getString(1);
		tag_num = cursor.getString(2);
	}

	@Override
	public String toString() {
		return "Tag{" +
				"id='" + id + '\'' +
				", tag_name='" + tag_name + '\'' +
				", tag_num='" + tag_num + '\'' +
				'}';
	}
}