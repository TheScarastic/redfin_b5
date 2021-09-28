package com.android.volley;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public class RequestQueue {
    public final Cache mCache;
    public CacheDispatcher mCacheDispatcher;
    public final ResponseDelivery mDelivery;
    public final Network mNetwork;
    public final AtomicInteger mSequenceGenerator = new AtomicInteger();
    public final Set<Request<?>> mCurrentRequests = new HashSet();
    public final PriorityBlockingQueue<Request<?>> mCacheQueue = new PriorityBlockingQueue<>();
    public final PriorityBlockingQueue<Request<?>> mNetworkQueue = new PriorityBlockingQueue<>();
    public final List<RequestFinishedListener> mFinishedListeners = new ArrayList();
    public final List<RequestEventListener> mEventListeners = new ArrayList();
    public final NetworkDispatcher[] mDispatchers = new NetworkDispatcher[4];

    /* loaded from: classes.dex */
    public interface RequestEventListener {
        void onRequestEvent(Request<?> request, int i);
    }

    @Deprecated
    /* loaded from: classes.dex */
    public interface RequestFinishedListener<T> {
        void onRequestFinished(Request<T> request);
    }

    public RequestQueue(Cache cache, Network network) {
        ExecutorDelivery executorDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
        this.mCache = cache;
        this.mNetwork = network;
        this.mDelivery = executorDelivery;
    }

    public void sendRequestEvent(Request<?> request, int i) {
        synchronized (this.mEventListeners) {
            for (RequestEventListener requestEventListener : this.mEventListeners) {
                requestEventListener.onRequestEvent(request, i);
            }
        }
    }
}
