package com.android.volley.toolbox;

import android.os.SystemClock;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Header;
import com.android.volley.Network;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public class BasicNetwork implements Network {
    public final BaseHttpStack mBaseHttpStack;
    public final ByteArrayPool mPool;

    public BasicNetwork(BaseHttpStack baseHttpStack) {
        ByteArrayPool byteArrayPool = new ByteArrayPool(QuickStepContract.SYSUI_STATE_TRACING_ENABLED);
        this.mBaseHttpStack = baseHttpStack;
        this.mPool = byteArrayPool;
    }

    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        byte[] bArr;
        IOException e;
        HttpResponse executeRequest;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        while (true) {
            Collections.emptyList();
            HttpResponse httpResponse = null;
            try {
                executeRequest = this.mBaseHttpStack.executeRequest(request, HttpHeaderParser.getCacheHeaders(request.mCacheEntry));
            } catch (IOException e2) {
                e = e2;
                bArr = null;
            }
            try {
                int i = executeRequest.mStatusCode;
                List<Header> headers = executeRequest.getHeaders();
                if (i == 304) {
                    return NetworkUtility.getNotModifiedNetworkResponse(request, SystemClock.elapsedRealtime() - elapsedRealtime, headers);
                }
                InputStream inputStream = executeRequest.mContent;
                if (inputStream == null) {
                    inputStream = null;
                }
                byte[] inputStreamToBytes = inputStream != null ? NetworkUtility.inputStreamToBytes(inputStream, executeRequest.mContentLength, this.mPool) : new byte[0];
                NetworkUtility.logSlowRequests(SystemClock.elapsedRealtime() - elapsedRealtime, request, inputStreamToBytes, i);
                if (i >= 200 && i <= 299) {
                    return new NetworkResponse(i, inputStreamToBytes, false, SystemClock.elapsedRealtime() - elapsedRealtime, headers);
                }
                throw new IOException();
            } catch (IOException e3) {
                e = e3;
                bArr = null;
                httpResponse = executeRequest;
                if (e instanceof SocketTimeoutException) {
                    NetworkUtility.attemptRetryOnException("socket", request, new TimeoutError());
                } else if (e instanceof MalformedURLException) {
                    StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Bad URL ");
                    m.append(request.mUrl);
                    throw new RuntimeException(m.toString(), e);
                } else if (httpResponse != null) {
                    int i2 = httpResponse.mStatusCode;
                    VolleyLog.e("Unexpected response code %d for %s", Integer.valueOf(i2), request.mUrl);
                    if (bArr != null) {
                        NetworkResponse networkResponse = new NetworkResponse(i2, bArr, false, SystemClock.elapsedRealtime() - elapsedRealtime, httpResponse.getHeaders());
                        if (i2 == 401 || i2 == 403) {
                            NetworkUtility.attemptRetryOnException("auth", request, new AuthFailureError(networkResponse));
                        } else if (i2 >= 400 && i2 <= 499) {
                            throw new ClientError(networkResponse);
                        } else if (i2 < 500 || i2 > 599) {
                            throw new ServerError(networkResponse);
                        } else {
                            throw new ServerError(networkResponse);
                        }
                    } else {
                        NetworkUtility.attemptRetryOnException("network", request, new NetworkError());
                    }
                } else {
                    throw new NoConnectionError(e);
                }
            }
        }
    }
}
