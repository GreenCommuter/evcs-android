//package org.evcs.android.util.wolmo;
//
//import android.content.ContentResolver;
//import android.content.Context;
//import android.database.Cursor;
//import android.net.Uri;
//import android.provider.MediaStore;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import ar.com.wolox.wolmo.core.util.ContextUtils;
//
///**
// * Utils class for managing {@link File}s.
// */
//public class FileUtils {
//
//    private FileUtils() {
//    }
//
//    /**
//     * Creates a file in the external public directory to store data.
//     * <p>
//     * The file ends up being stored as:
//     * filename + "." + extension
//     *
//     * @param filename  File name, used as described above
//     * @param extension ImageFormat of the file, used as described above
//     * @return {@link File} result of the creation
//     * @throws IOException If a file could not be created
//     */
//    public static File createFile(Context context,
//            @NonNull String filename, @NonNull String extension, String type) throws IOException {
//        File storageDir = context.getExternalFilesDir(type);
//
//        //The suffix will be appended as it is, we need to add the dot manually
//        if (!extension.startsWith(".")) {
//            extension = "." + extension;
//        }
//
//        return File.createTempFile(filename, extension, storageDir);
//    }
//
//    /**
//     * Get the physical path to a stored File by providing a URI of a content provider.
//     *
//     * @param fileUri A URI of a content provider pointing to an image resource
//     * @return A path to the real file location, or null if it can't find it
//     */
//    @Nullable
//    public static String getRealPathFromUri(@NonNull Uri fileUri) {
//        Cursor cursor = null;
//        try {
//            String[] proj = {MediaStore.Images.Media.DATA};
//            cursor = ContextUtils
//                    .getAppContext()
//                    .getContentResolver()
//                    .query(fileUri, proj, null, null, null);
//
//            if (cursor == null) return null;
//
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            return cursor.getString(columnIndex);
//        } finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//        }
//    }
//
//    @Nullable
//    public static File createCopy(
//            @NonNull Context context, @NonNull Uri uri) {
//        final ContentResolver contentResolver = context.getContentResolver();
//        if (contentResolver == null)
//            return null;
//
//        // Create file path inside app's data dir
//        String filePath = context.getApplicationInfo().dataDir + File.separator
//                + System.currentTimeMillis();
//
//        File file = new File(filePath);
//        try {
//            InputStream inputStream = contentResolver.openInputStream(uri);
//            if (inputStream == null)
//                return null;
//
//            OutputStream outputStream = new FileOutputStream(file);
//            byte[] buf = new byte[1024];
//            int len;
//            while ((len = inputStream.read(buf)) > 0)
//                outputStream.write(buf, 0, len);
//
//            outputStream.close();
//            inputStream.close();
//        } catch (IOException ignore) {
//            return null;
//        }
//
//        return file;
//    }
//
//}