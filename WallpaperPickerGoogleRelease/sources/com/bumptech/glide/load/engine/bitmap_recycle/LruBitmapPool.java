package com.bumptech.glide.load.engine.bitmap_recycle;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.bumptech.glide.gifdecoder.GifHeaderParser$$ExternalSyntheticOutline0;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class LruBitmapPool implements BitmapPool {
    public static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;
    public final Set<Bitmap.Config> allowedConfigs;
    public long currentSize;
    public int evictions;
    public int hits;
    public final long initialMaxSize;
    public long maxSize;
    public int misses;
    public int puts;
    public final LruPoolStrategy strategy;
    public final BitmapTracker tracker = new NullBitmapTracker();

    /* loaded from: classes.dex */
    public interface BitmapTracker {
    }

    /* loaded from: classes.dex */
    public static final class NullBitmapTracker implements BitmapTracker {
    }

    public LruBitmapPool(long j) {
        SizeConfigStrategy sizeConfigStrategy = new SizeConfigStrategy();
        HashSet hashSet = new HashSet(Arrays.asList(Bitmap.Config.values()));
        hashSet.add(null);
        hashSet.remove(Bitmap.Config.HARDWARE);
        Set<Bitmap.Config> unmodifiableSet = Collections.unmodifiableSet(hashSet);
        this.initialMaxSize = j;
        this.maxSize = j;
        this.strategy = sizeConfigStrategy;
        this.allowedConfigs = unmodifiableSet;
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    public void clearMemory() {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            Log.d("LruBitmapPool", "clearMemory");
        }
        trimToSize(0);
    }

    public final void dump() {
        if (Log.isLoggable("LruBitmapPool", 2)) {
            dumpUnchecked();
        }
    }

    public final void dumpUnchecked() {
        int i = this.hits;
        int i2 = this.misses;
        int i3 = this.puts;
        int i4 = this.evictions;
        long j = this.currentSize;
        long j2 = this.maxSize;
        String valueOf = String.valueOf(this.strategy);
        StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(valueOf.length() + 151, "Hits=", i, ", misses=", i2);
        m.append(", puts=");
        m.append(i3);
        m.append(", evictions=");
        m.append(i4);
        m.append(", currentSize=");
        m.append(j);
        m.append(", maxSize=");
        m.append(j2);
        m.append("\nStrategy=");
        m.append(valueOf);
        Log.v("LruBitmapPool", m.toString());
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    public Bitmap get(int i, int i2, Bitmap.Config config) {
        Bitmap dirtyOrNull = getDirtyOrNull(i, i2, config);
        if (dirtyOrNull != null) {
            dirtyOrNull.eraseColor(0);
            return dirtyOrNull;
        }
        if (config == null) {
            config = DEFAULT_CONFIG;
        }
        return Bitmap.createBitmap(i, i2, config);
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    public Bitmap getDirty(int i, int i2, Bitmap.Config config) {
        Bitmap dirtyOrNull = getDirtyOrNull(i, i2, config);
        if (dirtyOrNull != null) {
            return dirtyOrNull;
        }
        if (config == null) {
            config = DEFAULT_CONFIG;
        }
        return Bitmap.createBitmap(i, i2, config);
    }

    public final synchronized Bitmap getDirtyOrNull(int i, int i2, Bitmap.Config config) {
        Bitmap bitmap;
        if (config != Bitmap.Config.HARDWARE) {
            bitmap = ((SizeConfigStrategy) this.strategy).get(i, i2, config != null ? config : DEFAULT_CONFIG);
            if (bitmap == null) {
                if (Log.isLoggable("LruBitmapPool", 3)) {
                    Objects.requireNonNull((SizeConfigStrategy) this.strategy);
                    String valueOf = String.valueOf(SizeConfigStrategy.getBitmapString(Util.getBitmapByteSize(i, i2, config), config));
                    Log.d("LruBitmapPool", valueOf.length() != 0 ? "Missing bitmap=".concat(valueOf) : new String("Missing bitmap="));
                }
                this.misses++;
            } else {
                this.hits++;
                long j = this.currentSize;
                Objects.requireNonNull((SizeConfigStrategy) this.strategy);
                this.currentSize = j - ((long) Util.getBitmapByteSize(bitmap));
                Objects.requireNonNull(this.tracker);
                bitmap.setHasAlpha(true);
                bitmap.setPremultiplied(true);
            }
            if (Log.isLoggable("LruBitmapPool", 2)) {
                Objects.requireNonNull((SizeConfigStrategy) this.strategy);
                String valueOf2 = String.valueOf(SizeConfigStrategy.getBitmapString(Util.getBitmapByteSize(i, i2, config), config));
                Log.v("LruBitmapPool", valueOf2.length() != 0 ? "Get bitmap=".concat(valueOf2) : new String("Get bitmap="));
            }
            dump();
        } else {
            String valueOf3 = String.valueOf(config);
            StringBuilder sb = new StringBuilder(valueOf3.length() + SysUiStatsLog.ASSIST_GESTURE_PROGRESS_REPORTED);
            sb.append("Cannot create a mutable Bitmap with config: ");
            sb.append(valueOf3);
            sb.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
            throw new IllegalArgumentException(sb.toString());
        }
        return bitmap;
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    public synchronized void put(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        } else if (!bitmap.isRecycled()) {
            if (bitmap.isMutable()) {
                Objects.requireNonNull((SizeConfigStrategy) this.strategy);
                if (((long) Util.getBitmapByteSize(bitmap)) <= this.maxSize && this.allowedConfigs.contains(bitmap.getConfig())) {
                    Objects.requireNonNull((SizeConfigStrategy) this.strategy);
                    int bitmapByteSize = Util.getBitmapByteSize(bitmap);
                    ((SizeConfigStrategy) this.strategy).put(bitmap);
                    Objects.requireNonNull(this.tracker);
                    this.puts++;
                    this.currentSize += (long) bitmapByteSize;
                    if (Log.isLoggable("LruBitmapPool", 2)) {
                        String valueOf = String.valueOf(((SizeConfigStrategy) this.strategy).logBitmap(bitmap));
                        Log.v("LruBitmapPool", valueOf.length() != 0 ? "Put bitmap in pool=".concat(valueOf) : new String("Put bitmap in pool="));
                    }
                    dump();
                    trimToSize(this.maxSize);
                    return;
                }
            }
            if (Log.isLoggable("LruBitmapPool", 2)) {
                String logBitmap = ((SizeConfigStrategy) this.strategy).logBitmap(bitmap);
                boolean isMutable = bitmap.isMutable();
                boolean contains = this.allowedConfigs.contains(bitmap.getConfig());
                StringBuilder sb = new StringBuilder(String.valueOf(logBitmap).length() + 78);
                sb.append("Reject bitmap from pool, bitmap: ");
                sb.append(logBitmap);
                sb.append(", is mutable: ");
                sb.append(isMutable);
                sb.append(", is allowed config: ");
                sb.append(contains);
                Log.v("LruBitmapPool", sb.toString());
            }
            bitmap.recycle();
        } else {
            throw new IllegalStateException("Cannot pool recycled bitmap");
        }
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    public synchronized void setSizeMultiplier(float f) {
        long round = (long) Math.round(((float) this.initialMaxSize) * f);
        this.maxSize = round;
        trimToSize(round);
    }

    @Override // com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
    @SuppressLint({"InlinedApi"})
    public void trimMemory(int i) {
        if (Log.isLoggable("LruBitmapPool", 3)) {
            StringBuilder sb = new StringBuilder(29);
            sb.append("trimMemory, level=");
            sb.append(i);
            Log.d("LruBitmapPool", sb.toString());
        }
        if (i >= 40) {
            if (Log.isLoggable("LruBitmapPool", 3)) {
                Log.d("LruBitmapPool", "clearMemory");
            }
            trimToSize(0);
        } else if (i >= 20 || i == 15) {
            trimToSize(this.maxSize / 2);
        }
    }

    public final synchronized void trimToSize(long j) {
        while (this.currentSize > j) {
            SizeConfigStrategy sizeConfigStrategy = (SizeConfigStrategy) this.strategy;
            Bitmap removeLast = sizeConfigStrategy.groupedMap.removeLast();
            if (removeLast != null) {
                sizeConfigStrategy.decrementBitmapOfSize(Integer.valueOf(Util.getBitmapByteSize(removeLast)), removeLast);
            }
            if (removeLast == null) {
                if (Log.isLoggable("LruBitmapPool", 5)) {
                    Log.w("LruBitmapPool", "Size mismatch, resetting");
                    dumpUnchecked();
                }
                this.currentSize = 0;
                return;
            }
            Objects.requireNonNull(this.tracker);
            long j2 = this.currentSize;
            Objects.requireNonNull((SizeConfigStrategy) this.strategy);
            this.currentSize = j2 - ((long) Util.getBitmapByteSize(removeLast));
            this.evictions++;
            if (Log.isLoggable("LruBitmapPool", 3)) {
                String valueOf = String.valueOf(((SizeConfigStrategy) this.strategy).logBitmap(removeLast));
                Log.d("LruBitmapPool", valueOf.length() != 0 ? "Evicting bitmap=".concat(valueOf) : new String("Evicting bitmap="));
            }
            dump();
            removeLast.recycle();
        }
    }
}
