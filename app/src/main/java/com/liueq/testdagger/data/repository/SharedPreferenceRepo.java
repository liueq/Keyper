package com.liueq.testdagger.data.repository;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liueq on 29/7/15.
 */
public interface SharedPreferenceRepo {

    void saveProperties(HashMap<String, String> map);

    HashMap<String, String> getAllProperties();

    String getProterties(String key);
}
