package com.liueq.keyper.data.repository;

import android.support.annotation.Nullable;

import com.liueq.keyper.data.model.Account;
import com.liueq.keyper.data.model.Tag;

import java.util.List;

/**
 * Created by liueq on 24/2/2016.
 * Tag Repository
 */
public interface TagRepo {

	/**
	 * Get all tags in db
	 * @return
	 */
	List<Tag> getAllTags();

	/**
	 * Insert a new tag or update the tag name
	 */
	String insertOrUpdateTag(@Nullable Tag tag);

	/**
	 * Delete a tag
	 * @param tag
	 * @return
	 */
	boolean deleteTag(Tag tag);

	/**
	 * Search tag by name
	 * @param tag_name
	 * @return
	 */
	List<Tag> searchTag(String tag_name);

	/**
	 * Search db, is there tag exist with same name.
	 * @param tag_name
	 * @return
	 */
	boolean hasTag(String tag_name);

	List<Account> getAccountFromTag(Tag tag);

	List<Tag> getTagFromAccount(Account account);

	boolean addAccountTag(Account account, List<Tag> tags);

	boolean deleteAccountTag(Account account, Tag tag);

	/**
	 * Replace an account`s all tags
	 * @param account
	 * @param tag_list
	 * @return
	 */
	boolean replaceAccountTag(Account account, List<Tag> tag_list);
}
