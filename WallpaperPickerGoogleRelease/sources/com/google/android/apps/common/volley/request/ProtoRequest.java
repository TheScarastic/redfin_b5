package com.google.android.apps.common.volley.request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Parser;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class ProtoRequest<ReqT extends MessageLite, ResT extends MessageLite> extends Request<ResT> {
    public final Callback<ResT> mCallback;
    public final Map<String, String> mHeaders;
    public final ReqT mRequestBody;
    public final Parser<ResT> mResponseParser;

    /* loaded from: classes.dex */
    public static final class Builder<ReqT extends MessageLite, ResT extends MessageLite> {
        public Callback<ResT> callback;
        public ReqT requestBody;
        public Parser<ResT> responseParser;
        public String url;
        public final HashMap<String, String> headers = new HashMap<>();
        public int requestMethod = 0;
    }

    /* loaded from: classes.dex */
    public interface Callback<T extends MessageLite> extends Response.Listener<T>, Response.ErrorListener {
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public ProtoRequest(com.google.android.apps.common.volley.request.ProtoRequest.Builder<ReqT, ResT> r6) {
        /*
            r5 = this;
            int r0 = r6.requestMethod
            java.lang.String r1 = r6.url
            com.google.android.apps.common.volley.request.ProtoRequest$Callback<ResT extends com.google.protobuf.MessageLite> r2 = r6.callback
            ReqT extends com.google.protobuf.MessageLite r3 = r6.requestBody
            com.google.protobuf.Parser<ResT extends com.google.protobuf.MessageLite> r4 = r6.responseParser
            java.util.HashMap<java.lang.String, java.lang.String> r6 = r6.headers
            r5.<init>(r0, r1, r2)
            r5.mCallback = r2
            r5.mRequestBody = r3
            r5.mResponseParser = r4
            java.util.HashMap r0 = new java.util.HashMap
            r0.<init>(r6)
            r5.mHeaders = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.apps.common.volley.request.ProtoRequest.<init>(com.google.android.apps.common.volley.request.ProtoRequest$Builder):void");
    }

    @Override // com.android.volley.Request
    public void deliverError(VolleyError volleyError) {
        Callback<ResT> callback = this.mCallback;
        if (callback != null) {
            callback.onErrorResponse(volleyError);
            return;
        }
        throw new IllegalStateException(volleyError);
    }

    @Override // com.android.volley.Request
    public void deliverResponse(Object obj) {
        MessageLite messageLite = (MessageLite) obj;
        Callback<ResT> callback = this.mCallback;
        if (callback != null) {
            callback.onResponse(messageLite);
        }
    }

    @Override // com.android.volley.Request
    public byte[] getBody() throws AuthFailureError {
        ReqT reqt = this.mRequestBody;
        if (reqt != null) {
            return reqt.toByteArray();
        }
        return null;
    }

    @Override // com.android.volley.Request
    public String getBodyContentType() {
        return "application/protobuf";
    }

    @Override // com.android.volley.Request
    public Map<String, String> getHeaders() {
        return this.mHeaders;
    }

    public ReqT getRequestBody() {
        return this.mRequestBody;
    }

    @Override // com.android.volley.Request
    public Response<ResT> parseNetworkResponse(NetworkResponse networkResponse) {
        Parser<ResT> parser = this.mResponseParser;
        if (parser != null) {
            try {
                return new Response<>(parser.parseFrom(networkResponse.data), HttpHeaderParser.parseCacheHeaders(networkResponse));
            } catch (Exception e) {
                return new Response<>(new ParseError(e));
            }
        } else {
            throw new IllegalStateException("must provide a praser to parse response message");
        }
    }
}
