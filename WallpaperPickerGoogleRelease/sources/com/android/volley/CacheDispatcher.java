package com.android.volley;

import android.os.Process;
import com.android.volley.Cache;
import com.android.volley.toolbox.DiskBasedCache;
import java.util.concurrent.BlockingQueue;
/* loaded from: classes.dex */
public class CacheDispatcher extends Thread {
    public static final boolean DEBUG = VolleyLog.DEBUG;
    public final Cache mCache;
    public final BlockingQueue<Request<?>> mCacheQueue;
    public final ResponseDelivery mDelivery;
    public final BlockingQueue<Request<?>> mNetworkQueue;
    public volatile boolean mQuit = false;
    public final WaitingRequestManager mWaitingRequestManager;

    public CacheDispatcher(BlockingQueue<Request<?>> blockingQueue, BlockingQueue<Request<?>> blockingQueue2, Cache cache, ResponseDelivery responseDelivery) {
        this.mCacheQueue = blockingQueue;
        this.mNetworkQueue = blockingQueue2;
        this.mCache = cache;
        this.mDelivery = responseDelivery;
        this.mWaitingRequestManager = new WaitingRequestManager(this, blockingQueue2, responseDelivery);
    }

    public void processRequest(final Request<?> request) throws InterruptedException {
        request.addMarker("cache-queue-take");
        request.sendEvent(1);
        try {
            request.isCanceled();
            Cache.Entry entry = ((DiskBasedCache) this.mCache).get(request.getCacheKey());
            if (entry == null) {
                request.addMarker("cache-miss");
                if (!this.mWaitingRequestManager.maybeAddToWaitingRequests(request)) {
                    this.mNetworkQueue.put(request);
                }
                return;
            }
            boolean z = false;
            if (entry.ttl < System.currentTimeMillis()) {
                request.addMarker("cache-hit-expired");
                request.mCacheEntry = entry;
                if (!this.mWaitingRequestManager.maybeAddToWaitingRequests(request)) {
                    this.mNetworkQueue.put(request);
                }
                return;
            }
            request.addMarker("cache-hit");
            Response<?> parseNetworkResponse = request.parseNetworkResponse(new NetworkResponse(entry.data, entry.responseHeaders));
            request.addMarker("cache-hit-parsed");
            if (!(parseNetworkResponse.error == null)) {
                request.addMarker("cache-parsing-failed");
                Cache cache = this.mCache;
                String cacheKey = request.getCacheKey();
                DiskBasedCache diskBasedCache = (DiskBasedCache) cache;
                synchronized (diskBasedCache) {
                    Cache.Entry entry2 = diskBasedCache.get(cacheKey);
                    if (entry2 != null) {
                        entry2.softTtl = 0;
                        entry2.ttl = 0;
                        diskBasedCache.put(cacheKey, entry2);
                    }
                }
                request.mCacheEntry = null;
                if (!this.mWaitingRequestManager.maybeAddToWaitingRequests(request)) {
                    this.mNetworkQueue.put(request);
                }
                return;
            }
            if (entry.softTtl < System.currentTimeMillis()) {
                z = true;
            }
            if (!z) {
                ((ExecutorDelivery) this.mDelivery).postResponse(request, parseNetworkResponse, null);
            } else {
                request.addMarker("cache-hit-refresh-needed");
                request.mCacheEntry = entry;
                parseNetworkResponse.intermediate = true;
                if (!this.mWaitingRequestManager.maybeAddToWaitingRequests(request)) {
                    ((ExecutorDelivery) this.mDelivery).postResponse(request, parseNetworkResponse, new Runnable() { // from class: com.android.volley.CacheDispatcher.1
                        @Override // java.lang.Runnable
                        public void run() {
                            try {
                                CacheDispatcher.this.mNetworkQueue.put(request);
                            } catch (InterruptedException unused) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                } else {
                    ((ExecutorDelivery) this.mDelivery).postResponse(request, parseNetworkResponse, null);
                }
            }
        } finally {
            request.sendEvent(2);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        if (DEBUG) {
            VolleyLog.v("start new dispatcher", new Object[0]);
        }
        Process.setThreadPriority(10);
        ((DiskBasedCache) this.mCache).initialize();
        while (true) {
            try {
                processRequest(this.mCacheQueue.take());
            } catch (InterruptedException unused) {
                if (this.mQuit) {
                    Thread.currentThread().interrupt();
                    return;
                }
                VolleyLog.e("Ignoring spurious interrupt of CacheDispatcher thread; use quit() to terminate it", new Object[0]);
            }
        }
    }
}
