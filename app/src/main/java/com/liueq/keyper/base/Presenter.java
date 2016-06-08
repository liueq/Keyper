package com.liueq.keyper.base;

import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by liueq on 17/2/2016.
 * Base Presenter
 */
public abstract class Presenter {

	protected HashMap<Class, Fragment> mAttachedFragment = new HashMap<Class, Fragment>();

	/**
	 * When fragment created, attach it with presenter
	 * @param name
	 * @param fragment
	 */
	public void attachFragment(Class name, Fragment fragment){
		mAttachedFragment.put(name, fragment);
	}

	/**
	 * Before fragment destroy, detach it with presenter
	 * @param name
	 */
	public void detachFragment(Class name){
		mAttachedFragment.remove(name);
	}

	/**
	 * Find a fragment from presenter
	 * @param name
	 * @return
	 */
	protected Fragment getFragment(Class name){
		return mAttachedFragment.get(name);
	}

}
