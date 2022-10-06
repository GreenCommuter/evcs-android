package org.evcs.android.util;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import com.base.core.util.ImageUtils;

import org.evcs.android.BaseConfiguration;
import org.evcs.android.Configuration;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileConversionUtils {

    //TODO: resize before rotating

    @NonNull
    public static MultipartBody.Part toRequestBodyConversion(@NonNull File pictureFile, String name) throws OutOfMemoryError {
        Bitmap pictureBitmap = ImageRotationUtils.straightenBitmap(pictureFile.getAbsolutePath());
        return toRequestBodyConversion(pictureBitmap, Bitmap.CompressFormat.JPEG, BaseConfiguration.IncidentPictures.IMAGE_QUALITY,
                pictureBitmap.getWidth(), pictureBitmap.getHeight(), pictureFile.getName(), name);
    }

    @NonNull
    public static MultipartBody.Part toRequestBodyConversion(@NonNull File pictureFile, @NonNull Bitmap.CompressFormat compressFormat,
                                                             int pictureQuality, int pictureWidth, int pictureHeight, String name) throws OutOfMemoryError {
        Bitmap pictureBitmap = ImageRotationUtils.straightenBitmap(pictureFile.getAbsolutePath());
        return toRequestBodyConversion(pictureBitmap, compressFormat, pictureQuality, pictureWidth, pictureHeight, pictureFile.getName(), name);
    }

    @NonNull
    private static MultipartBody.Part toRequestBodyConversion(Bitmap pictureBitmap, @NonNull Bitmap.CompressFormat compressFormat,
                                                             int pictureQuality, int pictureWidth, int pictureHeight, String pictureName,
                                                             String formName) {

        byte[] imageContent = ImageUtils.getImageAsByteArray(pictureBitmap, compressFormat,
                pictureQuality, pictureWidth, pictureHeight);

        RequestBody requestBody = RequestBody
                .create(MediaType.parse(BaseConfiguration.Avatar.IMAGE_PNG_MIME_TYPE), imageContent);

        return MultipartBody.Part.createFormData(formName, pictureName, requestBody);
    }
}