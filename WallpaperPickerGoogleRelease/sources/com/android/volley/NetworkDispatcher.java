package com.android.volley;

import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.android.volley.ExecutorDelivery;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
/* loaded from: classes.dex */
public class NetworkDispatcher extends Thread {
    public final Cache mCache;
    public final ResponseDelivery mDelivery;
    public final Network mNetwork;
    public final BlockingQueue<Request<?>> mQueue;
    public volatile boolean mQuit = false;

    public NetworkDispatcher(BlockingQueue<Request<?>> blockingQueue, Network network, Cache cache, ResponseDelivery responseDelivery) {
        this.mQueue = blockingQueue;
        this.mNetwork = network;
        this.mCache = cache;
        this.mDelivery = responseDelivery;
    }

    public void processRequest(Request<?> request) {
        NetworkResponse performRequest;
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            request.sendEvent(3);
            try {
                request.addMarker("network-queue-take");
                request.isCanceled();
                TrafficStats.setThreadStatsTag(request.mDefaultTrafficStatsTag);
                performRequest = ((BasicNetwork) this.mNetwork).performRequest(request);
                request.addMarker("network-http-complete");
            } catch (VolleyError e) {
                e.setNetworkTimeMs(SystemClock.elapsedRealtime() - elapsedRealtime);
                ExecutorDelivery executorDelivery = (ExecutorDelivery) this.mDelivery;
                Objects.requireNonNull(executorDelivery);
                request.addMarker("post-error");
                executorDelivery.mResponsePoster.execute(new ExecutorDelivery.ResponseDeliveryRunnable(request, new Response(e), null));
                request.notifyListenerResponseNotUsable();
            } catch (Exception e2) {
                Log.e("Volley", VolleyLog.buildMessage("Unhandled exception %s", e2.toString()), e2);
                VolleyError volleyError = new VolleyError(e2);
                volleyError.setNetworkTimeMs(SystemClock.elapsedRealtime() - elapsedRealtime);
                ExecutorDelivery executorDelivery2 = (ExecutorDelivery) this.mDelivery;
                Objects.requireNonNull(executorDelivery2);
                request.addMarker("post-error");
                executorDelivery2.mResponsePoster.execute(new ExecutorDelivery.ResponseDeliveryRunnable(request, new Response(volleyError), null));
                request.notifyListenerResponseNotUsable();
            }
            if (!performRequest.notModified || !request.hasHadResponseDelivered()) {
                Response<?> parseNetworkResponse = request.parseNetworkResponse(performRequest);
                request.addMarker("network-parse-complete");
                if (request.mShouldCache && parseNetworkResponse.cacheEntry != null) {
                    ((DiskBasedCache) this.mCache).put(request.getCacheKey(), parseNetworkResponse.cacheEntry);
                    request.addMarker("network-cache-written");
                }
                request.markDelivered();
                ((ExecutorDelivery) this.mDelivery).postResponse(request, parseNetworkResponse, null);
                request.notifyListenerResponseReceived(parseNetworkResponse);
                return;
            }
            request.finish("not-modified");
            request.notifyListenerResponseNotUsable();
        } finally {
            request.sendEvent(4);
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        Process.setThreadPriority(10);
        while (true) {
            try {
                processRequest(this.mQueue.take());
            } catch (InterruptedException unused) {
                if (this.mQuit) {
                    Thread.currentThread().interrupt();
                    return;
                }
                VolleyLog.e("Ignoring spurious interrupt of NetworkDispatcher thread; use quit() to terminate it", new Object[0]);
            }
        }
    }
}
