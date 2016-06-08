package com.liueq.keyper.domain.interactor;

import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;

import javax.inject.Inject;

/**
 * Created by liueq on 30/7/15.
 * Check password UseCase(No use)
 */
public class CheckPasswordUC extends UseCase {

    public final static String TAG = "CPUC";

    private SharedPreferenceRepoImpl impl;

    @Inject
    public CheckPasswordUC(SharedPreferenceRepoImpl impl){
        this.impl = impl;
    }

}
