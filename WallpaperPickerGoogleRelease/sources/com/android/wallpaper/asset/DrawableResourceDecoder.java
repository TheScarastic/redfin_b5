package com.android.wallpaper.asset;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import java.io.IOException;
/* loaded from: classes.dex */
public class DrawableResourceDecoder implements ResourceDecoder<Drawable, Drawable> {
    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Drawable> decode(Drawable drawable, int i, int i2, Options options) throws IOException {
        return new DrawableResource<Drawable>(this, drawable) { // from class: com.android.wallpaper.asset.DrawableResourceDecoder.1
            @Override // com.bumptech.glide.load.engine.Resource
            public Class<Drawable> getResourceClass() {
                return Drawable.class;
            }

            @Override // com.bumptech.glide.load.engine.Resource
            public int getSize() {
                return get().getIntrinsicHeight() * get().getIntrinsicWidth() * 4;
            }

            @Override // com.bumptech.glide.load.engine.Resource
            public void recycle() {
            }
        };
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public /* bridge */ /* synthetic */ boolean handles(Drawable drawable, Options options) throws IOException {
        return true;
    }
}
