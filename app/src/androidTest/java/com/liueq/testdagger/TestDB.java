package com.liueq.testdagger;

import android.app.Application;
import android.content.Context;
import android.test.ApplicationTestCase;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryDBImpl;

import net.sqlcipher.database.SQLiteDatabase;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by liueq on 16/2/2016.
 */
public class TestDB extends ApplicationTestCase<Application> {

	public TestDB(){
		super(Application.class);
	}

	@Test
	public void testInit(){
		boolean flag = true;
		try{
			AccountRepository ar = new AccountRepositoryDBImpl(getContext(), null);
		}catch(Exception e){
			flag = false;
		}finally {
			Assert.assertTrue(flag);
		}
	}

	@Test
	public void testGetAccountList(){
		AccountRepository ar = new AccountRepositoryDBImpl(getContext(), null);
		List<Account> list = ar.getAccountList();
		if(list != null){
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testInsertDB(){
		AccountRepositoryDBImpl ardb = new AccountRepositoryDBImpl(getContext(), null);

		Account account = new Account();
		account.site = "site1";
		account.mail = "mail1";
		account.username = "username1";
		account.description = "description1";
		account.password = "password1";
		boolean result = ardb.insertAccount(account);
		Assert.assertTrue(result);
	}
}
