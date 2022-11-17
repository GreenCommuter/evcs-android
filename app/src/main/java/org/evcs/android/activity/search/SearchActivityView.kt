package org.evcs.android.activity.search

import org.evcs.android.model.Location
import org.evcs.android.ui.view.shared.IErrorView

interface SearchActivityView : IErrorView {
    fun showLocations(page: List<Location?>?, pagesLeft: Boolean, onFirstPage: Boolean);
}
