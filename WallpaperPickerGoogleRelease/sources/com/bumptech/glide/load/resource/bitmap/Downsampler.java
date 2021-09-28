package com.bumptech.glide.load.resource.bitmap;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.android.systemui.shared.system.QuickStepContract;
import com.bumptech.glide.gifdecoder.GifHeaderParser$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
/* loaded from: classes.dex */
public final class Downsampler {
    public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
    public final BitmapPool bitmapPool;
    public final ArrayPool byteArrayPool;
    public final DisplayMetrics displayMetrics;
    public final HardwareConfigState hardwareConfigState;
    public final List<ImageHeaderParser> parsers;
    public static final Option<DecodeFormat> DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.PREFER_ARGB_8888);
    public static final DecodeCallbacks EMPTY_CALLBACKS = new DecodeCallbacks() { // from class: com.bumptech.glide.load.resource.bitmap.Downsampler.1
        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) {
        }

        @Override // com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks
        public void onObtainBounds() {
        }
    };
    public static final Queue<BitmapFactory.Options> OPTIONS_QUEUE = new ArrayDeque(0);

    /* loaded from: classes.dex */
    public interface DecodeCallbacks {
        void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) throws IOException;

        void onObtainBounds();
    }

    static {
        Option<DownsampleStrategy> option = DownsampleStrategy.OPTION;
        Boolean bool = Boolean.FALSE;
        FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", bool);
        ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", bool);
        Collections.unmodifiableSet(new HashSet(Arrays.asList("image/vnd.wap.wbmp", "image/x-ico")));
        Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
        char[] cArr = Util.HEX_CHAR_ARRAY;
    }

    public Downsampler(List<ImageHeaderParser> list, DisplayMetrics displayMetrics, BitmapPool bitmapPool, ArrayPool arrayPool) {
        if (HardwareConfigState.instance == null) {
            synchronized (HardwareConfigState.class) {
                if (HardwareConfigState.instance == null) {
                    HardwareConfigState.instance = new HardwareConfigState();
                }
            }
        }
        this.hardwareConfigState = HardwareConfigState.instance;
        this.parsers = list;
        Objects.requireNonNull(displayMetrics, "Argument must not be null");
        this.displayMetrics = displayMetrics;
        Objects.requireNonNull(bitmapPool, "Argument must not be null");
        this.bitmapPool = bitmapPool;
        Objects.requireNonNull(arrayPool, "Argument must not be null");
        this.byteArrayPool = arrayPool;
    }

    public static Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool) throws IOException {
        if (options.inJustDecodeBounds) {
            inputStream.mark(10485760);
        } else {
            decodeCallbacks.onObtainBounds();
        }
        try {
            int i = options.outWidth;
            int i2 = options.outHeight;
            String str = options.outMimeType;
            Lock lock = TransformationUtils.BITMAP_DRAWABLE_LOCK;
            lock.lock();
            try {
                Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                lock.unlock();
                if (options.inJustDecodeBounds) {
                    inputStream.reset();
                }
                return decodeStream;
            } catch (IllegalArgumentException e) {
                IOException newIoExceptionForInBitmapAssertion = newIoExceptionForInBitmapAssertion(e, i, i2, str, options);
                if (Log.isLoggable("Downsampler", 3)) {
                    Log.d("Downsampler", "Failed to decode with inBitmap, trying again without Bitmap re-use", newIoExceptionForInBitmapAssertion);
                }
                if (options.inBitmap != null) {
                    try {
                        inputStream.reset();
                        bitmapPool.put(options.inBitmap);
                        options.inBitmap = null;
                        Bitmap decodeStream2 = decodeStream(inputStream, options, decodeCallbacks, bitmapPool);
                        TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
                        return decodeStream2;
                    } catch (IOException unused) {
                        throw newIoExceptionForInBitmapAssertion;
                    }
                } else {
                    throw newIoExceptionForInBitmapAssertion;
                }
            }
        } catch (Throwable th) {
            TransformationUtils.BITMAP_DRAWABLE_LOCK.unlock();
            throw th;
        }
    }

    @TargetApi(19)
    public static String getBitmapString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int allocationByteCount = bitmap.getAllocationByteCount();
        StringBuilder sb = new StringBuilder(14);
        sb.append(" (");
        sb.append(allocationByteCount);
        sb.append(")");
        String sb2 = sb.toString();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        String valueOf = String.valueOf(bitmap.getConfig());
        StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(sb2, valueOf.length() + 26), "[", width, "x", height);
        m.append("] ");
        m.append(valueOf);
        m.append(sb2);
        return m.toString();
    }

    public static int getDensityMultiplier(double d) {
        if (d > 1.0d) {
            d = 1.0d / d;
        }
        return (int) Math.round(d * 2.147483647E9d);
    }

    public static int[] getDimensions(InputStream inputStream, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool) throws IOException {
        options.inJustDecodeBounds = true;
        decodeStream(inputStream, options, decodeCallbacks, bitmapPool);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    public static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException illegalArgumentException, int i, int i2, String str, BitmapFactory.Options options) {
        String bitmapString = getBitmapString(options.inBitmap);
        StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(bitmapString, XMPPathFactory$$ExternalSyntheticOutline0.m(str, 99)), "Exception decoding bitmap, outWidth: ", i, ", outHeight: ", i2);
        m.append(", outMimeType: ");
        m.append(str);
        m.append(", inBitmap: ");
        m.append(bitmapString);
        return new IOException(m.toString(), illegalArgumentException);
    }

    public static void releaseOptions(BitmapFactory.Options options) {
        resetOptions(options);
        Queue<BitmapFactory.Options> queue = OPTIONS_QUEUE;
        synchronized (queue) {
            ((ArrayDeque) queue).offer(options);
        }
    }

    public static void resetOptions(BitmapFactory.Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.inDensity = 0;
        options.inTargetDensity = 0;
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;
        options.inBitmap = null;
        options.inMutable = true;
    }

    public static int round(double d) {
        return (int) (d + 0.5d);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int i, int i2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        BitmapFactory.Options options2;
        Preconditions.checkArgument(inputStream.markSupported(), "You must provide an InputStream that supports mark()");
        byte[] bArr = (byte[]) this.byteArrayPool.get(QuickStepContract.SYSUI_STATE_ONE_HANDED_ACTIVE, byte[].class);
        synchronized (Downsampler.class) {
            Queue<BitmapFactory.Options> queue = OPTIONS_QUEUE;
            synchronized (queue) {
                options2 = (BitmapFactory.Options) ((ArrayDeque) queue).poll();
            }
            if (options2 == null) {
                options2 = new BitmapFactory.Options();
                resetOptions(options2);
            }
        }
        options2.inTempStorage = bArr;
        DecodeFormat decodeFormat = (DecodeFormat) options.get(DECODE_FORMAT);
        DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
        boolean booleanValue = ((Boolean) options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS)).booleanValue();
        Option<Boolean> option = ALLOW_HARDWARE_CONFIG;
        try {
            return BitmapResource.obtain(decodeFromWrappedStreams(inputStream, options2, downsampleStrategy, decodeFormat, options.get(option) != null && ((Boolean) options.get(option)).booleanValue(), i, i2, booleanValue, decodeCallbacks), this.bitmapPool);
        } finally {
            releaseOptions(options2);
            this.byteArrayPool.put(bArr);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:110:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:111:0x02da  */
    /* JADX WARNING: Removed duplicated region for block: B:143:0x0353  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x0381  */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x03cb  */
    /* JADX WARNING: Removed duplicated region for block: B:153:0x03db  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x03fd  */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x04a0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Bitmap decodeFromWrappedStreams(java.io.InputStream r27, android.graphics.BitmapFactory.Options r28, com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r29, com.bumptech.glide.load.DecodeFormat r30, boolean r31, int r32, int r33, boolean r34, com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeCallbacks r35) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 1340
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.Downsampler.decodeFromWrappedStreams(java.io.InputStream, android.graphics.BitmapFactory$Options, com.bumptech.glide.load.resource.bitmap.DownsampleStrategy, com.bumptech.glide.load.DecodeFormat, boolean, int, int, boolean, com.bumptech.glide.load.resource.bitmap.Downsampler$DecodeCallbacks):android.graphics.Bitmap");
    }
}
