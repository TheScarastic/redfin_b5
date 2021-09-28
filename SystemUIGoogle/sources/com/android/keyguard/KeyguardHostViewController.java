package com.android.keyguard;

import android.app.ActivityManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.media.AudioManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.MathUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.R$styleable;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.settingslib.Utils;
import com.android.systemui.R$bool;
import com.android.systemui.R$integer;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.util.ViewController;
import java.io.File;
/* loaded from: classes.dex */
public class KeyguardHostViewController extends ViewController<KeyguardHostView> {
    private final AudioManager mAudioManager;
    private Runnable mCancelAction;
    private ActivityStarter.OnDismissAction mDismissAction;
    private final KeyguardSecurityContainerController mKeyguardSecurityContainerController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private final KeyguardSecurityContainer.SecurityCallback mSecurityCallback;
    private final TelephonyManager mTelephonyManager;
    private final ViewMediatorCallback mViewMediatorCallback;
    private final KeyguardUpdateMonitorCallback mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardHostViewController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            KeyguardHostViewController.this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onTrustGrantedWithFlags(int i, int i2) {
            if (i2 == KeyguardUpdateMonitor.getCurrentUser()) {
                boolean isVisibleToUser = ((KeyguardHostView) ((ViewController) KeyguardHostViewController.this).mView).isVisibleToUser();
                boolean z = true;
                boolean z2 = (i & 1) != 0;
                if ((i & 2) == 0) {
                    z = false;
                }
                if (!z2 && !z) {
                    return;
                }
                if (!KeyguardHostViewController.this.mViewMediatorCallback.isScreenOn() || (!isVisibleToUser && !z)) {
                    KeyguardHostViewController.this.mViewMediatorCallback.playTrustedSound();
                    return;
                }
                if (!isVisibleToUser) {
                    Log.i("KeyguardViewBase", "TrustAgent dismissed Keyguard.");
                }
                KeyguardHostViewController.this.mSecurityCallback.dismiss(false, i2, false);
            }
        }
    };
    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() { // from class: com.android.keyguard.KeyguardHostViewController$$ExternalSyntheticLambda0
        @Override // android.view.View.OnKeyListener
        public final boolean onKey(View view, int i, KeyEvent keyEvent) {
            return KeyguardHostViewController.m10$r8$lambda$JrjRYhNBUiyIPkhGa_AFPX2_a8(KeyguardHostViewController.this, view, i, keyEvent);
        }
    };

    public /* synthetic */ boolean lambda$new$0(View view, int i, KeyEvent keyEvent) {
        return interceptMediaKey(keyEvent);
    }

    public KeyguardHostViewController(KeyguardHostView keyguardHostView, KeyguardUpdateMonitor keyguardUpdateMonitor, AudioManager audioManager, TelephonyManager telephonyManager, ViewMediatorCallback viewMediatorCallback, KeyguardSecurityContainerController.Factory factory) {
        super(keyguardHostView);
        AnonymousClass2 r2 = new KeyguardSecurityContainer.SecurityCallback() { // from class: com.android.keyguard.KeyguardHostViewController.2
            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public boolean dismiss(boolean z, int i, boolean z2) {
                return KeyguardHostViewController.this.mKeyguardSecurityContainerController.showNextSecurityScreenOrFinish(z, i, z2);
            }

            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public void userActivity() {
                KeyguardHostViewController.this.mViewMediatorCallback.userActivity();
            }

            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public void onSecurityModeChanged(KeyguardSecurityModel.SecurityMode securityMode, boolean z) {
                KeyguardHostViewController.this.mViewMediatorCallback.setNeedsInput(z);
            }

            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public void finish(boolean z, int i) {
                boolean z2;
                if (KeyguardHostViewController.this.mDismissAction != null) {
                    z2 = KeyguardHostViewController.this.mDismissAction.onDismiss();
                    KeyguardHostViewController.this.mDismissAction = null;
                    KeyguardHostViewController.this.mCancelAction = null;
                } else {
                    z2 = false;
                }
                if (KeyguardHostViewController.this.mViewMediatorCallback == null) {
                    return;
                }
                if (z2) {
                    KeyguardHostViewController.this.mViewMediatorCallback.keyguardDonePending(z, i);
                } else {
                    KeyguardHostViewController.this.mViewMediatorCallback.keyguardDone(z, i);
                }
            }

            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public void reset() {
                KeyguardHostViewController.this.mViewMediatorCallback.resetKeyguard();
            }

            @Override // com.android.keyguard.KeyguardSecurityContainer.SecurityCallback
            public void onCancelClicked() {
                KeyguardHostViewController.this.mViewMediatorCallback.onCancelClicked();
            }
        };
        this.mSecurityCallback = r2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAudioManager = audioManager;
        this.mTelephonyManager = telephonyManager;
        this.mViewMediatorCallback = viewMediatorCallback;
        this.mKeyguardSecurityContainerController = factory.create(r2);
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mKeyguardSecurityContainerController.init();
        updateResources();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((KeyguardHostView) this.mView).setViewMediatorCallback(this.mViewMediatorCallback);
        this.mViewMediatorCallback.setNeedsInput(this.mKeyguardSecurityContainerController.needsInput());
        this.mKeyguardUpdateMonitor.registerCallback(this.mUpdateCallback);
        ((KeyguardHostView) this.mView).setOnKeyListener(this.mOnKeyListener);
        this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mUpdateCallback);
        ((KeyguardHostView) this.mView).setOnKeyListener(null);
    }

    public void cleanUp() {
        this.mKeyguardSecurityContainerController.onPause();
    }

    public void resetSecurityContainer() {
        this.mKeyguardSecurityContainerController.reset();
    }

    public boolean dismiss(int i) {
        return this.mSecurityCallback.dismiss(false, i, false);
    }

    public void onResume() {
        this.mKeyguardSecurityContainerController.onResume(1);
        ((KeyguardHostView) this.mView).requestFocus();
    }

    public CharSequence getAccessibilityTitleForCurrentMode() {
        return this.mKeyguardSecurityContainerController.getTitle();
    }

    public void appear(int i) {
        if (((KeyguardHostView) this.mView).getHeight() == 0 || ((KeyguardHostView) this.mView).getHeight() == i) {
            ((KeyguardHostView) this.mView).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.keyguard.KeyguardHostViewController.3
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    ((KeyguardHostView) ((ViewController) KeyguardHostViewController.this).mView).getViewTreeObserver().removeOnPreDrawListener(this);
                    KeyguardHostViewController.this.mKeyguardSecurityContainerController.startAppearAnimation();
                    return true;
                }
            });
            ((KeyguardHostView) this.mView).requestLayout();
            return;
        }
        this.mKeyguardSecurityContainerController.startAppearAnimation();
    }

    public void showPromptReason(int i) {
        this.mKeyguardSecurityContainerController.showPromptReason(i);
    }

    public void showMessage(CharSequence charSequence, ColorStateList colorStateList) {
        this.mKeyguardSecurityContainerController.showMessage(charSequence, colorStateList);
    }

    public void showErrorMessage(CharSequence charSequence) {
        showMessage(charSequence, Utils.getColorError(((KeyguardHostView) this.mView).getContext()));
    }

    public void setOnDismissAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        Runnable runnable2 = this.mCancelAction;
        if (runnable2 != null) {
            runnable2.run();
            this.mCancelAction = null;
        }
        this.mDismissAction = onDismissAction;
        this.mCancelAction = runnable;
    }

    public void cancelDismissAction() {
        setOnDismissAction(null, null);
    }

    public void startDisappearAnimation(Runnable runnable) {
        if (!this.mKeyguardSecurityContainerController.startDisappearAnimation(runnable) && runnable != null) {
            runnable.run();
        }
    }

    public void onPause() {
        this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(true);
        this.mKeyguardSecurityContainerController.onPause();
        ((KeyguardHostView) this.mView).clearFocus();
    }

    public void showPrimarySecurityScreen() {
        this.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
    }

    public void setExpansion(float f) {
        ((KeyguardHostView) this.mView).setAlpha(MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 1.0f, 0.0f, f), 0.0f, 1.0f));
        T t = this.mView;
        ((KeyguardHostView) t).setTranslationY(f * ((float) ((KeyguardHostView) t).getHeight()));
    }

    public void onStartingToHide() {
        this.mKeyguardSecurityContainerController.onStartingToHide();
    }

    public boolean hasDismissActions() {
        return (this.mDismissAction == null && this.mCancelAction == null) ? false : true;
    }

    public KeyguardSecurityModel.SecurityMode getCurrentSecurityMode() {
        return this.mKeyguardSecurityContainerController.getCurrentSecurityMode();
    }

    public boolean shouldEnableMenuKey() {
        return !((KeyguardHostView) this.mView).getResources().getBoolean(R$bool.config_disableMenuKeyInLockScreen) || ActivityManager.isRunningInTestHarness() || new File("/data/local/enable_menu_key").exists();
    }

    public boolean dispatchBackKeyEventPreIme() {
        return this.mKeyguardSecurityContainerController.getCurrentSecurityMode() == KeyguardSecurityModel.SecurityMode.Password;
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() == 0) {
            if (!(keyCode == 79 || keyCode == 130 || keyCode == 222)) {
                if (!(keyCode == 126 || keyCode == 127)) {
                    switch (keyCode) {
                        case 85:
                            break;
                        case 86:
                        case 87:
                        case 88:
                        case 89:
                        case R$styleable.Constraint_layout_constraintVertical_chainStyle:
                        case R$styleable.Constraint_layout_constraintVertical_weight:
                            break;
                        default:
                            return false;
                    }
                }
                TelephonyManager telephonyManager = this.mTelephonyManager;
                if (!(telephonyManager == null || telephonyManager.getCallState() == 0)) {
                    return true;
                }
            }
            handleMediaKeyEvent(keyEvent);
            return true;
        } else if (keyEvent.getAction() != 1) {
            return false;
        } else {
            if (!(keyCode == 79 || keyCode == 130 || keyCode == 222 || keyCode == 126 || keyCode == 127)) {
                switch (keyCode) {
                    case 85:
                    case 86:
                    case 87:
                    case 88:
                    case 89:
                    case R$styleable.Constraint_layout_constraintVertical_chainStyle:
                    case R$styleable.Constraint_layout_constraintVertical_weight:
                        break;
                    default:
                        return false;
                }
            }
            handleMediaKeyEvent(keyEvent);
            return true;
        }
    }

    private void handleMediaKeyEvent(KeyEvent keyEvent) {
        this.mAudioManager.dispatchMediaKeyEvent(keyEvent);
    }

    public void finish(boolean z, int i) {
        this.mSecurityCallback.finish(z, i);
    }

    public void updateResources() {
        int i;
        Resources resources = ((KeyguardHostView) this.mView).getResources();
        if (!resources.getBoolean(R$bool.can_use_one_handed_bouncer) || !resources.getBoolean(17891532)) {
            i = resources.getInteger(R$integer.keyguard_host_view_gravity);
        } else {
            i = resources.getInteger(R$integer.keyguard_host_view_one_handed_gravity);
        }
        if (((KeyguardHostView) this.mView).getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ((KeyguardHostView) this.mView).getLayoutParams();
            if (layoutParams.gravity != i) {
                layoutParams.gravity = i;
                ((KeyguardHostView) this.mView).setLayoutParams(layoutParams);
            }
        }
        KeyguardSecurityContainerController keyguardSecurityContainerController = this.mKeyguardSecurityContainerController;
        if (keyguardSecurityContainerController != null) {
            keyguardSecurityContainerController.updateResources();
        }
    }

    public void updateKeyguardPosition(float f) {
        KeyguardSecurityContainerController keyguardSecurityContainerController = this.mKeyguardSecurityContainerController;
        if (keyguardSecurityContainerController != null) {
            keyguardSecurityContainerController.updateKeyguardPosition(f);
        }
    }
}
