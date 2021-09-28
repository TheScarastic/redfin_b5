package com.android.systemui.demomode;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DemoModeAvailabilityTracker.kt */
/* loaded from: classes.dex */
public abstract class DemoModeAvailabilityTracker {
    private final Context context;
    private boolean isInDemoMode = checkIsDemoModeOn();
    private boolean isDemoModeAvailable = checkIsDemoModeAllowed();
    private final DemoModeAvailabilityTracker$allowedObserver$1 allowedObserver = new DemoModeAvailabilityTracker$allowedObserver$1(this, new Handler(Looper.getMainLooper()));
    private final DemoModeAvailabilityTracker$onObserver$1 onObserver = new DemoModeAvailabilityTracker$onObserver$1(this, new Handler(Looper.getMainLooper()));

    public abstract void onDemoModeAvailabilityChanged();

    public abstract void onDemoModeFinished();

    public abstract void onDemoModeStarted();

    public DemoModeAvailabilityTracker(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final boolean isInDemoMode() {
        return this.isInDemoMode;
    }

    public final void setInDemoMode(boolean z) {
        this.isInDemoMode = z;
    }

    public final boolean isDemoModeAvailable() {
        return this.isDemoModeAvailable;
    }

    public final void setDemoModeAvailable(boolean z) {
        this.isDemoModeAvailable = z;
    }

    public final void startTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_demo_allowed"), false, this.allowedObserver);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("sysui_tuner_demo_on"), false, this.onObserver);
    }

    public final void stopTracking() {
        ContentResolver contentResolver = this.context.getContentResolver();
        contentResolver.unregisterContentObserver(this.allowedObserver);
        contentResolver.unregisterContentObserver(this.onObserver);
    }

    /* access modifiers changed from: private */
    public final boolean checkIsDemoModeAllowed() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_demo_allowed", 0) != 0;
    }

    /* access modifiers changed from: private */
    public final boolean checkIsDemoModeOn() {
        return Settings.Global.getInt(this.context.getContentResolver(), "sysui_tuner_demo_on", 0) != 0;
    }
}
