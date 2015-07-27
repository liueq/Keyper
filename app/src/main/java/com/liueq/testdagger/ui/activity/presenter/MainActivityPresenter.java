package com.liueq.testdagger.ui.activity.presenter;

import android.os.Debug;
import android.os.Environment;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.liueq.testdagger.BuildConfig;
import com.liueq.testdagger.Constants;
import com.liueq.testdagger.activity.MainActivity;
import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.repository.AccountRepositoryImpl;
import com.liueq.testdagger.domain.interactor.GetAccountDetailUseCase;
import com.liueq.testdagger.domain.interactor.GetAccountListUseCase;
import com.liueq.testdagger.domain.interactor.SearchAccountUseCase;
import com.liueq.testdagger.utils.Encrypter;
import com.liueq.testdagger.utils.FileReader;
import com.liueq.testdagger.utils.JsonParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.inject.Inject;

/**
 * Created by liueq on 13/7/15.
 */
public class MainActivityPresenter {

    public final static String TAG = "MainActivityPresenter";

    private MainActivity mainActivity;
    List<Account> mAccountList;
    GetAccountListUseCase getAccountListUseCase;
    SearchAccountUseCase searchAccountUseCase;

    public MainActivityPresenter(MainActivity mainActivity, List<Account> list, GetAccountListUseCase getAccountListUseCase, SearchAccountUseCase searchAccountUseCase) {
        this.mainActivity = mainActivity;
        this.mAccountList = list;
        this.getAccountListUseCase = getAccountListUseCase;
        this.searchAccountUseCase = searchAccountUseCase;
    }


    public void loadData(){
        mainActivity.updateUI((List<Account>) getAccountListUseCase.execute());
    }

    public void search(String searchKey){
        mainActivity.updateUI(searchAccountUseCase.execute(searchKey) );
    }

}
