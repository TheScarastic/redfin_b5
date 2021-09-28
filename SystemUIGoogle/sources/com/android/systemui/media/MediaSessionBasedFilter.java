package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.util.Log;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.statusbar.phone.NotificationListenerWithPlugins;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.TypeIntrinsics;
/* compiled from: MediaSessionBasedFilter.kt */
/* loaded from: classes.dex */
public final class MediaSessionBasedFilter implements MediaDataManager.Listener {
    private final Executor backgroundExecutor;
    private final Executor foregroundExecutor;
    private final MediaSessionManager sessionManager;
    private final Set<MediaDataManager.Listener> listeners = new LinkedHashSet();
    private final LinkedHashMap<String, List<MediaController>> packageControllers = new LinkedHashMap<>();
    private final Map<String, Set<MediaSession.Token>> keyedTokens = new LinkedHashMap();
    private final Set<MediaSession.Token> tokensWithNotifications = new LinkedHashSet();
    private final MediaSessionBasedFilter$sessionListener$1 sessionListener = new MediaSessionBasedFilter$sessionListener$1(this);

    public MediaSessionBasedFilter(final Context context, MediaSessionManager mediaSessionManager, Executor executor, Executor executor2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(mediaSessionManager, "sessionManager");
        Intrinsics.checkNotNullParameter(executor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(executor2, "backgroundExecutor");
        this.sessionManager = mediaSessionManager;
        this.foregroundExecutor = executor;
        this.backgroundExecutor = executor2;
        executor2.execute(new Runnable() { // from class: com.android.systemui.media.MediaSessionBasedFilter.1
            @Override // java.lang.Runnable
            public final void run() {
                ComponentName componentName = new ComponentName(context, NotificationListenerWithPlugins.class);
                this.sessionManager.addOnActiveSessionsChangedListener(this.sessionListener, componentName);
                MediaSessionBasedFilter mediaSessionBasedFilter = this;
                List<MediaController> activeSessions = mediaSessionBasedFilter.sessionManager.getActiveSessions(componentName);
                Intrinsics.checkNotNullExpressionValue(activeSessions, "sessionManager.getActiveSessions(name)");
                mediaSessionBasedFilter.handleControllersChanged(activeSessions);
            }
        });
    }

    public final boolean addListener(MediaDataManager.Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        return this.listeners.add(listener);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        this.backgroundExecutor.execute(new Runnable(mediaData, str2, str, this, z) { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataLoaded$1
            final /* synthetic */ MediaData $data;
            final /* synthetic */ boolean $immediately;
            final /* synthetic */ String $key;
            final /* synthetic */ String $oldKey;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.$data = r1;
                this.$oldKey = r2;
                this.$key = r3;
                this.this$0 = r4;
                this.$immediately = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ArrayList arrayList;
                Integer num;
                Boolean bool;
                MediaSession.Token token = this.$data.getToken();
                if (token != null) {
                    this.this$0.tokensWithNotifications.add(token);
                }
                String str3 = this.$oldKey;
                boolean z3 = str3 != null && !Intrinsics.areEqual(this.$key, str3);
                if (z3) {
                    Map map = this.this$0.keyedTokens;
                    String str4 = this.$oldKey;
                    Objects.requireNonNull(map, "null cannot be cast to non-null type kotlin.collections.MutableMap<K, V>");
                    Set set = (Set) TypeIntrinsics.asMutableMap(map).remove(str4);
                    if (set != null) {
                        Set set2 = (Set) this.this$0.keyedTokens.put(this.$key, set);
                    }
                }
                MediaController mediaController = null;
                if (this.$data.getToken() != null) {
                    Set set3 = (Set) this.this$0.keyedTokens.get(this.$key);
                    if (set3 == null) {
                        bool = null;
                    } else {
                        bool = Boolean.valueOf(set3.add(this.$data.getToken()));
                    }
                    if (bool == null) {
                        Set set4 = (Set) this.this$0.keyedTokens.put(this.$key, SetsKt__SetsKt.mutableSetOf(this.$data.getToken()));
                    }
                }
                List list = (List) this.this$0.packageControllers.get(this.$data.getPackageName());
                if (list == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    for (Object obj : list) {
                        MediaController.PlaybackInfo playbackInfo = ((MediaController) obj).getPlaybackInfo();
                        Integer valueOf = playbackInfo == null ? null : Integer.valueOf(playbackInfo.getPlaybackType());
                        if (valueOf != null && valueOf.intValue() == 2) {
                            arrayList.add(obj);
                        }
                    }
                }
                if (arrayList == null) {
                    num = null;
                } else {
                    num = Integer.valueOf(arrayList.size());
                }
                if (num != null && num.intValue() == 1) {
                    mediaController = (MediaController) CollectionsKt.firstOrNull(arrayList);
                }
                if (z3 || mediaController == null || Intrinsics.areEqual(mediaController.getSessionToken(), this.$data.getToken()) || !this.this$0.tokensWithNotifications.contains(mediaController.getSessionToken())) {
                    this.this$0.dispatchMediaDataLoaded(this.$key, this.$oldKey, this.$data, this.$immediately);
                    return;
                }
                Log.d("MediaSessionBasedFilter", "filtering key=" + this.$key + " local=" + this.$data.getToken() + " remote=" + mediaController.getSessionToken());
                Set set5 = (Set) this.this$0.keyedTokens.get(this.$key);
                Intrinsics.checkNotNull(set5);
                if (!set5.contains(mediaController.getSessionToken())) {
                    this.this$0.dispatchMediaDataRemoved(this.$key);
                }
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        this.backgroundExecutor.execute(new Runnable(this, str, smartspaceMediaData) { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataLoaded$1
            final /* synthetic */ SmartspaceMediaData $data;
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$data = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.dispatchSmartspaceMediaDataLoaded(this.$key, this.$data);
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.backgroundExecutor.execute(new Runnable(this, str) { // from class: com.android.systemui.media.MediaSessionBasedFilter$onMediaDataRemoved$1
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.keyedTokens.remove(this.$key);
                this.this$0.dispatchMediaDataRemoved(this.$key);
            }
        });
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.backgroundExecutor.execute(new Runnable(this, str, z) { // from class: com.android.systemui.media.MediaSessionBasedFilter$onSmartspaceMediaDataRemoved$1
            final /* synthetic */ boolean $immediately;
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$immediately = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.dispatchSmartspaceMediaDataRemoved(this.$key, this.$immediately);
            }
        });
    }

    /* access modifiers changed from: private */
    public final void dispatchMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z) {
        this.foregroundExecutor.execute(new Runnable(this, str, str2, mediaData, z) { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchMediaDataLoaded$1
            final /* synthetic */ boolean $immediately;
            final /* synthetic */ MediaData $info;
            final /* synthetic */ String $key;
            final /* synthetic */ String $oldKey;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$oldKey = r3;
                this.$info = r4;
                this.$immediately = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(this.this$0.listeners);
                String str3 = this.$key;
                String str4 = this.$oldKey;
                MediaData mediaData2 = this.$info;
                boolean z2 = this.$immediately;
                for (MediaDataManager.Listener listener : set) {
                    MediaDataManager.Listener.DefaultImpls.onMediaDataLoaded$default(listener, str3, str4, mediaData2, z2, false, 16, null);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final void dispatchMediaDataRemoved(String str) {
        this.foregroundExecutor.execute(new Runnable(this, str) { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchMediaDataRemoved$1
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(this.this$0.listeners);
                String str2 = this.$key;
                for (MediaDataManager.Listener listener : set) {
                    listener.onMediaDataRemoved(str2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final void dispatchSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData) {
        this.foregroundExecutor.execute(new Runnable(this, str, smartspaceMediaData) { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataLoaded$1
            final /* synthetic */ SmartspaceMediaData $info;
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$info = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(this.this$0.listeners);
                String str2 = this.$key;
                SmartspaceMediaData smartspaceMediaData2 = this.$info;
                for (MediaDataManager.Listener listener : set) {
                    MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, str2, smartspaceMediaData2, false, 4, null);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final void dispatchSmartspaceMediaDataRemoved(String str, boolean z) {
        this.foregroundExecutor.execute(new Runnable(this, str, z) { // from class: com.android.systemui.media.MediaSessionBasedFilter$dispatchSmartspaceMediaDataRemoved$1
            final /* synthetic */ boolean $immediately;
            final /* synthetic */ String $key;
            final /* synthetic */ MediaSessionBasedFilter this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$immediately = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                Set<MediaDataManager.Listener> set = CollectionsKt___CollectionsKt.toSet(this.this$0.listeners);
                String str2 = this.$key;
                boolean z2 = this.$immediately;
                for (MediaDataManager.Listener listener : set) {
                    listener.onSmartspaceMediaDataRemoved(str2, z2);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public final void handleControllersChanged(List<MediaController> list) {
        Boolean bool;
        this.packageControllers.clear();
        for (MediaController mediaController : list) {
            List<MediaController> list2 = this.packageControllers.get(mediaController.getPackageName());
            if (list2 == null) {
                bool = null;
            } else {
                bool = Boolean.valueOf(list2.add(mediaController));
            }
            if (bool == null) {
                this.packageControllers.put(mediaController.getPackageName(), CollectionsKt__CollectionsKt.mutableListOf(mediaController));
            }
        }
        Set<MediaSession.Token> set = this.tokensWithNotifications;
        ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(list, 10));
        for (MediaController mediaController2 : list) {
            arrayList.add(mediaController2.getSessionToken());
        }
        set.retainAll(arrayList);
    }
}
