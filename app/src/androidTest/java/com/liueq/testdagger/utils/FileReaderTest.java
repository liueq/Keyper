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
import java.io.FileNotFoundException;
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
    String mTestPath = Environment.getExternalStorageDirectory().getPath() + "/Test";
    String mTestFile = "password_test.json";

    @Before
    public void setUp() throws IOException {
        fileReader = FileReader.getInstance();
        fileReader.setmFilePath(mTestPath);
        fileReader.setmFilName(mTestFile);

        File f = new File(mTestPath);
        if(!f.exists()){
            f.mkdir();
        }
        File testfile = new File(mTestPath, mTestFile);
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

    @Test
    public void testPersistData() throws FileNotFoundException {
        fileReader.setmFilName("password_test_persisit.txt");
        String save_data = "Hello world";
        fileReader.presistData(save_data);

        java.io.FileReader fr = new java.io.FileReader(new File(mTestPath, "password_test_persisit.txt"));
        Scanner scanner = new Scanner(fr);

        String result = scanner.nextLine();

        assertEquals(save_data, result);
    }
}
