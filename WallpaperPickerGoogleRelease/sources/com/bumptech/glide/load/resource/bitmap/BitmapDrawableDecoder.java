package com.bumptech.glide.load.resource.bitmap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;
/* loaded from: classes.dex */
public class BitmapDrawableDecoder<DataType> implements ResourceDecoder<DataType, BitmapDrawable> {
    public final /* synthetic */ int $r8$classId = 0;
    public final ResourceDecoder<DataType, Bitmap> decoder;
    public final Object resources;

    public BitmapDrawableDecoder(ResourceDrawableDecoder resourceDrawableDecoder, BitmapPool bitmapPool) {
        this.decoder = resourceDrawableDecoder;
        this.resources = bitmapPool;
    }

    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Type inference failed for r1v3. Raw type applied. Possible types: com.bumptech.glide.load.engine.Resource<android.graphics.Bitmap>, com.bumptech.glide.load.engine.Resource<android.graphics.drawable.BitmapDrawable> */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<BitmapDrawable> decode(Object obj, int i, int i2, Options options) {
        switch (this.$r8$classId) {
            case 0:
                return LazyBitmapDrawableResource.obtain((Resources) this.resources, this.decoder.decode(obj, i, i2, options));
            default:
                Resource decode = ((ResourceDrawableDecoder) this.decoder).decode((Uri) obj);
                if (decode == null) {
                    return null;
                }
                return DrawableToBitmapConverter.convert((BitmapPool) this.resources, ((DrawableResource) decode).get(), i, i2);
        }
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(Object obj, Options options) {
        switch (this.$r8$classId) {
            case 0:
                return this.decoder.handles(obj, options);
            default:
                return "android.resource".equals(((Uri) obj).getScheme());
        }
    }

    public BitmapDrawableDecoder(Resources resources, ResourceDecoder resourceDecoder) {
        this.resources = resources;
        this.decoder = resourceDecoder;
    }
}
