package org.evcs.android.util;

import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;

/**
 * Utils that short-hands common uses of a {@link TypedArray}.
 */
public final class TypedArrayUtils {

    /**
     * Empty constructor.
     */
    private TypedArrayUtils() {}

    /**
     * Attempts to retrieve a resource {@link String}. If it fails, it tries to retrieve it as
     * a literal (ie: a hard-coded one in xml).
     *
     * @param typedArray Target array that holds the attributes
     * @param stringIndex Index of the custom attribute
     *
     * @return A {@link String} as a result of the retrieval
     */
    public static @Nullable String getStringResourceWithLiteralFallback(
            TypedArray typedArray, @StyleableRes int stringIndex) {
        String resource = typedArray.getNonResourceString(stringIndex);
        if (resource == null) {
            resource = typedArray.getString(stringIndex);
        }

        return resource;
    }
}
