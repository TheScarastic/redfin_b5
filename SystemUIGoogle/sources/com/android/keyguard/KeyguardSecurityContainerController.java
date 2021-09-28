package com.android.keyguard;

import android.app.admin.DevicePolicyManager;
import android.content.res.ColorStateList;
import android.metrics.LogMaker;
import android.util.Log;
import android.util.Slog;
import android.view.MotionEvent;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.AdminSecondaryLockScreenController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.utils.ThreadUtils;
import com.android.systemui.DejankUtils;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardSecurityContainerController extends ViewController<KeyguardSecurityContainer> implements KeyguardSecurityView {
    private final AdminSecondaryLockScreenController mAdminSecondaryLockScreenController;
    private final ConfigurationController mConfigurationController;
    private ConfigurationController.ConfigurationListener mConfigurationListener;
    private KeyguardSecurityModel.SecurityMode mCurrentSecurityMode;
    private final Gefingerpoken mGlobalTouchListener;
    private KeyguardSecurityCallback mKeyguardSecurityCallback;
    private final KeyguardStateController mKeyguardStateController;
    private int mLastOrientation;
    private final LockPatternUtils mLockPatternUtils;
    private final MetricsLogger mMetricsLogger;
    private final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    private final KeyguardSecurityModel mSecurityModel;
    private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
    private KeyguardSecurityContainer.SwipeListener mSwipeListener;
    private final UiEventLogger mUiEventLogger;
    private final KeyguardUpdateMonitor mUpdateMonitor;

    private KeyguardSecurityContainerController(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityContainer.SecurityCallback securityCallback, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController) {
        super(keyguardSecurityContainer);
        this.mLastOrientation = 0;
        this.mCurrentSecurityMode = KeyguardSecurityModel.SecurityMode.Invalid;
        this.mGlobalTouchListener = new Gefingerpoken() { // from class: com.android.keyguard.KeyguardSecurityContainerController.1
            private MotionEvent mTouchDown;

            @Override // com.android.systemui.Gefingerpoken
            public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
                return false;
            }

            @Override // com.android.systemui.Gefingerpoken
            public boolean onTouchEvent(MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == 0) {
                    MotionEvent motionEvent2 = this.mTouchDown;
                    if (motionEvent2 != null) {
                        motionEvent2.recycle();
                        this.mTouchDown = null;
                    }
                    this.mTouchDown = MotionEvent.obtain(motionEvent);
                    return false;
                } else if (this.mTouchDown == null) {
                    return false;
                } else {
                    if (motionEvent.getActionMasked() != 1 && motionEvent.getActionMasked() != 3) {
                        return false;
                    }
                    this.mTouchDown.recycle();
                    this.mTouchDown = null;
                    return false;
                }
            }
        };
        this.mKeyguardSecurityCallback = new KeyguardSecurityCallback() { // from class: com.android.keyguard.KeyguardSecurityContainerController.2
            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void userActivity() {
                if (KeyguardSecurityContainerController.this.mSecurityCallback != null) {
                    KeyguardSecurityContainerController.this.mSecurityCallback.userActivity();
                }
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void onUserInput() {
                KeyguardSecurityContainerController.this.mUpdateMonitor.cancelFaceAuth();
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void dismiss(boolean z, int i) {
                dismiss(z, i, false);
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void dismiss(boolean z, int i, boolean z2) {
                KeyguardSecurityContainerController.this.mSecurityCallback.dismiss(z, i, z2);
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void reportUnlockAttempt(int i, boolean z, int i2) {
                KeyguardSecurityContainer.BouncerUiEvent bouncerUiEvent;
                if (z) {
                    SysUiStatsLog.write(64, 2);
                    KeyguardSecurityContainerController.this.mLockPatternUtils.reportSuccessfulPasswordAttempt(i);
                    ThreadUtils.postOnBackgroundThread(KeyguardSecurityContainerController$2$$ExternalSyntheticLambda0.INSTANCE);
                } else {
                    SysUiStatsLog.write(64, 1);
                    KeyguardSecurityContainerController.this.reportFailedUnlockAttempt(i, i2);
                }
                KeyguardSecurityContainerController.this.mMetricsLogger.write(new LogMaker(197).setType(z ? 10 : 11));
                UiEventLogger uiEventLogger2 = KeyguardSecurityContainerController.this.mUiEventLogger;
                if (z) {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_SUCCESS;
                } else {
                    bouncerUiEvent = KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_PASSWORD_FAILURE;
                }
                uiEventLogger2.log(bouncerUiEvent);
            }

            /* access modifiers changed from: private */
            public static /* synthetic */ void lambda$reportUnlockAttempt$0() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException unused) {
                }
                System.gc();
                System.runFinalization();
                System.gc();
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void reset() {
                KeyguardSecurityContainerController.this.mSecurityCallback.reset();
            }

            @Override // com.android.keyguard.KeyguardSecurityCallback
            public void onCancelClicked() {
                KeyguardSecurityContainerController.this.mSecurityCallback.onCancelClicked();
            }
        };
        this.mSwipeListener = new KeyguardSecurityContainer.SwipeListener() { // from class: com.android.keyguard.KeyguardSecurityContainerController.3
            @Override // com.android.keyguard.KeyguardSecurityContainer.SwipeListener
            public void onSwipeUp() {
                if (!KeyguardSecurityContainerController.this.mUpdateMonitor.isFaceDetectionRunning()) {
                    KeyguardSecurityContainerController.this.mUpdateMonitor.requestFaceAuth(true);
                    KeyguardSecurityContainerController.this.mKeyguardSecurityCallback.userActivity();
                    KeyguardSecurityContainerController.this.showMessage(null, null);
                }
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardSecurityContainerController.4
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onOverlayChanged() {
                KeyguardSecurityContainerController.this.mSecurityViewFlipperController.reloadColors();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onUiModeChanged() {
                KeyguardSecurityContainerController.this.mSecurityViewFlipperController.reloadColors();
            }
        };
        this.mLockPatternUtils = lockPatternUtils;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mSecurityModel = keyguardSecurityModel;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mKeyguardStateController = keyguardStateController;
        this.mSecurityCallback = securityCallback;
        this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
        this.mAdminSecondaryLockScreenController = factory.create(this.mKeyguardSecurityCallback);
        this.mConfigurationController = configurationController;
        this.mLastOrientation = getResources().getConfiguration().orientation;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mSecurityViewFlipperController.init();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((KeyguardSecurityContainer) this.mView).setSwipeListener(this.mSwipeListener);
        ((KeyguardSecurityContainer) this.mView).addMotionEventListener(this.mGlobalTouchListener);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        ((KeyguardSecurityContainer) this.mView).removeMotionEventListener(this.mGlobalTouchListener);
    }

    public void onPause() {
        this.mAdminSecondaryLockScreenController.hide();
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onPause();
        }
        ((KeyguardSecurityContainer) this.mView).onPause();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ KeyguardSecurityModel.SecurityMode lambda$showPrimarySecurityScreen$0() {
        return this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
    }

    public void showPrimarySecurityScreen(boolean z) {
        showSecurityScreen((KeyguardSecurityModel.SecurityMode) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.keyguard.KeyguardSecurityContainerController$$ExternalSyntheticLambda0
            @Override // java.util.function.Supplier
            public final Object get() {
                return KeyguardSecurityContainerController.this.lambda$showPrimarySecurityScreen$0();
            }
        }));
    }

    public void showPromptReason(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            if (i != 0) {
                Log.i("KeyguardSecurityView", "Strong auth required, reason: " + i);
            }
            getCurrentSecurityController().showPromptReason(i);
        }
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().showMessage(charSequence, colorStateList);
        }
    }

    public KeyguardSecurityModel.SecurityMode getCurrentSecurityMode() {
        return this.mCurrentSecurityMode;
    }

    public void reset() {
        ((KeyguardSecurityContainer) this.mView).reset();
        this.mSecurityViewFlipperController.reset();
    }

    public CharSequence getTitle() {
        return ((KeyguardSecurityContainer) this.mView).getTitle();
    }

    public void onResume(int i) {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onResume(i);
        }
        ((KeyguardSecurityContainer) this.mView).onResume(this.mSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()), this.mKeyguardStateController.isFaceAuthEnabled());
    }

    public void startAppearAnimation() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().startAppearAnimation();
        }
    }

    public boolean startDisappearAnimation(Runnable runnable) {
        ((KeyguardSecurityContainer) this.mView).startDisappearAnimation(getCurrentSecurityMode());
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            return getCurrentSecurityController().startDisappearAnimation(runnable);
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public void onStartingToHide() {
        if (this.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
            getCurrentSecurityController().onStartingToHide();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x009f A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00b1  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00ce  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean showNextSecurityScreenOrFinish(boolean r11, int r12, boolean r13) {
        /*
            r10 = this;
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r0 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.UNKNOWN
            com.android.keyguard.KeyguardUpdateMonitor r1 = r10.mUpdateMonitor
            boolean r1 = r1.getUserHasTrust(r12)
            r2 = 5
            r3 = 4
            r4 = 2
            r5 = 3
            r6 = -1
            r7 = 0
            r8 = 1
            if (r1 == 0) goto L_0x0017
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r11 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_EXTENDED_ACCESS
            r3 = r5
        L_0x0014:
            r1 = r7
            goto L_0x009d
        L_0x0017:
            com.android.keyguard.KeyguardUpdateMonitor r1 = r10.mUpdateMonitor
            boolean r1 = r1.getUserUnlockedWithBiometric(r12)
            if (r1 == 0) goto L_0x0023
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r11 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_BIOMETRIC
            r3 = r4
            goto L_0x0014
        L_0x0023:
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.None
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r9 = r10.getCurrentSecurityMode()
            if (r1 != r9) goto L_0x003e
            com.android.keyguard.KeyguardSecurityModel r11 = r10.mSecurityModel
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r11 = r11.getSecurityMode(r12)
            if (r1 != r11) goto L_0x0037
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r11 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_NONE_SECURITY
            r3 = r7
            goto L_0x0014
        L_0x0037:
            r10.showSecurityScreen(r11)
            r11 = r0
            r3 = r6
            r8 = r7
            goto L_0x0014
        L_0x003e:
            if (r11 == 0) goto L_0x0099
            int[] r11 = com.android.keyguard.KeyguardSecurityContainerController.AnonymousClass5.$SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r9 = r10.getCurrentSecurityMode()
            int r9 = r9.ordinal()
            r11 = r11[r9]
            if (r11 == r8) goto L_0x0094
            if (r11 == r4) goto L_0x0094
            if (r11 == r5) goto L_0x0094
            if (r11 == r3) goto L_0x0079
            if (r11 == r2) goto L_0x0079
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r1 = "Bad security screen "
            r11.append(r1)
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r1 = r10.getCurrentSecurityMode()
            r11.append(r1)
            java.lang.String r1 = ", fail safe"
            r11.append(r1)
            java.lang.String r11 = r11.toString()
            java.lang.String r1 = "KeyguardSecurityView"
            android.util.Log.v(r1, r11)
            r10.showPrimarySecurityScreen(r7)
            goto L_0x0099
        L_0x0079:
            com.android.keyguard.KeyguardSecurityModel r11 = r10.mSecurityModel
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r11 = r11.getSecurityMode(r12)
            if (r11 != r1) goto L_0x0090
            com.android.internal.widget.LockPatternUtils r1 = r10.mLockPatternUtils
            int r4 = com.android.keyguard.KeyguardUpdateMonitor.getCurrentUser()
            boolean r1 = r1.isLockScreenDisabled(r4)
            if (r1 == 0) goto L_0x0090
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r11 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_SIM
            goto L_0x0014
        L_0x0090:
            r10.showSecurityScreen(r11)
            goto L_0x0099
        L_0x0094:
            com.android.keyguard.KeyguardSecurityContainer$BouncerUiEvent r11 = com.android.keyguard.KeyguardSecurityContainer.BouncerUiEvent.BOUNCER_DISMISS_PASSWORD
            r1 = r8
            r3 = r1
            goto L_0x009d
        L_0x0099:
            r11 = r0
            r3 = r6
            r1 = r7
            r8 = r1
        L_0x009d:
            if (r8 == 0) goto L_0x00af
            if (r13 != 0) goto L_0x00af
            com.android.keyguard.KeyguardUpdateMonitor r13 = r10.mUpdateMonitor
            android.content.Intent r13 = r13.getSecondaryLockscreenRequirement(r12)
            if (r13 == 0) goto L_0x00af
            com.android.keyguard.AdminSecondaryLockScreenController r10 = r10.mAdminSecondaryLockScreenController
            r10.show(r13)
            return r7
        L_0x00af:
            if (r3 == r6) goto L_0x00c5
            com.android.internal.logging.MetricsLogger r13 = r10.mMetricsLogger
            android.metrics.LogMaker r4 = new android.metrics.LogMaker
            r5 = 197(0xc5, float:2.76E-43)
            r4.<init>(r5)
            android.metrics.LogMaker r2 = r4.setType(r2)
            android.metrics.LogMaker r2 = r2.setSubtype(r3)
            r13.write(r2)
        L_0x00c5:
            if (r11 == r0) goto L_0x00cc
            com.android.internal.logging.UiEventLogger r13 = r10.mUiEventLogger
            r13.log(r11)
        L_0x00cc:
            if (r8 == 0) goto L_0x00d3
            com.android.keyguard.KeyguardSecurityContainer$SecurityCallback r10 = r10.mSecurityCallback
            r10.finish(r1, r12)
        L_0x00d3:
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSecurityContainerController.showNextSecurityScreenOrFinish(boolean, int, boolean):boolean");
    }

    /* renamed from: com.android.keyguard.KeyguardSecurityContainerController$5  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode;

        static {
            int[] iArr = new int[KeyguardSecurityModel.SecurityMode.values().length];
            $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode = iArr;
            try {
                iArr[KeyguardSecurityModel.SecurityMode.Pattern.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.Password.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.PIN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPin.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.SimPuk.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    @Override // com.android.keyguard.KeyguardSecurityView
    public boolean needsInput() {
        return getCurrentSecurityController().needsInput();
    }

    @VisibleForTesting
    void showSecurityScreen(KeyguardSecurityModel.SecurityMode securityMode) {
        if (securityMode != KeyguardSecurityModel.SecurityMode.Invalid && securityMode != this.mCurrentSecurityMode) {
            KeyguardInputViewController<KeyguardInputView> currentSecurityController = getCurrentSecurityController();
            if (currentSecurityController != null) {
                currentSecurityController.onPause();
            }
            KeyguardInputViewController<KeyguardInputView> changeSecurityMode = changeSecurityMode(securityMode);
            if (changeSecurityMode != null) {
                changeSecurityMode.onResume(2);
                this.mSecurityViewFlipperController.show(changeSecurityMode);
                ((KeyguardSecurityContainer) this.mView).updateLayoutForSecurityMode(securityMode);
            }
            this.mSecurityCallback.onSecurityModeChanged(securityMode, changeSecurityMode != null && changeSecurityMode.needsInput());
        }
    }

    public void reportFailedUnlockAttempt(int i, int i2) {
        int i3 = 1;
        int currentFailedPasswordAttempts = this.mLockPatternUtils.getCurrentFailedPasswordAttempts(i) + 1;
        DevicePolicyManager devicePolicyManager = this.mLockPatternUtils.getDevicePolicyManager();
        int maximumFailedPasswordsForWipe = devicePolicyManager.getMaximumFailedPasswordsForWipe(null, i);
        int i4 = maximumFailedPasswordsForWipe > 0 ? maximumFailedPasswordsForWipe - currentFailedPasswordAttempts : Integer.MAX_VALUE;
        if (i4 < 5) {
            int profileWithMinimumFailedPasswordsForWipe = devicePolicyManager.getProfileWithMinimumFailedPasswordsForWipe(i);
            if (profileWithMinimumFailedPasswordsForWipe == i) {
                if (profileWithMinimumFailedPasswordsForWipe != 0) {
                    i3 = 3;
                }
            } else if (profileWithMinimumFailedPasswordsForWipe != -10000) {
                i3 = 2;
            }
            if (i4 > 0) {
                ((KeyguardSecurityContainer) this.mView).showAlmostAtWipeDialog(currentFailedPasswordAttempts, i4, i3);
            } else {
                Slog.i("KeyguardSecurityView", "Too many unlock attempts; user " + profileWithMinimumFailedPasswordsForWipe + " will be wiped!");
                ((KeyguardSecurityContainer) this.mView).showWipeDialog(currentFailedPasswordAttempts, i3);
            }
        }
        this.mLockPatternUtils.reportFailedPasswordAttempt(i);
        if (i2 > 0) {
            this.mLockPatternUtils.reportPasswordLockout(i2, i);
            ((KeyguardSecurityContainer) this.mView).showTimeoutDialog(i, i2, this.mLockPatternUtils, this.mSecurityModel.getSecurityMode(i));
        }
    }

    private KeyguardInputViewController<KeyguardInputView> getCurrentSecurityController() {
        return this.mSecurityViewFlipperController.getSecurityView(this.mCurrentSecurityMode, this.mKeyguardSecurityCallback);
    }

    private KeyguardInputViewController<KeyguardInputView> changeSecurityMode(KeyguardSecurityModel.SecurityMode securityMode) {
        this.mCurrentSecurityMode = securityMode;
        return getCurrentSecurityController();
    }

    public void updateResources() {
        int i = getResources().getConfiguration().orientation;
        if (i != this.mLastOrientation) {
            this.mLastOrientation = i;
            ((KeyguardSecurityContainer) this.mView).updateLayoutForSecurityMode(this.mCurrentSecurityMode);
        }
    }

    public void updateKeyguardPosition(float f) {
        ((KeyguardSecurityContainer) this.mView).updateKeyguardPosition(f);
    }

    /* loaded from: classes.dex */
    static class Factory {
        private final AdminSecondaryLockScreenController.Factory mAdminSecondaryLockScreenControllerFactory;
        private final ConfigurationController mConfigurationController;
        private final KeyguardSecurityModel mKeyguardSecurityModel;
        private final KeyguardStateController mKeyguardStateController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        private final LockPatternUtils mLockPatternUtils;
        private final MetricsLogger mMetricsLogger;
        private final KeyguardSecurityViewFlipperController mSecurityViewFlipperController;
        private final UiEventLogger mUiEventLogger;
        private final KeyguardSecurityContainer mView;

        /* access modifiers changed from: package-private */
        public Factory(KeyguardSecurityContainer keyguardSecurityContainer, AdminSecondaryLockScreenController.Factory factory, LockPatternUtils lockPatternUtils, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityModel keyguardSecurityModel, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, KeyguardStateController keyguardStateController, KeyguardSecurityViewFlipperController keyguardSecurityViewFlipperController, ConfigurationController configurationController) {
            this.mView = keyguardSecurityContainer;
            this.mAdminSecondaryLockScreenControllerFactory = factory;
            this.mLockPatternUtils = lockPatternUtils;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mMetricsLogger = metricsLogger;
            this.mUiEventLogger = uiEventLogger;
            this.mKeyguardStateController = keyguardStateController;
            this.mSecurityViewFlipperController = keyguardSecurityViewFlipperController;
            this.mConfigurationController = configurationController;
        }

        public KeyguardSecurityContainerController create(KeyguardSecurityContainer.SecurityCallback securityCallback) {
            return new KeyguardSecurityContainerController(this.mView, this.mAdminSecondaryLockScreenControllerFactory, this.mLockPatternUtils, this.mKeyguardUpdateMonitor, this.mKeyguardSecurityModel, this.mMetricsLogger, this.mUiEventLogger, this.mKeyguardStateController, securityCallback, this.mSecurityViewFlipperController, this.mConfigurationController);
        }
    }
}
