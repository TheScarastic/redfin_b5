package com.android.systemui.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import androidx.appcompat.R$styleable;
import com.android.settingslib.Utils;
import com.android.systemui.Dumpable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.notification.row.HybridGroupManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.collections.CollectionsKt__CollectionsJVMKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
/* compiled from: MediaDataManager.kt */
/* loaded from: classes.dex */
public final class MediaDataManager implements Dumpable, BcSmartspaceDataPlugin.SmartspaceTargetListener {
    private final ActivityStarter activityStarter;
    private boolean allowMediaRecommendations;
    private final MediaDataManager$appChangeReceiver$1 appChangeReceiver;
    private final Executor backgroundExecutor;
    private final int bgColor;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private final DelayableExecutor foregroundExecutor;
    private final Set<Listener> internalListeners;
    private final MediaControllerFactory mediaControllerFactory;
    private final MediaDataFilter mediaDataFilter;
    private final LinkedHashMap<String, MediaData> mediaEntries;
    private SmartspaceMediaData smartspaceMediaData;
    private final SmartspaceMediaDataProvider smartspaceMediaDataProvider;
    private SmartspaceSession smartspaceSession;
    private final SystemClock systemClock;
    private final int themeText;
    private final TunerService tunerService;
    private boolean useMediaResumption;
    private final boolean useQsMediaPlayer;
    public static final Companion Companion = new Companion(null);
    public static final String SMARTSPACE_UI_SURFACE_LABEL = "media_data_manager";
    public static final String EXTRAS_MEDIA_SOURCE_PACKAGE_NAME = "package_name";
    public static final int MAX_COMPACT_ACTIONS = 3;

    public MediaDataManager(Context context, Executor executor, DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory, BroadcastDispatcher broadcastDispatcher, DumpManager dumpManager, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter, ActivityStarter activityStarter, SmartspaceMediaDataProvider smartspaceMediaDataProvider, boolean z, boolean z2, SystemClock systemClock, TunerService tunerService) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        this.context = context;
        this.backgroundExecutor = executor;
        this.foregroundExecutor = delayableExecutor;
        this.mediaControllerFactory = mediaControllerFactory;
        this.broadcastDispatcher = broadcastDispatcher;
        this.mediaDataFilter = mediaDataFilter;
        this.activityStarter = activityStarter;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider;
        this.useMediaResumption = z;
        this.useQsMediaPlayer = z2;
        this.systemClock = systemClock;
        this.tunerService = tunerService;
        this.themeText = Utils.getColorAttr(context, 16842806).getDefaultColor();
        this.bgColor = context.getColor(17170502);
        this.internalListeners = new LinkedHashSet();
        this.mediaEntries = new LinkedHashMap<>();
        this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        this.allowMediaRecommendations = com.android.systemui.util.Utils.allowMediaRecommendations(context);
        MediaDataManager$appChangeReceiver$1 mediaDataManager$appChangeReceiver$1 = new BroadcastReceiver(this) { // from class: com.android.systemui.media.MediaDataManager$appChangeReceiver$1
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String[] stringArrayExtra;
                String encodedSchemeSpecificPart;
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                String action = intent.getAction();
                if (action != null) {
                    int hashCode = action.hashCode();
                    if (hashCode != -1001645458) {
                        if (hashCode != -757780528) {
                            if (hashCode != 525384130 || !action.equals("android.intent.action.PACKAGE_REMOVED")) {
                                return;
                            }
                        } else if (!action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                            return;
                        }
                        Uri data = intent.getData();
                        if (!(data == null || (encodedSchemeSpecificPart = data.getEncodedSchemeSpecificPart()) == null)) {
                            this.this$0.removeAllForPackage(encodedSchemeSpecificPart);
                        }
                    } else if (action.equals("android.intent.action.PACKAGES_SUSPENDED") && (stringArrayExtra = intent.getStringArrayExtra("android.intent.extra.changed_package_list")) != null) {
                        MediaDataManager mediaDataManager = this.this$0;
                        for (String str : stringArrayExtra) {
                            Intrinsics.checkNotNullExpressionValue(str, "it");
                            mediaDataManager.removeAllForPackage(str);
                        }
                    }
                }
            }
        };
        this.appChangeReceiver = mediaDataManager$appChangeReceiver$1;
        dumpManager.registerDumpable("MediaDataManager", this);
        addInternalListener(mediaTimeoutListener);
        addInternalListener(mediaResumeListener);
        addInternalListener(mediaSessionBasedFilter);
        mediaSessionBasedFilter.addListener(mediaDeviceManager);
        mediaSessionBasedFilter.addListener(mediaDataCombineLatest);
        mediaDeviceManager.addListener(mediaDataCombineLatest);
        mediaDataCombineLatest.addListener(mediaDataFilter);
        mediaTimeoutListener.setTimeoutCallback(new Function2<String, Boolean, Unit>(this) { // from class: com.android.systemui.media.MediaDataManager.1
            final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // kotlin.jvm.functions.Function2
            public /* bridge */ /* synthetic */ Unit invoke(String str, Boolean bool) {
                invoke(str, bool.booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(String str, boolean z3) {
                Intrinsics.checkNotNullParameter(str, "token");
                MediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(this.this$0, str, z3, false, 4, null);
            }
        });
        mediaResumeListener.setManager(this);
        mediaDataFilter.setMediaDataManager$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this);
        broadcastDispatcher.registerReceiver(mediaDataManager$appChangeReceiver$1, new IntentFilter("android.intent.action.PACKAGES_SUSPENDED"), null, UserHandle.ALL);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(mediaDataManager$appChangeReceiver$1, intentFilter);
        smartspaceMediaDataProvider.registerListener(this);
        Object systemService = context.getSystemService(SmartspaceManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "context.getSystemService(SmartspaceManager::class.java)");
        SmartspaceSession createSmartspaceSession = ((SmartspaceManager) systemService).createSmartspaceSession(new SmartspaceConfig.Builder(context, SMARTSPACE_UI_SURFACE_LABEL).build());
        this.smartspaceSession = createSmartspaceSession;
        if (createSmartspaceSession != null) {
            createSmartspaceSession.addOnTargetsAvailableListener(Executors.newCachedThreadPool(), new SmartspaceSession.OnTargetsAvailableListener(this) { // from class: com.android.systemui.media.MediaDataManager$2$1
                final /* synthetic */ MediaDataManager this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                public final void onTargetsAvailable(List<SmartspaceTarget> list) {
                    SmartspaceMediaDataProvider smartspaceMediaDataProvider2 = this.this$0.smartspaceMediaDataProvider;
                    Intrinsics.checkNotNullExpressionValue(list, "targets");
                    smartspaceMediaDataProvider2.onTargetsAvailable(list);
                }
            });
        }
        SmartspaceSession smartspaceSession = this.smartspaceSession;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
        tunerService.addTunable(new TunerService.Tunable(this) { // from class: com.android.systemui.media.MediaDataManager.4
            final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public void onTuningChanged(String str, String str2) {
                MediaDataManager mediaDataManager = this.this$0;
                mediaDataManager.allowMediaRecommendations = com.android.systemui.util.Utils.allowMediaRecommendations(mediaDataManager.context);
                if (!this.this$0.allowMediaRecommendations) {
                    MediaDataManager mediaDataManager2 = this.this$0;
                    mediaDataManager2.dismissSmartspaceRecommendation(mediaDataManager2.getSmartspaceMediaData().getTargetId(), 0);
                }
            }
        }, "qs_media_recommend");
    }

    /* compiled from: MediaDataManager.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final SmartspaceMediaData getSmartspaceMediaData() {
        return this.smartspaceMediaData;
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public MediaDataManager(Context context, Executor executor, DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter, ActivityStarter activityStarter, SmartspaceMediaDataProvider smartspaceMediaDataProvider, SystemClock systemClock, TunerService tunerService) {
        this(context, executor, delayableExecutor, mediaControllerFactory, broadcastDispatcher, dumpManager, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter, activityStarter, smartspaceMediaDataProvider, com.android.systemui.util.Utils.useMediaResumption(context), com.android.systemui.util.Utils.useQsMediaPlayer(context), systemClock, tunerService);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(systemClock, "clock");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
    }

    public final void onNotificationAdded(String str, StatusBarNotification statusBarNotification) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        if (!this.useQsMediaPlayer || !MediaDataManagerKt.isMediaNotification(statusBarNotification)) {
            onNotificationRemoved(str);
            return;
        }
        Assert.isMainThread();
        String packageName = statusBarNotification.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "sbn.packageName");
        String findExistingEntry = findExistingEntry(str, packageName);
        if (findExistingEntry == null) {
            MediaData mediaData = MediaDataManagerKt.LOADING;
            String packageName2 = statusBarNotification.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName2, "sbn.packageName");
            this.mediaEntries.put(str, MediaData.copy$default(mediaData, 0, false, 0, null, null, null, null, null, null, null, packageName2, null, null, null, false, null, false, false, null, false, null, false, 0, 8387583, null));
        } else if (!Intrinsics.areEqual(findExistingEntry, str)) {
            MediaData remove = this.mediaEntries.remove(findExistingEntry);
            Intrinsics.checkNotNull(remove);
            this.mediaEntries.put(str, remove);
        }
        loadMediaData(str, statusBarNotification, findExistingEntry);
    }

    public final void removeAllForPackage(String str) {
        Assert.isMainThread();
        LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
            if (Intrinsics.areEqual(entry.getValue().getPackageName(), str)) {
                linkedHashMap2.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
            removeEntry((String) entry2.getKey());
        }
    }

    public final void setResumeAction(String str, Runnable runnable) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            mediaData.setResumeAction(runnable);
            mediaData.setHasCheckedForResume(true);
        }
    }

    public final void addResumptionControls(int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        Intrinsics.checkNotNullParameter(mediaDescription, "desc");
        Intrinsics.checkNotNullParameter(runnable, "action");
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(str, "appName");
        Intrinsics.checkNotNullParameter(pendingIntent, "appIntent");
        Intrinsics.checkNotNullParameter(str2, "packageName");
        if (!this.mediaEntries.containsKey(str2)) {
            this.mediaEntries.put(str2, MediaData.copy$default(MediaDataManagerKt.LOADING, 0, false, 0, null, null, null, null, null, null, null, str2, null, null, null, false, runnable, false, false, null, true, null, false, 0, 7830527, null));
        }
        this.backgroundExecutor.execute(new Runnable(this, i, mediaDescription, runnable, token, str, pendingIntent, str2) { // from class: com.android.systemui.media.MediaDataManager$addResumptionControls$1
            final /* synthetic */ Runnable $action;
            final /* synthetic */ PendingIntent $appIntent;
            final /* synthetic */ String $appName;
            final /* synthetic */ MediaDescription $desc;
            final /* synthetic */ String $packageName;
            final /* synthetic */ MediaSession.Token $token;
            final /* synthetic */ int $userId;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$userId = r2;
                this.$desc = r3;
                this.$action = r4;
                this.$token = r5;
                this.$appName = r6;
                this.$appIntent = r7;
                this.$packageName = r8;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.loadMediaDataInBgForResumption(this.$userId, this.$desc, this.$action, this.$token, this.$appName, this.$appIntent, this.$packageName);
            }
        });
    }

    private final String findExistingEntry(String str, String str2) {
        if (this.mediaEntries.containsKey(str)) {
            return str;
        }
        if (this.mediaEntries.containsKey(str2)) {
            return str2;
        }
        return null;
    }

    private final void loadMediaData(String str, StatusBarNotification statusBarNotification, String str2) {
        this.backgroundExecutor.execute(new Runnable(this, str, statusBarNotification, str2) { // from class: com.android.systemui.media.MediaDataManager$loadMediaData$1
            final /* synthetic */ String $key;
            final /* synthetic */ String $oldKey;
            final /* synthetic */ StatusBarNotification $sbn;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
                this.$sbn = r3;
                this.$oldKey = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.loadMediaDataInBg(this.$key, this.$sbn, this.$oldKey);
            }
        });
    }

    public final void addListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.addListener(listener);
    }

    public final void removeListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.removeListener(listener);
    }

    private final boolean addInternalListener(Listener listener) {
        return this.internalListeners.add(listener);
    }

    private final void notifyMediaDataLoaded(String str, String str2, MediaData mediaData) {
        for (Listener listener : this.internalListeners) {
            Listener.DefaultImpls.onMediaDataLoaded$default(listener, str, str2, mediaData, false, false, 24, null);
        }
    }

    private final void notifySmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData) {
        for (Listener listener : this.internalListeners) {
            Listener.DefaultImpls.onSmartspaceMediaDataLoaded$default(listener, str, smartspaceMediaData, false, 4, null);
        }
    }

    private final void notifyMediaDataRemoved(String str) {
        for (Listener listener : this.internalListeners) {
            listener.onMediaDataRemoved(str);
        }
    }

    public final void notifySmartspaceMediaDataRemoved(String str, boolean z) {
        for (Listener listener : this.internalListeners) {
            listener.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public static /* synthetic */ void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(MediaDataManager mediaDataManager, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 4) != 0) {
            z2 = false;
        }
        mediaDataManager.setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(str, z, z2);
    }

    public final void setTimedOut$frameworks__base__packages__SystemUI__android_common__SystemUI_core(String str, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "token");
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            if (mediaData.getActive() != (!z) || z2) {
                mediaData.setActive(!z);
                Log.d("MediaDataManager", "Updating " + str + " timedOut: " + z);
                onMediaDataLoaded(str, str, mediaData);
            }
        }
    }

    public final void removeEntry(String str) {
        this.mediaEntries.remove(str);
        notifyMediaDataRemoved(str);
    }

    public final boolean dismissMediaData(String str, long j) {
        Intrinsics.checkNotNullParameter(str, "key");
        boolean z = this.mediaEntries.get(str) != null;
        this.backgroundExecutor.execute(new Runnable(this, str) { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$1
            final /* synthetic */ String $key;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaSession.Token token;
                MediaData mediaData = (MediaData) this.this$0.mediaEntries.get(this.$key);
                if (mediaData != null) {
                    MediaDataManager mediaDataManager = this.this$0;
                    if (mediaData.isLocalSession() && (token = mediaData.getToken()) != null) {
                        mediaDataManager.mediaControllerFactory.create(token).getTransportControls().stop();
                    }
                }
            }
        });
        this.foregroundExecutor.executeDelayed(new Runnable(this, str) { // from class: com.android.systemui.media.MediaDataManager$dismissMediaData$2
            final /* synthetic */ String $key;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$key = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.removeEntry(this.$key);
            }
        }, j);
        return z;
    }

    public final void dismissSmartspaceRecommendation(String str, long j) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (Intrinsics.areEqual(this.smartspaceMediaData.getTargetId(), str)) {
            Log.d("MediaDataManager", "Dismissing Smartspace media target");
            if (this.smartspaceMediaData.isActive()) {
                this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, false, null, null, null, 0, R$styleable.AppCompatTheme_windowNoTitle, null);
            }
            this.foregroundExecutor.executeDelayed(new Runnable(this) { // from class: com.android.systemui.media.MediaDataManager$dismissSmartspaceRecommendation$1
                final /* synthetic */ MediaDataManager this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    MediaDataManager mediaDataManager = this.this$0;
                    mediaDataManager.notifySmartspaceMediaDataRemoved(mediaDataManager.getSmartspaceMediaData().getTargetId(), true);
                }
            }, j);
        }
    }

    public final void loadMediaDataInBgForResumption(int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        if (TextUtils.isEmpty(mediaDescription.getTitle())) {
            Log.e("MediaDataManager", "Description incomplete");
            this.mediaEntries.remove(str2);
            return;
        }
        Log.d("MediaDataManager", "adding track for " + i + " from browser: " + mediaDescription);
        Bitmap iconBitmap = mediaDescription.getIconBitmap();
        if (iconBitmap == null && mediaDescription.getIconUri() != null) {
            Uri iconUri = mediaDescription.getIconUri();
            Intrinsics.checkNotNull(iconUri);
            iconBitmap = loadBitmapFromUri(iconUri);
        }
        this.foregroundExecutor.execute(new Runnable(this, str2, i, str, mediaDescription, iconBitmap != null ? Icon.createWithBitmap(iconBitmap) : null, getResumeMediaAction(runnable), token, pendingIntent, runnable, this.systemClock.elapsedRealtime()) { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBgForResumption$1
            final /* synthetic */ PendingIntent $appIntent;
            final /* synthetic */ String $appName;
            final /* synthetic */ Icon $artworkIcon;
            final /* synthetic */ MediaDescription $desc;
            final /* synthetic */ long $lastActive;
            final /* synthetic */ MediaAction $mediaAction;
            final /* synthetic */ String $packageName;
            final /* synthetic */ Runnable $resumeAction;
            final /* synthetic */ MediaSession.Token $token;
            final /* synthetic */ int $userId;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$packageName = r2;
                this.$userId = r3;
                this.$appName = r4;
                this.$desc = r5;
                this.$artworkIcon = r6;
                this.$mediaAction = r7;
                this.$token = r8;
                this.$appIntent = r9;
                this.$resumeAction = r10;
                this.$lastActive = r11;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaDataManager mediaDataManager = this.this$0;
                String str3 = this.$packageName;
                int i2 = this.$userId;
                int i3 = mediaDataManager.bgColor;
                String str4 = this.$appName;
                CharSequence subtitle = this.$desc.getSubtitle();
                CharSequence title = this.$desc.getTitle();
                Icon icon = this.$artworkIcon;
                List list = CollectionsKt__CollectionsJVMKt.listOf(this.$mediaAction);
                List list2 = CollectionsKt__CollectionsJVMKt.listOf(0);
                String str5 = this.$packageName;
                mediaDataManager.onMediaDataLoaded(str3, null, new MediaData(i2, true, i3, str4, null, subtitle, title, icon, list, list2, str5, this.$token, this.$appIntent, null, false, this.$resumeAction, false, true, str5, true, null, false, this.$lastActive, 3211264, null));
            }
        });
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:49:0x0102 */
    /* JADX DEBUG: Multi-variable search result rejected for r4v6, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r2v2, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r3v4, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r2v23, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r4v21, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r4v24, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r4v27, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r4v39, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r4v40, resolved type: T */
    /* JADX DEBUG: Multi-variable search result rejected for r2v24, resolved type: T */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3 */
    /* JADX WARN: Type inference failed for: r3v31, types: [java.util.List, T] */
    /* JADX WARN: Type inference failed for: r3v32, types: [java.util.ArrayList] */
    /* JADX WARN: Type inference failed for: r3v34 */
    /* JADX WARN: Type inference failed for: r2v21, types: [java.lang.CharSequence, T] */
    /* JADX WARN: Type inference failed for: r4v20, types: [java.lang.CharSequence, T] */
    /* JADX WARN: Type inference failed for: r3v35 */
    public final void loadMediaDataInBg(String str, StatusBarNotification statusBarNotification, String str2) {
        Bitmap bitmap;
        Icon icon;
        T t;
        int i;
        Notification.Action[] actionArr;
        Icon icon2;
        T t2;
        MediaSession.Token token = (MediaSession.Token) statusBarNotification.getNotification().extras.getParcelable("android.mediaSession");
        MediaController create = this.mediaControllerFactory.create(token);
        MediaMetadata metadata = create.getMetadata();
        Notification notification = statusBarNotification.getNotification();
        Intrinsics.checkNotNullExpressionValue(notification, "sbn.notification");
        if (metadata == null) {
            bitmap = null;
        } else {
            bitmap = metadata.getBitmap("android.media.metadata.ART");
        }
        if (bitmap == null) {
            if (metadata == null) {
                bitmap = null;
            } else {
                bitmap = metadata.getBitmap("android.media.metadata.ALBUM_ART");
            }
        }
        if (bitmap == null && metadata != null) {
            bitmap = loadBitmapFromUri(metadata);
        }
        if (bitmap == null) {
            icon = notification.getLargeIcon();
        } else {
            icon = Icon.createWithBitmap(bitmap);
        }
        int i2 = 0;
        if (icon != null && bitmap == null) {
            if (icon.getType() == 1 || icon.getType() == 5) {
                icon.getBitmap();
            } else {
                Drawable loadDrawable = icon.loadDrawable(this.context);
                Intrinsics.checkNotNullExpressionValue(loadDrawable, "artWorkIcon.loadDrawable(context)");
                Canvas canvas = new Canvas(Bitmap.createBitmap(loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888));
                loadDrawable.setBounds(0, 0, loadDrawable.getIntrinsicWidth(), loadDrawable.getIntrinsicHeight());
                loadDrawable.draw(canvas);
            }
        }
        String loadHeaderAppName = Notification.Builder.recoverBuilder(this.context, notification).loadHeaderAppName();
        Icon smallIcon = statusBarNotification.getNotification().getSmallIcon();
        Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
        T string = metadata == null ? 0 : metadata.getString("android.media.metadata.DISPLAY_TITLE");
        ref$ObjectRef.element = string;
        if (string == 0) {
            if (metadata == null) {
                t2 = 0;
            } else {
                t2 = metadata.getString("android.media.metadata.TITLE");
            }
            ref$ObjectRef.element = t2;
        }
        if (ref$ObjectRef.element == 0) {
            ref$ObjectRef.element = HybridGroupManager.resolveTitle(notification);
        }
        Ref$ObjectRef ref$ObjectRef2 = new Ref$ObjectRef();
        T string2 = metadata == null ? 0 : metadata.getString("android.media.metadata.ARTIST");
        ref$ObjectRef2.element = string2;
        if (string2 == 0) {
            ref$ObjectRef2.element = HybridGroupManager.resolveText(notification);
        }
        ArrayList arrayList = new ArrayList();
        Notification.Action[] actionArr2 = notification.actions;
        Ref$ObjectRef ref$ObjectRef3 = new Ref$ObjectRef();
        int[] intArray = notification.extras.getIntArray("android.compactActions");
        if (intArray == null) {
            t = 0;
        } else {
            t = ArraysKt___ArraysKt.toMutableList(intArray);
        }
        if (t == 0) {
            t = new ArrayList();
        }
        ref$ObjectRef3.element = t;
        int size = ((List) t).size();
        int i3 = MAX_COMPACT_ACTIONS;
        if (size > i3) {
            Log.e("MediaDataManager", "Too many compact actions for " + str + ", limiting to first " + i3);
            i2 = 0;
            ref$ObjectRef3.element = ((List) ref$ObjectRef3.element).subList(0, i3);
        }
        if (actionArr2 != null) {
            int length = actionArr2.length;
            int i4 = i2;
            while (i4 < length) {
                Notification.Action action = actionArr2[i4];
                int i5 = i4 + 1;
                if (action.getIcon() == null) {
                    actionArr = actionArr2;
                    StringBuilder sb = new StringBuilder();
                    i = length;
                    sb.append("No icon for action ");
                    sb.append(i4);
                    sb.append(' ');
                    sb.append((Object) action.title);
                    Log.i("MediaDataManager", sb.toString());
                    ((List) ref$ObjectRef3.element).remove(Integer.valueOf(i4));
                } else {
                    actionArr = actionArr2;
                    i = length;
                    MediaDataManager$loadMediaDataInBg$runnable$1 mediaDataManager$loadMediaDataInBg$runnable$1 = action.actionIntent != null ? new Runnable(action, this) { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$runnable$1
                        final /* synthetic */ Notification.Action $action;
                        final /* synthetic */ MediaDataManager this$0;

                        /* access modifiers changed from: package-private */
                        {
                            this.$action = r1;
                            this.this$0 = r2;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            if (this.$action.isAuthenticationRequired()) {
                                ActivityStarter activityStarter = this.this$0.activityStarter;
                                final MediaDataManager mediaDataManager = this.this$0;
                                final Notification.Action action2 = this.$action;
                                activityStarter.dismissKeyguardThenExecute(new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$runnable$1.1
                                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                                    public final boolean onDismiss() {
                                        MediaDataManager mediaDataManager2 = mediaDataManager;
                                        PendingIntent pendingIntent = action2.actionIntent;
                                        Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
                                        return mediaDataManager2.sendPendingIntent(pendingIntent);
                                    }
                                }, AnonymousClass2.INSTANCE, true);
                                return;
                            }
                            MediaDataManager mediaDataManager2 = this.this$0;
                            PendingIntent pendingIntent = this.$action.actionIntent;
                            Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
                            mediaDataManager2.sendPendingIntent(pendingIntent);
                        }
                    } : null;
                    Icon icon3 = action.getIcon();
                    Integer valueOf = icon3 == null ? null : Integer.valueOf(icon3.getType());
                    if (valueOf != null && valueOf.intValue() == 2) {
                        String packageName = statusBarNotification.getPackageName();
                        Icon icon4 = action.getIcon();
                        Intrinsics.checkNotNull(icon4);
                        icon2 = Icon.createWithResource(packageName, icon4.getResId());
                    } else {
                        icon2 = action.getIcon();
                    }
                    arrayList.add(new MediaAction(icon2.setTint(this.themeText), mediaDataManager$loadMediaDataInBg$runnable$1, action.title));
                }
                i4 = i5;
                actionArr2 = actionArr;
                length = i;
            }
        }
        MediaController.PlaybackInfo playbackInfo = create.getPlaybackInfo();
        Integer valueOf2 = playbackInfo == null ? null : Integer.valueOf(playbackInfo.getPlaybackType());
        boolean z = valueOf2 != null && valueOf2.intValue() == 1;
        PlaybackState playbackState = create.getPlaybackState();
        this.foregroundExecutor.execute(new Runnable(this, str, str2, statusBarNotification, loadHeaderAppName, smallIcon, ref$ObjectRef2, ref$ObjectRef, icon, arrayList, ref$ObjectRef3, token, notification, z, playbackState == null ? null : Boolean.valueOf(NotificationMediaManager.isPlayingState(playbackState.getState())), this.systemClock.elapsedRealtime()) { // from class: com.android.systemui.media.MediaDataManager$loadMediaDataInBg$1
            final /* synthetic */ List<MediaAction> $actionIcons;
            final /* synthetic */ Ref$ObjectRef<List<Integer>> $actionsToShowCollapsed;
            final /* synthetic */ String $app;
            final /* synthetic */ Icon $artWorkIcon;
            final /* synthetic */ Ref$ObjectRef<CharSequence> $artist;
            final /* synthetic */ boolean $isLocalSession;
            final /* synthetic */ Boolean $isPlaying;
            final /* synthetic */ String $key;
            final /* synthetic */ long $lastActive;
            final /* synthetic */ Notification $notif;
            final /* synthetic */ String $oldKey;
            final /* synthetic */ StatusBarNotification $sbn;
            final /* synthetic */ Icon $smallIcon;
            final /* synthetic */ Ref$ObjectRef<CharSequence> $song;
            final /* synthetic */ MediaSession.Token $token;
            final /* synthetic */ MediaDataManager this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r4;
                this.$key = r5;
                this.$oldKey = r6;
                this.$sbn = r7;
                this.$app = r8;
                this.$smallIcon = r9;
                this.$artist = r10;
                this.$song = r11;
                this.$artWorkIcon = r12;
                this.$actionIcons = r13;
                this.$actionsToShowCollapsed = r14;
                this.$token = r15;
                this.$notif = r16;
                this.$isLocalSession = r17;
                this.$isPlaying = r18;
                this.$lastActive = r19;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaData mediaData = (MediaData) this.this$0.mediaEntries.get(this.$key);
                Boolean bool = null;
                Runnable resumeAction = mediaData == null ? null : mediaData.getResumeAction();
                MediaData mediaData2 = (MediaData) this.this$0.mediaEntries.get(this.$key);
                if (mediaData2 != null) {
                    bool = Boolean.valueOf(mediaData2.getHasCheckedForResume());
                }
                boolean areEqual = Intrinsics.areEqual(bool, Boolean.TRUE);
                MediaData mediaData3 = (MediaData) this.this$0.mediaEntries.get(this.$key);
                boolean active = mediaData3 == null ? true : mediaData3.getActive();
                MediaDataManager mediaDataManager = this.this$0;
                String str3 = this.$key;
                String str4 = this.$oldKey;
                String packageName2 = this.$sbn.getPackageName();
                Intrinsics.checkNotNullExpressionValue(packageName2, "sbn.packageName");
                mediaDataManager.onMediaDataLoaded(str3, str4, new MediaData(this.$sbn.getNormalizedUserId(), true, this.this$0.bgColor, this.$app, this.$smallIcon, this.$artist.element, this.$song.element, this.$artWorkIcon, this.$actionIcons, this.$actionsToShowCollapsed.element, packageName2, this.$token, this.$notif.contentIntent, null, active, resumeAction, this.$isLocalSession, false, this.$key, areEqual, this.$isPlaying, this.$sbn.isClearable(), this.$lastActive, 131072, null));
            }
        });
    }

    private final Bitmap loadBitmapFromUri(MediaMetadata mediaMetadata) {
        String[] strArr = MediaDataManagerKt.ART_URIS;
        int length = strArr.length;
        int i = 0;
        while (i < length) {
            String str = strArr[i];
            i++;
            String string = mediaMetadata.getString(str);
            if (!TextUtils.isEmpty(string)) {
                Uri parse = Uri.parse(string);
                Intrinsics.checkNotNullExpressionValue(parse, "parse(uriString)");
                Bitmap loadBitmapFromUri = loadBitmapFromUri(parse);
                if (loadBitmapFromUri != null) {
                    Log.d("MediaDataManager", Intrinsics.stringPlus("loaded art from ", str));
                    return loadBitmapFromUri;
                }
            }
        }
        return null;
    }

    public final boolean sendPendingIntent(PendingIntent pendingIntent) {
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            Log.d("MediaDataManager", "Intent canceled", e);
            return false;
        }
    }

    private final Bitmap loadBitmapFromUri(Uri uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        if (!uri.getScheme().equals("content") && !uri.getScheme().equals("android.resource") && !uri.getScheme().equals("file")) {
            return null;
        }
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.context.getContentResolver(), uri), MediaDataManager$loadBitmapFromUri$1.INSTANCE);
        } catch (IOException e) {
            Log.e("MediaDataManager", "Unable to load bitmap", e);
            return null;
        } catch (RuntimeException e2) {
            Log.e("MediaDataManager", "Unable to load bitmap", e2);
            return null;
        }
    }

    private final MediaAction getResumeMediaAction(Runnable runnable) {
        return new MediaAction(Icon.createWithResource(this.context, R$drawable.lb_ic_play).setTint(this.themeText), runnable, this.context.getString(R$string.controls_media_resume));
    }

    public final void onMediaDataLoaded(String str, String str2, MediaData mediaData) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        Assert.isMainThread();
        if (this.mediaEntries.containsKey(str)) {
            this.mediaEntries.put(str, mediaData);
            notifyMediaDataLoaded(str, str2, mediaData);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceTargetListener
    public void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        Intrinsics.checkNotNullParameter(list, "targets");
        if (!this.allowMediaRecommendations) {
            Log.d("MediaDataManager", "Smartspace recommendation is disabled in Settings.");
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (obj instanceof SmartspaceTarget) {
                arrayList.add(obj);
            }
        }
        int size = arrayList.size();
        if (size != 0) {
            if (size != 1) {
                Log.wtf("MediaDataManager", "More than 1 Smartspace Media Update. Resetting the status...");
                notifySmartspaceMediaDataRemoved(this.smartspaceMediaData.getTargetId(), false);
                this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
                return;
            }
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) arrayList.get(0);
            if (!Intrinsics.areEqual(this.smartspaceMediaData.getTargetId(), smartspaceTarget.getSmartspaceTargetId())) {
                Log.d("MediaDataManager", "Forwarding Smartspace media update.");
                SmartspaceMediaData smartspaceMediaData = toSmartspaceMediaData(smartspaceTarget, true);
                this.smartspaceMediaData = smartspaceMediaData;
                notifySmartspaceMediaDataLoaded(smartspaceMediaData.getTargetId(), this.smartspaceMediaData);
            }
        } else if (this.smartspaceMediaData.isActive()) {
            Log.d("MediaDataManager", "Set Smartspace media to be inactive for the data update");
            SmartspaceMediaData copy$default = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, false, null, null, null, 0, R$styleable.AppCompatTheme_windowNoTitle, null);
            this.smartspaceMediaData = copy$default;
            notifySmartspaceMediaDataRemoved(copy$default.getTargetId(), false);
        }
    }

    public final void onNotificationRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        Assert.isMainThread();
        MediaData remove = this.mediaEntries.remove(str);
        if (this.useMediaResumption) {
            Boolean bool = null;
            if ((remove == null ? null : remove.getResumeAction()) != null) {
                if (remove != null) {
                    bool = Boolean.valueOf(remove.isLocalSession());
                }
                if (bool.booleanValue()) {
                    Log.d("MediaDataManager", "Not removing " + str + " because resumable");
                    Runnable resumeAction = remove.getResumeAction();
                    Intrinsics.checkNotNull(resumeAction);
                    boolean z = false;
                    MediaData copy$default = MediaData.copy$default(remove, 0, false, 0, null, null, null, null, null, CollectionsKt__CollectionsJVMKt.listOf(getResumeMediaAction(resumeAction)), CollectionsKt__CollectionsJVMKt.listOf(0), null, null, null, null, false, null, false, true, null, false, null, true, 0, 6141183, null);
                    String packageName = remove.getPackageName();
                    if (this.mediaEntries.put(packageName, copy$default) == null) {
                        z = true;
                    }
                    if (z) {
                        notifyMediaDataLoaded(packageName, str, copy$default);
                        return;
                    }
                    notifyMediaDataRemoved(str);
                    notifyMediaDataLoaded(packageName, packageName, copy$default);
                    return;
                }
            }
        }
        if (remove != null) {
            notifyMediaDataRemoved(str);
        }
    }

    public final void setMediaResumptionEnabled(boolean z) {
        if (this.useMediaResumption != z) {
            this.useMediaResumption = z;
            if (!z) {
                LinkedHashMap<String, MediaData> linkedHashMap = this.mediaEntries;
                LinkedHashMap linkedHashMap2 = new LinkedHashMap();
                for (Map.Entry<String, MediaData> entry : linkedHashMap.entrySet()) {
                    if (!entry.getValue().getActive()) {
                        linkedHashMap2.put(entry.getKey(), entry.getValue());
                    }
                }
                for (Map.Entry entry2 : linkedHashMap2.entrySet()) {
                    this.mediaEntries.remove(entry2.getKey());
                    notifyMediaDataRemoved((String) entry2.getKey());
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        this.mediaDataFilter.onSwipeToDismiss();
    }

    public final boolean hasActiveMedia() {
        return this.mediaDataFilter.hasActiveMedia();
    }

    public final boolean hasAnyMedia() {
        return this.mediaDataFilter.hasAnyMedia();
    }

    /* compiled from: MediaDataManager.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2);

        void onMediaDataRemoved(String str);

        void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z);

        void onSmartspaceMediaDataRemoved(String str, boolean z);

        /* compiled from: MediaDataManager.kt */
        /* loaded from: classes.dex */
        public static final class DefaultImpls {
            public static void onMediaDataRemoved(Listener listener, String str) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(str, "key");
            }

            public static void onSmartspaceMediaDataLoaded(Listener listener, String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(str, "key");
                Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
            }

            public static void onSmartspaceMediaDataRemoved(Listener listener, String str, boolean z) {
                Intrinsics.checkNotNullParameter(listener, "this");
                Intrinsics.checkNotNullParameter(str, "key");
            }

            public static /* synthetic */ void onMediaDataLoaded$default(Listener listener, String str, String str2, MediaData mediaData, boolean z, boolean z2, int i, Object obj) {
                if (obj == null) {
                    if ((i & 8) != 0) {
                        z = true;
                    }
                    if ((i & 16) != 0) {
                        z2 = false;
                    }
                    listener.onMediaDataLoaded(str, str2, mediaData, z, z2);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onMediaDataLoaded");
            }

            public static /* synthetic */ void onSmartspaceMediaDataLoaded$default(Listener listener, String str, SmartspaceMediaData smartspaceMediaData, boolean z, int i, Object obj) {
                if (obj == null) {
                    if ((i & 4) != 0) {
                        z = false;
                    }
                    listener.onSmartspaceMediaDataLoaded(str, smartspaceMediaData, z);
                    return;
                }
                throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSmartspaceMediaDataLoaded");
            }
        }
    }

    private final SmartspaceMediaData toSmartspaceMediaData(SmartspaceTarget smartspaceTarget, boolean z) {
        String packageName = packageName(smartspaceTarget);
        if (packageName == null) {
            SmartspaceMediaData empty_smartspace_media_data = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
            String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
            Intrinsics.checkNotNullExpressionValue(smartspaceTargetId, "target.smartspaceTargetId");
            return SmartspaceMediaData.copy$default(empty_smartspace_media_data, smartspaceTargetId, z, false, null, null, null, 0, R$styleable.AppCompatTheme_windowMinWidthMajor, null);
        }
        String smartspaceTargetId2 = smartspaceTarget.getSmartspaceTargetId();
        Intrinsics.checkNotNullExpressionValue(smartspaceTargetId2, "target.smartspaceTargetId");
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        List iconGrid = smartspaceTarget.getIconGrid();
        Intrinsics.checkNotNullExpressionValue(iconGrid, "target.iconGrid");
        return new SmartspaceMediaData(smartspaceTargetId2, z, true, packageName, baseAction, iconGrid, 0);
    }

    private final String packageName(SmartspaceTarget smartspaceTarget) {
        String string;
        List<SmartspaceAction> iconGrid = smartspaceTarget.getIconGrid();
        if (iconGrid == null || iconGrid.isEmpty()) {
            Log.w("MediaDataManager", "Empty or null media recommendation list.");
            return null;
        }
        for (SmartspaceAction smartspaceAction : iconGrid) {
            Bundle extras = smartspaceAction.getExtras();
            if (!(extras == null || (string = extras.getString(EXTRAS_MEDIA_SOURCE_PACKAGE_NAME)) == null)) {
                return string;
            }
        }
        Log.w("MediaDataManager", "No valid package name is provided.");
        return null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("internalListeners: ", this.internalListeners));
        printWriter.println(Intrinsics.stringPlus("externalListeners: ", this.mediaDataFilter.getListeners$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        printWriter.println(Intrinsics.stringPlus("mediaEntries: ", this.mediaEntries));
        printWriter.println(Intrinsics.stringPlus("useMediaResumption: ", Boolean.valueOf(this.useMediaResumption)));
    }
}
