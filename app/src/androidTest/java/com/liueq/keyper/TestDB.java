package com.liueq.keyper;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.repository.AccountRepo;
import com.liueq.keyper.data.repository.AccountRepoDBImpl;

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
			AccountRepo ar = new AccountRepoDBImpl(getContext(), null);
		}catch(Exception e){
			flag = false;
		}finally {
			Assert.assertTrue(flag);
		}
	}

	@Test
	public void testGetAccountList(){
		AccountRepo ar = new AccountRepoDBImpl(getContext(), null);
		List<Account> list = ar.getAccountList();
		if(list != null){
			Assert.assertTrue(false);
		}
	}

	@Test
	public void testInsertDB(){
		AccountRepoDBImpl ardb = new AccountRepoDBImpl(getContext(), null);

		Account account = new Account();
		account.site = "site1";
		account.mail = "mail1";
		account.username = "username1";
		account.description = "description1";
		account.password = "password1";
//		boolean result = ardb.insertAccount(account);
//		Assert.assertTrue(result);
	}
}
