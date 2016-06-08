package com.liueq.keyper.domain.interactor;

import com.liueq.keyper.data.repository.SharedPreferenceRepoImpl;

import javax.inject.Inject;

/**
 * Created by liueq on 29/7/15.
 * 所有设置SharedPreference 的操作 (合并到GetSpUC中)
 */
public class SetSpUC {

    private SharedPreferenceRepoImpl impl;

    @Inject
    public SetSpUC(SharedPreferenceRepoImpl impl){
        this.impl = impl;
    }
}
