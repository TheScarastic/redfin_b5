package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import android.util.Log;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
/* loaded from: classes.dex */
public class StandardGifDecoder implements GifDecoder {
    public int[] act;
    public final GifDecoder.BitmapProvider bitmapProvider;
    public byte[] block;
    public int downsampledHeight;
    public int downsampledWidth;
    public int framePointer;
    public GifHeader header;
    public Boolean isFirstFrameTransparent;
    public byte[] mainPixels;
    public int[] mainScratch;
    public byte[] pixelStack;
    public short[] prefix;
    public Bitmap previousImage;
    public ByteBuffer rawData;
    public int sampleSize;
    public boolean savePrevious;
    public int status;
    public byte[] suffix;
    public final int[] pct = new int[256];
    public Bitmap.Config bitmapConfig = Bitmap.Config.ARGB_8888;

    public StandardGifDecoder(GifDecoder.BitmapProvider bitmapProvider, GifHeader gifHeader, ByteBuffer byteBuffer, int i) {
        int[] iArr;
        this.bitmapProvider = bitmapProvider;
        this.header = new GifHeader();
        synchronized (this) {
            if (i > 0) {
                int highestOneBit = Integer.highestOneBit(i);
                this.status = 0;
                this.header = gifHeader;
                this.framePointer = -1;
                ByteBuffer asReadOnlyBuffer = byteBuffer.asReadOnlyBuffer();
                this.rawData = asReadOnlyBuffer;
                asReadOnlyBuffer.position(0);
                this.rawData.order(ByteOrder.LITTLE_ENDIAN);
                this.savePrevious = false;
                Iterator<GifFrame> it = gifHeader.frames.iterator();
                while (true) {
                    if (it.hasNext()) {
                        if (it.next().dispose == 3) {
                            this.savePrevious = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                this.sampleSize = highestOneBit;
                int i2 = gifHeader.width;
                this.downsampledWidth = i2 / highestOneBit;
                int i3 = gifHeader.height;
                this.downsampledHeight = i3 / highestOneBit;
                this.mainPixels = ((GifBitmapProvider) this.bitmapProvider).obtainByteArray(i2 * i3);
                GifDecoder.BitmapProvider bitmapProvider2 = this.bitmapProvider;
                int i4 = this.downsampledWidth * this.downsampledHeight;
                ArrayPool arrayPool = ((GifBitmapProvider) bitmapProvider2).arrayPool;
                if (arrayPool == null) {
                    iArr = new int[i4];
                } else {
                    iArr = (int[]) arrayPool.get(i4, int[].class);
                }
                this.mainScratch = iArr;
            } else {
                StringBuilder sb = new StringBuilder(41);
                sb.append("Sample size must be >=0, not: ");
                sb.append(i);
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public void clear() {
        ArrayPool arrayPool;
        ArrayPool arrayPool2;
        ArrayPool arrayPool3;
        this.header = null;
        byte[] bArr = this.mainPixels;
        if (!(bArr == null || (arrayPool3 = ((GifBitmapProvider) this.bitmapProvider).arrayPool) == null)) {
            arrayPool3.put(bArr);
        }
        int[] iArr = this.mainScratch;
        if (!(iArr == null || (arrayPool2 = ((GifBitmapProvider) this.bitmapProvider).arrayPool) == null)) {
            arrayPool2.put(iArr);
        }
        Bitmap bitmap = this.previousImage;
        if (bitmap != null) {
            ((GifBitmapProvider) this.bitmapProvider).bitmapPool.put(bitmap);
        }
        this.previousImage = null;
        this.rawData = null;
        this.isFirstFrameTransparent = null;
        byte[] bArr2 = this.block;
        if (bArr2 != null && (arrayPool = ((GifBitmapProvider) this.bitmapProvider).arrayPool) != null) {
            arrayPool.put(bArr2);
        }
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public int getByteSize() {
        return (this.mainScratch.length * 4) + this.rawData.limit() + this.mainPixels.length;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public ByteBuffer getData() {
        return this.rawData;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public int getFrameCount() {
        return this.header.frameCount;
    }

    public final Bitmap getNextBitmap() {
        Boolean bool = this.isFirstFrameTransparent;
        Bitmap.Config config = (bool == null || bool.booleanValue()) ? Bitmap.Config.ARGB_8888 : this.bitmapConfig;
        Bitmap dirty = ((GifBitmapProvider) this.bitmapProvider).bitmapPool.getDirty(this.downsampledWidth, this.downsampledHeight, config);
        dirty.setHasAlpha(true);
        return dirty;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public int getNextDelay() {
        int i;
        GifHeader gifHeader = this.header;
        int i2 = gifHeader.frameCount;
        if (i2 <= 0 || (i = this.framePointer) < 0) {
            return 0;
        }
        if (i < 0 || i >= i2) {
            return -1;
        }
        return gifHeader.frames.get(i).delay;
    }

    @Override // com.bumptech.glide.gifdecoder.GifDecoder
    public synchronized Bitmap getNextFrame() {
        if (this.header.frameCount <= 0 || this.framePointer < 0) {
            if (Log.isLoggable("StandardGifDecoder", 3)) {
                int i = this.header.frameCount;
                int i2 = this.framePointer;
                StringBuilder sb = new StringBuilder(72);
                sb.append("Unable to decode frame, frameCount=");
                sb.append(i);
                sb.append(", framePointer=");
                sb.append(i2);
                Log.d("StandardGifDecoder", sb.toString());
            }
            this.status = 1;
        }
        int i3 = this.status;
        if (!(i3 == 1 || i3 == 2)) {
            this.status = 0;
            if (this.block == null) {
                this.block = ((GifBitmapProvider) this.bitmapProvider).obtainByteArray(255);
            }
            GifFrame gifFrame = this.header.frames.get(this.framePointer);
            int i4 = this.framePointer - 1;
            GifFrame gifFrame2 = i4 >= 0 ? this.header.frames.get(i4) : null;
            int[] iArr = gifFrame.lct;
            if (iArr == null) {
                iArr = this.header.gct;
            }
            this.act = iArr;
            if (iArr == null) {
                if (Log.isLoggable("StandardGifDecoder", 3)) {
                    int i5 = this.framePointer;
                    StringBuilder sb2 = new StringBuilder(49);
                    sb2.append("No valid color table found for frame #");
                    sb2.append(i5);
                    Log.d("StandardGifDecoder", sb2.toString());
                }
                this.status = 1;
                return null;
            }
            if (gifFrame.transparency) {
                System.arraycopy(iArr, 0, this.pct, 0, iArr.length);
                int[] iArr2 = this.pct;
                this.act = iArr2;
                iArr2[gifFrame.transIndex] = 0;
            }
            return setPixels(gifFrame, gifFrame2);
        }
        if (Log.isLoggable("StandardGifDecoder", 3)) {
            int i6 = this.status;
            StringBuilder sb3 = new StringBuilder(42);
            sb3.append("Unable to decode frame, status=");
            sb3.append(i6);
            Log.d("StandardGifDecoder", sb3.toString());
        }
        return null;
    }

    public void setDefaultBitmapConfig(Bitmap.Config config) {
        if (config == Bitmap.Config.ARGB_8888 || config == Bitmap.Config.RGB_565) {
            this.bitmapConfig = config;
            return;
        }
        String valueOf = String.valueOf(config);
        String valueOf2 = String.valueOf(Bitmap.Config.ARGB_8888);
        String valueOf3 = String.valueOf(Bitmap.Config.RGB_565);
        throw new IllegalArgumentException(FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(R$string$$ExternalSyntheticOutline0.m(valueOf3.length() + valueOf2.length() + valueOf.length() + 41, "Unsupported format: ", valueOf, ", must be one of ", valueOf2), " or ", valueOf3));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r5v32, resolved type: boolean */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0045, code lost:
        if (r3.bgIndex == r36.transIndex) goto L_0x0050;
     */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0067  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.graphics.Bitmap setPixels(com.bumptech.glide.gifdecoder.GifFrame r36, com.bumptech.glide.gifdecoder.GifFrame r37) {
        /*
        // Method dump skipped, instructions count: 1078
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.gifdecoder.StandardGifDecoder.setPixels(com.bumptech.glide.gifdecoder.GifFrame, com.bumptech.glide.gifdecoder.GifFrame):android.graphics.Bitmap");
    }
}
