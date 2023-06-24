package org.evcs.android.features.profile.payments

import org.evcs.android.ui.view.shared.IErrorView

interface PaginationView<K> : IErrorView {
    fun showEmpty()
    fun showItems(list: List<K?>, pagesLeft: Boolean, onFirstPage: Boolean)
}
