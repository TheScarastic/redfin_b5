package com.android.systemui.media;

import android.graphics.ImageDecoder;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
final class MediaDataManager$loadBitmapFromUri$1 implements ImageDecoder.OnHeaderDecodedListener {
    public static final MediaDataManager$loadBitmapFromUri$1 INSTANCE = new MediaDataManager$loadBitmapFromUri$1();

    MediaDataManager$loadBitmapFromUri$1() {
    }

    @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
    public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setAllocator(1);
    }
}
