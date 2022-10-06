package org.evcs.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;


/**
 * Utils class to provide handy methods to apply operations from/to {@link Bitmap} instances.
 */
public final class BitmapUtils {

    private BitmapUtils() {}

    /**
     * Retrieve a {@link Bitmap} from an asset.
     *
     * @param context used for {@link Drawable} fetching
     * @param res id of the target resource
     *
     * @return a {@link Bitmap} instance of with the contents of the target drawable resource
     */
    public static Bitmap bitmapFromResource(Context context, @DrawableRes int res) {
        Drawable drawable = ContextCompat.getDrawable(context, res);

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

}
