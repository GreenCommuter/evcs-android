package org.evcs.android.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.evcs.android.R;

/**
 * Utility class for generating spinner {@link Dialog}s.
 */
public final class SpinnerUtils {

    private SpinnerUtils() {}

    /**
     * Generate a default {@link ProgressDialog} with the corresponding parameters set.
     *
     * @param context    {@link Context} for which to instantiate it.
     * @param messageId  Content of the spinner.
     * @param cancelable Whether it's cancelable by backpressing and other cancelling events.
     * @return
     */
    public static ProgressDialog generateDefaultSpinner(
            @NonNull Context context, @StringRes int messageId, boolean cancelable) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(messageId));
        progressDialog.setCancelable(cancelable);

        return progressDialog;
    }

    public static ProgressDialog getDefaultProgressDialog(Context context) {
        return SpinnerUtils.generateDefaultSpinner(
                context, R.string.progress_dialog_loading, false);
    }

    public static Dialog getNewProgressDialog(Context context, @LayoutRes int layout) {
        Dialog dialog = new Dialog(context);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(layout);
        dialog.setCancelable(false);
        return dialog;
    }

}
