package com.google.android.systemui.statusbar.policy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.power.EnhancedEstimates;
import com.android.systemui.settings.UserContentResolverProvider;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class BatteryControllerImplGoogle extends BatteryControllerImpl implements ReverseChargingController.ReverseChargingChangeCallback {
    static final String EBS_STATE_AUTHORITY = "com.google.android.flipendo.api";
    protected final ContentObserver mContentObserver;
    private final UserContentResolverProvider mContentResolverProvider;
    private boolean mExtremeSaver;
    private String mName;
    private boolean mReverse;
    private final ReverseChargingController mReverseChargingController;
    private int mRtxLevel;
    static final Uri IS_EBS_ENABLED_OBSERVABLE_URI = Uri.parse("content://com.google.android.flipendo.api/get_flipendo_state");
    private static final boolean DEBUG = Log.isLoggable("BatteryControllerGoogle", 3);

    public BatteryControllerImplGoogle(Context context, EnhancedEstimates enhancedEstimates, PowerManager powerManager, BroadcastDispatcher broadcastDispatcher, DemoModeController demoModeController, Handler handler, Handler handler2, UserContentResolverProvider userContentResolverProvider, ReverseChargingController reverseChargingController) {
        super(context, enhancedEstimates, powerManager, broadcastDispatcher, demoModeController, handler, handler2);
        this.mReverseChargingController = reverseChargingController;
        this.mContentResolverProvider = userContentResolverProvider;
        this.mContentObserver = new ContentObserver(handler2) { // from class: com.google.android.systemui.statusbar.policy.BatteryControllerImplGoogle.1
            @Override // android.database.ContentObserver
            public void onChange(boolean z, Uri uri) {
                if (BatteryControllerImplGoogle.DEBUG) {
                    Log.d("BatteryControllerGoogle", "Change in EBS value " + uri.toSafeString());
                }
                BatteryControllerImplGoogle batteryControllerImplGoogle = BatteryControllerImplGoogle.this;
                batteryControllerImplGoogle.setExtremeSaver(batteryControllerImplGoogle.isExtremeBatterySaving());
            }
        };
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl, com.android.systemui.statusbar.policy.BatteryController
    public void init() {
        super.init();
        resetReverseInfo();
        this.mReverseChargingController.init(this);
        this.mReverseChargingController.addCallback((ReverseChargingController.ReverseChargingChangeCallback) this);
        try {
            ContentResolver userContentResolver = this.mContentResolverProvider.getUserContentResolver();
            Uri uri = IS_EBS_ENABLED_OBSERVABLE_URI;
            userContentResolver.registerContentObserver(uri, false, this.mContentObserver, -1);
            this.mContentObserver.onChange(false, uri);
        } catch (Exception e) {
            Log.w("BatteryControllerGoogle", "Couldn't register to observe provider", e);
        }
    }

    @Override // com.google.android.systemui.reversecharging.ReverseChargingController.ReverseChargingChangeCallback
    public void onReverseChargingChanged(boolean z, int i, String str) {
        this.mReverse = z;
        this.mRtxLevel = i;
        this.mName = str;
        if (DEBUG) {
            Log.d("BatteryControllerGoogle", "onReverseChargingChanged(): rtx=" + (z ? 1 : 0) + " level=" + i + " name=" + str + " this=" + this);
        }
        fireReverseChanged();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl
    public void addCallback(BatteryController.BatteryStateChangeCallback batteryStateChangeCallback) {
        super.addCallback(batteryStateChangeCallback);
        batteryStateChangeCallback.onReverseChanged(this.mReverse, this.mRtxLevel, this.mName);
        batteryStateChangeCallback.onExtremeBatterySaverChanged(this.mExtremeSaver);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl, android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        this.mReverseChargingController.handleIntentForReverseCharging(intent);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isReverseSupported() {
        return this.mReverseChargingController.isReverseSupported();
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public boolean isReverseOn() {
        return this.mReverse;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController
    public void setReverseState(boolean z) {
        this.mReverseChargingController.setReverseState(z);
    }

    private void resetReverseInfo() {
        this.mReverse = false;
        this.mRtxLevel = -1;
        this.mName = null;
    }

    /* access modifiers changed from: private */
    public void setExtremeSaver(boolean z) {
        if (z != this.mExtremeSaver) {
            this.mExtremeSaver = z;
            fireExtremeSaverChanged();
        }
    }

    private void fireExtremeSaverChanged() {
        synchronized (this.mChangeCallbacks) {
            int size = this.mChangeCallbacks.size();
            for (int i = 0; i < size; i++) {
                this.mChangeCallbacks.get(i).onExtremeBatterySaverChanged(this.mExtremeSaver);
            }
        }
    }

    private void fireReverseChanged() {
        synchronized (this.mChangeCallbacks) {
            ArrayList arrayList = new ArrayList(this.mChangeCallbacks);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ((BatteryController.BatteryStateChangeCallback) arrayList.get(i)).onReverseChanged(this.mReverse, this.mRtxLevel, this.mName);
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isExtremeBatterySaving() {
        Bundle bundle;
        try {
            bundle = this.mContentResolverProvider.getUserContentResolver().call(EBS_STATE_AUTHORITY, "get_flipendo_state", (String) null, new Bundle());
        } catch (IllegalArgumentException unused) {
            bundle = new Bundle();
        }
        return bundle.getBoolean("flipendo_state", false);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryControllerImpl, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(fileDescriptor, printWriter, strArr);
        printWriter.print("  mReverse=");
        printWriter.println(this.mReverse);
        printWriter.print("  mExtremeSaver=");
        printWriter.println(this.mExtremeSaver);
    }
}
