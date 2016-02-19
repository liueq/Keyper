package com.liueq.testdagger.data.database;

/**
 * Created by liueq on 1/2/2016.
 * All the tables in database
 */
public class DBTables {

	/**
	 * 固定格式的Password table
	 */
	public static class Password{

		public static final String table_name = "password";

		public static final String id = "id";

		public static final String site = "site";

		public static final String username = "username";

		public static final String password = "password";

		public static final String email = "email";

		public static final String description = "description";

		public static String SQL_CREATE = "CREATE TABLE " + table_name + " ( " + id + " INTEGER NOT NULL PRIMARY KEY, " +
				site + " TEXT NOT NULL, " +
				username + " TEXT, " +
				password + " TEXT, " +
				email + " TEXT, " +
				description + " TEXT)";

		public static String [] ALL_COLUMN = {id, site, username, password, email, description};
	}

	/**
	 * 标星 table
	 */
	public static class Star{

		public static final String table_name = "star";

		public static final String id = "id";

		public static final String type = "type";

		public static final String link_id = "link_id"; //Foreign key

		public static String SQL_CREATE = "CREATE TABLE " + table_name + " (" + id + " INTEGER NOT NULL PRIMARY KEY, " +
				type + " TEXT NOT NULL, " +
				link_id + " INTEGER NOT NULL) ";
//				"FOREIGN KEY ( " + link_id + " ) REFERENCES " + Password.table_name + " ( " + Password.id + " ))"; //似乎不能用 Foreign key, 因为link_id 可能是对应的两个表的id

		public static String [] ALL_COLUMN = {id, type, link_id};

	}

	/**
	 * 自定义Fields table
	 * BLOB 存放的是HashMap
	 */
	public static class Free{

		public static final String table_name = "free";

		public static final String id = "id";

		public static final String data = "data";

		public static String SQL_CREATE = "CREATE TABLE " + table_name + " (" + id + " INTEGER NOT NULL PRIMARY KEY, " +
				data + " BLOB NOT NULL )";

		public static String [] ALL_COLUMN = {id, data};
	}
}
