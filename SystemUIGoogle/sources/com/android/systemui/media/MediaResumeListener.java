package com.android.systemui.media;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.media.MediaDescription;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.ResumeMediaBrowser;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: MediaResumeListener.kt */
/* loaded from: classes.dex */
public final class MediaResumeListener implements MediaDataManager.Listener, Dumpable {
    private final Executor backgroundExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private int currentUserId;
    private ResumeMediaBrowser mediaBrowser;
    private final ResumeMediaBrowserFactory mediaBrowserFactory;
    private MediaDataManager mediaDataManager;
    private final TunerService tunerService;
    private boolean useMediaResumption;
    private final BroadcastReceiver userChangeReceiver;
    private final ConcurrentLinkedQueue<ComponentName> resumeComponents = new ConcurrentLinkedQueue<>();
    private final MediaResumeListener$mediaBrowserCallback$1 mediaBrowserCallback = new MediaResumeListener$mediaBrowserCallback$1(this);

    @VisibleForTesting
    public static /* synthetic */ void getUserChangeReceiver$annotations() {
    }

    public MediaResumeListener(Context context, BroadcastDispatcher broadcastDispatcher, Executor executor, TunerService tunerService, ResumeMediaBrowserFactory resumeMediaBrowserFactory, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(resumeMediaBrowserFactory, "mediaBrowserFactory");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.context = context;
        this.broadcastDispatcher = broadcastDispatcher;
        this.backgroundExecutor = executor;
        this.tunerService = tunerService;
        this.mediaBrowserFactory = resumeMediaBrowserFactory;
        this.useMediaResumption = Utils.useMediaResumption(context);
        this.currentUserId = context.getUserId();
        MediaResumeListener$userChangeReceiver$1 mediaResumeListener$userChangeReceiver$1 = new BroadcastReceiver(this) { // from class: com.android.systemui.media.MediaResumeListener$userChangeReceiver$1
            final /* synthetic */ MediaResumeListener this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Intrinsics.checkNotNullParameter(context2, "context");
                Intrinsics.checkNotNullParameter(intent, "intent");
                if (Intrinsics.areEqual("android.intent.action.USER_UNLOCKED", intent.getAction())) {
                    this.this$0.loadMediaResumptionControls();
                } else if (Intrinsics.areEqual("android.intent.action.USER_SWITCHED", intent.getAction())) {
                    this.this$0.currentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                    this.this$0.loadSavedComponents();
                }
            }
        };
        this.userChangeReceiver = mediaResumeListener$userChangeReceiver$1;
        if (this.useMediaResumption) {
            dumpManager.registerDumpable("MediaResumeListener", this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            broadcastDispatcher.registerReceiver(mediaResumeListener$userChangeReceiver$1, intentFilter, null, UserHandle.ALL);
            loadSavedComponents();
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(String str) {
        MediaDataManager.Listener.DefaultImpls.onMediaDataRemoved(this, str);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataLoaded(this, str, smartspaceMediaData, z);
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        MediaDataManager.Listener.DefaultImpls.onSmartspaceMediaDataRemoved(this, str, z);
    }

    public final void setManager(MediaDataManager mediaDataManager) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "manager");
        this.mediaDataManager = mediaDataManager;
        this.tunerService.addTunable(new TunerService.Tunable(this) { // from class: com.android.systemui.media.MediaResumeListener$setManager$1
            final /* synthetic */ MediaResumeListener this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public void onTuningChanged(String str, String str2) {
                MediaResumeListener mediaResumeListener = this.this$0;
                mediaResumeListener.useMediaResumption = Utils.useMediaResumption(mediaResumeListener.context);
                MediaDataManager mediaDataManager2 = this.this$0.mediaDataManager;
                if (mediaDataManager2 != null) {
                    mediaDataManager2.setMediaResumptionEnabled(this.this$0.useMediaResumption);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                    throw null;
                }
            }
        }, "qs_media_resumption");
    }

    /* access modifiers changed from: private */
    public final void loadSavedComponents() {
        List<String> split;
        boolean z;
        this.resumeComponents.clear();
        List<String> list = null;
        String string = this.context.getSharedPreferences("media_control_prefs", 0).getString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), null);
        if (string != null && (split = new Regex(":").split(string, 0)) != null) {
            if (!split.isEmpty()) {
                ListIterator<String> listIterator = split.listIterator(split.size());
                while (listIterator.hasPrevious()) {
                    if (listIterator.previous().length() == 0) {
                        z = true;
                        continue;
                    } else {
                        z = false;
                        continue;
                    }
                    if (!z) {
                        list = CollectionsKt___CollectionsKt.take(split, listIterator.nextIndex() + 1);
                        break;
                    }
                }
            }
            list = CollectionsKt__CollectionsKt.emptyList();
        }
        if (list != null) {
            for (String str : list) {
                List list2 = StringsKt__StringsKt.split$default(str, new String[]{"/"}, false, 0, 6, null);
                this.resumeComponents.add(new ComponentName((String) list2.get(0), (String) list2.get(1)));
            }
        }
        String arrays = Arrays.toString(this.resumeComponents.toArray());
        Intrinsics.checkNotNullExpressionValue(arrays, "java.util.Arrays.toString(this)");
        Log.d("MediaResumeListener", Intrinsics.stringPlus("loaded resume components ", arrays));
    }

    /* access modifiers changed from: private */
    public final void loadMediaResumptionControls() {
        if (this.useMediaResumption) {
            for (ComponentName componentName : this.resumeComponents) {
                this.mediaBrowserFactory.create(this.mediaBrowserCallback, componentName).findRecentMedia();
            }
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        ArrayList arrayList;
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (this.useMediaResumption) {
            if (!str.equals(str2)) {
                ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
                if (resumeMediaBrowser != null) {
                    resumeMediaBrowser.disconnect();
                }
                this.mediaBrowser = null;
            }
            if (mediaData.getResumeAction() == null && !mediaData.getHasCheckedForResume() && mediaData.isLocalSession()) {
                Log.d("MediaResumeListener", Intrinsics.stringPlus("Checking for service component for ", mediaData.getPackageName()));
                List<ResolveInfo> queryIntentServices = this.context.getPackageManager().queryIntentServices(new Intent("android.media.browse.MediaBrowserService"), 0);
                if (queryIntentServices == null) {
                    arrayList = null;
                } else {
                    arrayList = new ArrayList();
                    for (Object obj : queryIntentServices) {
                        if (Intrinsics.areEqual(((ResolveInfo) obj).serviceInfo.packageName, mediaData.getPackageName())) {
                            arrayList.add(obj);
                        }
                    }
                }
                if (arrayList == null || arrayList.size() <= 0) {
                    MediaDataManager mediaDataManager = this.mediaDataManager;
                    if (mediaDataManager != null) {
                        mediaDataManager.setResumeAction(str, null);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                        throw null;
                    }
                } else {
                    this.backgroundExecutor.execute(new Runnable(this, str, arrayList) { // from class: com.android.systemui.media.MediaResumeListener$onMediaDataLoaded$1
                        final /* synthetic */ List<ResolveInfo> $inf;
                        final /* synthetic */ String $key;
                        final /* synthetic */ MediaResumeListener this$0;

                        /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: java.util.List<? extends android.content.pm.ResolveInfo> */
                        /* JADX WARN: Multi-variable type inference failed */
                        /* access modifiers changed from: package-private */
                        {
                            this.this$0 = r1;
                            this.$key = r2;
                            this.$inf = r3;
                        }

                        @Override // java.lang.Runnable
                        public final void run() {
                            MediaResumeListener mediaResumeListener = this.this$0;
                            String str3 = this.$key;
                            Intrinsics.checkNotNull(this.$inf);
                            ComponentName componentName = this.$inf.get(0).getComponentInfo().getComponentName();
                            Intrinsics.checkNotNullExpressionValue(componentName, "!!.get(0).componentInfo.componentName");
                            mediaResumeListener.tryUpdateResumptionList(str3, componentName);
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public final void tryUpdateResumptionList(String str, ComponentName componentName) {
        Log.d("MediaResumeListener", Intrinsics.stringPlus("Testing if we can connect to ", componentName));
        MediaDataManager mediaDataManager = this.mediaDataManager;
        if (mediaDataManager != null) {
            mediaDataManager.setResumeAction(str, null);
            ResumeMediaBrowser resumeMediaBrowser = this.mediaBrowser;
            if (resumeMediaBrowser != null) {
                resumeMediaBrowser.disconnect();
            }
            ResumeMediaBrowser create = this.mediaBrowserFactory.create(new ResumeMediaBrowser.Callback(componentName, this, str) { // from class: com.android.systemui.media.MediaResumeListener$tryUpdateResumptionList$1
                final /* synthetic */ ComponentName $componentName;
                final /* synthetic */ String $key;
                final /* synthetic */ MediaResumeListener this$0;

                /* access modifiers changed from: package-private */
                {
                    this.$componentName = r1;
                    this.this$0 = r2;
                    this.$key = r3;
                }

                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                public void onConnected() {
                    Log.d("MediaResumeListener", Intrinsics.stringPlus("Connected to ", this.$componentName));
                }

                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                public void onError() {
                    Log.e("MediaResumeListener", Intrinsics.stringPlus("Cannot resume with ", this.$componentName));
                    this.this$0.mediaBrowser = null;
                }

                @Override // com.android.systemui.media.ResumeMediaBrowser.Callback
                public void addTrack(MediaDescription mediaDescription, ComponentName componentName2, ResumeMediaBrowser resumeMediaBrowser2) {
                    Intrinsics.checkNotNullParameter(mediaDescription, "desc");
                    Intrinsics.checkNotNullParameter(componentName2, "component");
                    Intrinsics.checkNotNullParameter(resumeMediaBrowser2, "browser");
                    Log.d("MediaResumeListener", Intrinsics.stringPlus("Can get resumable media from ", this.$componentName));
                    MediaDataManager mediaDataManager2 = this.this$0.mediaDataManager;
                    if (mediaDataManager2 != null) {
                        mediaDataManager2.setResumeAction(this.$key, this.this$0.getResumeAction(this.$componentName));
                        this.this$0.updateResumptionList(this.$componentName);
                        this.this$0.mediaBrowser = null;
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
                    throw null;
                }
            }, componentName);
            this.mediaBrowser = create;
            if (create != null) {
                create.testConnection();
                return;
            }
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
        throw null;
    }

    /* access modifiers changed from: private */
    public final void updateResumptionList(ComponentName componentName) {
        this.resumeComponents.remove(componentName);
        this.resumeComponents.add(componentName);
        if (this.resumeComponents.size() > 5) {
            this.resumeComponents.remove();
        }
        StringBuilder sb = new StringBuilder();
        for (ComponentName componentName2 : this.resumeComponents) {
            sb.append(componentName2.flattenToString());
            sb.append(":");
        }
        this.context.getSharedPreferences("media_control_prefs", 0).edit().putString(Intrinsics.stringPlus("browser_components_", Integer.valueOf(this.currentUserId)), sb.toString()).apply();
    }

    /* access modifiers changed from: private */
    public final Runnable getResumeAction(ComponentName componentName) {
        return new Runnable(this, componentName) { // from class: com.android.systemui.media.MediaResumeListener$getResumeAction$1
            final /* synthetic */ ComponentName $componentName;
            final /* synthetic */ MediaResumeListener this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$componentName = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                MediaResumeListener mediaResumeListener = this.this$0;
                mediaResumeListener.mediaBrowser = mediaResumeListener.mediaBrowserFactory.create(null, this.$componentName);
                ResumeMediaBrowser resumeMediaBrowser = this.this$0.mediaBrowser;
                if (resumeMediaBrowser != null) {
                    resumeMediaBrowser.restart();
                }
            }
        };
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println(Intrinsics.stringPlus("resumeComponents: ", this.resumeComponents));
    }
}
