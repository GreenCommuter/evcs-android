package org.evcs.android.features.shared;

import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.core.presenter.BasePresenter;
import com.base.core.util.ToastUtils;
import com.rollbar.android.Rollbar;

import org.evcs.android.EVCSApplication;
import org.evcs.android.R;
import org.evcs.android.databinding.EvcsDialogFragmentBinding;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This fragment provides an immutable dialog view with the standard EVCS design. It has a
 * title and subtitle and buttons that can be added one at a time with their respective listener.
 * The cancel button has a different design. show() must be called when everything is ready.
 */
public class EVCSDialogFragment extends SingletonDialog<BasePresenter> {

    private LinearLayout mLayout;
    private TextView mTitle;
    private TextView mSubtitle;
    private int mButtonMargin;
    @ColorInt int mGrey;
    @ColorInt int mTransparent;

    private Map<String, ButtonInfo> mButtons = new LinkedHashMap<>();
    private String mTitleResource;
    private String mSubtitleResource;
    private List<View> mViews;
    //Don't show if null
    private @Nullable String mCancel;
    private OnClickListener mCancelOnClickListener;
    private boolean mLogParams;

    @Override
    public void init() {}

    @Override
    public int layout() {
        return R.layout.evcs_dialog_fragment;
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public void setUi(View v) {
        EvcsDialogFragmentBinding binding = EvcsDialogFragmentBinding.bind(v);
        mLayout = binding.evcsDialogFragmentLayout;
        mTitle = binding.evcsDialogFragmentTitle;
        mSubtitle = binding.evcsDialogFragmentSubtitle;
        mGrey = getResources().getColor(R.color.evcs_grey);
        mTransparent = getResources().getColor(R.color.evcs_transparent);
        mButtonMargin = (int) getResources().getDimension(R.dimen.spacing_medium);
    }

    //We need the layout to be non null to add the buttons. Therefore, everything must be set before
    //calling show()
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitle.setText(mTitleResource);
        mTitle.setVisibility(mTitleResource == null ? View.GONE : View.VISIBLE);
        mSubtitle.setText(mSubtitleResource);
        mSubtitle.setVisibility(mSubtitleResource == null ? View.GONE : View.VISIBLE);

        try {
            for (View v : mViews) {
                mLayout.addView(v);
            }

            for (String label : mButtons.keySet()) {
                mLayout.addView(getButton(label));
            }
        } catch (NullPointerException e) {
            Rollbar.reportException(e, "warning");
            Rollbar.reportMessage("handled: " + mTitleResource, "warning");
            ToastUtils.show(R.string.unknown_error);
            mLogParams = true;
        }

        if (mCancel != null) {
            mLayout.addView(getCancelButton(mCancel));
        }
        getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCancelable()) dismiss();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (mCancelOnClickListener != null && isCancelable()) {
            mCancelOnClickListener.onClick(EVCSDialogFragment.this);
        }
        return super.onBackPressed();
    }

    // I couldn't find a way to do this through xml
    protected Button getButton(final String label) {
        Button button = new Button(new ContextThemeWrapper(getContext(), R.style.Button_Orange));
        button.setTextColor(ContextCompat
            .getColorStateList(getContext(), R.color.button_text_color_selector_orange));
        button.setBackground(getResources().getDrawable(mButtons.get(label).background));
        button.setAllCaps(mButtons.get(label).upperCase);
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(mButtonMargin, mButtonMargin, mButtonMargin, mButtonMargin);
        button.setLayoutParams(layoutParams);

        button.setText(label);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtons.get(label).listener.onClick(EVCSDialogFragment.this);
            }
        });
        return button;
    }

    protected Button getCancelButton(String cancel) {
        Button button = new Button(getContext());
        button.setBackgroundColor(mTransparent);
        button.setTextColor(mGrey);
        button.setAllCaps(false);
        LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);

        button.setText(cancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mCancelOnClickListener != null) {
                    mCancelOnClickListener.onClick(EVCSDialogFragment.this);
                }
            }
        });
        return button;
    }

    protected void setParams(String titleResource, String subtitleResource,
        Map<String, ButtonInfo> buttons, List<View> views, String cancel,
        OnClickListener cancelOnClickListener, boolean cancelable) {
        if (mLogParams) {
            Rollbar.reportMessage("Title: " + mTitleResource, "warning");
        }
        mTitleResource = titleResource;
        mSubtitleResource = subtitleResource;
        mButtons = buttons;
        mViews = views;
        mCancel = cancel;
        mCancelOnClickListener = cancelOnClickListener;
        setCancelable(cancelable);
    }

    //TODO: replace this for dismissing the dialog every time a button is pressed, unless added somehow else
    public static OnClickListener getDismissOnClickListener() {
        return new OnClickListener() {
            @Override
            public void onClick(@NonNull EVCSDialogFragment fragment) {
                fragment.dismiss();
            }
        };
    }

    /**
     * Builder class to customize a {@link EVCSDialogFragment}.
     */
    public static class Builder {
        protected final Map<String, ButtonInfo> mButtons = new LinkedHashMap<>();
        protected final ArrayList<View> mViews = new ArrayList<>();
        protected String mTitleResource;
        protected String mSubtitleResource;
        protected String mCancel;
        protected OnClickListener mCancelOnClickListener;
        protected boolean mCancelable = true;

        /**
         * Sets the title for the dialog
         *
         * @param title Title to show
         *
         * @return Builder for further customization
         */
        public Builder setTitle(String title) {
            mTitleResource = title;
            return this;
        }

        /**
         * Sets the subtitle or message for the dialog
         *
         * @param subtitle Main message to show
         *
         * @return Builder for further customization
         */
        public Builder setSubtitle(String subtitle) {
            mSubtitleResource = subtitle;
            return this;
        }

        /**
         * Shows a cancel label to close the dialog. When this cancel is clicked, the {@link
         * EVCSDialogFragment.OnClickListener} set
         * with {@link EVCSDialogFragment.Builder#withCancelOnClickListener(OnClickListener)}
         * will be called.
         *
         * @param show <b>true</b> to show the cancel label, <b>false</b> to hide it.
         * @return Builder for further customization
         */
        public Builder showCancel(boolean show) {
            String defaultText = EVCSApplication.getInstance().getResources().getString(R.string.app_cancel);
            return showCancel(show ? defaultText : null);
        }

        public Builder showCancel(@Nullable String text) {
            mCancel = text;
            return this;
        }

        /**
         * Set a {@link EVCSDialogFragment.OnClickListener} to listen when the cancel
         * button is clicked.
         * If {@link EVCSDialogFragment#isCancelable()} is true, this listener will be
         * called when the back button is pressed.
         *
         * @param listener Listener to set
         * @return Builder for further customization
         */
        public Builder withCancelOnClickListener(@NonNull OnClickListener listener) {
            mCancelOnClickListener = listener;
            return this;
        }

        /**
         * Adds a new {@link Button} to the dialog with the text and click listener passed as argument.
         * By default this method sets the text as upper case.
         *
         * @param text Text to show in the button
         * @param listener Click listener for the button.
         * @return Builder for further customization
         */
        public Builder addButton(@NonNull String text, @NonNull OnClickListener listener) {
            return addButton(text, false, listener);
        }

        /**
         * Adds a new {@link Button} to the dialog with the text and click listener passed as argument.
         * You can set whether you want te button text as upper case or not.
         * If not upper case is used, the button will not change the text.
         *
         * @param text Text to show in the button
         * @param upperCase <b>true</b> to set the text to upper case, <b>false</b> to keep it unchanged
         * @param listener Click listener for the button.
         * @return Builder for further customization
         */
        public Builder addButton(String text, boolean upperCase, OnClickListener listener) {
            return addButton(text, upperCase, listener, R.drawable.layout_corners_rounded_orange_gradient);
        }

        public Builder addButton(String text, boolean upperCase, OnClickListener listener, @DrawableRes int background) {
            mButtons.put(text, new ButtonInfo(upperCase, listener, background));
            return this;
        }

        /**
         * Adds a custom view to the dialog
         *
         * @param view View to add to the dialog fragment
         * @return Builder for further customization
         */
        public Builder addView(@NonNull View view) {
            mViews.add(view);
            return this;
        }

        /**
         * Sets the dialog as cancelable or not.
         * By default this is set to <b>true</b>.
         * <b>NOTE: </b> Keep in mind that if the dialog isn't cancelable and there is no "cancel"
         * button and no button dismisses the dialog, the user can't close the dialog.
         *
         * @param cancelable <b>true</b> to set the dialog cancelable.
         * @return Builder for further customization
         */
        public Builder setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return this;
        }

        /**
         * Builds the {@link EVCSDialogFragment}
         * @return Dialog to show
         */
        public EVCSDialogFragment build() {
            EVCSDialogFragment fragment = new EVCSDialogFragment();
            fragment.setParams(mTitleResource, mSubtitleResource, mButtons, mViews, mCancel,
                mCancelOnClickListener, mCancelable);
            return fragment;
        }

        /**
         * Builds and shows the {@link EVCSDialogFragment} using the {@link FragmentManager}
         * passed as argument.
         *
         * @param fm Fragment manager to show the fragment
         */
        public void show(@NonNull FragmentManager fm) {
            build().show(fm);
        }

    }

    protected static class ButtonInfo {
        OnClickListener listener;
        boolean upperCase;
        @DrawableRes int background;

        public ButtonInfo(boolean upperCase, OnClickListener listener, @DrawableRes int background) {
            this.listener = listener;
            this.upperCase = upperCase;
            this.background = background;
        }
    }

    /**
     * Click listener for the buttons on this {@link EVCSDialogFragment}.
     */
    public interface OnClickListener {

        /**
         * Called when the user clicks on the requested view.
         *
         * @param fragment Dialog where the view was clicked.
         */
        void onClick(@NonNull EVCSDialogFragment fragment);
    }

}
