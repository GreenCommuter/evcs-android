package org.evcs.android.features.image;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.base.core.activity.BaseActivity;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.DraweeTransition;

import org.evcs.android.R;
import org.evcs.android.databinding.ActivityBaseNavhostBinding;
import org.evcs.android.databinding.ActivityImageDetailsBinding;

import butterknife.BindView;
import me.relex.photodraweeview.PhotoDraweeView;

public class ImageDetailsActivity extends BaseActivity {

    public static final String IMAGE_URL_KEY = "image_url_key";
    public static final String IMAGE_CAPTION_KEY = "image_caption_key";

    @BindView(R.id.activity_images_detail) PhotoDraweeView mImageDetail;
    @BindView(R.id.activity_toolbar) Toolbar mToolbar;

    private String mImageUrl;
    private String mImageCaption;

    public static Intent getUrlIntent(Activity from, String url, String caption) {
        Intent intent = new Intent(from, ImageDetailsActivity.class);
        intent.putExtra(ImageDetailsActivity.IMAGE_URL_KEY, url);
        intent.putExtra(ImageDetailsActivity.IMAGE_CAPTION_KEY, caption);
        return intent;
    }

    @Override
    protected View inflate(LayoutInflater layoutInflater) {
        ActivityImageDetailsBinding binding = ActivityImageDetailsBinding.inflate(layoutInflater);
        return binding.getRoot();
    }

    @Override
    protected void init() {
        Bundle bundle = getIntent().getExtras();
        ScalingUtils.ScaleType toScaleType = ScalingUtils.ScaleType.FIT_CENTER;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ScalingUtils.ScaleType fromScaleType = ScalingUtils.ScaleType.CENTER_CROP;

            getWindow().setSharedElementEnterTransition(
                    DraweeTransition.createTransitionSet(fromScaleType, toScaleType));
            getWindow().setSharedElementReturnTransition(
                    DraweeTransition.createTransitionSet(toScaleType, fromScaleType));
            mImageDetail.setTransitionName(bundle.getString(IMAGE_URL_KEY));
        }

        setSupportActionBar(mToolbar);
        mImageDetail.setPhotoUri(Uri.parse(bundle.getString(IMAGE_URL_KEY)));
        getSupportActionBar().setTitle(bundle.getString(IMAGE_CAPTION_KEY));
    }

    @Override
    public void setListeners() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}
