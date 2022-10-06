package org.evcs.android.util;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import androidx.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

public class ImageRotationUtils {

    private static final String TAG = "ImageRotationUtils";
    private static final int ORIENTATION_ERROR = 0;

    /**
     *
     * @param picturePath Picture's path
     * @return An {@link int} with picture's orientation, and if it fails returns ORIENTATION_ERROR
     */
    private static int getImageSourceOrientation(@NonNull String picturePath) {
        try {
            ExifInterface exif = new ExifInterface(picturePath);
            return exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return ORIENTATION_ERROR;
        }
    }

    /**
     *
     * @param picturePath Picture's bath
     * @return a {@link Bitmap} with the correct orientation. For example, if it is turned left or right it will
     * straighten it. However, if device doesn't save the picture with a different orientation, it will
     * return the images as it was stored.
     */
    @NonNull
    public static Bitmap straightenBitmap(@NonNull String picturePath) throws OutOfMemoryError {
        int orientation = getImageSourceOrientation(picturePath);
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmRotated;
    }
}
