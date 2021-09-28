package com.bumptech.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.manager.RequestTracker;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestCoordinator;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.request.ThumbnailRequestCoordinator;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public class RequestBuilder<TranscodeType> extends BaseRequestOptions<RequestBuilder<TranscodeType>> {
    public final Context context;
    public final GlideContext glideContext;
    public boolean isDefaultTransitionOptionsSet = true;
    public boolean isModelSet;
    public boolean isThumbnailBuilt;
    public Object model;
    public List<RequestListener<TranscodeType>> requestListeners;
    public final RequestManager requestManager;
    public RequestBuilder<TranscodeType> thumbnailBuilder;
    public final Class<TranscodeType> transcodeClass;
    public TransitionOptions<?, ? super TranscodeType> transitionOptions;

    /* renamed from: com.bumptech.glide.RequestBuilder$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType;
        public static final /* synthetic */ int[] $SwitchMap$com$bumptech$glide$Priority;

        static {
            int[] iArr = new int[Priority.values().length];
            $SwitchMap$com$bumptech$glide$Priority = iArr;
            try {
                iArr[3] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[2] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[1] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$bumptech$glide$Priority[0] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            int[] iArr2 = new int[ImageView.ScaleType.values().length];
            $SwitchMap$android$widget$ImageView$ScaleType = iArr2;
            try {
                iArr2[ImageView.ScaleType.CENTER_CROP.ordinal()] = 1;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER_INSIDE.ordinal()] = 2;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_START.ordinal()] = 4;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.FIT_XY.ordinal()] = 6;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.CENTER.ordinal()] = 7;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ImageView.ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    static {
        new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).priority(Priority.LOW).skipMemoryCache(true);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r7v4, resolved type: java.util.List<com.bumptech.glide.request.RequestListener<TranscodeType>> */
    /* JADX WARN: Multi-variable type inference failed */
    @SuppressLint({"CheckResult"})
    public RequestBuilder(Glide glide, RequestManager requestManager, Class<TranscodeType> cls, Context context) {
        this.requestManager = requestManager;
        this.transcodeClass = cls;
        this.context = context;
        GlideContext glideContext = requestManager.glide.glideContext;
        TransitionOptions<?, ?> transitionOptions = glideContext.defaultTransitionOptions.get(cls);
        if (transitionOptions == null) {
            for (Map.Entry<Class<?>, TransitionOptions<?, ?>> entry : glideContext.defaultTransitionOptions.entrySet()) {
                if (entry.getKey().isAssignableFrom(cls)) {
                    transitionOptions = (TransitionOptions<?, ? super TranscodeType>) entry.getValue();
                }
            }
        }
        this.transitionOptions = (TransitionOptions<?, ? super TranscodeType>) (transitionOptions == null ? (TransitionOptions<?, ? super TranscodeType>) GlideContext.DEFAULT_TRANSITION_OPTIONS : transitionOptions);
        this.glideContext = glide.glideContext;
        for (RequestListener<Object> requestListener : requestManager.defaultRequestListeners) {
            if (requestListener != null) {
                if (this.requestListeners == null) {
                    this.requestListeners = new ArrayList();
                }
                this.requestListeners.add(requestListener);
            }
        }
        apply((BaseRequestOptions<?>) requestManager.requestOptions);
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions
    public RequestBuilder<TranscodeType> apply(BaseRequestOptions<?> baseRequestOptions) {
        Objects.requireNonNull(baseRequestOptions, "Argument must not be null");
        return (RequestBuilder) super.apply(baseRequestOptions);
    }

    public final Request buildRequestRecursive(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2, BaseRequestOptions<?> baseRequestOptions) {
        Priority priority2;
        int i3;
        int i4;
        RequestBuilder<TranscodeType> requestBuilder = this.thumbnailBuilder;
        if (requestBuilder == null) {
            return obtainRequest(target, requestListener, baseRequestOptions, requestCoordinator, transitionOptions, priority, i, i2);
        }
        if (!this.isThumbnailBuilt) {
            TransitionOptions<?, ? super TranscodeType> transitionOptions2 = requestBuilder.isDefaultTransitionOptionsSet ? transitionOptions : requestBuilder.transitionOptions;
            if (BaseRequestOptions.isSet(requestBuilder.fields, 8)) {
                priority2 = this.thumbnailBuilder.priority;
            } else {
                priority2 = getThumbnailPriority(priority);
            }
            RequestBuilder<TranscodeType> requestBuilder2 = this.thumbnailBuilder;
            int i5 = requestBuilder2.overrideWidth;
            int i6 = requestBuilder2.overrideHeight;
            if (Util.isValidDimensions(i, i2)) {
                RequestBuilder<TranscodeType> requestBuilder3 = this.thumbnailBuilder;
                if (!Util.isValidDimensions(requestBuilder3.overrideWidth, requestBuilder3.overrideHeight)) {
                    int i7 = baseRequestOptions.overrideWidth;
                    i3 = baseRequestOptions.overrideHeight;
                    i4 = i7;
                    ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(requestCoordinator);
                    Request obtainRequest = obtainRequest(target, requestListener, baseRequestOptions, thumbnailRequestCoordinator, transitionOptions, priority, i, i2);
                    this.isThumbnailBuilt = true;
                    RequestBuilder<TranscodeType> requestBuilder4 = this.thumbnailBuilder;
                    Request buildRequestRecursive = requestBuilder4.buildRequestRecursive(target, requestListener, thumbnailRequestCoordinator, transitionOptions2, priority2, i4, i3, requestBuilder4);
                    this.isThumbnailBuilt = false;
                    thumbnailRequestCoordinator.full = obtainRequest;
                    thumbnailRequestCoordinator.thumb = buildRequestRecursive;
                    return thumbnailRequestCoordinator;
                }
            }
            i3 = i6;
            i4 = i5;
            ThumbnailRequestCoordinator thumbnailRequestCoordinator = new ThumbnailRequestCoordinator(requestCoordinator);
            Request obtainRequest = obtainRequest(target, requestListener, baseRequestOptions, thumbnailRequestCoordinator, transitionOptions, priority, i, i2);
            this.isThumbnailBuilt = true;
            RequestBuilder<TranscodeType> requestBuilder4 = this.thumbnailBuilder;
            Request buildRequestRecursive = requestBuilder4.buildRequestRecursive(target, requestListener, thumbnailRequestCoordinator, transitionOptions2, priority2, i4, i3, requestBuilder4);
            this.isThumbnailBuilt = false;
            thumbnailRequestCoordinator.full = obtainRequest;
            thumbnailRequestCoordinator.thumb = buildRequestRecursive;
            return thumbnailRequestCoordinator;
        }
        throw new IllegalStateException("You cannot use a request as both the main request and a thumbnail, consider using clone() on the request(s) passed to thumbnail()");
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions, java.lang.Object
    /* renamed from: clone */
    public BaseRequestOptions mo7clone() {
        RequestBuilder requestBuilder = (RequestBuilder) super.clone();
        requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
        return requestBuilder;
    }

    public final Priority getThumbnailPriority(Priority priority) {
        int ordinal = priority.ordinal();
        if (ordinal == 0 || ordinal == 1) {
            return Priority.IMMEDIATE;
        }
        if (ordinal == 2) {
            return Priority.HIGH;
        }
        if (ordinal == 3) {
            return Priority.NORMAL;
        }
        String valueOf = String.valueOf(this.priority);
        throw new IllegalArgumentException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 18, "unknown priority: ", valueOf));
    }

    public final <Y extends Target<TranscodeType>> Y into(Y y, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions) {
        Util.assertMainThread();
        Objects.requireNonNull(y, "Argument must not be null");
        if (this.isModelSet) {
            Request buildRequestRecursive = buildRequestRecursive(y, requestListener, null, this.transitionOptions, baseRequestOptions.priority, baseRequestOptions.overrideWidth, baseRequestOptions.overrideHeight, baseRequestOptions);
            Request request = y.getRequest();
            if (buildRequestRecursive.isEquivalentTo(request)) {
                if (!(!baseRequestOptions.isCacheable && request.isComplete())) {
                    buildRequestRecursive.recycle();
                    Objects.requireNonNull(request, "Argument must not be null");
                    if (!request.isRunning()) {
                        request.begin();
                    }
                    return y;
                }
            }
            this.requestManager.clear(y);
            y.setRequest(buildRequestRecursive);
            RequestManager requestManager = this.requestManager;
            requestManager.targetTracker.targets.add(y);
            RequestTracker requestTracker = requestManager.requestTracker;
            requestTracker.requests.add(buildRequestRecursive);
            if (!requestTracker.isPaused) {
                buildRequestRecursive.begin();
            } else {
                buildRequestRecursive.clear();
                if (Log.isLoggable("RequestTracker", 2)) {
                    Log.v("RequestTracker", "Paused, delaying request");
                }
                requestTracker.pendingRequests.add(buildRequestRecursive);
            }
            return y;
        }
        throw new IllegalArgumentException("You must call #load() before calling #into()");
    }

    public RequestBuilder<TranscodeType> load(Uri uri) {
        this.model = uri;
        this.isModelSet = true;
        return this;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r7v0, resolved type: com.bumptech.glide.request.target.Target<TranscodeType> */
    /* JADX DEBUG: Multi-variable search result rejected for r8v0, resolved type: com.bumptech.glide.request.RequestListener<TranscodeType> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r3v0. Raw type applied. Possible types: java.lang.Class<TranscodeType>, java.lang.Class<R> */
    /* JADX DEBUG: Type inference failed for r6v1. Raw type applied. Possible types: java.util.List<com.bumptech.glide.request.RequestListener<TranscodeType>>, java.util.List<com.bumptech.glide.request.RequestListener<R>> */
    /* JADX DEBUG: Type inference failed for r11v1. Raw type applied. Possible types: com.bumptech.glide.request.transition.TransitionFactory<? super ? super TranscodeType>, com.bumptech.glide.request.transition.TransitionFactory<? super R> */
    public final Request obtainRequest(Target<TranscodeType> target, RequestListener<TranscodeType> requestListener, BaseRequestOptions<?> baseRequestOptions, RequestCoordinator requestCoordinator, TransitionOptions<?, ? super TranscodeType> transitionOptions, Priority priority, int i, int i2) {
        Context context = this.context;
        GlideContext glideContext = this.glideContext;
        Object obj = this.model;
        Class cls = (Class<TranscodeType>) this.transcodeClass;
        List list = (List<RequestListener<TranscodeType>>) this.requestListeners;
        Engine engine = glideContext.engine;
        TransitionFactory transitionFactory = (TransitionFactory<? super Object>) transitionOptions.transitionFactory;
        SingleRequest singleRequest = (SingleRequest) ((FactoryPools.FactoryPool) SingleRequest.POOL).acquire();
        if (singleRequest == null) {
            singleRequest = new SingleRequest();
        }
        singleRequest.context = context;
        singleRequest.glideContext = glideContext;
        singleRequest.model = obj;
        singleRequest.transcodeClass = cls;
        singleRequest.requestOptions = baseRequestOptions;
        singleRequest.overrideWidth = i;
        singleRequest.overrideHeight = i2;
        singleRequest.priority = priority;
        singleRequest.target = target;
        singleRequest.targetListener = requestListener;
        singleRequest.requestListeners = list;
        singleRequest.requestCoordinator = requestCoordinator;
        singleRequest.engine = engine;
        singleRequest.animationFactory = transitionFactory;
        singleRequest.status = 1;
        return singleRequest;
    }

    public FutureTarget<TranscodeType> submit() {
        final RequestFutureTarget requestFutureTarget = new RequestFutureTarget(this.glideContext.mainHandler, RecyclerView.UNDEFINED_DURATION, RecyclerView.UNDEFINED_DURATION);
        if (Util.isOnBackgroundThread()) {
            this.glideContext.mainHandler.post(new Runnable() { // from class: com.bumptech.glide.RequestBuilder.1
                @Override // java.lang.Runnable
                public void run() {
                    if (!requestFutureTarget.isCancelled()) {
                        RequestBuilder requestBuilder = RequestBuilder.this;
                        RequestFutureTarget requestFutureTarget2 = requestFutureTarget;
                        requestBuilder.into(requestFutureTarget2, requestFutureTarget2, requestBuilder);
                    }
                }
            });
        } else {
            into(requestFutureTarget, requestFutureTarget, this);
        }
        return requestFutureTarget;
    }

    public RequestBuilder<TranscodeType> transition(TransitionOptions<?, ? super TranscodeType> transitionOptions) {
        this.transitionOptions = transitionOptions;
        this.isDefaultTransitionOptionsSet = false;
        return this;
    }

    @Override // com.bumptech.glide.request.BaseRequestOptions, java.lang.Object
    /* renamed from: clone  reason: collision with other method in class */
    public Object mo7clone() throws CloneNotSupportedException {
        RequestBuilder requestBuilder = (RequestBuilder) super.clone();
        requestBuilder.transitionOptions = requestBuilder.transitionOptions.clone();
        return requestBuilder;
    }

    public RequestBuilder<TranscodeType> load(Object obj) {
        this.model = obj;
        this.isModelSet = true;
        return this;
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0085  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x008b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.bumptech.glide.request.target.ViewTarget<android.widget.ImageView, TranscodeType> into(android.widget.ImageView r5) {
        /*
            r4 = this;
            com.bumptech.glide.util.Util.assertMainThread()
            java.lang.String r0 = "Argument must not be null"
            java.util.Objects.requireNonNull(r5, r0)
            int r0 = r4.fields
            r1 = 2048(0x800, float:2.87E-42)
            boolean r0 = com.bumptech.glide.request.BaseRequestOptions.isSet(r0, r1)
            if (r0 != 0) goto L_0x0073
            boolean r0 = r4.isTransformationAllowed
            if (r0 == 0) goto L_0x0073
            android.widget.ImageView$ScaleType r0 = r5.getScaleType()
            if (r0 == 0) goto L_0x0073
            int[] r0 = com.bumptech.glide.RequestBuilder.AnonymousClass2.$SwitchMap$android$widget$ImageView$ScaleType
            android.widget.ImageView$ScaleType r1 = r5.getScaleType()
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 1
            switch(r0) {
                case 1: goto L_0x0063;
                case 2: goto L_0x0051;
                case 3: goto L_0x003f;
                case 4: goto L_0x003f;
                case 5: goto L_0x003f;
                case 6: goto L_0x002d;
                default: goto L_0x002c;
            }
        L_0x002c:
            goto L_0x0073
        L_0x002d:
            com.bumptech.glide.request.BaseRequestOptions r0 = r4.mo7clone()
            com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r2 = com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.CENTER_INSIDE
            com.bumptech.glide.load.resource.bitmap.CenterInside r3 = new com.bumptech.glide.load.resource.bitmap.CenterInside
            r3.<init>()
            com.bumptech.glide.request.BaseRequestOptions r0 = r0.optionalTransform(r2, r3)
            r0.isScaleOnlyOrNoTransform = r1
            goto L_0x0074
        L_0x003f:
            com.bumptech.glide.request.BaseRequestOptions r0 = r4.mo7clone()
            com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r2 = com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.FIT_CENTER
            com.bumptech.glide.load.resource.bitmap.FitCenter r3 = new com.bumptech.glide.load.resource.bitmap.FitCenter
            r3.<init>()
            com.bumptech.glide.request.BaseRequestOptions r0 = r0.optionalTransform(r2, r3)
            r0.isScaleOnlyOrNoTransform = r1
            goto L_0x0074
        L_0x0051:
            com.bumptech.glide.request.BaseRequestOptions r0 = r4.mo7clone()
            com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r2 = com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.CENTER_INSIDE
            com.bumptech.glide.load.resource.bitmap.CenterInside r3 = new com.bumptech.glide.load.resource.bitmap.CenterInside
            r3.<init>()
            com.bumptech.glide.request.BaseRequestOptions r0 = r0.optionalTransform(r2, r3)
            r0.isScaleOnlyOrNoTransform = r1
            goto L_0x0074
        L_0x0063:
            com.bumptech.glide.request.BaseRequestOptions r0 = r4.mo7clone()
            com.bumptech.glide.load.resource.bitmap.DownsampleStrategy r1 = com.bumptech.glide.load.resource.bitmap.DownsampleStrategy.CENTER_OUTSIDE
            com.bumptech.glide.load.resource.bitmap.CenterCrop r2 = new com.bumptech.glide.load.resource.bitmap.CenterCrop
            r2.<init>()
            com.bumptech.glide.request.BaseRequestOptions r0 = r0.optionalTransform(r1, r2)
            goto L_0x0074
        L_0x0073:
            r0 = r4
        L_0x0074:
            com.bumptech.glide.GlideContext r1 = r4.glideContext
            java.lang.Class<TranscodeType> r2 = r4.transcodeClass
            com.bumptech.glide.request.target.ImageViewTargetFactory r1 = r1.imageViewTargetFactory
            java.util.Objects.requireNonNull(r1)
            java.lang.Class<android.graphics.Bitmap> r1 = android.graphics.Bitmap.class
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x008b
            com.bumptech.glide.request.target.BitmapImageViewTarget r1 = new com.bumptech.glide.request.target.BitmapImageViewTarget
            r1.<init>(r5)
            goto L_0x0098
        L_0x008b:
            java.lang.Class<android.graphics.drawable.Drawable> r1 = android.graphics.drawable.Drawable.class
            boolean r1 = r1.isAssignableFrom(r2)
            if (r1 == 0) goto L_0x009d
            com.bumptech.glide.request.target.DrawableImageViewTarget r1 = new com.bumptech.glide.request.target.DrawableImageViewTarget
            r1.<init>(r5)
        L_0x0098:
            r5 = 0
            r4.into(r1, r5, r0)
            return r1
        L_0x009d:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException
            java.lang.String r5 = java.lang.String.valueOf(r2)
            int r0 = r5.length()
            int r0 = r0 + 64
            java.lang.String r1 = "Unhandled class: "
            java.lang.String r2 = ", try .as*(Class).transcode(ResourceTranscoder)"
            java.lang.String r5 = androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0.m(r0, r1, r5, r2)
            r4.<init>(r5)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.RequestBuilder.into(android.widget.ImageView):com.bumptech.glide.request.target.ViewTarget");
    }
}
