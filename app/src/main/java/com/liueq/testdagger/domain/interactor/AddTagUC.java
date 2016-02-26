package com.liueq.testdagger.domain.interactor;

import android.text.TextUtils;

import com.liueq.testdagger.data.model.Account;
import com.liueq.testdagger.data.model.Tag;
import com.liueq.testdagger.data.repository.TagRepo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by liueq on 26/2/2016.
 * Add Tag UseCase
 */
public class AddTagUC extends UseCase{

	TagRepo mTR;

	@Inject
	public AddTagUC(TagRepo TR) {
		mTR = TR;
	}

	/**
	 * Get an account's available tags
	 * @param account
	 * @return
	 */
	public List<Tag> getAvailableTags(Account account){
		List<Tag> all_list = mTR.getAllTags();
		List<Tag> has_list = mTR.getTagFromAccount(account);
		List<Tag> available_list = new ArrayList<Tag>();

		for(Tag t : all_list){
			if(!has_list.contains(t)){
				available_list.add(t);
			}
		}

		return available_list;
	}

	/**
	 * Search tags by tag_name
	 * Search scope is available tags
	 * @param tag_name
	 * @return
	 */
	public List<Tag> searchTags(Account account, String tag_name){
		if (TextUtils.isEmpty(tag_name)) {
			return new ArrayList<Tag>();
		}

		List<Tag> search_list = mTR.searchTag(tag_name);
		List<Tag> available_list = getAvailableTags(account);
		List<Tag> result_list = new ArrayList<Tag>();

		for(Tag t : search_list){
			if(available_list.contains(t)){
				result_list.add(t);
			}
		}

		return result_list;
	}

	/**
	 * Add tag to account, if tag is not exists, insert a tag to db
	 * account sync with db, just in memory
	 * @param account
	 * @param tag
	 * @return
	 */
	public boolean addTag(Account account, Tag tag) {
		if (TextUtils.isEmpty(tag.id)) {
			String tag_id = mTR.insertOrUpdateTag(tag);
			if(TextUtils.isEmpty(tag_id)){
				return false;
			}
			tag.id = tag_id;
		}

		account.tag_list.add(tag);
		return true;
	}
}