package com.google.android.systemui.dreamliner;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.service.dreams.IDreamManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptStateProvider;
import com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.google.android.systemui.dreamliner.WirelessCharger;
import com.google.android.systemui.elmyra.gates.KeyguardVisibility;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class DockObserver extends BroadcastReceiver implements DockManager {
    @VisibleForTesting
    static final String ACTION_ALIGN_STATE_CHANGE = "com.google.android.systemui.dreamliner.ALIGNMENT_CHANGE";
    @VisibleForTesting
    static final String ACTION_CHALLENGE = "com.google.android.systemui.dreamliner.ACTION_CHALLENGE";
    @VisibleForTesting
    static final String ACTION_DOCK_UI_ACTIVE = "com.google.android.systemui.dreamliner.ACTION_DOCK_UI_ACTIVE";
    @VisibleForTesting
    static final String ACTION_DOCK_UI_IDLE = "com.google.android.systemui.dreamliner.ACTION_DOCK_UI_IDLE";
    @VisibleForTesting
    static final String ACTION_GET_DOCK_INFO = "com.google.android.systemui.dreamliner.ACTION_GET_DOCK_INFO";
    @VisibleForTesting
    static final String ACTION_KEY_EXCHANGE = "com.google.android.systemui.dreamliner.ACTION_KEY_EXCHANGE";
    @VisibleForTesting
    static final String ACTION_REBIND_DOCK_SERVICE = "com.google.android.systemui.dreamliner.ACTION_REBIND_DOCK_SERVICE";
    @VisibleForTesting
    static final String ACTION_START_DREAMLINER_CONTROL_SERVICE = "com.google.android.apps.dreamliner.START";
    @VisibleForTesting
    static final String COMPONENTNAME_DREAMLINER_CONTROL_SERVICE = "com.google.android.apps.dreamliner/.DreamlinerControlService";
    @VisibleForTesting
    static final String EXTRA_ALIGN_STATE = "align_state";
    @VisibleForTesting
    static final String EXTRA_CHALLENGE_DATA = "challenge_data";
    @VisibleForTesting
    static final String EXTRA_CHALLENGE_DOCK_ID = "challenge_dock_id";
    @VisibleForTesting
    static final String EXTRA_PUBLIC_KEY = "public_key";
    @VisibleForTesting
    static final String KEY_SHOWING = "showing";
    @VisibleForTesting
    static final int RESULT_NOT_FOUND = 1;
    @VisibleForTesting
    static final int RESULT_OK = 0;
    @VisibleForTesting
    static volatile ExecutorService mSingleThreadExecutor;
    private final ConfigurationController mConfigurationController;
    private final Context mContext;
    private final DockAlignmentController mDockAlignmentController;
    @VisibleForTesting
    DockGestureController mDockGestureController;
    private ImageView mDreamlinerGear;
    @VisibleForTesting
    DreamlinerServiceConn mDreamlinerServiceConn;
    private DockIndicationController mIndicationController;
    private final NotificationInterruptSuppressor mInterruptSuppressor;
    private final DelayableExecutor mMainExecutor;
    private Runnable mPhotoAction;
    private FrameLayout mPhotoPreview;
    private final StatusBarStateController mStatusBarStateController;
    private final CurrentUserTracker mUserTracker;
    private final WirelessCharger mWirelessCharger;
    private static final boolean DEBUG = Log.isLoggable("DLObserver", 3);
    private static boolean sIsDockingUiShowing = false;
    @VisibleForTesting
    final DreamlinerBroadcastReceiver mDreamlinerReceiver = new DreamlinerBroadcastReceiver();
    @VisibleForTesting
    int mDockState = 0;
    @VisibleForTesting
    int mLastAlignState = -1;
    private int mFanLevel = -1;
    private final List<DockManager.DockEventListener> mClients = new ArrayList();
    private final List<DockManager.AlignmentStateListener> mAlignmentStateListeners = new ArrayList();

    public DockObserver(final Context context, WirelessCharger wirelessCharger, BroadcastDispatcher broadcastDispatcher, StatusBarStateController statusBarStateController, NotificationInterruptStateProvider notificationInterruptStateProvider, ConfigurationController configurationController, DelayableExecutor delayableExecutor) {
        AnonymousClass2 r0 = new NotificationInterruptSuppressor() { // from class: com.google.android.systemui.dreamliner.DockObserver.2
            @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
            public String getName() {
                return "DLObserver";
            }

            @Override // com.android.systemui.statusbar.notification.interruption.NotificationInterruptSuppressor
            public boolean suppressInterruptions(NotificationEntry notificationEntry) {
                return DockObserver.isDockingUiShowing();
            }
        };
        this.mInterruptSuppressor = r0;
        this.mMainExecutor = delayableExecutor;
        this.mContext = context;
        this.mUserTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.dreamliner.DockObserver.1
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                DockObserver.this.stopDreamlinerService(context);
                DockObserver.this.updateCurrentDockingStatus(context);
            }
        };
        this.mWirelessCharger = wirelessCharger;
        if (wirelessCharger == null) {
            Log.i("DLObserver", "wireless charger is null, check dock component.");
        }
        this.mStatusBarStateController = statusBarStateController;
        context.registerReceiver(this, getDockIntentFilter());
        this.mDockAlignmentController = new DockAlignmentController(wirelessCharger, this);
        notificationInterruptStateProvider.addSuppressor(r0);
        this.mConfigurationController = configurationController;
        refreshFanLevel(null);
    }

    public void registerDockAlignInfo() {
        this.mDockAlignmentController.registerAlignInfoListener();
    }

    public void setDreamlinerGear(ImageView imageView) {
        this.mDreamlinerGear = imageView;
    }

    public void setPhotoPreview(FrameLayout frameLayout) {
        this.mPhotoPreview = frameLayout;
    }

    public void setIndicationController(DockIndicationController dockIndicationController) {
        this.mIndicationController = dockIndicationController;
        this.mConfigurationController.addCallback(dockIndicationController);
    }

    @Override // com.android.systemui.dock.DockManager
    public void addListener(DockManager.DockEventListener dockEventListener) {
        if (DEBUG) {
            Log.d("DLObserver", "add listener: " + dockEventListener);
        }
        if (!this.mClients.contains(dockEventListener)) {
            this.mClients.add(dockEventListener);
        }
        this.mMainExecutor.execute(new Runnable(dockEventListener) { // from class: com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda2
            public final /* synthetic */ DockManager.DockEventListener f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DockObserver.this.lambda$addListener$0(this.f$1);
            }
        });
    }

    @Override // com.android.systemui.dock.DockManager
    public void removeListener(DockManager.DockEventListener dockEventListener) {
        if (DEBUG) {
            Log.d("DLObserver", "remove listener: " + dockEventListener);
        }
        this.mClients.remove(dockEventListener);
    }

    @Override // com.android.systemui.dock.DockManager
    public boolean isDocked() {
        int i = this.mDockState;
        return i == 1 || i == 2;
    }

    @Override // com.android.systemui.dock.DockManager
    public boolean isHidden() {
        return this.mDockState == 2;
    }

    @Override // com.android.systemui.dock.DockManager
    public void addAlignmentStateListener(DockManager.AlignmentStateListener alignmentStateListener) {
        if (DEBUG) {
            Log.d("DLObserver", "add alignment listener: " + alignmentStateListener);
        }
        if (!this.mAlignmentStateListeners.contains(alignmentStateListener)) {
            this.mAlignmentStateListeners.add(alignmentStateListener);
        }
    }

    /* access modifiers changed from: private */
    public void tryTurnScreenOff(Context context) {
        PowerManager powerManager = (PowerManager) context.getSystemService(PowerManager.class);
        if (powerManager.isScreenOn()) {
            powerManager.goToSleep(SystemClock.uptimeMillis());
        }
    }

    /* access modifiers changed from: private */
    public void onDockStateChanged(int i) {
        if (this.mDockState != i) {
            if (DEBUG) {
                Log.d("DLObserver", "dock state changed from " + this.mDockState + " to " + i);
            }
            int i2 = this.mDockState;
            this.mDockState = i;
            for (int i3 = 0; i3 < this.mClients.size(); i3++) {
                lambda$addListener$0(this.mClients.get(i3));
            }
            DockIndicationController dockIndicationController = this.mIndicationController;
            if (dockIndicationController != null) {
                dockIndicationController.setDocking(isDocked());
            }
            if (i2 == 0 && i == 1) {
                notifyDreamlinerAlignStateChanged(this.mLastAlignState);
                lambda$onFanLevelChange$2();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onAlignStateChanged(int i) {
        if (DEBUG) {
            Log.d("DLObserver", "onAlignStateChanged alignState = " + i);
        }
        this.mLastAlignState = i;
        for (DockManager.AlignmentStateListener alignmentStateListener : this.mAlignmentStateListeners) {
            alignmentStateListener.onAlignmentStateChanged(i);
        }
        runPhotoAction();
        notifyDreamlinerAlignStateChanged(i);
    }

    private void notifyDreamlinerAlignStateChanged(int i) {
        if (isDocked()) {
            this.mContext.sendBroadcastAsUser(new Intent(ACTION_ALIGN_STATE_CHANGE).putExtra(EXTRA_ALIGN_STATE, i).addFlags(1073741824), UserHandle.CURRENT);
        }
    }

    /* access modifiers changed from: private */
    public void refreshFanLevel(Runnable runnable) {
        Log.d("DLObserver", "command=2");
        runOnBackgroundThread(new Runnable(runnable) { // from class: com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda3
            public final /* synthetic */ Runnable f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                DockObserver.this.lambda$refreshFanLevel$1(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$refreshFanLevel$1(Runnable runnable) {
        if (this.mWirelessCharger == null) {
            Log.i("DLObserver", "hint is UNKNOWN for null wireless charger HAL");
            this.mFanLevel = -1;
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            this.mFanLevel = this.mWirelessCharger.getFanLevel();
            if (DEBUG) {
                Log.d("DLObserver", "command=2, l=" + this.mFanLevel + ", spending time=" + (System.currentTimeMillis() - currentTimeMillis));
            }
        }
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: package-private */
    public void onFanLevelChange() {
        refreshFanLevel(new Runnable() { // from class: com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                DockObserver.this.lambda$onFanLevelChange$2();
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: notifyDreamlinerLatestFanLevel */
    public void lambda$onFanLevelChange$2() {
        Log.d("DLObserver", "notify l=" + this.mFanLevel + ", isDocked=" + isDocked());
        if (isDocked()) {
            this.mContext.sendBroadcastAsUser(new Intent("com.google.android.systemui.dreamliner.ACTION_UPDATE_FAN_LEVEL").putExtra("fan_level", this.mFanLevel).addFlags(1073741824), UserHandle.CURRENT);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: dispatchDockEvent */
    public void lambda$addListener$0(DockManager.DockEventListener dockEventListener) {
        if (DEBUG) {
            Log.d("DLObserver", "onDockEvent mDockState = " + this.mDockState);
        }
        dockEventListener.onEvent(this.mDockState);
    }

    private final Intent getBatteryStatus(Context context) {
        return context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    private boolean isChargingOrFull(Intent intent) {
        int intExtra = intent.getIntExtra("status", -1);
        return intExtra == 2 || intExtra == 5;
    }

    @VisibleForTesting
    final void updateCurrentDockingStatus(Context context) {
        notifyForceEnabledAmbientDisplay(false);
        if (isChargingOrFull(getBatteryStatus(context)) && this.mWirelessCharger != null) {
            runOnBackgroundThread(new IsDockPresent(context));
        }
    }

    private IntentFilter getDockIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        intentFilter.addAction(ACTION_REBIND_DOCK_SERVICE);
        intentFilter.setPriority(1000);
        return intentFilter;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            if (DEBUG) {
                Log.i("DLObserver", "onReceive(); " + intent.getAction());
            }
            String action = intent.getAction();
            action.hashCode();
            char c = 65535;
            switch (action.hashCode()) {
                case -1886648615:
                    if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                        c = 0;
                        break;
                    }
                    break;
                case 798292259:
                    if (action.equals("android.intent.action.BOOT_COMPLETED")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1019184907:
                    if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1318602046:
                    if (action.equals(ACTION_REBIND_DOCK_SERVICE)) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    stopDreamlinerService(context);
                    sIsDockingUiShowing = false;
                    return;
                case 1:
                case 3:
                    updateCurrentDockingStatus(context);
                    return;
                case 2:
                    if (this.mWirelessCharger != null) {
                        runOnBackgroundThread(new IsDockPresent(context));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    /* access modifiers changed from: private */
    public synchronized void startDreamlinerService(Context context, int i, int i2, int i3) {
        notifyForceEnabledAmbientDisplay(true);
        if (this.mDreamlinerServiceConn == null) {
            this.mDreamlinerReceiver.registerReceiver(context);
            ImageView imageView = this.mDreamlinerGear;
            DockGestureController dockGestureController = new DockGestureController(context, imageView, this.mPhotoPreview, (View) imageView.getParent(), this.mStatusBarStateController, (KeyguardStateController) Dependency.get(KeyguardStateController.class));
            this.mDockGestureController = dockGestureController;
            this.mConfigurationController.addCallback(dockGestureController);
            Intent intent = new Intent(ACTION_START_DREAMLINER_CONTROL_SERVICE);
            intent.setComponent(ComponentName.unflattenFromString(COMPONENTNAME_DREAMLINER_CONTROL_SERVICE));
            intent.putExtra("type", i);
            intent.putExtra("orientation", i2);
            intent.putExtra("id", i3);
            intent.putExtra("occluded", new KeyguardVisibility(context).isKeyguardOccluded());
            try {
                DreamlinerServiceConn dreamlinerServiceConn = new DreamlinerServiceConn(context);
                this.mDreamlinerServiceConn = dreamlinerServiceConn;
                if (context.bindServiceAsUser(intent, dreamlinerServiceConn, 1, new UserHandle(this.mUserTracker.getCurrentUserId()))) {
                    this.mUserTracker.startTracking();
                    return;
                }
            } catch (SecurityException e) {
                Log.e("DLObserver", e.getMessage(), e);
            }
            this.mDreamlinerServiceConn = null;
            Log.w("DLObserver", "Unable to bind Dreamliner service: " + intent);
        }
    }

    /* access modifiers changed from: private */
    public void stopDreamlinerService(Context context) {
        notifyForceEnabledAmbientDisplay(false);
        onDockStateChanged(0);
        try {
            if (this.mDreamlinerServiceConn != null) {
                if (assertNotNull(this.mDockGestureController, DockGestureController.class.getSimpleName())) {
                    this.mConfigurationController.removeCallback(this.mDockGestureController);
                    this.mDockGestureController.stopMonitoring();
                    this.mDockGestureController = null;
                }
                this.mUserTracker.stopTracking();
                this.mDreamlinerReceiver.unregisterReceiver(context);
                context.unbindService(this.mDreamlinerServiceConn);
                this.mDreamlinerServiceConn = null;
            }
        } catch (IllegalArgumentException e) {
            Log.e("DLObserver", e.getMessage(), e);
        }
    }

    /* access modifiers changed from: private */
    public boolean assertNotNull(Object obj, String str) {
        if (obj != null) {
            return true;
        }
        Log.w("DLObserver", str + " is null");
        return false;
    }

    private void notifyForceEnabledAmbientDisplay(boolean z) {
        IDreamManager dreamManagerInstance = getDreamManagerInstance();
        if (dreamManagerInstance != null) {
            try {
                dreamManagerInstance.forceAmbientDisplayEnabled(z);
            } catch (RemoteException unused) {
            }
        } else {
            Log.e("DLObserver", "DreamManager not found");
        }
    }

    private IDreamManager getDreamManagerInstance() {
        return IDreamManager.Stub.asInterface(ServiceManager.checkService("dreams"));
    }

    /* access modifiers changed from: private */
    public void sendDockIdleIntent(Context context) {
        if (DEBUG) {
            Log.d("DLObserver", "sendDockIdleIntent()");
        }
        context.sendBroadcast(new Intent("android.intent.action.DOCK_IDLE").addFlags(1073741824));
    }

    /* access modifiers changed from: private */
    public void sendDockActiveIntent(Context context) {
        if (DEBUG) {
            Log.d("DLObserver", "sendDockActiveIntent()");
        }
        context.sendBroadcast(new Intent("android.intent.action.DOCK_ACTIVE").addFlags(1073741824));
    }

    /* access modifiers changed from: private */
    public void triggerKeyExchangeWithDock(Intent intent) {
        ResultReceiver resultReceiver;
        if (DEBUG) {
            Log.d("DLObserver", "triggerKeyExchangeWithDock");
        }
        if (intent != null && (resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER")) != null) {
            byte[] byteArrayExtra = intent.getByteArrayExtra(EXTRA_PUBLIC_KEY);
            if (byteArrayExtra == null || byteArrayExtra.length <= 0) {
                resultReceiver.send(1, null);
            } else {
                runOnBackgroundThread(new KeyExchangeWithDock(resultReceiver, byteArrayExtra));
            }
        }
    }

    /* access modifiers changed from: private */
    public void triggerChallengeWithDock(Intent intent) {
        ResultReceiver resultReceiver;
        if (DEBUG) {
            Log.d("DLObserver", "triggerChallengeWithDock");
        }
        if (intent != null && (resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER")) != null) {
            byte byteExtra = intent.getByteExtra(EXTRA_CHALLENGE_DOCK_ID, (byte) -1);
            byte[] byteArrayExtra = intent.getByteArrayExtra(EXTRA_CHALLENGE_DATA);
            if (byteArrayExtra == null || byteArrayExtra.length <= 0 || byteExtra < 0) {
                resultReceiver.send(1, null);
            } else {
                runOnBackgroundThread(new ChallengeWithDock(resultReceiver, byteExtra, byteArrayExtra));
            }
        }
    }

    /* access modifiers changed from: private */
    public void configPhotoAction(Intent intent) {
        if (DEBUG) {
            Log.d("DLObserver", "handlePhotoAction");
        }
        if (intent != null) {
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            boolean booleanExtra = intent.getBooleanExtra("enabled", false);
            DockGestureController dockGestureController = this.mDockGestureController;
            if (dockGestureController != null) {
                dockGestureController.setPhotoEnabled(booleanExtra);
            }
            if (resultReceiver != null && this.mIndicationController != null) {
                this.mPhotoAction = new Runnable(resultReceiver) { // from class: com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda1
                    public final /* synthetic */ ResultReceiver f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        DockObserver.this.lambda$configPhotoAction$3(this.f$1);
                    }
                };
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$configPhotoAction$3(ResultReceiver resultReceiver) {
        this.mIndicationController.showPromo(resultReceiver);
    }

    /* access modifiers changed from: private */
    public void runPhotoAction() {
        if (this.mLastAlignState == 0 && this.mPhotoAction != null && !this.mIndicationController.isPromoShowing()) {
            this.mMainExecutor.executeDelayed(this.mPhotoAction, Duration.ofSeconds(3).toMillis());
        }
    }

    /* access modifiers changed from: private */
    public void handlePhotoFailure() {
        Log.w("DLObserver", "Fail to launch photo");
        DockGestureController dockGestureController = this.mDockGestureController;
        if (dockGestureController != null) {
            dockGestureController.handlePhotoFailure();
        }
    }

    private byte[] convertArrayListToPrimitiveArray(ArrayList<Byte> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        int size = arrayList.size();
        byte[] bArr = new byte[size];
        for (int i = 0; i < size; i++) {
            bArr[i] = arrayList.get(i).byteValue();
        }
        return bArr;
    }

    /* access modifiers changed from: private */
    public Bundle createKeyExchangeResponseBundle(byte b, ArrayList<Byte> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        byte[] convertArrayListToPrimitiveArray = convertArrayListToPrimitiveArray(arrayList);
        Bundle bundle = new Bundle();
        bundle.putByte("dock_id", b);
        bundle.putByteArray("dock_public_key", convertArrayListToPrimitiveArray);
        return bundle;
    }

    /* access modifiers changed from: private */
    public Bundle createChallengeResponseBundle(ArrayList<Byte> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        byte[] convertArrayListToPrimitiveArray = convertArrayListToPrimitiveArray(arrayList);
        Bundle bundle = new Bundle();
        bundle.putByteArray("challenge_response", convertArrayListToPrimitiveArray);
        return bundle;
    }

    /* access modifiers changed from: private */
    public Bundle createWpcAuthDigestsResponseBundle(byte b, byte b2, ArrayList<byte[]> arrayList) {
        Bundle bundle = new Bundle();
        bundle.putByte("slot_populated_mask", b);
        bundle.putByte("slot_returned_mask", b2);
        ArrayList<? extends Parcelable> arrayList2 = new ArrayList<>();
        if (arrayList != null) {
            arrayList.forEach(new Consumer(arrayList2) { // from class: com.google.android.systemui.dreamliner.DockObserver$$ExternalSyntheticLambda4
                public final /* synthetic */ ArrayList f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    DockObserver.lambda$createWpcAuthDigestsResponseBundle$4(this.f$0, (byte[]) obj);
                }
            });
        }
        bundle.putParcelableArrayList("wpc_digests", arrayList2);
        return bundle;
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$createWpcAuthDigestsResponseBundle$4(ArrayList arrayList, byte[] bArr) {
        Bundle bundle = new Bundle();
        bundle.putByteArray("wpc_digest", bArr);
        arrayList.add(bundle);
    }

    /* access modifiers changed from: private */
    public Bundle createWpcAuthCertificateResponseBundle(ArrayList<Byte> arrayList) {
        if (arrayList == null || arrayList.isEmpty()) {
            return null;
        }
        byte[] convertArrayListToPrimitiveArray = convertArrayListToPrimitiveArray(arrayList);
        Bundle bundle = new Bundle();
        bundle.putByteArray("wpc_cert", convertArrayListToPrimitiveArray);
        return bundle;
    }

    /* access modifiers changed from: private */
    public Bundle createWpcAuthChallengeResponseBundle(byte b, byte b2, byte b3, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2) {
        Bundle bundle = new Bundle();
        bundle.putByte("max_protocol_ver", b);
        bundle.putByte("slot_populated_mask", b2);
        bundle.putByte("cert_lsb", b3);
        bundle.putByteArray("signature_r", convertArrayListToPrimitiveArray(arrayList));
        bundle.putByteArray("signature_s", convertArrayListToPrimitiveArray(arrayList2));
        return bundle;
    }

    /* access modifiers changed from: private */
    public Bundle createGetFeatureResponse(long j) {
        Bundle bundle = new Bundle();
        bundle.putLong("charger_feature", j);
        return bundle;
    }

    /* access modifiers changed from: private */
    public static void runOnBackgroundThread(Runnable runnable) {
        if (mSingleThreadExecutor == null) {
            mSingleThreadExecutor = Executors.newSingleThreadExecutor();
        }
        mSingleThreadExecutor.execute(runnable);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class IsDockPresent implements Runnable {
        final Context context;

        public IsDockPresent(Context context) {
            this.context = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.asyncIsDockPresent(new IsDockPresentCallback(this.context));
            }
        }
    }

    /* loaded from: classes2.dex */
    private class GetDockInfo implements Runnable {
        final Context context;
        final ResultReceiver resultReceiver;

        public GetDockInfo(ResultReceiver resultReceiver, Context context) {
            this.resultReceiver = resultReceiver;
            this.context = context;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.getInformation(new GetInformationCallback(this.resultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class KeyExchangeWithDock implements Runnable {
        final byte[] publicKey;
        final ResultReceiver resultReceiver;

        public KeyExchangeWithDock(ResultReceiver resultReceiver, byte[] bArr) {
            this.publicKey = bArr;
            this.resultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.keyExchange(this.publicKey, new KeyExchangeCallback(this.resultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetFanSimpleInformation implements Runnable {
        final byte mFanId;
        final ResultReceiver mResultReceiver;

        GetFanSimpleInformation(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
                byte b = this.mFanId;
                wirelessCharger.getFanSimpleInformation(b, new GetFanSimpleInformationCallback(b, this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetFanInformation implements Runnable {
        final byte mFanId;
        final ResultReceiver mResultReceiver;

        public GetFanInformation(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                WirelessCharger wirelessCharger = DockObserver.this.mWirelessCharger;
                byte b = this.mFanId;
                wirelessCharger.getFanInformation(b, new GetFanInformationCallback(b, this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SetFan implements Runnable {
        final byte mFanId;
        final byte mFanMode;
        final int mFanRpm;

        public SetFan(byte b, byte b2, int i) {
            this.mFanId = b;
            this.mFanMode = b2;
            this.mFanRpm = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.setFan(this.mFanId, this.mFanMode, this.mFanRpm, new SetFanCallback());
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ChallengeWithDock implements Runnable {
        final byte[] challengeData;
        final byte dockId;
        final ResultReceiver resultReceiver;

        public ChallengeWithDock(ResultReceiver resultReceiver, byte b, byte[] bArr) {
            this.dockId = b;
            this.challengeData = bArr;
            this.resultReceiver = resultReceiver;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.challenge(this.dockId, this.challengeData, new ChallengeCallback(this.resultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetWpcAuthDigests implements Runnable {
        final ResultReceiver mResultReceiver;
        final byte mSlotMask;

        GetWpcAuthDigests(ResultReceiver resultReceiver, byte b) {
            this.mResultReceiver = resultReceiver;
            this.mSlotMask = b;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.getWpcAuthDigests(this.mSlotMask, new GetWpcAuthDigestsCallback(this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetWpcAuthCertificate implements Runnable {
        final short mLength;
        final short mOffset;
        final ResultReceiver mResultReceiver;
        final byte mSlotNum;

        GetWpcAuthCertificate(ResultReceiver resultReceiver, byte b, short s, short s2) {
            this.mResultReceiver = resultReceiver;
            this.mSlotNum = b;
            this.mOffset = s;
            this.mLength = s2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.getWpcAuthCertificate(this.mSlotNum, this.mOffset, this.mLength, new GetWpcAuthCertificateCallback(this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetWpcAuthChallengeResponse implements Runnable {
        final ResultReceiver mResultReceiver;
        final byte mSlotNum;
        final byte[] mWpcNonce;

        GetWpcAuthChallengeResponse(ResultReceiver resultReceiver, byte b, byte[] bArr) {
            this.mResultReceiver = resultReceiver;
            this.mSlotNum = b;
            this.mWpcNonce = bArr;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.getWpcAuthChallengeResponse(this.mSlotNum, this.mWpcNonce, new GetWpcAuthChallengeResponseCallback(this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SetFeatures implements Runnable {
        final long mChargerId;
        final long mFeature;
        final ResultReceiver mResultReceiver;

        SetFeatures(ResultReceiver resultReceiver, long j, long j2) {
            this.mResultReceiver = resultReceiver;
            this.mChargerId = j;
            this.mFeature = j2;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.setFeatures(this.mChargerId, this.mFeature, new DockObserver$SetFeatures$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0(int i) {
            this.mResultReceiver.send(i, null);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class GetFeatures implements Runnable {
        final long mChargerId;
        final ResultReceiver mResultReceiver;

        GetFeatures(ResultReceiver resultReceiver, long j) {
            this.mResultReceiver = resultReceiver;
            this.mChargerId = j;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DockObserver.this.mWirelessCharger != null) {
                DockObserver.this.mWirelessCharger.getFeatures(this.mChargerId, new GetFeaturesCallback(this.mResultReceiver));
            }
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes2.dex */
    public final class DreamlinerServiceConn implements ServiceConnection {
        final Context mContext;

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        }

        public DreamlinerServiceConn(Context context) {
            this.mContext = context;
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            DockObserver.this.sendDockActiveIntent(this.mContext);
        }

        @Override // android.content.ServiceConnection
        public void onBindingDied(ComponentName componentName) {
            DockObserver.this.stopDreamlinerService(this.mContext);
            boolean unused = DockObserver.sIsDockingUiShowing = false;
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class IsDockPresentCallback implements WirelessCharger.IsDockPresentCallback {
        private final Context mContext;

        IsDockPresentCallback(Context context) {
            this.mContext = context;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.IsDockPresentCallback
        public void onCallback(boolean z, byte b, byte b2, boolean z2, int i) {
            if (DockObserver.DEBUG) {
                Log.i("DLObserver", "isDockPresent() docked: " + z + ", id: " + i);
            }
            if (z) {
                DockObserver.this.startDreamlinerService(this.mContext, b, b2, i);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class GetInformationCallback implements WirelessCharger.GetInformationCallback {
        private final ResultReceiver mResultReceiver;

        GetInformationCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetInformationCallback
        public void onCallback(int i, DockInfo dockInfo) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "getInformation() Result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "getInformation() DockInfo: " + dockInfo.toString());
                }
                this.mResultReceiver.send(0, dockInfo.toBundle());
            } else if (i != 1) {
                this.mResultReceiver.send(1, null);
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    static final class GetFanSimpleInformationCallback implements WirelessCharger.GetFanSimpleInformationCallback {
        private final byte mFanId;
        private final ResultReceiver mResultReceiver;

        GetFanSimpleInformationCallback(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetFanSimpleInformationCallback
        public void onCallback(int i, Bundle bundle) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "Callback of command=3, result=" + i + ", i=" + ((int) this.mFanId));
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "Callback of command=3, i=" + bundle.getByte("fan_id", (byte) -1) + ", m=" + bundle.getByte("fan_mode", (byte) -1) + ", cr=" + bundle.getInt("fan_current_rpm", -1));
                }
                this.mResultReceiver.send(0, bundle);
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    static final class GetFanInformationCallback implements WirelessCharger.GetFanInformationCallback {
        private final byte mFanId;
        private final ResultReceiver mResultReceiver;

        GetFanInformationCallback(byte b, ResultReceiver resultReceiver) {
            this.mFanId = b;
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetFanInformationCallback
        public void onCallback(int i, Bundle bundle) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "Callback of command=0, result=" + i + ", i=" + ((int) this.mFanId));
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "Callback of command=0, i=" + bundle.getByte("fan_id", (byte) -1) + ", m=" + bundle.getByte("fan_mode", (byte) -1) + ", cr=" + bundle.getInt("fan_current_rpm", -1) + ", mir=" + bundle.getInt("fan_min_rpm", -1) + ", mxr=" + bundle.getInt("fan_max_rpm", -1) + ", t=" + bundle.getByte("fan_type", (byte) -1) + ", c=" + bundle.getByte("fan_count", (byte) -1));
                }
                this.mResultReceiver.send(0, bundle);
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    static final class SetFanCallback implements WirelessCharger.SetFanCallback {
        SetFanCallback() {
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.SetFanCallback
        public void onCallback(int i, Bundle bundle) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "Callback of command=1, i=" + bundle.getByte("fan_id", (byte) -1) + ", m=" + bundle.getByte("fan_mode", (byte) -1) + ", cr=" + bundle.getInt("fan_current_rpm", -1));
            }
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class KeyExchangeCallback implements WirelessCharger.KeyExchangeCallback {
        private final ResultReceiver mResultReceiver;

        KeyExchangeCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.KeyExchangeCallback
        public void onCallback(int i, byte b, ArrayList<Byte> arrayList) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "keyExchange() Result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "keyExchange() key: " + arrayList);
                }
                this.mResultReceiver.send(0, DockObserver.this.createKeyExchangeResponseBundle(b, arrayList));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class ChallengeCallback implements WirelessCharger.ChallengeCallback {
        private final ResultReceiver mResultReceiver;

        ChallengeCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.ChallengeCallback
        public void onCallback(int i, ArrayList<Byte> arrayList) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "challenge() Result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "challenge() response: " + arrayList);
                }
                this.mResultReceiver.send(0, DockObserver.this.createChallengeResponseBundle(arrayList));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class GetWpcAuthDigestsCallback implements WirelessCharger.GetWpcAuthDigestsCallback {
        private final ResultReceiver mResultReceiver;

        GetWpcAuthDigestsCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetWpcAuthDigestsCallback
        public void onCallback(int i, byte b, byte b2, ArrayList<byte[]> arrayList) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "GWAD() result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "GWAD() response: pm=" + ((int) b) + ", rm=" + ((int) b2) + ", d=" + arrayList);
                }
                this.mResultReceiver.send(0, DockObserver.this.createWpcAuthDigestsResponseBundle(b, b2, arrayList));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class GetWpcAuthCertificateCallback implements WirelessCharger.GetWpcAuthCertificateCallback {
        private final ResultReceiver mResultReceiver;

        GetWpcAuthCertificateCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetWpcAuthCertificateCallback
        public void onCallback(int i, ArrayList<Byte> arrayList) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "GWAC() result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "GWAC() response: c=" + arrayList);
                }
                this.mResultReceiver.send(0, DockObserver.this.createWpcAuthCertificateResponseBundle(arrayList));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class GetWpcAuthChallengeResponseCallback implements WirelessCharger.GetWpcAuthChallengeResponseCallback {
        private final ResultReceiver mResultReceiver;

        GetWpcAuthChallengeResponseCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetWpcAuthChallengeResponseCallback
        public void onCallback(int i, byte b, byte b2, byte b3, ArrayList<Byte> arrayList, ArrayList<Byte> arrayList2) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "GWACR() result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "GWACR() response: mpv=" + ((int) b) + ", pm=" + ((int) b2) + ", chl=" + ((int) b3) + ", rv=" + arrayList + ", sv=" + arrayList2);
                }
                this.mResultReceiver.send(0, DockObserver.this.createWpcAuthChallengeResponseBundle(b, b2, b3, arrayList, arrayList2));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    @VisibleForTesting
    /* loaded from: classes2.dex */
    final class GetFeaturesCallback implements WirelessCharger.GetFeaturesCallback {
        private final ResultReceiver mResultReceiver;

        GetFeaturesCallback(ResultReceiver resultReceiver) {
            this.mResultReceiver = resultReceiver;
        }

        @Override // com.google.android.systemui.dreamliner.WirelessCharger.GetFeaturesCallback
        public void onCallback(int i, long j) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "GF() result: " + i);
            }
            if (i == 0) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "GF() response: f=" + j);
                }
                this.mResultReceiver.send(0, DockObserver.this.createGetFeatureResponse(j));
                return;
            }
            this.mResultReceiver.send(1, null);
        }
    }

    public static boolean isDockingUiShowing() {
        return sIsDockingUiShowing;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* loaded from: classes2.dex */
    public class DreamlinerBroadcastReceiver extends BroadcastReceiver {
        private boolean mListening;

        DreamlinerBroadcastReceiver() {
        }

        /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                if (DockObserver.DEBUG) {
                    Log.d("DLObserver", "Dock Receiver.onReceive(): " + intent.getAction());
                }
                String action = intent.getAction();
                action.hashCode();
                char c = 65535;
                switch (action.hashCode()) {
                    case -2133451883:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_LEVEL")) {
                            c = 0;
                            break;
                        }
                        break;
                    case -1863595884:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_SET_FEATURES")) {
                            c = 1;
                            break;
                        }
                        break;
                    case -1627881412:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_SET_FAN")) {
                            c = 2;
                            break;
                        }
                        break;
                    case -1616532553:
                        if (action.equals(DockObserver.ACTION_GET_DOCK_INFO)) {
                            c = 3;
                            break;
                        }
                        break;
                    case -1598391011:
                        if (action.equals(DockObserver.ACTION_KEY_EXCHANGE)) {
                            c = 4;
                            break;
                        }
                        break;
                    case -1584152500:
                        if (action.equals("com.google.android.systemui.dreamliner.photo_error")) {
                            c = 5;
                            break;
                        }
                        break;
                    case -1579804275:
                        if (action.equals(DockObserver.ACTION_DOCK_UI_IDLE)) {
                            c = 6;
                            break;
                        }
                        break;
                    case -1458969207:
                        if (action.equals(DockObserver.ACTION_CHALLENGE)) {
                            c = 7;
                            break;
                        }
                        break;
                    case -1185055092:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_SIMPLE_INFO")) {
                            c = '\b';
                            break;
                        }
                        break;
                    case -686255721:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_DIGESTS")) {
                            c = '\t';
                            break;
                        }
                        break;
                    case -545730616:
                        if (action.equals("com.google.android.systemui.dreamliner.paired")) {
                            c = '\n';
                            break;
                        }
                        break;
                    case -484477188:
                        if (action.equals("com.google.android.systemui.dreamliner.resume")) {
                            c = 11;
                            break;
                        }
                        break;
                    case -390730981:
                        if (action.equals("com.google.android.systemui.dreamliner.undock")) {
                            c = '\f';
                            break;
                        }
                        break;
                    case 664552276:
                        if (action.equals("com.google.android.systemui.dreamliner.dream")) {
                            c = '\r';
                            break;
                        }
                        break;
                    case 675144007:
                        if (action.equals("com.google.android.systemui.dreamliner.pause")) {
                            c = 14;
                            break;
                        }
                        break;
                    case 675346819:
                        if (action.equals("com.google.android.systemui.dreamliner.photo")) {
                            c = 15;
                            break;
                        }
                        break;
                    case 717413661:
                        if (action.equals("com.google.android.systemui.dreamliner.assistant_poodle")) {
                            c = 16;
                            break;
                        }
                        break;
                    case 882378784:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FEATURES")) {
                            c = 17;
                            break;
                        }
                        break;
                    case 1954561023:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CERTIFICATE")) {
                            c = 18;
                            break;
                        }
                        break;
                    case 1996802687:
                        if (action.equals(DockObserver.ACTION_DOCK_UI_ACTIVE)) {
                            c = 19;
                            break;
                        }
                        break;
                    case 2009307741:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_FAN_INFO")) {
                            c = 20;
                            break;
                        }
                        break;
                    case 2121889077:
                        if (action.equals("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CHALLENGE_RESPONSE")) {
                            c = 21;
                            break;
                        }
                        break;
                }
                switch (c) {
                    case 0:
                        DockObserver.this.refreshFanLevel(new DockObserver$DreamlinerBroadcastReceiver$$ExternalSyntheticLambda0(this));
                        return;
                    case 1:
                        setFeatures(intent);
                        return;
                    case 2:
                        setFan(intent);
                        return;
                    case 3:
                        ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
                        if (resultReceiver != null) {
                            DockObserver.runOnBackgroundThread(new GetDockInfo(resultReceiver, context));
                            return;
                        }
                        return;
                    case 4:
                        DockObserver.this.triggerKeyExchangeWithDock(intent);
                        return;
                    case 5:
                        DockObserver.this.handlePhotoFailure();
                        return;
                    case 6:
                        DockObserver.this.sendDockIdleIntent(context);
                        boolean unused = DockObserver.sIsDockingUiShowing = true;
                        return;
                    case 7:
                        DockObserver.this.triggerChallengeWithDock(intent);
                        return;
                    case '\b':
                        getFanSimpleInformation(intent);
                        return;
                    case '\t':
                        getWpcAuthDigests(intent);
                        return;
                    case '\n':
                        DockObserver dockObserver = DockObserver.this;
                        if (dockObserver.assertNotNull(dockObserver.mDockGestureController, DockGestureController.class.getSimpleName())) {
                            DockObserver.this.mDockGestureController.setTapAction((PendingIntent) intent.getParcelableExtra("single_tap_action"));
                            break;
                        }
                        break;
                    case 11:
                        break;
                    case '\f':
                        DockObserver.this.onDockStateChanged(0);
                        DockObserver dockObserver2 = DockObserver.this;
                        if (dockObserver2.assertNotNull(dockObserver2.mDockGestureController, DockGestureController.class.getSimpleName())) {
                            DockObserver.this.mDockGestureController.stopMonitoring();
                            return;
                        }
                        return;
                    case '\r':
                        DockObserver.this.tryTurnScreenOff(context);
                        return;
                    case 14:
                        DockObserver.this.onDockStateChanged(2);
                        DockObserver dockObserver3 = DockObserver.this;
                        if (dockObserver3.assertNotNull(dockObserver3.mDockGestureController, DockGestureController.class.getSimpleName())) {
                            DockObserver.this.mDockGestureController.stopMonitoring();
                            return;
                        }
                        return;
                    case 15:
                        DockObserver.this.configPhotoAction(intent);
                        DockObserver.this.runPhotoAction();
                        return;
                    case 16:
                        if (DockObserver.this.mIndicationController != null) {
                            DockObserver.this.mIndicationController.setShowing(intent.getBooleanExtra(DockObserver.KEY_SHOWING, false));
                            return;
                        }
                        return;
                    case 17:
                        getFeatures(intent);
                        return;
                    case 18:
                        getWpcAuthCertificate(intent);
                        return;
                    case 19:
                        DockObserver.this.sendDockActiveIntent(context);
                        boolean unused2 = DockObserver.sIsDockingUiShowing = false;
                        return;
                    case 20:
                        getFanInformation(intent);
                        return;
                    case 21:
                        getWpcAuthChallengeResponse(intent);
                        return;
                    default:
                        return;
                }
                DockObserver.this.onDockStateChanged(1);
                DockObserver dockObserver4 = DockObserver.this;
                if (dockObserver4.assertNotNull(dockObserver4.mDockGestureController, DockGestureController.class.getSimpleName())) {
                    DockObserver.this.mDockGestureController.startMonitoring();
                }
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onReceive$0() {
            DockObserver.this.lambda$onFanLevelChange$2();
        }

        private void getFanSimpleInformation(Intent intent) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "command=3, i=" + ((int) intent.getByteExtra("fan_id", (byte) -1)));
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver != null) {
                DockObserver.runOnBackgroundThread(new GetFanSimpleInformation(intent.getByteExtra("fan_id", (byte) 0), resultReceiver));
            }
        }

        private void getFanInformation(Intent intent) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "command=0, i=" + ((int) intent.getByteExtra("fan_id", (byte) -1)));
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver != null) {
                DockObserver.runOnBackgroundThread(new GetFanInformation(intent.getByteExtra("fan_id", (byte) 0), resultReceiver));
            }
        }

        private void setFan(Intent intent) {
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "command=1, i=" + ((int) intent.getByteExtra("fan_id", (byte) -1)) + ", m=" + ((int) intent.getByteExtra("fan_mode", (byte) -1)) + ", r=" + intent.getIntExtra("fan_rpm", -1));
            }
            byte byteExtra = intent.getByteExtra("fan_id", (byte) 0);
            byte byteExtra2 = intent.getByteExtra("fan_mode", (byte) 0);
            int intExtra = intent.getIntExtra("fan_rpm", -1);
            if (byteExtra2 == 1 && intExtra == -1) {
                Log.e("DLObserver", "Failed to get r.");
            } else {
                DockObserver.runOnBackgroundThread(new SetFan(byteExtra, byteExtra2, intExtra));
            }
        }

        private void getWpcAuthDigests(Intent intent) {
            byte byteExtra = intent.getByteExtra("slot_mask", (byte) -1);
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "gWAD, mask=" + ((int) byteExtra));
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver == null) {
                return;
            }
            if (byteExtra == -1) {
                resultReceiver.send(1, null);
            } else {
                DockObserver.runOnBackgroundThread(new GetWpcAuthDigests(resultReceiver, byteExtra));
            }
        }

        private void getWpcAuthCertificate(Intent intent) {
            byte byteExtra = intent.getByteExtra("slot_number", (byte) -1);
            short shortExtra = intent.getShortExtra("cert_offset", -1);
            short shortExtra2 = intent.getShortExtra("cert_length", -1);
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "gWAC, num=" + ((int) byteExtra) + ", offset=" + ((int) shortExtra) + ", length=" + ((int) shortExtra2));
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver == null) {
                return;
            }
            if (byteExtra == -1 || shortExtra == -1 || shortExtra2 == -1) {
                resultReceiver.send(1, null);
            } else {
                DockObserver.runOnBackgroundThread(new GetWpcAuthCertificate(resultReceiver, byteExtra, shortExtra, shortExtra2));
            }
        }

        private void getWpcAuthChallengeResponse(Intent intent) {
            byte byteExtra = intent.getByteExtra("slot_number", (byte) -1);
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "gWACR, num=" + ((int) byteExtra));
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver != null) {
                byte[] byteArrayExtra = intent.getByteArrayExtra("wpc_nonce");
                if (byteArrayExtra == null || byteArrayExtra.length <= 0) {
                    resultReceiver.send(1, null);
                } else {
                    DockObserver.runOnBackgroundThread(new GetWpcAuthChallengeResponse(resultReceiver, byteExtra, byteArrayExtra));
                }
            }
        }

        private void getFeatures(Intent intent) {
            long longExtra = intent.getLongExtra("charger_id", -1);
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "gF, id=" + longExtra);
            }
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (resultReceiver == null) {
                return;
            }
            if (longExtra == -1) {
                resultReceiver.send(1, null);
            } else {
                DockObserver.runOnBackgroundThread(new GetFeatures(resultReceiver, longExtra));
            }
        }

        private void setFeatures(Intent intent) {
            long longExtra = intent.getLongExtra("charger_id", -1);
            long longExtra2 = intent.getLongExtra("charger_feature", -1);
            ResultReceiver resultReceiver = (ResultReceiver) intent.getParcelableExtra("android.intent.extra.RESULT_RECEIVER");
            if (DockObserver.DEBUG) {
                Log.d("DLObserver", "sF, id=" + longExtra + ", feature=" + longExtra2);
            }
            if (resultReceiver == null) {
                return;
            }
            if (longExtra == -1 || longExtra2 == -1) {
                resultReceiver.send(1, null);
            } else {
                DockObserver.runOnBackgroundThread(new SetFeatures(resultReceiver, longExtra, longExtra2));
            }
        }

        private IntentFilter getIntentFilter() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(DockObserver.ACTION_GET_DOCK_INFO);
            intentFilter.addAction(DockObserver.ACTION_DOCK_UI_IDLE);
            intentFilter.addAction(DockObserver.ACTION_DOCK_UI_ACTIVE);
            intentFilter.addAction(DockObserver.ACTION_KEY_EXCHANGE);
            intentFilter.addAction(DockObserver.ACTION_CHALLENGE);
            intentFilter.addAction("com.google.android.systemui.dreamliner.dream");
            intentFilter.addAction("com.google.android.systemui.dreamliner.paired");
            intentFilter.addAction("com.google.android.systemui.dreamliner.pause");
            intentFilter.addAction("com.google.android.systemui.dreamliner.resume");
            intentFilter.addAction("com.google.android.systemui.dreamliner.undock");
            intentFilter.addAction("com.google.android.systemui.dreamliner.assistant_poodle");
            intentFilter.addAction("com.google.android.systemui.dreamliner.photo");
            intentFilter.addAction("com.google.android.systemui.dreamliner.photo_error");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_INFO");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_SIMPLE_INFO");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_SET_FAN");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FAN_LEVEL");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_DIGESTS");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CERTIFICATE");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_WPC_CHALLENGE_RESPONSE");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_GET_FEATURES");
            intentFilter.addAction("com.google.android.systemui.dreamliner.ACTION_SET_FEATURES");
            return intentFilter;
        }

        public void registerReceiver(Context context) {
            if (!this.mListening) {
                context.registerReceiverAsUser(this, UserHandle.ALL, getIntentFilter(), "com.google.android.systemui.permission.WIRELESS_CHARGER_STATUS", null);
                this.mListening = true;
            }
        }

        public void unregisterReceiver(Context context) {
            if (this.mListening) {
                context.unregisterReceiver(this);
                this.mListening = false;
            }
        }
    }
}
