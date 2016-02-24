package com.liueq.testdagger.data.model;

/**
 * Created by liueq on 24/2/2016.
 * Tag obj
 */
public class Tag {

	public String id;
	public String tag_name;
	public String tag_num;

	@Override
	public String toString() {
		return "Tag{" +
				"id='" + id + '\'' +
				", tag_name='" + tag_name + '\'' +
				", tag_num='" + tag_num + '\'' +
				'}';
	}
}