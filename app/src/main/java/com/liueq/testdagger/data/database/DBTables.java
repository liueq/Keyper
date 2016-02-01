package com.liueq.testdagger.data.database;

/**
 * Created by liueq on 1/2/2016.
 * All the tables in database
 */
public class DBTables {

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
}
