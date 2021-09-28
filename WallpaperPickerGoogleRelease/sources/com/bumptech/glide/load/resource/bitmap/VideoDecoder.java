package com.bumptech.glide.load.resource.bitmap;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Objects;
/* loaded from: classes.dex */
public class VideoDecoder<T> implements ResourceDecoder<T, Bitmap> {
    public static final int DEFAULT_FRAME_OPTION = 2;
    public final BitmapPool bitmapPool;
    public final MediaMetadataRetrieverFactory factory;
    public final MediaMetadataRetrieverInitializer<T> initializer;
    public static final Option<Long> TARGET_FRAME = new Option<>("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.TargetFrame", -1L, new Option.CacheKeyUpdater<Long>() { // from class: com.bumptech.glide.load.resource.bitmap.VideoDecoder.1
        public final ByteBuffer buffer = ByteBuffer.allocate(8);

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [byte[], java.lang.Object, java.security.MessageDigest] */
        @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
        public void update(byte[] bArr, Long l, MessageDigest messageDigest) {
            Long l2 = l;
            messageDigest.update(bArr);
            synchronized (this.buffer) {
                this.buffer.position(0);
                messageDigest.update(this.buffer.putLong(l2.longValue()).array());
            }
        }
    });
    public static final Option<Integer> FRAME_OPTION = new Option<>("com.bumptech.glide.load.resource.bitmap.VideoBitmapDecode.FrameOption", 2, new Option.CacheKeyUpdater<Integer>() { // from class: com.bumptech.glide.load.resource.bitmap.VideoDecoder.2
        public final ByteBuffer buffer = ByteBuffer.allocate(4);

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [byte[], java.lang.Object, java.security.MessageDigest] */
        @Override // com.bumptech.glide.load.Option.CacheKeyUpdater
        public void update(byte[] bArr, Integer num, MessageDigest messageDigest) {
            Integer num2 = num;
            if (num2 != null) {
                messageDigest.update(bArr);
                synchronized (this.buffer) {
                    this.buffer.position(0);
                    messageDigest.update(this.buffer.putInt(num2.intValue()).array());
                }
            }
        }
    });
    public static final MediaMetadataRetrieverFactory DEFAULT_FACTORY = new MediaMetadataRetrieverFactory();

    /* loaded from: classes.dex */
    public static final class AssetFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<AssetFileDescriptor> {
        public AssetFileDescriptorInitializer(AnonymousClass1 r1) {
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [android.media.MediaMetadataRetriever, java.lang.Object] */
        @Override // com.bumptech.glide.load.resource.bitmap.VideoDecoder.MediaMetadataRetrieverInitializer
        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, AssetFileDescriptor assetFileDescriptor) {
            AssetFileDescriptor assetFileDescriptor2 = assetFileDescriptor;
            mediaMetadataRetriever.setDataSource(assetFileDescriptor2.getFileDescriptor(), assetFileDescriptor2.getStartOffset(), assetFileDescriptor2.getLength());
        }
    }

    /* loaded from: classes.dex */
    public static class MediaMetadataRetrieverFactory {
    }

    /* loaded from: classes.dex */
    public interface MediaMetadataRetrieverInitializer<T> {
        void initialize(MediaMetadataRetriever mediaMetadataRetriever, T t);
    }

    /* loaded from: classes.dex */
    public static final class ParcelFileDescriptorInitializer implements MediaMetadataRetrieverInitializer<ParcelFileDescriptor> {
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [android.media.MediaMetadataRetriever, java.lang.Object] */
        @Override // com.bumptech.glide.load.resource.bitmap.VideoDecoder.MediaMetadataRetrieverInitializer
        public void initialize(MediaMetadataRetriever mediaMetadataRetriever, ParcelFileDescriptor parcelFileDescriptor) {
            mediaMetadataRetriever.setDataSource(parcelFileDescriptor.getFileDescriptor());
        }
    }

    public VideoDecoder(BitmapPool bitmapPool, MediaMetadataRetrieverInitializer<T> mediaMetadataRetrieverInitializer, MediaMetadataRetrieverFactory mediaMetadataRetrieverFactory) {
        this.bitmapPool = bitmapPool;
        this.initializer = mediaMetadataRetrieverInitializer;
        this.factory = mediaMetadataRetrieverFactory;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Bitmap decodeFrame(android.media.MediaMetadataRetriever r9, long r10, int r12, int r13, int r14, com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r15) {
        /*
            r0 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r13 == r0) goto L_0x005a
            if (r14 == r0) goto L_0x005a
            com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r0 = com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.NONE
            if (r15 == r0) goto L_0x005a
            r0 = 18
            java.lang.String r0 = r9.extractMetadata(r0)     // Catch: all -> 0x004b
            int r0 = java.lang.Integer.parseInt(r0)     // Catch: all -> 0x004b
            r1 = 19
            java.lang.String r1 = r9.extractMetadata(r1)     // Catch: all -> 0x004b
            int r1 = java.lang.Integer.parseInt(r1)     // Catch: all -> 0x004b
            r2 = 24
            java.lang.String r2 = r9.extractMetadata(r2)     // Catch: all -> 0x004b
            int r2 = java.lang.Integer.parseInt(r2)     // Catch: all -> 0x004b
            r3 = 90
            if (r2 == r3) goto L_0x0030
            r3 = 270(0x10e, float:3.78E-43)
            if (r2 != r3) goto L_0x0033
        L_0x0030:
            r8 = r1
            r1 = r0
            r0 = r8
        L_0x0033:
            float r13 = r15.getScaleFactor(r0, r1, r13, r14)     // Catch: all -> 0x004b
            float r14 = (float) r0     // Catch: all -> 0x004b
            float r14 = r14 * r13
            int r6 = java.lang.Math.round(r14)     // Catch: all -> 0x004b
            float r14 = (float) r1     // Catch: all -> 0x004b
            float r13 = r13 * r14
            int r7 = java.lang.Math.round(r13)     // Catch: all -> 0x004b
            r2 = r9
            r3 = r10
            r5 = r12
            android.graphics.Bitmap r13 = r2.getScaledFrameAtTime(r3, r5, r6, r7)     // Catch: all -> 0x004b
            goto L_0x005b
        L_0x004b:
            r13 = move-exception
            r14 = 3
            java.lang.String r15 = "VideoDecoder"
            boolean r14 = android.util.Log.isLoggable(r15, r14)
            if (r14 == 0) goto L_0x005a
            java.lang.String r14 = "Exception trying to decode frame on oreo+"
            android.util.Log.d(r15, r14, r13)
        L_0x005a:
            r13 = 0
        L_0x005b:
            if (r13 != 0) goto L_0x0061
            android.graphics.Bitmap r13 = r9.getFrameAtTime(r10, r12)
        L_0x0061:
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.VideoDecoder.decodeFrame(android.media.MediaMetadataRetriever, long, int, int, int, com.bumptech.glide.load.resource.bitmap.DownsampleStrategy):android.graphics.Bitmap");
    }

    /* JADX INFO: finally extract failed */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:24:0x004d */
    /* JADX DEBUG: Multi-variable search result rejected for r12v4, resolved type: android.media.MediaMetadataRetriever */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v3, types: [com.bumptech.glide.load.resource.bitmap.DownsampleStrategy] */
    /* JADX WARN: Type inference failed for: r12v6, types: [android.media.MediaMetadataRetriever] */
    /* JADX WARN: Type inference failed for: r12v8 */
    /* JADX WARN: Type inference failed for: r12v9 */
    @Override // com.bumptech.glide.load.ResourceDecoder
    public Resource<Bitmap> decode(T t, int i, int i2, Options options) throws IOException {
        long longValue = ((Long) options.get(TARGET_FRAME)).longValue();
        if (longValue >= 0 || longValue == -1) {
            Integer num = (Integer) options.get(FRAME_OPTION);
            if (num == null) {
                num = 2;
            }
            DownsampleStrategy downsampleStrategy = (DownsampleStrategy) options.get(DownsampleStrategy.OPTION);
            MediaMetadataRetriever mediaMetadataRetriever = downsampleStrategy;
            if (downsampleStrategy == null) {
                mediaMetadataRetriever = DownsampleStrategy.DEFAULT;
            }
            try {
                Objects.requireNonNull(this.factory);
                mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    this.initializer.initialize(mediaMetadataRetriever, t);
                    Bitmap decodeFrame = decodeFrame(mediaMetadataRetriever, longValue, num.intValue(), i, i2, mediaMetadataRetriever);
                    mediaMetadataRetriever.release();
                    return BitmapResource.obtain(decodeFrame, this.bitmapPool);
                } catch (RuntimeException e) {
                    throw new IOException(e);
                }
            } catch (Throwable th) {
                mediaMetadataRetriever.release();
                throw th;
            }
        } else {
            StringBuilder sb = new StringBuilder(83);
            sb.append("Requested frame must be non-negative, or DEFAULT_FRAME, given: ");
            sb.append(longValue);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    @Override // com.bumptech.glide.load.ResourceDecoder
    public boolean handles(T t, Options options) {
        return true;
    }
}
