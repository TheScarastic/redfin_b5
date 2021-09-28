package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.disklrucache.Util;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.DataCacheWriter;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheWriteLocker;
import java.io.File;
import java.io.IOException;
/* loaded from: classes.dex */
public class DiskLruCacheWrapper implements DiskCache {
    public final File directory;
    public DiskLruCache diskLruCache;
    public final long maxSize;
    public final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();
    public final SafeKeyGenerator safeKeyGenerator = new SafeKeyGenerator();

    @Deprecated
    public DiskLruCacheWrapper(File file, long j) {
        this.directory = file;
        this.maxSize = j;
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache
    public synchronized void clear() {
        try {
            DiskLruCache diskCache = getDiskCache();
            diskCache.close();
            Util.deleteContents(diskCache.directory);
        } catch (IOException e) {
            if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                Log.w("DiskLruCacheWrapper", "Unable to clear disk cache or disk cache cleared externally", e);
            }
        }
        resetDiskCache();
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache
    public File get(Key key) {
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
            String valueOf = String.valueOf(key);
            Log.v("DiskLruCacheWrapper", Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf.length() + safeKey.length() + 29, "Get: Obtained: ", safeKey, " for for Key: ", valueOf));
        }
        try {
            DiskLruCache.Value value = getDiskCache().get(safeKey);
            if (value != null) {
                return value.files[0];
            }
            return null;
        } catch (IOException e) {
            if (!Log.isLoggable("DiskLruCacheWrapper", 5)) {
                return null;
            }
            Log.w("DiskLruCacheWrapper", "Unable to get from disk cache", e);
            return null;
        }
    }

    public final synchronized DiskLruCache getDiskCache() throws IOException {
        if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
        }
        return this.diskLruCache;
    }

    @Override // com.bumptech.glide.load.engine.cache.DiskCache
    public void put(Key key, DiskCache.Writer writer) {
        DiskCacheWriteLocker.WriteLock writeLock;
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        DiskCacheWriteLocker diskCacheWriteLocker = this.writeLocker;
        synchronized (diskCacheWriteLocker) {
            writeLock = diskCacheWriteLocker.locks.get(safeKey);
            if (writeLock == null) {
                DiskCacheWriteLocker.WriteLockPool writeLockPool = diskCacheWriteLocker.writeLockPool;
                synchronized (writeLockPool.pool) {
                    writeLock = writeLockPool.pool.poll();
                }
                if (writeLock == null) {
                    writeLock = new DiskCacheWriteLocker.WriteLock();
                }
                diskCacheWriteLocker.locks.put(safeKey, writeLock);
            }
            writeLock.interestedThreads++;
        }
        writeLock.lock.lock();
        try {
            if (Log.isLoggable("DiskLruCacheWrapper", 2)) {
                String valueOf = String.valueOf(key);
                StringBuilder sb = new StringBuilder(safeKey.length() + 29 + valueOf.length());
                sb.append("Put: Obtained: ");
                sb.append(safeKey);
                sb.append(" for for Key: ");
                sb.append(valueOf);
                Log.v("DiskLruCacheWrapper", sb.toString());
            }
            try {
                DiskLruCache diskCache = getDiskCache();
                if (diskCache.get(safeKey) == null) {
                    DiskLruCache.Editor edit = diskCache.edit(safeKey);
                    if (edit == null) {
                        throw new IllegalStateException(safeKey.length() != 0 ? "Had two simultaneous puts for: ".concat(safeKey) : new String("Had two simultaneous puts for: "));
                    }
                    try {
                        DataCacheWriter dataCacheWriter = (DataCacheWriter) writer;
                        if (dataCacheWriter.encoder.encode(dataCacheWriter.data, edit.getFile(0), dataCacheWriter.options)) {
                            DiskLruCache.access$2100(DiskLruCache.this, edit, true);
                            edit.committed = true;
                        }
                    } finally {
                        if (!edit.committed) {
                            try {
                                edit.abort();
                            } catch (IOException unused) {
                            }
                        }
                    }
                }
            } catch (IOException e) {
                if (Log.isLoggable("DiskLruCacheWrapper", 5)) {
                    Log.w("DiskLruCacheWrapper", "Unable to put to disk cache", e);
                }
            }
        } finally {
            this.writeLocker.release(safeKey);
        }
    }

    public final synchronized void resetDiskCache() {
        this.diskLruCache = null;
    }
}
