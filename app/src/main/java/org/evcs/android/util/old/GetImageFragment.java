//package org.evcs.android.util.wolmo;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.StringRes;
//
//import java.io.File;
//
//import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
//import ar.com.wolox.wolmo.core.permission.PermissionListener;
//import ar.com.wolox.wolmo.core.permission.PermissionManager;
//import ar.com.wolox.wolmo.core.presenter.BasePresenter;
//
///**
// * Class to help to load images from the gallery or from the camera.
// *
// * @param <T> Presenter for this fragment
// */
//public abstract class GetImageFragment<T extends BasePresenter> extends WolmoFragment<T> {
//
//    private static final String[] CAMERA_PERMISSIONS = new String[]{
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//    private static final String[] GALLERY_PERMISSIONS = new String[]{
//            Manifest.permission.READ_EXTERNAL_STORAGE};
//
//    private static final int INTENT_CODE_IMAGE_GALLERY = 9000;
//    private static final int INTENT_CODE_IMAGE_CAMERA = 9001;
//
//    private File mPictureTakenFile;
//    private OnImageReturnCallback mImageCallback;
//
//    /* Error types */
//
//    /**
//     * Error Types returned on {@link OnImageReturnCallback#error(Error)}
//     * <li>
//     * <ul>{@link Error#USER_CANCELED}: Image selection canceled by the user.</ul>
//     * <ul>{@link Error#ERROR_DATA}: Image URI was not returned.</ul>
//     * <ul>{@link Error#ERROR_UNKNOWN}: Unknown error.</ul>
//     * <ul>{@link Error#PERMISSION_DENIED}: Gallery/Camera permission denied.</ul>
//     * </li>
//     */
//    protected enum Error {
//        USER_CANCELED,      // Image selection canceled by the user
//        ERROR_DATA,         // Image URI was no returned
//        ERROR_UNKNOWN,      // Unknown error
//        PERMISSION_DENIED   // Gallery/Camera Permission denied
//    }
//
//    /**
//     * Callback for image selection
//     */
//    public interface OnImageReturnCallback {
//
//        /**
//         * Method for when the image retrieval was a success, exposing the {@link Uri} of the image.
//         *
//         * @param file retrieved image file
//         *             Returning the Uri brought us some trouble. Anyway, it can be easily retrieved
//         *             calling {@link Uri#fromFile(File)}
//         */
//        void success(@NonNull File file);
//
//        /**
//         * Method for when the image retrieval was a failure, exposing the corresponding
//         * {@link Error} instance.
//         *
//         * @param error describing the failure reason
//         */
//        void error(@NonNull Error error);
//
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case INTENT_CODE_IMAGE_GALLERY:
//                    if (data != null) {
//                        mImageCallback.success(FileUtils.createCopy(getContext(), data.getData()));
//                    } else {
//                        notifyError(Error.ERROR_DATA);
//                    }
//                    break;
//
//                case INTENT_CODE_IMAGE_CAMERA:
//                    ImageUtils.addPictureToDeviceGallery(Uri.fromFile(mPictureTakenFile));
//                    mImageCallback.success(mPictureTakenFile);
//                    break;
//
//                default:
//                    notifyError(Error.ERROR_UNKNOWN);
//            }
//        } else if (resultCode == Activity.RESULT_CANCELED) {
//            switch (requestCode) {
//                case INTENT_CODE_IMAGE_GALLERY:
//                case INTENT_CODE_IMAGE_CAMERA:
//                    notifyError(Error.USER_CANCELED);
//                    break;
//
//                default:
//                    notifyError(Error.ERROR_UNKNOWN);
//            }
//
//        }
//
//        clearCallbacks();
//    }
//
//    private void clearCallbacks() {
//        mImageCallback = null;
//    }
//
//    private void notifyError(Error error) {
//        if (mImageCallback != null) {
//            mImageCallback.error(error);
//        }
//    }
//
//    /**
//     * Resource ID to show as a toast if there is an error obtaining the image from the gallery.
//     *
//     * @return Error string resource id
//     */
//    @StringRes
//    protected abstract int galleryErrorResId();
//
//    /**
//     * Resource ID to show as a toast if there is an error obtaining the image from the camera.
//     *
//     * @return Error string resource id
//     */
//    @StringRes
//    protected abstract int cameraErrorResId();
//
//    /**
//     * Filename of the image taken by the camera.
//     *
//     * @return Image filename
//     */
//    @NonNull
//    protected abstract String pictureTakenFilename();
//
//    /**
//     * Start a request for an image from Gallery.
//     *
//     * @param onImageReturnCallback callback for request result
//     */
//    protected void selectImageFromGallery(
//            @NonNull final OnImageReturnCallback onImageReturnCallback) {
//        PermissionManager.getInstance().requestPermission(
//                this,
//                new PermissionListener() {
//                    @Override
//                    public void onPermissionsGranted() {
//                        mImageCallback = onImageReturnCallback;
//
//                        ImageUtils.getImageFromGallery(
//                                GetImageFragment.this,
//                                INTENT_CODE_IMAGE_GALLERY,
//                                galleryErrorResId());
//                    }
//
//                    @Override
//                    public void onPermissionsDenied(@NonNull String[] deniedPermissions) {
//                        onImageReturnCallback.error(Error.PERMISSION_DENIED);
//                    }
//                },
//                GALLERY_PERMISSIONS);
//    }
//
//    /**
//     * Start a request for an image from Camera.
//     *
//     * @param onImageReturnCallback callback for request result
//     */
//    protected void takePicture(@NonNull final OnImageReturnCallback onImageReturnCallback) {
//        PermissionManager.getInstance().requestPermission(
//                this,
//                new PermissionListener() {
//                    @Override
//                    public void onPermissionsGranted() {
//                        mImageCallback = onImageReturnCallback;
//
//                        mPictureTakenFile =
//                                ImageUtils.getImageFromCamera(
//                                        GetImageFragment.this,
//                                        INTENT_CODE_IMAGE_CAMERA,
//                                        pictureTakenFilename(),
//                                        ImageUtils.PNG,
//                                        cameraErrorResId());
//                    }
//
//                    @Override
//                    public void onPermissionsDenied(@NonNull String[] deniedPermissions) {
//                        onImageReturnCallback.error(Error.PERMISSION_DENIED);
//                    }
//                },
//                CAMERA_PERMISSIONS);
//    }
//
//}