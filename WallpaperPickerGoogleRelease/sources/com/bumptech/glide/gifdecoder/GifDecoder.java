package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
/* loaded from: classes.dex */
public interface GifDecoder {

    /* loaded from: classes.dex */
    public interface BitmapProvider {
    }

    void advance();

    void clear();

    int getByteSize();

    int getCurrentFrameIndex();

    ByteBuffer getData();

    int getFrameCount();

    int getNextDelay();

    Bitmap getNextFrame();
}
