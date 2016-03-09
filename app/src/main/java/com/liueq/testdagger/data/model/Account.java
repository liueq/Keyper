package com.liueq.testdagger.data.model;

import android.text.TextUtils;

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
    public String create_time;
    public String update_time;

    public boolean is_stared;

    public List<Tag> tag_list = new ArrayList<Tag>();

    public Account(){}

    public Account(Cursor cursor){
        id = String.valueOf(cursor.getInt(0));
        site = cursor.getString(1);
        username = cursor.getString(2);
        password = cursor.getString(3);
        mail = cursor.getString(4);
        description = cursor.getString(5);
        create_time = cursor.getString(6);
        update_time = cursor.getString(7);
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
                ", create_time='" + create_time + '\'' +
                ", update_time='" + update_time + '\'' +
                ", is_stared=" + is_stared +
                ", tag_list=" + tag_list +
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

        a.tag_list = new ArrayList<Tag>();
        for(Tag t : this.tag_list){
            a.tag_list.add(t);
        }
        return a;
    }

	/**
     * Compare all fields
     * @param another
     * @return
     */
    public boolean equalAllFields(Account another){
        if(!stringEqual(this.id, another.id)){
            return false;
        }

        if(!stringEqual(this.site, another.site)){
            return false;
        }

        if(!stringEqual(this.username, another.username)){
            return false;
        }
        if(!stringEqual(this.password, another.password)){
            return false;
        }
        if(!stringEqual(this.mail, another.mail)){
            return false;
        }
        if(!stringEqual(this.description, another.description)){
            return false;
        }

        if(this.tag_list == null && another.tag_list == null){
            //OK
        }else if(this.tag_list != null && another.tag_list != null){
            if(this.tag_list.size() != another.tag_list.size()){
                return false;
            }else{
                if(!this.tag_list.containsAll(another.tag_list)){
                    return false;
                }
            }
        }else{
            return false;
        }

        return true;
    }

    private boolean stringEqual(String a, String b){
        if(TextUtils.isEmpty(a) && TextUtils.isEmpty(b)){
            return true;
        }else if(!TextUtils.isEmpty(a) && !TextUtils.isEmpty(b)){
            if(a.equals(b)){
                return true;
            }
        }

        return false;
    }
}
