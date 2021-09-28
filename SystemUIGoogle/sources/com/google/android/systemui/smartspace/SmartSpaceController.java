package com.google.android.systemui.smartspace;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.smartspace.nano.SmartspaceProto$CardWrapper;
import com.android.systemui.util.Assert;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class SmartSpaceController implements Dumpable {
    static final boolean DEBUG = Log.isLoggable("SmartSpaceController", 3);
    private final AlarmManager mAlarmManager;
    private boolean mAlarmRegistered;
    private final Context mAppContext;
    private final Handler mBackgroundHandler;
    private final Context mContext;
    private boolean mHidePrivateData;
    private boolean mHideWorkData;
    private final KeyguardUpdateMonitorCallback mKeyguardMonitorCallback;
    private boolean mSmartSpaceEnabledBroadcastSent;
    private final ProtoStore mStore;
    private final Handler mUiHandler;
    private final ArrayList<SmartSpaceUpdateListener> mListeners = new ArrayList<>();
    private final AlarmManager.OnAlarmListener mExpireAlarmAction = new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda0
        @Override // android.app.AlarmManager.OnAlarmListener
        public final void onAlarm() {
            SmartSpaceController.this.lambda$new$0();
        }
    };
    private int mCurrentUserId = UserHandle.myUserId();
    private final SmartSpaceData mData = new SmartSpaceData();

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        onExpire(false);
    }

    public SmartSpaceController(Context context, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler, AlarmManager alarmManager, DumpManager dumpManager) {
        AnonymousClass1 r0 = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.smartspace.SmartSpaceController.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onTimeChanged() {
                if (SmartSpaceController.this.mData != null && SmartSpaceController.this.mData.hasCurrent() && SmartSpaceController.this.mData.getExpirationRemainingMillis() > 0) {
                    SmartSpaceController.this.update();
                }
            }
        };
        this.mKeyguardMonitorCallback = r0;
        this.mContext = context;
        Handler handler2 = new Handler(Looper.getMainLooper());
        this.mUiHandler = handler2;
        this.mStore = new ProtoStore(context);
        new HandlerThread("smartspace-background").start();
        this.mBackgroundHandler = handler;
        this.mAppContext = context;
        this.mAlarmManager = alarmManager;
        if (!isSmartSpaceDisabledByExperiments()) {
            keyguardUpdateMonitor.registerCallback(r0);
            reloadData();
            onGsaChanged();
            context.registerReceiver(new BroadcastReceiver() { // from class: com.google.android.systemui.smartspace.SmartSpaceController.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    SmartSpaceController.this.onGsaChanged();
                }
            }, GSAIntents.getGsaPackageFilter("android.intent.action.PACKAGE_ADDED", "android.intent.action.PACKAGE_CHANGED", "android.intent.action.PACKAGE_REMOVED", "android.intent.action.PACKAGE_DATA_CLEARED"));
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.USER_SWITCHED");
            intentFilter.addAction("android.intent.action.USER_UNLOCKED");
            context.registerReceiver(new UserSwitchReceiver(), intentFilter);
            context.registerReceiver(new SmartSpaceBroadcastReceiver(this), new IntentFilter("com.google.android.apps.nexuslauncher.UPDATE_SMARTSPACE"), "android.permission.CAPTURE_AUDIO_HOTWORD", handler2);
            dumpManager.registerDumpable(SmartSpaceController.class.getName(), this);
        }
    }

    private SmartSpaceCard loadSmartSpaceData(boolean z) {
        SmartspaceProto$CardWrapper smartspaceProto$CardWrapper = new SmartspaceProto$CardWrapper();
        ProtoStore protoStore = this.mStore;
        if (protoStore.load("smartspace_" + this.mCurrentUserId + "_" + z, smartspaceProto$CardWrapper)) {
            return SmartSpaceCard.fromWrapper(this.mContext, smartspaceProto$CardWrapper, !z);
        }
        return null;
    }

    public void onNewCard(NewCardInfo newCardInfo) {
        boolean z = DEBUG;
        if (z) {
            Log.d("SmartSpaceController", "onNewCard: " + newCardInfo);
        }
        if (newCardInfo == null) {
            return;
        }
        if (newCardInfo.getUserId() == this.mCurrentUserId) {
            this.mBackgroundHandler.post(new Runnable(newCardInfo) { // from class: com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda1
                public final /* synthetic */ NewCardInfo f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    SmartSpaceController.this.lambda$onNewCard$2(this.f$1);
                }
            });
        } else if (z) {
            Log.d("SmartSpaceController", "Ignore card that belongs to another user target: " + this.mCurrentUserId + " current: " + this.mCurrentUserId);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNewCard$2(NewCardInfo newCardInfo) {
        SmartspaceProto$CardWrapper wrapper = newCardInfo.toWrapper(this.mContext);
        if (!this.mHidePrivateData || !this.mHideWorkData) {
            ProtoStore protoStore = this.mStore;
            protoStore.store(wrapper, "smartspace_" + this.mCurrentUserId + "_" + newCardInfo.isPrimary());
        }
        this.mUiHandler.post(new Runnable(newCardInfo, newCardInfo.shouldDiscard() ? null : SmartSpaceCard.fromWrapper(this.mContext, wrapper, newCardInfo.isPrimary())) { // from class: com.google.android.systemui.smartspace.SmartSpaceController$$ExternalSyntheticLambda2
            public final /* synthetic */ NewCardInfo f$1;
            public final /* synthetic */ SmartSpaceCard f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                SmartSpaceController.this.lambda$onNewCard$1(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onNewCard$1(NewCardInfo newCardInfo, SmartSpaceCard smartSpaceCard) {
        if (newCardInfo.isPrimary()) {
            this.mData.mCurrentCard = smartSpaceCard;
        } else {
            this.mData.mWeatherCard = smartSpaceCard;
        }
        this.mData.handleExpire();
        update();
    }

    private void clearStore() {
        ProtoStore protoStore = this.mStore;
        protoStore.store(null, "smartspace_" + this.mCurrentUserId + "_true");
        ProtoStore protoStore2 = this.mStore;
        protoStore2.store(null, "smartspace_" + this.mCurrentUserId + "_false");
    }

    /* access modifiers changed from: private */
    public void update() {
        Assert.isMainThread();
        boolean z = DEBUG;
        if (z) {
            Log.d("SmartSpaceController", "update");
        }
        if (this.mAlarmRegistered) {
            this.mAlarmManager.cancel(this.mExpireAlarmAction);
            this.mAlarmRegistered = false;
        }
        long expiresAtMillis = this.mData.getExpiresAtMillis();
        if (expiresAtMillis > 0) {
            this.mAlarmManager.set(0, expiresAtMillis, "SmartSpace", this.mExpireAlarmAction, this.mUiHandler);
            this.mAlarmRegistered = true;
        }
        if (this.mListeners != null) {
            if (z) {
                Log.d("SmartSpaceController", "notifying listeners data=" + this.mData);
            }
            ArrayList arrayList = new ArrayList(this.mListeners);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((SmartSpaceUpdateListener) arrayList.get(i)).onSmartSpaceUpdated(this.mData);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onExpire(boolean z) {
        Assert.isMainThread();
        this.mAlarmRegistered = false;
        if (this.mData.handleExpire() || z) {
            update();
        } else if (DEBUG) {
            Log.d("SmartSpaceController", "onExpire - cancelled");
        }
    }

    public void setHideSensitiveData(boolean z, boolean z2) {
        if (!(this.mHidePrivateData == z && this.mHideWorkData == z2)) {
            this.mHidePrivateData = z;
            this.mHideWorkData = z2;
            ArrayList arrayList = new ArrayList(this.mListeners);
            boolean z3 = false;
            for (int i = 0; i < arrayList.size(); i++) {
                ((SmartSpaceUpdateListener) arrayList.get(i)).onSensitiveModeChanged(z, z2);
            }
            if (this.mData.getCurrentCard() != null) {
                boolean z4 = this.mHidePrivateData && !this.mData.getCurrentCard().isWorkProfile();
                if (this.mHideWorkData && this.mData.getCurrentCard().isWorkProfile()) {
                    z3 = true;
                }
                if (z4 || z3) {
                    clearStore();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onGsaChanged() {
        if (DEBUG) {
            Log.d("SmartSpaceController", "onGsaChanged");
        }
        if (UserHandle.myUserId() == 0) {
            this.mAppContext.sendBroadcast(new Intent("com.google.android.systemui.smartspace.ENABLE_UPDATE").setPackage("com.google.android.googlequicksearchbox").addFlags(268435456));
            this.mSmartSpaceEnabledBroadcastSent = true;
        }
        ArrayList arrayList = new ArrayList(this.mListeners);
        for (int i = 0; i < arrayList.size(); i++) {
            ((SmartSpaceUpdateListener) arrayList.get(i)).onGsaChanged();
        }
    }

    public void reloadData() {
        this.mData.mCurrentCard = loadSmartSpaceData(true);
        this.mData.mWeatherCard = loadSmartSpaceData(false);
        update();
    }

    private boolean isSmartSpaceDisabledByExperiments() {
        boolean z;
        String string = Settings.Global.getString(this.mContext.getContentResolver(), "always_on_display_constants");
        KeyValueListParser keyValueListParser = new KeyValueListParser(',');
        try {
            keyValueListParser.setString(string);
            z = keyValueListParser.getBoolean("smart_space_enabled", true);
        } catch (IllegalArgumentException unused) {
            Log.e("SmartSpaceController", "Bad AOD constants");
            z = true;
        }
        return !z;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println();
        printWriter.println("SmartspaceController");
        printWriter.println("  initial broadcast: " + this.mSmartSpaceEnabledBroadcastSent);
        printWriter.println("  weather " + this.mData.mWeatherCard);
        printWriter.println("  current " + this.mData.mCurrentCard);
        printWriter.println("serialized:");
        printWriter.println("  weather " + loadSmartSpaceData(false));
        printWriter.println("  current " + loadSmartSpaceData(true));
        printWriter.println("disabled by experiment: " + isSmartSpaceDisabledByExperiments());
    }

    public void addListener(SmartSpaceUpdateListener smartSpaceUpdateListener) {
        Assert.isMainThread();
        this.mListeners.add(smartSpaceUpdateListener);
        SmartSpaceData smartSpaceData = this.mData;
        if (!(smartSpaceData == null || smartSpaceUpdateListener == null)) {
            smartSpaceUpdateListener.onSmartSpaceUpdated(smartSpaceData);
        }
        if (smartSpaceUpdateListener != null) {
            smartSpaceUpdateListener.onSensitiveModeChanged(this.mHidePrivateData, this.mHideWorkData);
        }
    }

    public void removeListener(SmartSpaceUpdateListener smartSpaceUpdateListener) {
        Assert.isMainThread();
        this.mListeners.remove(smartSpaceUpdateListener);
    }

    /* loaded from: classes2.dex */
    private class UserSwitchReceiver extends BroadcastReceiver {
        private UserSwitchReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (SmartSpaceController.DEBUG) {
                Log.d("SmartSpaceController", "Switching user: " + intent.getAction() + " uid: " + UserHandle.myUserId());
            }
            if (intent.getAction().equals("android.intent.action.USER_SWITCHED")) {
                SmartSpaceController.this.mCurrentUserId = intent.getIntExtra("android.intent.extra.user_handle", -1);
                SmartSpaceController.this.mData.clear();
                SmartSpaceController.this.onExpire(true);
            }
            SmartSpaceController.this.onExpire(true);
        }
    }
}
