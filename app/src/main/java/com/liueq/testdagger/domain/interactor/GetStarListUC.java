package com.liueq.testdagger.domain.interactor;

import com.liueq.testdagger.Constants;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepo;
import com.liueq.testdagger.data.repository.StarRepo;
import com.liueq.testdagger.utils.Encrypter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 27/7/15.
 * 获取AccountList的UseCase
 *      包括从AccountRepo, StarRepo 获取数据
 */
public class GetStarListUC extends UseCase {

	AccountRepo mAR;
	StarRepo mSR;

	GetSpUC mGetSpUC;

	public final static String TAG = "GetALUS";

	@Inject
	public GetStarListUC(AccountRepo AR, StarRepo SR, GetSpUC getSpUC){
		this.mAR = AR;
		this.mSR = SR;
		this.mGetSpUC = getSpUC;
	}

	public List<Account> executeDB(){
		List<Account> list = mAR.getAccountList();
		list = mSR.getStarStatusList(list);

		List<Account> star_list = new ArrayList<Account>();
		for(Account a : list){
			if(a.is_stared){
				star_list.add(a);
			}
		}
		return star_list;
	}

}
