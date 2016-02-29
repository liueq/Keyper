package com.liueq.testdagger.data.model;

import net.sqlcipher.Cursor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 13/7/15.
 * Account obj
 */
public class Account implements Serializable, Comparable<Account>, Cloneable {

    public String id;
    public String site;
    public String username;
    public String password;
    public String mail;
    public String description;

    public boolean is_stared;

    public List<Tag> tag_list = new ArrayList<Tag>();

    public Account(){
//        Random random = new Random();
//        this.id = String.valueOf(System.currentTimeMillis() + random.nextInt(1000));
    }

    public Account(Cursor cursor){
        id = String.valueOf(cursor.getInt(0));
        site = cursor.getString(1);
        username = cursor.getString(2);
        password = cursor.getString(3);
        mail = cursor.getString(4);
        description = cursor.getString(5);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id='" + id + '\'' +
                ", site='" + site + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mail='" + mail + '\'' +
                ", description='" + description + '\'' +
                ", is_stared=" + is_stared +
                '}';
    }

    @Override
    public int compareTo(Account another) {
        if(this.site.compareTo(another.site) == 0){
            return this.id.compareTo(another.id);
        }else{
            return this.site.compareTo(another.site);
        }
    }

    @Override
    public Account clone() throws CloneNotSupportedException {
        super.clone();
        Account a = new Account();
        a.id = this.id;
        a.site = this.site;
        a.username = this.username;
        a.password = this.password;
        a.mail = this.mail;
        a.description = this.description;
        a.is_stared = this.is_stared;
        return a;
    }
}
