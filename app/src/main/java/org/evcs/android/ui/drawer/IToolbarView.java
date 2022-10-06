package org.evcs.android.ui.drawer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface IToolbarView {

    /**
     * Returns the toolbar status for this fragment.
     * <p>
     * The toolbar and drawer can be in different states so we can configure the toolbar with this method.
     * For example a back arrow can be used as the navigation icon and when the user touches it
     * {@link android.app.Activity#onBackPressed()} will be called. Otherwise the hamburger icon
     * will be used and the drawer will be opened when the user clicks it.
     * <p>
     * Currently when the toolbar show the <b>back</b> arrow, the drawer is disabled, this can be changed in the future.
     * Also when the toolbar is hidden, the drawer is also locked and hidden.
     *
     * @return the state for the toolbar and drawer
     */
    @NonNull
    ToolbarState getToolbarState();

    /**
     * Returns the title for the toolbar.
     *
     * @return Title to show in the toolbar
     */
    @Nullable
    String getToolbarTitle();


    /**
     * Lists all the states the toolbar can be
     */
    enum ToolbarState {
        SHOW_HAMBURGER,
        SHOW_ARROW,
        HIDE_TOOLBAR,
        TRANSPARENT_TOOLBAR,
        SHOW_HAMBURGUER_NEW,
        SHOW_ARROW_NEW;
    }

}
