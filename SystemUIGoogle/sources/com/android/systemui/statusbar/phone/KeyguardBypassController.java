package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.biometrics.BiometricSourceType;
import com.android.systemui.Dumpable;
import com.android.systemui.R$integer;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.tuner.TunerService;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardBypassController.kt */
/* loaded from: classes.dex */
public class KeyguardBypassController implements Dumpable, StackScrollAlgorithm.BypassController {
    public static final Companion Companion = new Companion(null);
    private boolean altBouncerShowing;
    private boolean bouncerShowing;
    private boolean bypassEnabled;
    private final int bypassOverride;
    private boolean hasFaceFeature;
    private boolean isPulseExpanding;
    private boolean launchingAffordance;
    private final KeyguardStateController mKeyguardStateController;
    private PendingUnlock pendingUnlock;
    private boolean qSExpanded;
    private final StatusBarStateController statusBarStateController;
    public BiometricUnlockController unlockController;
    private boolean userHasDeviceEntryIntent;

    public final boolean getUserHasDeviceEntryIntent() {
        return this.userHasDeviceEntryIntent;
    }

    public final void setUserHasDeviceEntryIntent(boolean z) {
        this.userHasDeviceEntryIntent = z;
    }

    /* compiled from: KeyguardBypassController.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static final class PendingUnlock {
        private final boolean isStrongBiometric;
        private final BiometricSourceType pendingUnlockType;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof PendingUnlock)) {
                return false;
            }
            PendingUnlock pendingUnlock = (PendingUnlock) obj;
            return this.pendingUnlockType == pendingUnlock.pendingUnlockType && this.isStrongBiometric == pendingUnlock.isStrongBiometric;
        }

        public int hashCode() {
            int hashCode = this.pendingUnlockType.hashCode() * 31;
            boolean z = this.isStrongBiometric;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            int i3 = z ? 1 : 0;
            return hashCode + i;
        }

        public String toString() {
            return "PendingUnlock(pendingUnlockType=" + this.pendingUnlockType + ", isStrongBiometric=" + this.isStrongBiometric + ')';
        }

        public PendingUnlock(BiometricSourceType biometricSourceType, boolean z) {
            Intrinsics.checkNotNullParameter(biometricSourceType, "pendingUnlockType");
            this.pendingUnlockType = biometricSourceType;
            this.isStrongBiometric = z;
        }

        public final BiometricSourceType getPendingUnlockType() {
            return this.pendingUnlockType;
        }

        public final boolean isStrongBiometric() {
            return this.isStrongBiometric;
        }
    }

    public final BiometricUnlockController getUnlockController() {
        BiometricUnlockController biometricUnlockController = this.unlockController;
        if (biometricUnlockController != null) {
            return biometricUnlockController;
        }
        Intrinsics.throwUninitializedPropertyAccessException("unlockController");
        throw null;
    }

    public final void setUnlockController(BiometricUnlockController biometricUnlockController) {
        Intrinsics.checkNotNullParameter(biometricUnlockController, "<set-?>");
        this.unlockController = biometricUnlockController;
    }

    public final void setPulseExpanding(boolean z) {
        this.isPulseExpanding = z;
    }

    @Override // com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm.BypassController
    public boolean isBypassEnabled() {
        return getBypassEnabled();
    }

    public final boolean getBypassEnabled() {
        int i = this.bypassOverride;
        return (i != 1 ? i != 2 ? this.bypassEnabled : false : true) && this.mKeyguardStateController.isFaceAuthEnabled();
    }

    public final void setBouncerShowing(boolean z) {
        this.bouncerShowing = z;
    }

    public final boolean getAltBouncerShowing() {
        return this.altBouncerShowing;
    }

    public final void setAltBouncerShowing(boolean z) {
        this.altBouncerShowing = z;
    }

    public final void setLaunchingAffordance(boolean z) {
        this.launchingAffordance = z;
    }

    public final void setQSExpanded(boolean z) {
        boolean z2 = this.qSExpanded != z;
        this.qSExpanded = z;
        if (z2 && !z) {
            maybePerformPendingUnlock();
        }
    }

    public KeyguardBypassController(Context context, final TunerService tunerService, StatusBarStateController statusBarStateController, NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.mKeyguardStateController = keyguardStateController;
        this.statusBarStateController = statusBarStateController;
        this.bypassOverride = context.getResources().getInteger(R$integer.config_face_unlock_bypass_override);
        boolean hasSystemFeature = context.getPackageManager().hasSystemFeature("android.hardware.biometrics.face");
        this.hasFaceFeature = hasSystemFeature;
        if (hasSystemFeature) {
            dumpManager.registerDumpable("KeyguardBypassController", this);
            statusBarStateController.addCallback(new StatusBarStateController.StateListener(this) { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.1
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
                public void onStateChanged(int i) {
                    if (i != 1) {
                        this.this$0.pendingUnlock = null;
                    }
                }
            });
            final int i = context.getResources().getBoolean(17891556) ? 1 : 0;
            tunerService.addTunable(new TunerService.Tunable(this) { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.2
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.tuner.TunerService.Tunable
                public void onTuningChanged(String str, String str2) {
                    this.this$0.bypassEnabled = tunerService.getValue(str, i) != 0;
                }
            }, "face_unlock_dismisses_keyguard");
            notificationLockscreenUserManager.addUserChangedListener(new NotificationLockscreenUserManager.UserChangedListener(this) { // from class: com.android.systemui.statusbar.phone.KeyguardBypassController.3
                final /* synthetic */ KeyguardBypassController this$0;

                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.statusbar.NotificationLockscreenUserManager.UserChangedListener
                public void onUserChanged(int i2) {
                    this.this$0.pendingUnlock = null;
                }
            });
        }
    }

    public final boolean onBiometricAuthenticated(BiometricSourceType biometricSourceType, boolean z) {
        Intrinsics.checkNotNullParameter(biometricSourceType, "biometricSourceType");
        if (biometricSourceType != BiometricSourceType.FACE || !getBypassEnabled()) {
            return true;
        }
        boolean canBypass = canBypass();
        if (!canBypass && (this.isPulseExpanding || this.qSExpanded)) {
            this.pendingUnlock = new PendingUnlock(biometricSourceType, z);
        }
        return canBypass;
    }

    public final void maybePerformPendingUnlock() {
        PendingUnlock pendingUnlock = this.pendingUnlock;
        if (pendingUnlock != null) {
            Intrinsics.checkNotNull(pendingUnlock);
            BiometricSourceType pendingUnlockType = pendingUnlock.getPendingUnlockType();
            PendingUnlock pendingUnlock2 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock2);
            if (onBiometricAuthenticated(pendingUnlockType, pendingUnlock2.isStrongBiometric())) {
                BiometricUnlockController unlockController = getUnlockController();
                PendingUnlock pendingUnlock3 = this.pendingUnlock;
                Intrinsics.checkNotNull(pendingUnlock3);
                BiometricSourceType pendingUnlockType2 = pendingUnlock3.getPendingUnlockType();
                PendingUnlock pendingUnlock4 = this.pendingUnlock;
                Intrinsics.checkNotNull(pendingUnlock4);
                unlockController.startWakeAndUnlock(pendingUnlockType2, pendingUnlock4.isStrongBiometric());
                this.pendingUnlock = null;
            }
        }
    }

    public final boolean canBypass() {
        if (!getBypassEnabled()) {
            return false;
        }
        if (!this.bouncerShowing && !this.altBouncerShowing && (this.statusBarStateController.getState() != 1 || this.launchingAffordance || this.isPulseExpanding || this.qSExpanded)) {
            return false;
        }
        return true;
    }

    public final boolean canPlaySubtleWindowAnimations() {
        if (!getBypassEnabled() || this.statusBarStateController.getState() != 1 || this.qSExpanded) {
            return false;
        }
        return true;
    }

    public final void onStartedGoingToSleep() {
        this.pendingUnlock = null;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("KeyguardBypassController:");
        PendingUnlock pendingUnlock = this.pendingUnlock;
        if (pendingUnlock != null) {
            Intrinsics.checkNotNull(pendingUnlock);
            printWriter.println(Intrinsics.stringPlus("  mPendingUnlock.pendingUnlockType: ", pendingUnlock.getPendingUnlockType()));
            PendingUnlock pendingUnlock2 = this.pendingUnlock;
            Intrinsics.checkNotNull(pendingUnlock2);
            printWriter.println(Intrinsics.stringPlus("  mPendingUnlock.isStrongBiometric: ", Boolean.valueOf(pendingUnlock2.isStrongBiometric())));
        } else {
            printWriter.println(Intrinsics.stringPlus("  mPendingUnlock: ", pendingUnlock));
        }
        printWriter.println(Intrinsics.stringPlus("  bypassEnabled: ", Boolean.valueOf(getBypassEnabled())));
        printWriter.println(Intrinsics.stringPlus("  canBypass: ", Boolean.valueOf(canBypass())));
        printWriter.println(Intrinsics.stringPlus("  bouncerShowing: ", Boolean.valueOf(this.bouncerShowing)));
        printWriter.println(Intrinsics.stringPlus("  altBouncerShowing: ", Boolean.valueOf(this.altBouncerShowing)));
        printWriter.println(Intrinsics.stringPlus("  isPulseExpanding: ", Boolean.valueOf(this.isPulseExpanding)));
        printWriter.println(Intrinsics.stringPlus("  launchingAffordance: ", Boolean.valueOf(this.launchingAffordance)));
        printWriter.println(Intrinsics.stringPlus("  qSExpanded: ", Boolean.valueOf(this.qSExpanded)));
        printWriter.println(Intrinsics.stringPlus("  hasFaceFeature: ", Boolean.valueOf(this.hasFaceFeature)));
        printWriter.println(Intrinsics.stringPlus("  userHasDeviceEntryIntent: ", Boolean.valueOf(this.userHasDeviceEntryIntent)));
    }

    /* compiled from: KeyguardBypassController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
