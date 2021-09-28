package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
/* loaded from: classes.dex */
public class BitmapEncoder implements ResourceEncoder<Bitmap> {
    public final ArrayPool arrayPool;
    public static final Option<Integer> COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT = new Option<>("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat", null, Option.EMPTY_UPDATER);

    public BitmapEncoder(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x006c, code lost:
        if (r6 != null) goto L_0x006e;
     */
    @Override // com.bumptech.glide.load.Encoder
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean encode(java.lang.Object r9, java.io.File r10, com.bumptech.glide.load.Options r11) {
        /*
            r8 = this;
            com.bumptech.glide.load.engine.Resource r9 = (com.bumptech.glide.load.engine.Resource) r9
            java.lang.String r0 = "BitmapEncoder"
            java.lang.Object r9 = r9.get()
            android.graphics.Bitmap r9 = (android.graphics.Bitmap) r9
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r1 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_FORMAT
            java.lang.Object r1 = r11.get(r1)
            android.graphics.Bitmap$CompressFormat r1 = (android.graphics.Bitmap.CompressFormat) r1
            if (r1 == 0) goto L_0x0015
            goto L_0x0020
        L_0x0015:
            boolean r1 = r9.hasAlpha()
            if (r1 == 0) goto L_0x001e
            android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.PNG
            goto L_0x0020
        L_0x001e:
            android.graphics.Bitmap$CompressFormat r1 = android.graphics.Bitmap.CompressFormat.JPEG
        L_0x0020:
            r9.getWidth()
            r9.getHeight()
            int r2 = com.bumptech.glide.util.LogTime.$r8$clinit     // Catch: all -> 0x00d9
            long r2 = android.os.SystemClock.elapsedRealtimeNanos()     // Catch: all -> 0x00d9
            com.bumptech.glide.load.Option<java.lang.Integer> r4 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_QUALITY     // Catch: all -> 0x00d9
            java.lang.Object r4 = r11.get(r4)     // Catch: all -> 0x00d9
            java.lang.Integer r4 = (java.lang.Integer) r4     // Catch: all -> 0x00d9
            int r4 = r4.intValue()     // Catch: all -> 0x00d9
            r5 = 0
            r6 = 0
            java.io.FileOutputStream r7 = new java.io.FileOutputStream     // Catch: IOException -> 0x005f, all -> 0x005d
            r7.<init>(r10)     // Catch: IOException -> 0x005f, all -> 0x005d
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r10 = r8.arrayPool     // Catch: IOException -> 0x005a, all -> 0x0057
            if (r10 == 0) goto L_0x004e
            com.bumptech.glide.load.data.BufferedOutputStream r10 = new com.bumptech.glide.load.data.BufferedOutputStream     // Catch: IOException -> 0x005a, all -> 0x0057
            com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool r8 = r8.arrayPool     // Catch: IOException -> 0x005a, all -> 0x0057
            r6 = 65536(0x10000, float:9.18355E-41)
            r10.<init>(r7, r8, r6)     // Catch: IOException -> 0x005a, all -> 0x0057
            r6 = r10
            goto L_0x004f
        L_0x004e:
            r6 = r7
        L_0x004f:
            r9.compress(r1, r4, r6)     // Catch: IOException -> 0x005f, all -> 0x005d
            r6.close()     // Catch: IOException -> 0x005f, all -> 0x005d
            r5 = 1
            goto L_0x006e
        L_0x0057:
            r8 = move-exception
            goto L_0x00d3
        L_0x005a:
            r8 = move-exception
            r6 = r7
            goto L_0x0060
        L_0x005d:
            r8 = move-exception
            goto L_0x00d2
        L_0x005f:
            r8 = move-exception
        L_0x0060:
            r10 = 3
            boolean r10 = android.util.Log.isLoggable(r0, r10)     // Catch: all -> 0x005d
            if (r10 == 0) goto L_0x006c
            java.lang.String r10 = "Failed to encode Bitmap"
            android.util.Log.d(r0, r10, r8)     // Catch: all -> 0x005d
        L_0x006c:
            if (r6 == 0) goto L_0x0071
        L_0x006e:
            r6.close()     // Catch: IOException -> 0x0071, all -> 0x00d9
        L_0x0071:
            r8 = 2
            boolean r8 = android.util.Log.isLoggable(r0, r8)     // Catch: all -> 0x00d9
            if (r8 == 0) goto L_0x00d1
            java.lang.String r8 = java.lang.String.valueOf(r1)     // Catch: all -> 0x00d9
            int r10 = com.bumptech.glide.util.Util.getBitmapByteSize(r9)     // Catch: all -> 0x00d9
            double r1 = com.bumptech.glide.util.LogTime.getElapsedMillis(r2)     // Catch: all -> 0x00d9
            com.bumptech.glide.load.Option<android.graphics.Bitmap$CompressFormat> r3 = com.bumptech.glide.load.resource.bitmap.BitmapEncoder.COMPRESSION_FORMAT     // Catch: all -> 0x00d9
            java.lang.Object r11 = r11.get(r3)     // Catch: all -> 0x00d9
            java.lang.String r11 = java.lang.String.valueOf(r11)     // Catch: all -> 0x00d9
            boolean r9 = r9.hasAlpha()     // Catch: all -> 0x00d9
            int r3 = r8.length()     // Catch: all -> 0x00d9
            int r3 = r3 + 105
            int r4 = r11.length()     // Catch: all -> 0x00d9
            int r3 = r3 + r4
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch: all -> 0x00d9
            r4.<init>(r3)     // Catch: all -> 0x00d9
            java.lang.String r3 = "Compressed with type: "
            r4.append(r3)     // Catch: all -> 0x00d9
            r4.append(r8)     // Catch: all -> 0x00d9
            java.lang.String r8 = " of size "
            r4.append(r8)     // Catch: all -> 0x00d9
            r4.append(r10)     // Catch: all -> 0x00d9
            java.lang.String r8 = " in "
            r4.append(r8)     // Catch: all -> 0x00d9
            r4.append(r1)     // Catch: all -> 0x00d9
            java.lang.String r8 = ", options format: "
            r4.append(r8)     // Catch: all -> 0x00d9
            r4.append(r11)     // Catch: all -> 0x00d9
            java.lang.String r8 = ", hasAlpha: "
            r4.append(r8)     // Catch: all -> 0x00d9
            r4.append(r9)     // Catch: all -> 0x00d9
            java.lang.String r8 = r4.toString()     // Catch: all -> 0x00d9
            android.util.Log.v(r0, r8)     // Catch: all -> 0x00d9
        L_0x00d1:
            return r5
        L_0x00d2:
            r7 = r6
        L_0x00d3:
            if (r7 == 0) goto L_0x00d8
            r7.close()     // Catch: IOException -> 0x00d8, all -> 0x00d9
        L_0x00d8:
            throw r8     // Catch: all -> 0x00d9
        L_0x00d9:
            r8 = move-exception
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.load.resource.bitmap.BitmapEncoder.encode(java.lang.Object, java.io.File, com.bumptech.glide.load.Options):boolean");
    }

    @Override // com.bumptech.glide.load.ResourceEncoder
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}
