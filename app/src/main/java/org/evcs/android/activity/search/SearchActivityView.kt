package org.evcs.android.activity.search

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView
import org.evcs.android.ui.view.shared.ILoadingView

interface SearchActivityView : IErrorView, ILoadingView {
    fun showLocations(page: List<Location?>?);
    fun onEmptyResponse();
}
