package com.liueq.testdagger.data.model;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by liueq on 13/7/15.
 */
public class Account implements Serializable {

    public String id;
    public String site;
    public String userName;
    public String password;
    public String mail;
    public String description;

    public Account(){
        Random random = new Random();
        this.id = String.valueOf(System.currentTimeMillis() + random.nextInt(1000));
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                "site='" + site + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}