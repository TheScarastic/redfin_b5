package com.android.systemui.privacy;

import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.provider.DeviceConfig;
import androidx.constraintlayout.widget.R$styleable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.appops.AppOpItem;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.logging.PrivacyLogger;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysJvmKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItemController.kt */
/* loaded from: classes.dex */
public final class PrivacyItemController implements Dumpable {
    public static final Companion Companion;
    private static final int[] OPS;
    private static final IntentFilter intentFilter;
    private boolean allIndicatorsAvailable;
    private final AppOpsController appOpsController;
    private final DelayableExecutor bgExecutor;
    private final PrivacyItemController$cb$1 cb;
    private final DeviceConfigProxy deviceConfigProxy;
    private final PrivacyItemController$devicePropertiesChangedListener$1 devicePropertiesChangedListener;
    private Runnable holdingRunnableCanceler;
    private final MyExecutor internalUiExecutor;
    private boolean listening;
    private boolean locationAvailable;
    private final PrivacyLogger logger;
    private final SystemClock systemClock;
    private final Runnable updateListAndNotifyChanges;
    private final UserTracker userTracker;
    private UserTracker.Callback userTrackerCallback;
    private static final int[] OPS_MIC_CAMERA = {26, R$styleable.Constraint_layout_goneMarginRight, 27, 100};
    private static final int[] OPS_LOCATION = {0, 1};
    private List<PrivacyItem> privacyList = CollectionsKt__CollectionsKt.emptyList();
    private List<Integer> currentUserIds = CollectionsKt__CollectionsKt.emptyList();
    private final List<WeakReference<Callback>> callbacks = new ArrayList();
    private final Runnable notifyChanges = new Runnable(this) { // from class: com.android.systemui.privacy.PrivacyItemController$notifyChanges$1
        final /* synthetic */ PrivacyItemController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // java.lang.Runnable
        public final void run() {
            List<PrivacyItem> privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core = this.this$0.getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
            for (WeakReference weakReference : this.this$0.callbacks) {
                PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
                if (callback != null) {
                    callback.onPrivacyItemsChanged(privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core);
                }
            }
        }
    };
    private boolean micCameraAvailable = isMicCameraEnabled();

    /* compiled from: PrivacyItemController.kt */
    /* loaded from: classes.dex */
    public interface Callback {
        default void onFlagLocationChanged(boolean z) {
        }

        default void onFlagMicCameraChanged(boolean z) {
        }

        void onPrivacyItemsChanged(List<PrivacyItem> list);
    }

    @VisibleForTesting
    public static /* synthetic */ void getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getUserTrackerCallback$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public PrivacyItemController(AppOpsController appOpsController, DelayableExecutor delayableExecutor, DelayableExecutor delayableExecutor2, DeviceConfigProxy deviceConfigProxy, UserTracker userTracker, PrivacyLogger privacyLogger, SystemClock systemClock, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(appOpsController, "appOpsController");
        Intrinsics.checkNotNullParameter(delayableExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "bgExecutor");
        Intrinsics.checkNotNullParameter(deviceConfigProxy, "deviceConfigProxy");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(privacyLogger, "logger");
        Intrinsics.checkNotNullParameter(systemClock, "systemClock");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.appOpsController = appOpsController;
        this.bgExecutor = delayableExecutor2;
        this.deviceConfigProxy = deviceConfigProxy;
        this.userTracker = userTracker;
        this.logger = privacyLogger;
        this.systemClock = systemClock;
        this.internalUiExecutor = new MyExecutor(this, delayableExecutor);
        this.updateListAndNotifyChanges = new Runnable(this, delayableExecutor) { // from class: com.android.systemui.privacy.PrivacyItemController$updateListAndNotifyChanges$1
            final /* synthetic */ DelayableExecutor $uiExecutor;
            final /* synthetic */ PrivacyItemController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$uiExecutor = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.updatePrivacyList();
                this.$uiExecutor.execute(this.this$0.notifyChanges);
            }
        };
        boolean isLocationEnabled = isLocationEnabled();
        this.locationAvailable = isLocationEnabled;
        this.allIndicatorsAvailable = this.micCameraAvailable && isLocationEnabled;
        PrivacyItemController$devicePropertiesChangedListener$1 privacyItemController$devicePropertiesChangedListener$1 = new DeviceConfig.OnPropertiesChangedListener(this) { // from class: com.android.systemui.privacy.PrivacyItemController$devicePropertiesChangedListener$1
            final /* synthetic */ PrivacyItemController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                Intrinsics.checkNotNullParameter(properties, "properties");
                if (!"privacy".equals(properties.getNamespace())) {
                    return;
                }
                if (properties.getKeyset().contains("camera_mic_icons_enabled") || properties.getKeyset().contains("location_indicators_enabled")) {
                    boolean z = false;
                    if (properties.getKeyset().contains("camera_mic_icons_enabled")) {
                        this.this$0.micCameraAvailable = properties.getBoolean("camera_mic_icons_enabled", true);
                        PrivacyItemController privacyItemController = this.this$0;
                        privacyItemController.setAllIndicatorsAvailable(privacyItemController.getMicCameraAvailable() && this.this$0.getLocationAvailable());
                        List<WeakReference> list = this.this$0.callbacks;
                        PrivacyItemController privacyItemController2 = this.this$0;
                        for (WeakReference weakReference : list) {
                            PrivacyItemController.Callback callback = (PrivacyItemController.Callback) weakReference.get();
                            if (callback != null) {
                                callback.onFlagMicCameraChanged(privacyItemController2.getMicCameraAvailable());
                            }
                        }
                    }
                    if (properties.getKeyset().contains("location_indicators_enabled")) {
                        this.this$0.setLocationAvailable(properties.getBoolean("location_indicators_enabled", false));
                        PrivacyItemController privacyItemController3 = this.this$0;
                        if (privacyItemController3.getMicCameraAvailable() && this.this$0.getLocationAvailable()) {
                            z = true;
                        }
                        privacyItemController3.setAllIndicatorsAvailable(z);
                        List<WeakReference> list2 = this.this$0.callbacks;
                        PrivacyItemController privacyItemController4 = this.this$0;
                        for (WeakReference weakReference2 : list2) {
                            PrivacyItemController.Callback callback2 = (PrivacyItemController.Callback) weakReference2.get();
                            if (callback2 != null) {
                                callback2.onFlagLocationChanged(privacyItemController4.getLocationAvailable());
                            }
                        }
                    }
                    this.this$0.internalUiExecutor.updateListeningState();
                }
            }
        };
        this.devicePropertiesChangedListener = privacyItemController$devicePropertiesChangedListener$1;
        this.cb = new PrivacyItemController$cb$1(this);
        this.userTrackerCallback = new UserTracker.Callback(this) { // from class: com.android.systemui.privacy.PrivacyItemController$userTrackerCallback$1
            final /* synthetic */ PrivacyItemController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.settings.UserTracker.Callback
            public void onUserChanged(int i, Context context) {
                Intrinsics.checkNotNullParameter(context, "userContext");
                this.this$0.update(true);
            }

            @Override // com.android.systemui.settings.UserTracker.Callback
            public void onProfilesChanged(List<? extends UserInfo> list) {
                Intrinsics.checkNotNullParameter(list, "profiles");
                this.this$0.update(true);
            }
        };
        deviceConfigProxy.addOnPropertiesChangedListener("privacy", delayableExecutor, privacyItemController$devicePropertiesChangedListener$1);
        dumpManager.registerDumpable("PrivacyItemController", this);
    }

    /* compiled from: PrivacyItemController.kt */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        @VisibleForTesting
        public static /* synthetic */ void getTIME_TO_HOLD_INDICATORS$annotations() {
        }

        private Companion() {
        }

        public final int[] getOPS_MIC_CAMERA() {
            return PrivacyItemController.OPS_MIC_CAMERA;
        }

        public final int[] getOPS_LOCATION() {
            return PrivacyItemController.OPS_LOCATION;
        }

        public final int[] getOPS() {
            return PrivacyItemController.OPS;
        }
    }

    static {
        Companion companion = new Companion(null);
        Companion = companion;
        OPS = ArraysKt___ArraysJvmKt.plus(companion.getOPS_MIC_CAMERA(), companion.getOPS_LOCATION());
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.intent.action.USER_SWITCHED");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_AVAILABLE");
        intentFilter2.addAction("android.intent.action.MANAGED_PROFILE_UNAVAILABLE");
        intentFilter = intentFilter2;
    }

    public final synchronized List<PrivacyItem> getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return CollectionsKt___CollectionsKt.toList(this.privacyList);
    }

    private final boolean isMicCameraEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "camera_mic_icons_enabled", true);
    }

    private final boolean isLocationEnabled() {
        return this.deviceConfigProxy.getBoolean("privacy", "location_indicators_enabled", false);
    }

    public final boolean getMicCameraAvailable() {
        return this.micCameraAvailable;
    }

    public final boolean getLocationAvailable() {
        return this.locationAvailable;
    }

    public final void setLocationAvailable(boolean z) {
        this.locationAvailable = z;
    }

    public final boolean getAllIndicatorsAvailable() {
        return this.allIndicatorsAvailable;
    }

    public final void setAllIndicatorsAvailable(boolean z) {
        this.allIndicatorsAvailable = z;
    }

    private final void unregisterListener() {
        this.userTracker.removeCallback(this.userTrackerCallback);
    }

    private final void registerReceiver() {
        this.userTracker.addCallback(this.userTrackerCallback, this.bgExecutor);
    }

    /* access modifiers changed from: private */
    public final void update(boolean z) {
        this.bgExecutor.execute(new Runnable(z, this) { // from class: com.android.systemui.privacy.PrivacyItemController$update$1
            final /* synthetic */ boolean $updateUsers;
            final /* synthetic */ PrivacyItemController this$0;

            /* access modifiers changed from: package-private */
            {
                this.$updateUsers = r1;
                this.this$0 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                if (this.$updateUsers) {
                    PrivacyItemController privacyItemController = this.this$0;
                    List<UserInfo> userProfiles = privacyItemController.userTracker.getUserProfiles();
                    ArrayList arrayList = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(userProfiles, 10));
                    for (UserInfo userInfo : userProfiles) {
                        arrayList.add(Integer.valueOf(userInfo.id));
                    }
                    privacyItemController.currentUserIds = arrayList;
                    this.this$0.logger.logCurrentProfilesChanged(this.this$0.currentUserIds);
                }
                this.this$0.updateListAndNotifyChanges.run();
            }
        });
    }

    /* access modifiers changed from: private */
    public final void setListeningState() {
        boolean z = (!this.callbacks.isEmpty()) & (this.micCameraAvailable || this.locationAvailable);
        if (this.listening != z) {
            this.listening = z;
            if (z) {
                this.appOpsController.addCallback(Companion.getOPS(), this.cb);
                registerReceiver();
                update(true);
                return;
            }
            this.appOpsController.removeCallback(Companion.getOPS(), this.cb);
            unregisterListener();
            update(false);
        }
    }

    private final void addCallback(WeakReference<Callback> weakReference) {
        this.callbacks.add(weakReference);
        if ((!this.callbacks.isEmpty()) && !this.listening) {
            this.internalUiExecutor.updateListeningState();
        } else if (this.listening) {
            this.internalUiExecutor.execute(new NotifyChangesToCallback(weakReference.get(), getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()));
        }
    }

    private final void removeCallback(WeakReference<Callback> weakReference) {
        this.callbacks.removeIf(new Predicate<WeakReference<Callback>>(weakReference) { // from class: com.android.systemui.privacy.PrivacyItemController$removeCallback$1
            final /* synthetic */ WeakReference<PrivacyItemController.Callback> $callback;

            /* access modifiers changed from: package-private */
            {
                this.$callback = r1;
            }

            public final boolean test(WeakReference<PrivacyItemController.Callback> weakReference2) {
                Intrinsics.checkNotNullParameter(weakReference2, "it");
                PrivacyItemController.Callback callback = weakReference2.get();
                if (callback == null) {
                    return true;
                }
                return callback.equals(this.$callback.get());
            }
        });
        if (this.callbacks.isEmpty()) {
            this.internalUiExecutor.updateListeningState();
        }
    }

    public final void addCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        addCallback(new WeakReference<>(callback));
    }

    public final void removeCallback(Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback(new WeakReference<>(callback));
    }

    /* access modifiers changed from: private */
    public final void updatePrivacyList() {
        Runnable runnable = this.holdingRunnableCanceler;
        if (runnable != null) {
            runnable.run();
            Unit unit = Unit.INSTANCE;
            this.holdingRunnableCanceler = null;
        }
        if (!this.listening) {
            this.privacyList = CollectionsKt__CollectionsKt.emptyList();
            return;
        }
        List<AppOpItem> activeAppOps = this.appOpsController.getActiveAppOps(true);
        Intrinsics.checkNotNullExpressionValue(activeAppOps, "appOpsController.getActiveAppOps(true)");
        ArrayList<AppOpItem> arrayList = new ArrayList();
        for (Object obj : activeAppOps) {
            AppOpItem appOpItem = (AppOpItem) obj;
            if (this.currentUserIds.contains(Integer.valueOf(UserHandle.getUserId(appOpItem.getUid()))) || appOpItem.getCode() == 100 || appOpItem.getCode() == 101) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList();
        for (AppOpItem appOpItem2 : arrayList) {
            Intrinsics.checkNotNullExpressionValue(appOpItem2, "it");
            PrivacyItem privacyItem = toPrivacyItem(appOpItem2);
            if (privacyItem != null) {
                arrayList2.add(privacyItem);
            }
        }
        this.privacyList = processNewList(CollectionsKt___CollectionsKt.distinct(arrayList2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX DEBUG: Type inference failed for r2v2. Raw type applied. Possible types: java.util.Iterator<T>, java.util.Iterator */
    /* JADX DEBUG: Type inference failed for r13v1. Raw type applied. Possible types: java.util.Iterator<T>, java.util.Iterator */
    /* JADX WARN: Type inference failed for: r4v6 */
    private final List<PrivacyItem> processNewList(List<PrivacyItem> list) {
        PrivacyItem privacyItem;
        this.logger.logRetrievedPrivacyItemsList(list);
        long elapsedRealtime = this.systemClock.elapsedRealtime() - 5000;
        List<PrivacyItem> privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core = getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core();
        ArrayList arrayList = new ArrayList();
        Iterator it = privacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core.iterator();
        while (true) {
            boolean z = true;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            PrivacyItem privacyItem2 = (PrivacyItem) next;
            if (privacyItem2.getTimeStampElapsed() <= elapsedRealtime || isIn(privacyItem2, list)) {
                z = false;
            }
            if (z) {
                arrayList.add(next);
            }
        }
        if (!arrayList.isEmpty()) {
            this.logger.logPrivacyItemsToHold(arrayList);
            Iterator it2 = arrayList.iterator();
            if (!it2.hasNext()) {
                privacyItem = null;
            } else {
                Object next2 = it2.next();
                if (!it2.hasNext()) {
                    privacyItem = next2;
                } else {
                    long timeStampElapsed = ((PrivacyItem) next2).getTimeStampElapsed();
                    do {
                        Object next3 = it2.next();
                        long timeStampElapsed2 = ((PrivacyItem) next3).getTimeStampElapsed();
                        if (timeStampElapsed > timeStampElapsed2) {
                            next2 = next3;
                            timeStampElapsed = timeStampElapsed2;
                        }
                    } while (it2.hasNext());
                    privacyItem = next2;
                }
            }
            PrivacyItem privacyItem3 = privacyItem;
            Intrinsics.checkNotNull(privacyItem3);
            long timeStampElapsed3 = privacyItem3.getTimeStampElapsed() - elapsedRealtime;
            this.logger.logPrivacyItemsUpdateScheduled(timeStampElapsed3);
            this.holdingRunnableCanceler = this.bgExecutor.executeDelayed(this.updateListAndNotifyChanges, timeStampElapsed3);
        }
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : list) {
            if (!((PrivacyItem) obj).getPaused()) {
                arrayList2.add(obj);
            }
        }
        return CollectionsKt___CollectionsKt.plus((Collection) arrayList2, (Iterable) arrayList);
    }

    private final PrivacyItem toPrivacyItem(AppOpItem appOpItem) {
        PrivacyType privacyType;
        int code = appOpItem.getCode();
        if (code == 0 || code == 1) {
            privacyType = PrivacyType.TYPE_LOCATION;
        } else {
            if (code != 26) {
                if (code == 27 || code == 100) {
                    privacyType = PrivacyType.TYPE_MICROPHONE;
                } else if (code != 101) {
                    return null;
                }
            }
            privacyType = PrivacyType.TYPE_CAMERA;
        }
        if (privacyType == PrivacyType.TYPE_LOCATION && !this.locationAvailable) {
            return null;
        }
        String packageName = appOpItem.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "appOpItem.packageName");
        return new PrivacyItem(privacyType, new PrivacyApplication(packageName, appOpItem.getUid()), appOpItem.getTimeStartedElapsed(), appOpItem.isDisabled());
    }

    /* compiled from: PrivacyItemController.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class NotifyChangesToCallback implements Runnable {
        private final Callback callback;
        private final List<PrivacyItem> list;

        public NotifyChangesToCallback(Callback callback, List<PrivacyItem> list) {
            Intrinsics.checkNotNullParameter(list, "list");
            this.callback = callback;
            this.list = list;
        }

        @Override // java.lang.Runnable
        public void run() {
            Callback callback = this.callback;
            if (callback != null) {
                callback.onPrivacyItemsChanged(this.list);
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("PrivacyItemController state:");
        printWriter.println(Intrinsics.stringPlus("  Listening: ", Boolean.valueOf(this.listening)));
        printWriter.println(Intrinsics.stringPlus("  Current user ids: ", this.currentUserIds));
        printWriter.println("  Privacy Items:");
        for (PrivacyItem privacyItem : getPrivacyList$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            printWriter.print("    ");
            printWriter.println(privacyItem.toString());
        }
        printWriter.println("  Callbacks:");
        Iterator<T> it = this.callbacks.iterator();
        while (it.hasNext()) {
            Callback callback = (Callback) ((WeakReference) it.next()).get();
            if (callback != null) {
                printWriter.print("    ");
                printWriter.println(callback.toString());
            }
        }
    }

    /* compiled from: PrivacyItemController.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public final class MyExecutor implements Executor {
        private final DelayableExecutor delegate;
        private Runnable listeningCanceller;
        final /* synthetic */ PrivacyItemController this$0;

        public MyExecutor(PrivacyItemController privacyItemController, DelayableExecutor delayableExecutor) {
            Intrinsics.checkNotNullParameter(privacyItemController, "this$0");
            Intrinsics.checkNotNullParameter(delayableExecutor, "delegate");
            this.this$0 = privacyItemController;
            this.delegate = delayableExecutor;
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            Intrinsics.checkNotNullParameter(runnable, "command");
            this.delegate.execute(runnable);
        }

        public final void updateListeningState() {
            Runnable runnable = this.listeningCanceller;
            if (runnable != null) {
                runnable.run();
            }
            this.listeningCanceller = this.delegate.executeDelayed(new PrivacyItemController$MyExecutor$updateListeningState$1(this.this$0), 0);
        }
    }

    private final boolean isIn(PrivacyItem privacyItem, List<PrivacyItem> list) {
        boolean z;
        if (!(list instanceof Collection) || !list.isEmpty()) {
            for (PrivacyItem privacyItem2 : list) {
                if (privacyItem2.getPrivacyType() == privacyItem.getPrivacyType() && Intrinsics.areEqual(privacyItem2.getApplication(), privacyItem.getApplication()) && privacyItem2.getTimeStampElapsed() == privacyItem.getTimeStampElapsed()) {
                    z = true;
                    continue;
                } else {
                    z = false;
                    continue;
                }
                if (z) {
                    return true;
                }
            }
        }
        return false;
    }
}
