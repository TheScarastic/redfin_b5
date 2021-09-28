package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.android.systemui.shared.system.QuickStepContract;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;
/* loaded from: classes.dex */
public class StreamBitmapDecoder implements ResourceDecoder<InputStream, Bitmap> {
    public final ArrayPool byteArrayPool;
    public final Downsampler downsampler;

    /* loaded from: classes.dex */
    public static class UntrustedCallbacks implements Downsampler.DecodeCallbacks {
        public final RecyclableBufferedInputStream bufferedStream;
        public final ExceptionCatchingInputStream exceptionStream;

        public UntrustedCallbacks(RecyclableBufferedInputStream recyclableBufferedInputStream, ExceptionCatchingInputStream exceptionCatchingInputStream) {
            this.bufferedStream = recyclableBufferedInputStream;
            this.exceptionStream = exceptionCatchingInputStream;
        }

        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException {
            IOException iOException = this.exceptionStream.exception;
            if (iOException != null) {
                if (bitmap != null) {
                    bitmapPool.put(bitmap);
                }
                throw iOException;
            }
        }

        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onObtainBounds() {
            RecyclableBufferedInputStream recyclableBufferedInputStream = this.bufferedStream;
            synchronized (recyclableBufferedInputStream) {
                recyclableBufferedInputStream.marklimit = recyclableBufferedInputStream.buf.length;
            }
        }
    }

    public StreamBitmapDecoder(Downsampler downsampler, ArrayPool arrayPool) {
        this.downsampler = downsampler;
        this.byteArrayPool = arrayPool;
    }

    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options) throws IOException {
        RecyclableBufferedInputStream recyclableBufferedInputStream;
        boolean z;
        ExceptionCatchingInputStream exceptionCatchingInputStream;
        InputStream inputStream2 = inputStream;
        if (inputStream2 instanceof RecyclableBufferedInputStream) {
            recyclableBufferedInputStream = (RecyclableBufferedInputStream) inputStream2;
            z = false;
        } else {
            recyclableBufferedInputStream = new RecyclableBufferedInputStream(inputStream2, this.byteArrayPool, QuickStepContract.SYSUI_STATE_ONE_HANDED_ACTIVE);
            z = true;
        }
        Queue<ExceptionCatchingInputStream> queue = ExceptionCatchingInputStream.QUEUE;
        synchronized (queue) {
            exceptionCatchingInputStream = (ExceptionCatchingInputStream) ((ArrayDeque) queue).poll();
        }
        if (exceptionCatchingInputStream == null) {
            exceptionCatchingInputStream = new ExceptionCatchingInputStream();
        }
        exceptionCatchingInputStream.wrapped = recyclableBufferedInputStream;
        try {
            return this.downsampler.decode(new MarkEnforcingInputStream(exceptionCatchingInputStream), i, i2, options, new UntrustedCallbacks(recyclableBufferedInputStream, exceptionCatchingInputStream));
        } finally {
            exceptionCatchingInputStream.release();
            if (z) {
                recyclableBufferedInputStream.release();
            }
        }
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(InputStream inputStream, Options options) throws IOException {
        Objects.requireNonNull(this.downsampler);
        return true;
    }
}
