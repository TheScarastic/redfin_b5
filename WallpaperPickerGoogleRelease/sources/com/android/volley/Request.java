package com.android.volley;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.text.TextUtils;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class Request<T> implements Comparable<Request<T>> {
    public Cache.Entry mCacheEntry;
    public final int mDefaultTrafficStatsTag;
    public Response.ErrorListener mErrorListener;
    public final VolleyLog.MarkerLog mEventLog;
    public final Object mLock;
    public final int mMethod;
    public NetworkRequestCompleteListener mRequestCompleteListener;
    public RequestQueue mRequestQueue;
    public boolean mResponseDelivered;
    public DefaultRetryPolicy mRetryPolicy;
    public Integer mSequence;
    public boolean mShouldCache;
    public final String mUrl;

    /* loaded from: classes.dex */
    public interface NetworkRequestCompleteListener {
    }

    /* loaded from: classes.dex */
    public enum Priority {
        /* Fake field, exist only in values array */
        LOW,
        NORMAL,
        /* Fake field, exist only in values array */
        HIGH,
        /* Fake field, exist only in values array */
        IMMEDIATE
    }

    public Request(int i, String str, Response.ErrorListener errorListener) {
        Uri parse;
        String host;
        this.mEventLog = VolleyLog.MarkerLog.ENABLED ? new VolleyLog.MarkerLog() : null;
        this.mLock = new Object();
        this.mShouldCache = true;
        int i2 = 0;
        this.mResponseDelivered = false;
        this.mCacheEntry = null;
        this.mMethod = i;
        this.mUrl = str;
        this.mErrorListener = errorListener;
        this.mRetryPolicy = new DefaultRetryPolicy();
        if (!(TextUtils.isEmpty(str) || (parse = Uri.parse(str)) == null || (host = parse.getHost()) == null)) {
            i2 = host.hashCode();
        }
        this.mDefaultTrafficStatsTag = i2;
    }

    public void addMarker(String str) {
        if (VolleyLog.MarkerLog.ENABLED) {
            this.mEventLog.add(str, Thread.currentThread().getId());
        }
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        Request request = (Request) obj;
        Objects.requireNonNull(request);
        return this.mSequence.intValue() - request.mSequence.intValue();
    }

    public void deliverError(VolleyError volleyError) {
        Response.ErrorListener errorListener;
        synchronized (this.mLock) {
            errorListener = this.mErrorListener;
        }
        if (errorListener != null) {
            errorListener.onErrorResponse(volleyError);
        }
    }

    public abstract void deliverResponse(T t);

    public void finish(final String str) {
        RequestQueue requestQueue = this.mRequestQueue;
        if (requestQueue != null) {
            synchronized (requestQueue.mCurrentRequests) {
                requestQueue.mCurrentRequests.remove(this);
            }
            synchronized (requestQueue.mFinishedListeners) {
                for (RequestQueue.RequestFinishedListener requestFinishedListener : requestQueue.mFinishedListeners) {
                    requestFinishedListener.onRequestFinished(this);
                }
            }
            requestQueue.sendRequestEvent(this, 5);
        }
        if (VolleyLog.MarkerLog.ENABLED) {
            final long id = Thread.currentThread().getId();
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.android.volley.Request.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Request.this.mEventLog.add(str, id);
                        Request request = Request.this;
                        request.mEventLog.finish(request.toString());
                    }
                });
                return;
            }
            this.mEventLog.add(str, id);
            this.mEventLog.finish(toString());
        }
    }

    public byte[] getBody() throws AuthFailureError {
        return null;
    }

    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    public String getCacheKey() {
        String str = this.mUrl;
        int i = this.mMethod;
        if (i == 0 || i == -1) {
            return str;
        }
        return Integer.toString(i) + '-' + str;
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return Collections.emptyMap();
    }

    public boolean hasHadResponseDelivered() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mResponseDelivered;
        }
        return z;
    }

    public boolean isCanceled() {
        synchronized (this.mLock) {
        }
        return false;
    }

    public void markDelivered() {
        synchronized (this.mLock) {
            this.mResponseDelivered = true;
        }
    }

    public void notifyListenerResponseNotUsable() {
        NetworkRequestCompleteListener networkRequestCompleteListener;
        synchronized (this.mLock) {
            networkRequestCompleteListener = this.mRequestCompleteListener;
        }
        if (networkRequestCompleteListener != null) {
            ((WaitingRequestManager) networkRequestCompleteListener).onNoUsableResponseReceived(this);
        }
    }

    public void notifyListenerResponseReceived(Response<?> response) {
        NetworkRequestCompleteListener networkRequestCompleteListener;
        List<Request<?>> remove;
        synchronized (this.mLock) {
            networkRequestCompleteListener = this.mRequestCompleteListener;
        }
        if (networkRequestCompleteListener != null) {
            WaitingRequestManager waitingRequestManager = (WaitingRequestManager) networkRequestCompleteListener;
            Cache.Entry entry = response.cacheEntry;
            if (entry != null) {
                if (!(entry.ttl < System.currentTimeMillis())) {
                    String cacheKey = getCacheKey();
                    synchronized (waitingRequestManager) {
                        remove = waitingRequestManager.mWaitingRequests.remove(cacheKey);
                    }
                    if (remove != null) {
                        if (VolleyLog.DEBUG) {
                            VolleyLog.v("Releasing %d waiting requests for cacheKey=%s.", Integer.valueOf(remove.size()), cacheKey);
                        }
                        for (Request<?> request : remove) {
                            ((ExecutorDelivery) waitingRequestManager.mResponseDelivery).postResponse(request, response, null);
                        }
                        return;
                    }
                    return;
                }
            }
            waitingRequestManager.onNoUsableResponseReceived(this);
        }
    }

    public abstract Response<T> parseNetworkResponse(NetworkResponse networkResponse);

    public void sendEvent(int i) {
        RequestQueue requestQueue = this.mRequestQueue;
        if (requestQueue != null) {
            requestQueue.sendRequestEvent(this, i);
        }
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("0x");
        m.append(Integer.toHexString(this.mDefaultTrafficStatsTag));
        String sb = m.toString();
        StringBuilder sb2 = new StringBuilder();
        isCanceled();
        sb2.append("[ ] ");
        sb2.append(this.mUrl);
        sb2.append(" ");
        sb2.append(sb);
        sb2.append(" ");
        sb2.append(Priority.NORMAL);
        sb2.append(" ");
        sb2.append(this.mSequence);
        return sb2.toString();
    }
}
