package com.bumptech.glide.request;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
/* loaded from: classes.dex */
public class RequestOptions extends BaseRequestOptions<RequestOptions> {
    public static RequestOptions centerCropOptions;
    public static RequestOptions noTransformOptions;
    public static RequestOptions skipMemoryCacheTrueOptions;

    public static RequestOptions bitmapTransform(Transformation<Bitmap> transformation) {
        return new RequestOptions().transform(transformation, true);
    }

    public static RequestOptions centerCropTransform() {
        if (centerCropOptions == null) {
            RequestOptions transform = new RequestOptions().transform(DownsampleStrategy.CENTER_OUTSIDE, new CenterCrop());
            transform.autoClone();
            centerCropOptions = transform;
        }
        return centerCropOptions;
    }

    public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy diskCacheStrategy) {
        return new RequestOptions().diskCacheStrategy(diskCacheStrategy);
    }

    public static RequestOptions noTransformation() {
        if (noTransformOptions == null) {
            RequestOptions dontTransform = new RequestOptions().dontTransform();
            dontTransform.autoClone();
            noTransformOptions = dontTransform;
        }
        return noTransformOptions;
    }

    public static <T> RequestOptions option(Option<T> option, T t) {
        return new RequestOptions().set(option, t);
    }
}
