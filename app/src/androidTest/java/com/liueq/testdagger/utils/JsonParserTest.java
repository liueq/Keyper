package com.liueq.testdagger.utils;

import android.test.AndroidTestCase;
import android.util.JsonReader;
import android.util.Log;
import static org.junit.Assert.assertThat;

import com.liueq.testdagger.data.model.Account;

import static org.hamcrest.CoreMatchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liueq on 27/7/15.
 */
public class JsonParserTest extends AndroidTestCase{

    @Before
    public void setUp(){}

    @After
    public void tearDown(){}


    @Test
    public void testObjToJson() throws IOException {
        List<Account> accountList = new ArrayList<Account>();
        for(int i = 1; i < 3; i++){
            Account account = new Account();
            account.id = "001" + i;
            account.site = "baidu" + i;
            account.password = "pass" + i;
            account.username = "name" + i;
            account.mail = "mail" + i;
            account.description = "desc" + i;

            accountList.add(account);
        }

        String json = JsonParser.objToJson(accountList);
        Log.i("liueq", "json is " + json);
        StringReader sr = new StringReader(json);
        JsonReader jr = new JsonReader(sr);

        jr.beginArray();
        while(jr.hasNext()){
            jr.beginObject();
            while(jr.hasNext()){
                String tagName = jr.nextName();
                if(tagName.equals("id")){
                    String id = jr.nextString();
                    if(id.contains("11")){
                        assertEquals("0011", id);
                    }else {
                        assertEquals("0012", id);
                    }
                }else if(tagName.equals("site")){
                    String site = jr.nextString();
                    if(site.contains("1")){
                        assertEquals("baidu1", site);
                    }else {
                        assertEquals("baidu2", site);
                    }
                }else if(tagName.equals("password")){
                    String pass = jr.nextString();
                    if(pass.contains("1")){
                        assertEquals("pass1", pass);
                    }else{
                        assertEquals("pass2", pass);
                    }
                }else if(tagName.equals("username")){
                    String name = jr.nextString();
                    if(name.contains("1")){
                        assertEquals("name1", name);
                    }else{
                        assertEquals("name2", name);
                    }
                }else if(tagName.equals("mail")){
                    String mail = jr.nextString();
                    if(mail.contains("1")){
                        assertEquals("mail1", mail);
                    }else{
                        assertEquals("mail2", mail);
                    }
                }else if(tagName.equals("description")){
                    String desc = jr.nextString();
                    if(desc.contains("1")){
                        assertEquals("desc1", desc);
                    }else{
                        assertEquals("desc2", desc);
                    }
                }
            }
            jr.endObject();
        }
        jr.endArray();

    }

    @Test
    public void testJsonToObj(){
        String source_data = "[{\"description\":\"Description\",\"id\":\"1437999132651\",\"mail\":\"123@email.com\",\"password\":\"74ZPWNWFVr832EvVn2OeBg\\u003d\\u003d\\n\",\"site\":\"www.baidu.com\",\"username\":\"liuerqiang\"},{\"description\":\"\",\"id\":\"1437999179175\",\"mail\":\"\",\"password\":\"rRxzhcIRa/w5ZZKVNDCQ2A\\u003d\\u003d\\n\",\"site\":\"ceshi\",\"username\":\"xiugai\"}]";
        StringReader sr = new StringReader(source_data);
        com.google.gson.stream.JsonReader jr = new com.google.gson.stream.JsonReader(sr);
        List<Account> list = JsonParser.jsonToObj(jr);

        assertEquals("1437999132651", list.get(0).id);
        assertEquals("Description", list.get(0).description);
        assertEquals("123@email.com", list.get(0).mail);
        assertEquals("www.baidu.com", list.get(0).site);
//        assertEquals("liuerqiang", list.get(0).username);

        assertThat(list.get(0).username, is("liuerqiang"));
    }




}
