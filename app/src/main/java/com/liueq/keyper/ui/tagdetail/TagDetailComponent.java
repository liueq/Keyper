package com.liueq.keyper.ui.tagdetail;

import com.liueq.keyper.base.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by liueq on 29/2/2016.
 * TagDetailActivity Component
 */
@ActivityScope
@Subcomponent(
		modules = {
				TagDetailModule.class
		}
)
public interface TagDetailComponent {

	TagDetailActivity inject(TagDetailActivity activity);

	TagDetailActivityPresenter presenter();
}
