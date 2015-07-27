package com.liueq.testdagger.utils;

import android.os.Environment;
import android.test.AndroidTestCase;

import com.google.gson.stream.JsonReader;

import junit.framework.TestResult;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.Scanner;

/**
 * Created by liueq on 27/7/15.
 */
public class FileReaderTest extends AndroidTestCase {

    FileReader fileReader;
    String data = "[{\"name\" : \"liueq\", \"age\" : \"23\", \"ok\" : \"yes\"},{\"name\" : \"liueq2\", \"age\" : \"232\", \"ok\" : \"yes2\"}]";

    @Before
    public void setUp() throws IOException {
        fileReader = FileReader.getInstance();
        File testfile = new File(Environment.getExternalStorageDirectory(), "password.json");
        FileWriter fileWriter = new FileWriter(testfile);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();


        String check_data = null;
        java.io.FileReader fileReader_io = new java.io.FileReader(testfile);
        Scanner scanner = new Scanner(fileReader_io);
        check_data = scanner.nextLine();

        assertEquals(data, check_data);
    }

    @After
    public void tearDown(){


    }

    @Test
    public void testRetrieveData() throws IOException {
        JsonReader jsonReader = fileReader.retrieveData();
        String value = null;
        jsonReader.beginArray();
        while(jsonReader.hasNext()) {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String tagName = jsonReader.nextName();
                if (tagName.equals("name")) {
                    value = jsonReader.nextString();
                }else if(tagName.equals("age")){
                    jsonReader.nextString();
                }else if(tagName.equals("ok")){
                    jsonReader.nextString();
                }
            }
            jsonReader.endObject();
        }
        jsonReader.endArray();
        assertEquals("liueq2", value);
    }

//    @Test
//    public void testPersistData(){
//        fileReader.presistData(data);
//    }
}
