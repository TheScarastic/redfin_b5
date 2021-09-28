package com.bumptech.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentTabHost$SavedState$$ExternalSyntheticOutline0;
import android.util.Log;
import androidx.core.content.ContextCompat;
import androidx.preference.R$string$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.ConnectivityMonitor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitor;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.manager.NullConnectivityMonitor;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.manager.TargetTracker;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: classes.dex */
public class RequestManager implements LifecycleListener {
    public static final RequestOptions DECODE_TYPE_BITMAP;
    public static final RequestOptions DOWNLOAD_ONLY_OPTIONS = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    public final Runnable addSelfToLifecycle;
    public final ConnectivityMonitor connectivityMonitor;
    public final Context context;
    public final CopyOnWriteArrayList<RequestListener<Object>> defaultRequestListeners;
    public final Glide glide;
    public final Lifecycle lifecycle;
    public final Handler mainHandler;
    public RequestOptions requestOptions;
    public final RequestTracker requestTracker;
    public final TargetTracker targetTracker = new TargetTracker();
    public final RequestManagerTreeNode treeNode;

    /* loaded from: classes.dex */
    public static class RequestManagerConnectivityListener implements ConnectivityMonitor.ConnectivityListener {
        public final RequestTracker requestTracker;

        public RequestManagerConnectivityListener(RequestTracker requestTracker) {
            this.requestTracker = requestTracker;
        }
    }

    static {
        RequestOptions decode = new RequestOptions().decode(Bitmap.class);
        decode.isLocked = true;
        DECODE_TYPE_BITMAP = decode;
        new RequestOptions().decode(GifDrawable.class).isLocked = true;
    }

    public RequestManager(Glide glide, Lifecycle lifecycle, RequestManagerTreeNode requestManagerTreeNode, Context context) {
        RequestTracker requestTracker = new RequestTracker();
        ConnectivityMonitorFactory connectivityMonitorFactory = glide.connectivityMonitorFactory;
        AnonymousClass1 r2 = new Runnable() { // from class: com.bumptech.glide.RequestManager.1
            @Override // java.lang.Runnable
            public void run() {
                RequestManager requestManager = RequestManager.this;
                requestManager.lifecycle.addListener(requestManager);
            }
        };
        this.addSelfToLifecycle = r2;
        Handler handler = new Handler(Looper.getMainLooper());
        this.mainHandler = handler;
        this.glide = glide;
        this.lifecycle = lifecycle;
        this.treeNode = requestManagerTreeNode;
        this.requestTracker = requestTracker;
        this.context = context;
        Context applicationContext = context.getApplicationContext();
        RequestManagerConnectivityListener requestManagerConnectivityListener = new RequestManagerConnectivityListener(requestTracker);
        Objects.requireNonNull((DefaultConnectivityMonitorFactory) connectivityMonitorFactory);
        boolean z = ContextCompat.checkSelfPermission(applicationContext, "android.permission.ACCESS_NETWORK_STATE") == 0;
        if (Log.isLoggable("ConnectivityMonitor", 3)) {
            Log.d("ConnectivityMonitor", z ? "ACCESS_NETWORK_STATE permission granted, registering connectivity monitor" : "ACCESS_NETWORK_STATE permission missing, cannot register connectivity monitor");
        }
        ConnectivityMonitor defaultConnectivityMonitor = z ? new DefaultConnectivityMonitor(applicationContext, requestManagerConnectivityListener) : new NullConnectivityMonitor();
        this.connectivityMonitor = defaultConnectivityMonitor;
        if (Util.isOnBackgroundThread()) {
            handler.post(r2);
        } else {
            lifecycle.addListener(this);
        }
        lifecycle.addListener(defaultConnectivityMonitor);
        this.defaultRequestListeners = new CopyOnWriteArrayList<>(glide.glideContext.defaultRequestListeners);
        RequestOptions clone = glide.glideContext.defaultRequestOptions.clone();
        clone.autoClone();
        this.requestOptions = clone;
        synchronized (glide.managers) {
            if (!glide.managers.contains(this)) {
                glide.managers.add(this);
            } else {
                throw new IllegalStateException("Cannot register already registered manager");
            }
        }
    }

    public <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> cls) {
        return new RequestBuilder<>(this.glide, this, cls, this.context);
    }

    public RequestBuilder<Bitmap> asBitmap() {
        return as(Bitmap.class).apply((BaseRequestOptions<?>) DECODE_TYPE_BITMAP);
    }

    public RequestBuilder<Drawable> asDrawable() {
        return as(Drawable.class);
    }

    public void clear(final Target<?> target) {
        boolean z;
        if (target != null) {
            if (!Util.isOnMainThread()) {
                this.mainHandler.post(new Runnable() { // from class: com.bumptech.glide.RequestManager.2
                    @Override // java.lang.Runnable
                    public void run() {
                        RequestManager.this.clear(target);
                    }
                });
            } else if (!untrack(target)) {
                Glide glide = this.glide;
                synchronized (glide.managers) {
                    Iterator<RequestManager> it = glide.managers.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (it.next().untrack(target)) {
                                z = true;
                                break;
                            }
                        } else {
                            z = false;
                            break;
                        }
                    }
                }
                if (!z && target.getRequest() != null) {
                    Request request = target.getRequest();
                    target.setRequest(null);
                    request.clear();
                }
            }
        }
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onDestroy() {
        this.targetTracker.onDestroy();
        Iterator it = ((ArrayList) Util.getSnapshot(this.targetTracker.targets)).iterator();
        while (it.hasNext()) {
            clear((Target) it.next());
        }
        this.targetTracker.targets.clear();
        RequestTracker requestTracker = this.requestTracker;
        Iterator it2 = ((ArrayList) Util.getSnapshot(requestTracker.requests)).iterator();
        while (it2.hasNext()) {
            requestTracker.clearRemoveAndMaybeRecycle((Request) it2.next(), false);
        }
        requestTracker.pendingRequests.clear();
        this.lifecycle.removeListener(this);
        this.lifecycle.removeListener(this.connectivityMonitor);
        this.mainHandler.removeCallbacks(this.addSelfToLifecycle);
        Glide glide = this.glide;
        synchronized (glide.managers) {
            if (glide.managers.contains(this)) {
                glide.managers.remove(this);
            } else {
                throw new IllegalStateException("Cannot unregister not yet registered manager");
            }
        }
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStart() {
        Util.assertMainThread();
        RequestTracker requestTracker = this.requestTracker;
        requestTracker.isPaused = false;
        Iterator it = ((ArrayList) Util.getSnapshot(requestTracker.requests)).iterator();
        while (it.hasNext()) {
            Request request = (Request) it.next();
            if (!request.isComplete() && !request.isRunning()) {
                request.begin();
            }
        }
        requestTracker.pendingRequests.clear();
        this.targetTracker.onStart();
    }

    @Override // com.bumptech.glide.manager.LifecycleListener
    public void onStop() {
        Util.assertMainThread();
        RequestTracker requestTracker = this.requestTracker;
        requestTracker.isPaused = true;
        Iterator it = ((ArrayList) Util.getSnapshot(requestTracker.requests)).iterator();
        while (it.hasNext()) {
            Request request = (Request) it.next();
            if (request.isRunning()) {
                request.clear();
                requestTracker.pendingRequests.add(request);
            }
        }
        this.targetTracker.onStop();
    }

    public String toString() {
        String obj = super.toString();
        String valueOf = String.valueOf(this.requestTracker);
        String valueOf2 = String.valueOf(this.treeNode);
        return FragmentTabHost$SavedState$$ExternalSyntheticOutline0.m(R$string$$ExternalSyntheticOutline0.m(valueOf2.length() + valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(obj, 21), obj, "{tracker=", valueOf, ", treeNode="), valueOf2, "}");
    }

    public boolean untrack(Target<?> target) {
        Request request = target.getRequest();
        if (request == null) {
            return true;
        }
        if (!this.requestTracker.clearRemoveAndMaybeRecycle(request, true)) {
            return false;
        }
        this.targetTracker.targets.remove(target);
        target.setRequest(null);
        return true;
    }
}
