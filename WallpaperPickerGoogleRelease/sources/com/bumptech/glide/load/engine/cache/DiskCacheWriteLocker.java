package com.bumptech.glide.load.engine.cache;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/* loaded from: classes.dex */
public final class DiskCacheWriteLocker {
    public final Map<String, WriteLock> locks = new HashMap();
    public final WriteLockPool writeLockPool = new WriteLockPool();

    /* loaded from: classes.dex */
    public static class WriteLock {
        public int interestedThreads;
        public final Lock lock = new ReentrantLock();
    }

    /* loaded from: classes.dex */
    public static class WriteLockPool {
        public final Queue<WriteLock> pool = new ArrayDeque();
    }

    public void release(String str) {
        WriteLock writeLock;
        synchronized (this) {
            writeLock = this.locks.get(str);
            Objects.requireNonNull(writeLock, "Argument must not be null");
            int i = writeLock.interestedThreads;
            if (i >= 1) {
                int i2 = i - 1;
                writeLock.interestedThreads = i2;
                if (i2 == 0) {
                    WriteLock remove = this.locks.remove(str);
                    if (remove.equals(writeLock)) {
                        WriteLockPool writeLockPool = this.writeLockPool;
                        synchronized (writeLockPool.pool) {
                            if (writeLockPool.pool.size() < 10) {
                                writeLockPool.pool.offer(remove);
                            }
                        }
                    } else {
                        String valueOf = String.valueOf(writeLock);
                        String valueOf2 = String.valueOf(remove);
                        StringBuilder sb = new StringBuilder(valueOf.length() + 79 + valueOf2.length() + str.length());
                        sb.append("Removed the wrong lock, expected to remove: ");
                        sb.append(valueOf);
                        sb.append(", but actually removed: ");
                        sb.append(valueOf2);
                        sb.append(", safeKey: ");
                        sb.append(str);
                        throw new IllegalStateException(sb.toString());
                    }
                }
            } else {
                int i3 = writeLock.interestedThreads;
                StringBuilder sb2 = new StringBuilder(str.length() + 81);
                sb2.append("Cannot release a lock that is not held, safeKey: ");
                sb2.append(str);
                sb2.append(", interestedThreads: ");
                sb2.append(i3);
                throw new IllegalStateException(sb2.toString());
            }
        }
        writeLock.lock.unlock();
    }
}
