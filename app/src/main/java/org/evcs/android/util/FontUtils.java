package org.evcs.android.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Menu;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import io.github.inflationx.calligraphy3.CalligraphyTypefaceSpan;

/**
 * Utility to simplify management and configurations of fonts.
 */
public final class FontUtils {

    private FontUtils() {}

    /**
     * Creates a new {@link SpannableString} with the text and fontPath passed as argument.
     *
     * @param context Context to get the font
     * @param text Text to format
     * @param fontPath Font path with the font
     * @return A new {@link SpannableString} with the applied font
     */
    public static SpannableString applyFont(Context context, CharSequence text, String fontPath) {
        SpannableString newText = new SpannableString(text);
        newText.setSpan(
            new CalligraphyTypefaceSpan(Typeface.createFromAsset(context.getAssets(), fontPath)), 0,
            newText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        return newText;
    }

    /**
     * This method will apply the font "Sansation-Regular" to the options in the menu.
     *
     * @param context Context of the application
     * @param menu Menu to apply font
     */
    public static void applyFontToMenu(Context context, Menu menu, String fontpath) {
        for (int i = 0; i < menu.size(); i++) {
            CharSequence title = menu.getItem(i).getTitle();
            menu.getItem(i).setTitle(
                FontUtils.applyFont(context, title, fontpath));
        }
    }

    /**
     * This paints and bolds the string in the middle. There should be a better way to do this
     * @param stringArray An array of three strings, of which the one in position 1 will be painted
     */
    public static SpannableString getSpannable(CharSequence[] stringArray, int color) {
        SpannableString spannable = new SpannableString(TextUtils.join(" ", stringArray));

        if (stringArray[0].equals(""))
            spannable = new SpannableString(stringArray[1] + " " + stringArray[2]);

        int start = stringArray[0].length();
        int end = start + stringArray[1].length() + 1;

        spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    public static SpannableString getAlternatingSpannable(String[] stringArray, int color) {
        SpannableString spannable = new SpannableString(TextUtils.join(" ", stringArray));

//        if (stringArray[0].equals(""))
//            spannable. = new SpannableString(stringArray[1] + " " + stringArray[2]);

        int length = 0;
        for (int i = 0; i + 1 < stringArray.length; i+=2) {

            length += stringArray[i].length() + 1;
            int start = length;
            length += stringArray[i + 1].length() + 1;
            int end = Math.min(length, spannable.length());

            spannable.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }
        return spannable;
    }

}
