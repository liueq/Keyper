package com.liueq.testdagger.data.model;

import net.sqlcipher.Cursor;

/**
 * Created by liueq on 24/2/2016.
 * Tag obj
 */
public class Tag implements Comparable<Tag>{

	public String id;
	public String tag_name;
	public String tag_num = "1";

	public Tag(){}

	public Tag(String id){
		this.id = id;
	}

	public Tag(String name, String num){
		this.tag_name = name;
		this.tag_num = num;
	}

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

	@Override
	public int compareTo(Tag another) {
		if(this.tag_name.compareTo(another.tag_name) == 0){
			return this.id.compareTo(another.id);
		}else{
			return this.tag_name.compareTo(another.tag_name);
		}
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Tag){
			Tag another = (Tag) o;
			if (this.id.equals(another.id) && this.tag_name.equals(another.tag_name)) {
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}