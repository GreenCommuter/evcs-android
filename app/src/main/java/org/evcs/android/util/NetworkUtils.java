package org.evcs.android.util;

import android.content.Context;

import org.evcs.android.R;
import org.evcs.android.features.shared.EVCSDialogFragment;

public final class NetworkUtils {

    /**
     * Empty constructor.
     */
    private NetworkUtils() {}

//    public static EVCSDialogFragment getConnectionErrorDialogToHelp(Context context) {
//        return new EVCSDialogFragment.Builder()
//                .setCancelable(false)
//                .setTitle(context.getString(R.string.placeholder_no_internet_dialog_title))
//                .setSubtitle(context.getString(R.string.placeholder_no_internet_dialog_subtitle))
//                .addButton("Help", new EVCSDialogFragment.OnClickListener() {
//                    @Override
//                    public void onClick(@NonNull EVCSDialogFragment fragment) {
//                        MainNavigationController.getInstance().onHelpSelected();
//                        fragment.dismiss();
//                    }
//                })
//                .build();
//    }

    public static EVCSDialogFragment getConnectionErrorDialog(Context context) {
        return new EVCSDialogFragment.Builder()
                .setCancelable(false)
                .setTitle(context.getString(R.string.placeholder_no_internet_dialog_title))
                .setSubtitle(context.getString(R.string.placeholder_no_internet_dialog_subtitle))
                .build();
    }
}
