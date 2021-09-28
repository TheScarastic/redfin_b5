package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;
import java.util.List;
/* loaded from: classes.dex */
public class ResourceDrawableDecoder implements ResourceDecoder<Uri, Drawable> {
    public final Context context;

    public ResourceDrawableDecoder(Context context) {
        this.context = context.getApplicationContext();
    }

    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public /* bridge */ /* synthetic */ Resource<Drawable> decode(Uri uri, int i, int i2, Options options) throws IOException {
        return decode(uri);
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(Uri uri, Options options) throws IOException {
        return uri.getScheme().equals("android.resource");
    }

    /* JADX WARN: Incorrect args count in method signature: (Landroid/net/Uri;IILcom/bumptech/glide/load/Options;)Lcom/bumptech/glide/load/engine/Resource<Landroid/graphics/drawable/Drawable;>; */
    public Resource decode(Uri uri) {
        Integer num;
        Context context;
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() == 2) {
            String authority = uri.getAuthority();
            num = Integer.valueOf(this.context.getResources().getIdentifier(pathSegments.get(1), pathSegments.get(0), authority));
        } else {
            if (pathSegments.size() == 1) {
                try {
                    num = Integer.valueOf(pathSegments.get(0));
                } catch (NumberFormatException unused) {
                }
            }
            num = null;
        }
        if (num == null) {
            String valueOf = String.valueOf(uri);
            throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 25, "Unrecognized Uri format: ", valueOf));
        } else if (num.intValue() != 0) {
            int intValue = num.intValue();
            String authority2 = uri.getAuthority();
            if (authority2.equals(this.context.getPackageName())) {
                context = this.context;
            } else {
                try {
                    context = this.context.createPackageContext(authority2, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    if (authority2.contains(this.context.getPackageName())) {
                        context = this.context;
                    } else {
                        String valueOf2 = String.valueOf(uri);
                        throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf2.length() + 57, "Failed to obtain context or unrecognized Uri format for: ", valueOf2), e);
                    }
                }
            }
            Drawable drawable = DrawableDecoderCompat.getDrawable(this.context, context, intValue, null);
            if (drawable != null) {
                return new NonOwnedDrawableResource(drawable);
            }
            return null;
        } else {
            String valueOf3 = String.valueOf(uri);
            throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf3.length() + 34, "Failed to obtain resource id for: ", valueOf3));
        }
    }
}
