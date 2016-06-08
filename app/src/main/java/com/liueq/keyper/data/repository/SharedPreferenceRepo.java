package com.liueq.keyper.data.repository;

import java.util.HashMap;

/**
 * Created by liueq on 29/7/15.
 * SharedPreference operation
 */
public interface SharedPreferenceRepo {

	/**
     * Save all properties (map)
     * @param map
     */
    void saveProperties(HashMap<String, String> map);

	/**
	 * Get all properties (map)
	 * @return
	 */
    HashMap<String, String> getAllProperties();

	/**
	 * Get a properties (like map)
	 * @param key
	 * @return
	 */
    String getProterties(String key);
}
