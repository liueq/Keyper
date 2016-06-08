package com.liueq.keyper.ui.advancesearch;

import com.liueq.keyper.base.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by liueq on 7/3/2016.
 * Advance search component
 */
@ActivityScope
@Subcomponent(
		modules = {
				AdvanceSearchModule.class
		}
)
public interface AdvanceSearchComponent {

	AdvanceSearchActivity inject(AdvanceSearchActivity activity);

	AdvanceSearchPresenter presenter();
}
