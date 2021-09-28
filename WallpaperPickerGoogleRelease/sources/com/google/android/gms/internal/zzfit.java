package com.google.android.gms.internal;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import androidx.preference.R$id;
import com.android.volley.CacheDispatcher;
import com.android.volley.NetworkDispatcher;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley$1;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.model.stream.HttpGlideUrlLoader;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public /* synthetic */ class zzfit implements zzfiy {
    public final /* synthetic */ int $r8$classId;
    public Object zza;
    public Object zzb;

    public zzfit(zzfis zzfis, zzfiq zzfiq) {
        this.$r8$classId = 0;
        this.zza = zzfis;
        this.zzb = zzfiq;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        RequestQueue requestQueue = (RequestQueue) this.zza;
        Objects.requireNonNull(requestQueue);
        request.mRequestQueue = requestQueue;
        synchronized (requestQueue.mCurrentRequests) {
            requestQueue.mCurrentRequests.add(request);
        }
        request.mSequence = Integer.valueOf(requestQueue.mSequenceGenerator.incrementAndGet());
        request.addMarker("add-to-queue");
        requestQueue.sendRequestEvent(request, 0);
        if (!request.mShouldCache) {
            requestQueue.mNetworkQueue.add(request);
        } else {
            requestQueue.mCacheQueue.add(request);
        }
    }

    public void loadImageBitmap(Uri uri, Target<Bitmap> target) {
        try {
            RequestBuilder<Bitmap> asBitmap = Glide.with((Context) this.zzb).asBitmap();
            asBitmap.model = uri;
            asBitmap.isModelSet = true;
            RequestBuilder<Bitmap> apply = asBitmap.apply((BaseRequestOptions<?>) RequestOptions.noTransformation()).apply((BaseRequestOptions<?>) RequestOptions.option(HttpGlideUrlLoader.TIMEOUT, 10000));
            apply.into(target, null, apply);
        } catch (Exception e) {
            Log.e("WallpaperRequester", "Unable to get Bitmap for image with url: " + uri, e);
        }
    }

    public File loadImageFile(Uri uri) {
        try {
            RequestManager with = Glide.with((Context) this.zzb);
            Objects.requireNonNull(with);
            RequestBuilder apply = with.as(File.class).apply((BaseRequestOptions<?>) RequestManager.DOWNLOAD_ONLY_OPTIONS);
            apply.model = uri;
            apply.isModelSet = true;
            return (File) ((RequestFutureTarget) apply.apply((BaseRequestOptions<?>) RequestOptions.option(HttpGlideUrlLoader.TIMEOUT, 10000)).submit()).get();
        } catch (Exception unused) {
            Log.e("WallpaperRequester", "Unable to get File for image with url: " + uri);
            return null;
        }
    }

    public String toString() {
        switch (this.$r8$classId) {
            case 2:
                StringBuilder sb = new StringBuilder(100);
                sb.append(this.zzb.getClass().getSimpleName());
                sb.append('{');
                int size = ((List) this.zza).size();
                for (int i = 0; i < size; i++) {
                    sb.append((String) ((List) this.zza).get(i));
                    if (i < size - 1) {
                        sb.append(", ");
                    }
                }
                sb.append('}');
                return sb.toString();
            default:
                return super.toString();
        }
    }

    @Override // com.google.android.gms.internal.zzfiy
    public Object zza() {
        zzfis zzfis = (zzfis) this.zza;
        zzfiq zzfiq = (zzfiq) this.zzb;
        Objects.requireNonNull(zzfiq);
        Map<String, String> zzc = zzfis.zza("gms:phenotype:phenotype_flag:debug_disable_caching") ? zzfiq.zzc() : zzfiq.zzf;
        if (zzc == null) {
            synchronized (zzfiq.zze) {
                zzc = zzfiq.zzf;
                if (zzc == null) {
                    zzc = zzfiq.zzc();
                    zzfiq.zzf = zzc;
                }
            }
        }
        if (zzc == null) {
            zzc = Collections.emptyMap();
        }
        return zzc.get(zzfis.zza);
    }

    public zzfit(Object obj) {
        this.$r8$classId = 2;
        Objects.requireNonNull(obj, "null reference");
        this.zzb = obj;
        this.zza = new ArrayList();
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public /* synthetic */ zzfit(Object obj, R$id r$id) {
        this(obj);
        this.$r8$classId = 2;
    }

    public zzfit(Context context) {
        this.$r8$classId = 1;
        this.zzb = context.getApplicationContext();
        RequestQueue requestQueue = new RequestQueue(new DiskBasedCache(new Volley$1(context.getApplicationContext().getApplicationContext())), new BasicNetwork(new HurlStack()));
        CacheDispatcher cacheDispatcher = requestQueue.mCacheDispatcher;
        if (cacheDispatcher != null) {
            cacheDispatcher.mQuit = true;
            cacheDispatcher.interrupt();
        }
        NetworkDispatcher[] networkDispatcherArr = requestQueue.mDispatchers;
        for (NetworkDispatcher networkDispatcher : networkDispatcherArr) {
            if (networkDispatcher != null) {
                networkDispatcher.mQuit = true;
                networkDispatcher.interrupt();
            }
        }
        CacheDispatcher cacheDispatcher2 = new CacheDispatcher(requestQueue.mCacheQueue, requestQueue.mNetworkQueue, requestQueue.mCache, requestQueue.mDelivery);
        requestQueue.mCacheDispatcher = cacheDispatcher2;
        cacheDispatcher2.start();
        for (int i = 0; i < requestQueue.mDispatchers.length; i++) {
            NetworkDispatcher networkDispatcher2 = new NetworkDispatcher(requestQueue.mNetworkQueue, requestQueue.mNetwork, requestQueue.mCache, requestQueue.mDelivery);
            requestQueue.mDispatchers[i] = networkDispatcher2;
            networkDispatcher2.start();
        }
        this.zza = requestQueue;
    }

    public zzfit zza(String str, Object obj) {
        String valueOf = String.valueOf(obj);
        StringBuilder sb = new StringBuilder(valueOf.length() + str.length() + 1);
        sb.append(str);
        sb.append("=");
        sb.append(valueOf);
        ((List) this.zza).add(sb.toString());
        return this;
    }
}
