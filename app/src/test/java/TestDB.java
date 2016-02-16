import android.content.Context;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepository;
import com.liueq.testdagger.data.repository.AccountRepositoryDBImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

/**
 * Created by liueq on 16/2/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestDB {

	@Mock
	Context mMockContext;

	@Test
	public void testInit(){
		boolean flag = true;
		try{
			AccountRepository ar = new AccountRepositoryDBImpl(mMockContext, null);
		}catch(Exception e){
			flag = false;
		}finally {
			Assert.assertTrue(flag);
		}
	}

	@Test
	public void testGetAccountList(){
		AccountRepository ar = new AccountRepositoryDBImpl(mMockContext, null);
		List<Account> list = ar.getAccountList();
		if(list == null || list.size() == 0){
			Assert.assertTrue(false);
		}
	}
}
