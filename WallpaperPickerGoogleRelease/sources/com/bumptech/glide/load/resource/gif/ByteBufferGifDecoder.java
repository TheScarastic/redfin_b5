package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.util.Log;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifHeader;
import com.bumptech.glide.gifdecoder.GifHeaderParser;
import com.bumptech.glide.gifdecoder.GifHeaderParser$$ExternalSyntheticOutline0;
import com.bumptech.glide.gifdecoder.StandardGifDecoder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.UnitTransformation;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
/* loaded from: classes.dex */
public class ByteBufferGifDecoder implements ResourceDecoder<ByteBuffer, GifDrawable> {
    public static final GifDecoderFactory GIF_DECODER_FACTORY = new GifDecoderFactory();
    public static final GifHeaderParserPool PARSER_POOL = new GifHeaderParserPool();
    public final Context context;
    public final GifDecoderFactory gifDecoderFactory;
    public final GifHeaderParserPool parserPool;
    public final List<ImageHeaderParser> parsers;
    public final GifBitmapProvider provider;

    /* loaded from: classes.dex */
    public static class GifDecoderFactory {
    }

    /* loaded from: classes.dex */
    public static class GifHeaderParserPool {
        public final Queue<GifHeaderParser> pool = new ArrayDeque(0);

        public GifHeaderParserPool() {
            char[] cArr = Util.HEX_CHAR_ARRAY;
        }

        public synchronized void release(GifHeaderParser gifHeaderParser) {
            gifHeaderParser.rawData = null;
            gifHeaderParser.header = null;
            this.pool.offer(gifHeaderParser);
        }
    }

    public ByteBufferGifDecoder(Context context, List<ImageHeaderParser> list, BitmapPool bitmapPool, ArrayPool arrayPool, GifHeaderParserPool gifHeaderParserPool, GifDecoderFactory gifDecoderFactory) {
        this.context = context.getApplicationContext();
        this.parsers = list;
        this.gifDecoderFactory = gifDecoderFactory;
        this.provider = new GifBitmapProvider(bitmapPool, arrayPool);
        this.parserPool = gifHeaderParserPool;
    }

    public static int getSampleSize(GifHeader gifHeader, int i, int i2) {
        int i3;
        int min = Math.min(gifHeader.height / i2, gifHeader.width / i);
        if (min == 0) {
            i3 = 0;
        } else {
            i3 = Integer.highestOneBit(min);
        }
        int max = Math.max(1, i3);
        if (Log.isLoggable("BufferGifDecoder", 2) && max > 1) {
            int i4 = gifHeader.width;
            int i5 = gifHeader.height;
            StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(125, "Downsampling GIF, sampleSize: ", max, ", target dimens: [", i);
            m.append("x");
            m.append(i2);
            m.append("], actual dimens: [");
            m.append(i4);
            m.append("x");
            m.append(i5);
            m.append("]");
            Log.v("BufferGifDecoder", m.toString());
        }
        return max;
    }

    /* Return type fixed from 'com.bumptech.glide.load.engine.Resource' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<GifDrawable> decode(ByteBuffer byteBuffer, int i, int i2, Options options) throws IOException {
        GifHeaderParser poll;
        ByteBuffer byteBuffer2 = byteBuffer;
        GifHeaderParserPool gifHeaderParserPool = this.parserPool;
        synchronized (gifHeaderParserPool) {
            poll = gifHeaderParserPool.pool.poll();
            if (poll == null) {
                poll = new GifHeaderParser();
            }
            poll.rawData = null;
            Arrays.fill(poll.block, (byte) 0);
            poll.header = new GifHeader();
            poll.blockSize = 0;
            ByteBuffer asReadOnlyBuffer = byteBuffer2.asReadOnlyBuffer();
            poll.rawData = asReadOnlyBuffer;
            asReadOnlyBuffer.position(0);
            poll.rawData.order(ByteOrder.LITTLE_ENDIAN);
        }
        try {
            return decode(byteBuffer2, i, i2, poll, options);
        } finally {
            this.parserPool.release(poll);
        }
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(ByteBuffer byteBuffer, Options options) throws IOException {
        ImageHeaderParser.ImageType imageType;
        ByteBuffer byteBuffer2 = byteBuffer;
        if (((Boolean) options.get(GifOptions.DISABLE_ANIMATION)).booleanValue()) {
            return false;
        }
        List<ImageHeaderParser> list = this.parsers;
        if (byteBuffer2 == null) {
            imageType = ImageHeaderParser.ImageType.UNKNOWN;
        } else {
            int size = list.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    imageType = ImageHeaderParser.ImageType.UNKNOWN;
                    break;
                }
                ImageHeaderParser.ImageType type = list.get(i).getType(byteBuffer2);
                if (type != ImageHeaderParser.ImageType.UNKNOWN) {
                    imageType = type;
                    break;
                }
                i++;
            }
        }
        if (imageType == ImageHeaderParser.ImageType.GIF) {
            return true;
        }
        return false;
    }

    public final GifDrawableResource decode(ByteBuffer byteBuffer, int i, int i2, GifHeaderParser gifHeaderParser, Options options) {
        int i3 = LogTime.$r8$clinit;
        long elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        try {
            GifHeader parseHeader = gifHeaderParser.parseHeader();
            if (parseHeader.frameCount > 0 && parseHeader.status == 0) {
                Bitmap.Config config = options.get(GifOptions.DECODE_FORMAT) == DecodeFormat.PREFER_RGB_565 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
                int sampleSize = getSampleSize(parseHeader, i, i2);
                GifDecoderFactory gifDecoderFactory = this.gifDecoderFactory;
                GifBitmapProvider gifBitmapProvider = this.provider;
                Objects.requireNonNull(gifDecoderFactory);
                StandardGifDecoder standardGifDecoder = new StandardGifDecoder(gifBitmapProvider, parseHeader, byteBuffer, sampleSize);
                standardGifDecoder.setDefaultBitmapConfig(config);
                standardGifDecoder.framePointer = (standardGifDecoder.framePointer + 1) % standardGifDecoder.header.frameCount;
                Bitmap nextFrame = standardGifDecoder.getNextFrame();
                if (nextFrame == null) {
                    return null;
                }
                GifDrawableResource gifDrawableResource = new GifDrawableResource(new GifDrawable(new GifDrawable.GifState(new GifFrameLoader(Glide.get(this.context), standardGifDecoder, i, i2, (UnitTransformation) UnitTransformation.TRANSFORMATION, nextFrame))));
                if (Log.isLoggable("BufferGifDecoder", 2)) {
                    double elapsedMillis = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                    StringBuilder sb = new StringBuilder(51);
                    sb.append("Decoded GIF from stream in ");
                    sb.append(elapsedMillis);
                    Log.v("BufferGifDecoder", sb.toString());
                }
                return gifDrawableResource;
            }
            if (Log.isLoggable("BufferGifDecoder", 2)) {
                double elapsedMillis2 = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                StringBuilder sb2 = new StringBuilder(51);
                sb2.append("Decoded GIF from stream in ");
                sb2.append(elapsedMillis2);
                Log.v("BufferGifDecoder", sb2.toString());
            }
            return null;
        } finally {
            if (Log.isLoggable("BufferGifDecoder", 2)) {
                double elapsedMillis3 = LogTime.getElapsedMillis(elapsedRealtimeNanos);
                StringBuilder sb3 = new StringBuilder(51);
                sb3.append("Decoded GIF from stream in ");
                sb3.append(elapsedMillis3);
                Log.v("BufferGifDecoder", sb3.toString());
            }
        }
    }
}
