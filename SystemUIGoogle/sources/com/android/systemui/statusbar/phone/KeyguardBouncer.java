package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.os.UserManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.WindowInsets;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardRootViewController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class KeyguardBouncer {
    private int mBouncerPromptReason;
    protected final ViewMediatorCallback mCallback;
    protected final ViewGroup mContainer;
    protected final Context mContext;
    private final DismissCallbackRegistry mDismissCallbackRegistry;
    private float mExpansion;
    private final List<BouncerExpansionCallback> mExpansionCallbacks;
    private final FalsingCollector mFalsingCollector;
    private final Handler mHandler;
    private boolean mIsAnimatingAway;
    private boolean mIsScrimmed;
    private final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
    private final KeyguardBypassController mKeyguardBypassController;
    private final KeyguardSecurityModel mKeyguardSecurityModel;
    private final KeyguardStateController mKeyguardStateController;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    private KeyguardHostViewController mKeyguardViewController;
    private final Runnable mRemoveViewRunnable;
    private final List<KeyguardResetCallback> mResetCallbacks;
    private final Runnable mResetRunnable;
    protected ViewGroup mRoot;
    private KeyguardRootViewController mRootViewController;
    private final Runnable mShowRunnable;
    private boolean mShowingSoon;
    private int mStatusBarHeight;
    private final KeyguardUpdateMonitorCallback mUpdateMonitorCallback;

    /* loaded from: classes.dex */
    public interface BouncerExpansionCallback {
        default void onExpansionChanged(float f) {
        }

        void onFullyHidden();

        void onFullyShown();

        void onStartingToHide();

        void onStartingToShow();

        default void onVisibilityChanged(boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public interface KeyguardResetCallback {
        void onKeyguardReset();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.resetSecurityContainer();
            Iterator it = new ArrayList(this.mResetCallbacks).iterator();
            while (it.hasNext()) {
                ((KeyguardResetCallback) it.next()).onKeyguardReset();
            }
        }
    }

    private KeyguardBouncer(Context context, ViewMediatorCallback viewMediatorCallback, ViewGroup viewGroup, DismissCallbackRegistry dismissCallbackRegistry, FalsingCollector falsingCollector, BouncerExpansionCallback bouncerExpansionCallback, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, Handler handler, KeyguardSecurityModel keyguardSecurityModel, KeyguardBouncerComponent.Factory factory) {
        ArrayList arrayList = new ArrayList();
        this.mExpansionCallbacks = arrayList;
        AnonymousClass1 r1 = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.KeyguardBouncer.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onStrongAuthStateChanged(int i) {
                KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
                keyguardBouncer.mBouncerPromptReason = keyguardBouncer.mCallback.getBouncerPromptReason();
            }
        };
        this.mUpdateMonitorCallback = r1;
        this.mRemoveViewRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBouncer$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardBouncer.this.removeView();
            }
        };
        this.mResetCallbacks = new ArrayList();
        this.mResetRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBouncer$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardBouncer.m325$r8$lambda$pzR4i9iPEUQESwC4brdBLVRbiA(KeyguardBouncer.this);
            }
        };
        this.mExpansion = 1.0f;
        this.mShowRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBouncer.2
            @Override // java.lang.Runnable
            public void run() {
                KeyguardBouncer.this.setVisibility(0);
                KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
                keyguardBouncer.showPromptReason(keyguardBouncer.mBouncerPromptReason);
                CharSequence consumeCustomMessage = KeyguardBouncer.this.mCallback.consumeCustomMessage();
                if (consumeCustomMessage != null) {
                    KeyguardBouncer.this.mKeyguardViewController.showErrorMessage(consumeCustomMessage);
                }
                KeyguardBouncer.this.mKeyguardViewController.appear(KeyguardBouncer.this.mStatusBarHeight);
                KeyguardBouncer.this.mShowingSoon = false;
                if (KeyguardBouncer.this.mExpansion == 0.0f) {
                    KeyguardBouncer.this.mKeyguardViewController.onResume();
                    KeyguardBouncer.this.mKeyguardViewController.resetSecurityContainer();
                    KeyguardBouncer keyguardBouncer2 = KeyguardBouncer.this;
                    keyguardBouncer2.showPromptReason(keyguardBouncer2.mBouncerPromptReason);
                }
                SysUiStatsLog.write(63, 2);
            }
        };
        this.mContext = context;
        this.mCallback = viewMediatorCallback;
        this.mContainer = viewGroup;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mFalsingCollector = falsingCollector;
        this.mDismissCallbackRegistry = dismissCallbackRegistry;
        this.mHandler = handler;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardSecurityModel = keyguardSecurityModel;
        this.mKeyguardBouncerComponentFactory = factory;
        keyguardUpdateMonitor.registerCallback(r1);
        this.mKeyguardBypassController = keyguardBypassController;
        arrayList.add(bouncerExpansionCallback);
    }

    public void show(boolean z) {
        show(z, true);
    }

    public void show(boolean z, boolean z2) {
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        if (currentUser != 0 || !UserManager.isSplitSystemUser()) {
            ensureView();
            this.mIsScrimmed = z2;
            if (z2) {
                setExpansion(0.0f);
            }
            if (z) {
                showPrimarySecurityScreen();
            }
            if (this.mRoot.getVisibility() != 0 && !this.mShowingSoon) {
                int currentUser2 = KeyguardUpdateMonitor.getCurrentUser();
                boolean z3 = false;
                if (!(UserManager.isSplitSystemUser() && currentUser2 == 0) && currentUser2 == currentUser) {
                    z3 = true;
                }
                if (!z3 || !this.mKeyguardViewController.dismiss(currentUser2)) {
                    if (!z3) {
                        Log.w("KeyguardBouncer", "User can't dismiss keyguard: " + currentUser2 + " != " + currentUser);
                    }
                    this.mShowingSoon = true;
                    DejankUtils.removeCallbacks(this.mResetRunnable);
                    if (!this.mKeyguardStateController.isFaceAuthEnabled() || needsFullscreenBouncer() || this.mKeyguardUpdateMonitor.userNeedsStrongAuth() || this.mKeyguardBypassController.getBypassEnabled()) {
                        DejankUtils.postAfterTraversal(this.mShowRunnable);
                    } else {
                        this.mHandler.postDelayed(this.mShowRunnable, 1200);
                    }
                    this.mCallback.onBouncerVisiblityChanged(true);
                    dispatchStartingToShow();
                }
            }
        }
    }

    public boolean isScrimmed() {
        return this.mIsScrimmed;
    }

    private void onFullyShown() {
        this.mFalsingCollector.onBouncerShown();
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController == null) {
            Log.wtf("KeyguardBouncer", "onFullyShown when view was null");
            return;
        }
        keyguardHostViewController.onResume();
        ViewGroup viewGroup = this.mRoot;
        if (viewGroup != null) {
            viewGroup.announceForAccessibility(this.mKeyguardViewController.getAccessibilityTitleForCurrentMode());
        }
    }

    private void onFullyHidden() {
        cancelShowRunnable();
        setVisibility(4);
        this.mFalsingCollector.onBouncerHidden();
        DejankUtils.postAfterTraversal(this.mResetRunnable);
    }

    /* access modifiers changed from: private */
    public void setVisibility(int i) {
        ViewGroup viewGroup = this.mRoot;
        if (viewGroup != null) {
            viewGroup.setVisibility(i);
            dispatchVisibilityChanged();
        }
    }

    public void showPromptReason(int i) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.showPromptReason(i);
        } else {
            Log.w("KeyguardBouncer", "Trying to show prompt reason on empty bouncer");
        }
    }

    public void showMessage(String str, ColorStateList colorStateList) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.showMessage(str, colorStateList);
        } else {
            Log.w("KeyguardBouncer", "Trying to show message on empty bouncer");
        }
    }

    private void cancelShowRunnable() {
        DejankUtils.removeCallbacks(this.mShowRunnable);
        this.mHandler.removeCallbacks(this.mShowRunnable);
        this.mShowingSoon = false;
    }

    public void showWithDismissAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        ensureView();
        setDismissAction(onDismissAction, runnable);
        show(false);
    }

    public void setDismissAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable) {
        this.mKeyguardViewController.setOnDismissAction(onDismissAction, runnable);
    }

    public void hide(boolean z) {
        if (isShowing()) {
            SysUiStatsLog.write(63, 1);
            this.mDismissCallbackRegistry.notifyDismissCancelled();
        }
        this.mIsScrimmed = false;
        this.mFalsingCollector.onBouncerHidden();
        this.mCallback.onBouncerVisiblityChanged(false);
        cancelShowRunnable();
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.cancelDismissAction();
            this.mKeyguardViewController.cleanUp();
        }
        this.mIsAnimatingAway = false;
        if (this.mRoot != null) {
            setVisibility(4);
            if (z) {
                this.mHandler.postDelayed(this.mRemoveViewRunnable, 50);
            }
        }
    }

    public void startPreHideAnimation(Runnable runnable) {
        this.mIsAnimatingAway = true;
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.startDisappearAnimation(runnable);
        } else if (runnable != null) {
            runnable.run();
        }
    }

    public void onScreenTurnedOff() {
        ViewGroup viewGroup;
        if (this.mKeyguardViewController != null && (viewGroup = this.mRoot) != null && viewGroup.getVisibility() == 0) {
            this.mKeyguardViewController.onPause();
        }
    }

    public boolean isShowing() {
        ViewGroup viewGroup;
        return (this.mShowingSoon || ((viewGroup = this.mRoot) != null && viewGroup.getVisibility() == 0)) && this.mExpansion == 0.0f && !isAnimatingAway();
    }

    public boolean getShowingSoon() {
        return this.mShowingSoon;
    }

    public boolean isAnimatingAway() {
        return this.mIsAnimatingAway;
    }

    public void prepare() {
        boolean z = this.mRoot != null;
        ensureView();
        if (z) {
            showPrimarySecurityScreen();
        }
        this.mBouncerPromptReason = this.mCallback.getBouncerPromptReason();
    }

    private void showPrimarySecurityScreen() {
        this.mKeyguardViewController.showPrimarySecurityScreen();
    }

    public void setExpansion(float f) {
        float f2 = this.mExpansion;
        boolean z = f2 != f;
        this.mExpansion = f;
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null && !this.mIsAnimatingAway) {
            keyguardHostViewController.setExpansion(f);
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0 && f2 != 0.0f) {
            onFullyShown();
            dispatchFullyShown();
        } else if (f == 1.0f && f2 != 1.0f) {
            onFullyHidden();
            dispatchFullyHidden();
        } else if (i != 0 && f2 == 0.0f) {
            dispatchStartingToHide();
            KeyguardHostViewController keyguardHostViewController2 = this.mKeyguardViewController;
            if (keyguardHostViewController2 != null) {
                keyguardHostViewController2.onStartingToHide();
            }
        }
        if (z) {
            dispatchExpansionChanged();
        }
    }

    public boolean willDismissWithAction() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        return keyguardHostViewController != null && keyguardHostViewController.hasDismissActions();
    }

    protected void ensureView() {
        boolean hasCallbacks = this.mHandler.hasCallbacks(this.mRemoveViewRunnable);
        if (this.mRoot == null || hasCallbacks) {
            inflateView();
        }
    }

    protected void inflateView() {
        removeView();
        this.mHandler.removeCallbacks(this.mRemoveViewRunnable);
        KeyguardBouncerComponent create = this.mKeyguardBouncerComponentFactory.create();
        KeyguardRootViewController keyguardRootViewController = create.getKeyguardRootViewController();
        this.mRootViewController = keyguardRootViewController;
        keyguardRootViewController.init();
        this.mRoot = this.mRootViewController.getView();
        KeyguardHostViewController keyguardHostViewController = create.getKeyguardHostViewController();
        this.mKeyguardViewController = keyguardHostViewController;
        keyguardHostViewController.init();
        ViewGroup viewGroup = this.mContainer;
        viewGroup.addView(this.mRoot, viewGroup.getChildCount());
        this.mStatusBarHeight = this.mRoot.getResources().getDimensionPixelOffset(R$dimen.status_bar_height);
        setVisibility(4);
        WindowInsets rootWindowInsets = this.mRoot.getRootWindowInsets();
        if (rootWindowInsets != null) {
            this.mRoot.dispatchApplyWindowInsets(rootWindowInsets);
        }
    }

    /* access modifiers changed from: protected */
    public void removeView() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2 = this.mRoot;
        if (viewGroup2 != null && viewGroup2.getParent() == (viewGroup = this.mContainer)) {
            viewGroup.removeView(this.mRoot);
            this.mRoot = null;
        }
    }

    public boolean needsFullscreenBouncer() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mKeyguardSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
        return securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk;
    }

    public boolean isFullscreenBouncer() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController == null) {
            return false;
        }
        KeyguardSecurityModel.SecurityMode currentSecurityMode = keyguardHostViewController.getCurrentSecurityMode();
        if (currentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPin || currentSecurityMode == KeyguardSecurityModel.SecurityMode.SimPuk) {
            return true;
        }
        return false;
    }

    public boolean isSecure() {
        return this.mKeyguardSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser()) != KeyguardSecurityModel.SecurityMode.None;
    }

    public boolean shouldDismissOnMenuPressed() {
        return this.mKeyguardViewController.shouldEnableMenuKey();
    }

    public boolean interceptMediaKey(KeyEvent keyEvent) {
        ensureView();
        return this.mKeyguardViewController.interceptMediaKey(keyEvent);
    }

    public boolean dispatchBackKeyEventPreIme() {
        ensureView();
        return this.mKeyguardViewController.dispatchBackKeyEventPreIme();
    }

    public void notifyKeyguardAuthenticated(boolean z) {
        ensureView();
        this.mKeyguardViewController.finish(z, KeyguardUpdateMonitor.getCurrentUser());
    }

    private void dispatchFullyShown() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onFullyShown();
        }
    }

    private void dispatchStartingToHide() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onStartingToHide();
        }
    }

    private void dispatchStartingToShow() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onStartingToShow();
        }
    }

    private void dispatchFullyHidden() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onFullyHidden();
        }
    }

    private void dispatchExpansionChanged() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onExpansionChanged(this.mExpansion);
        }
    }

    private void dispatchVisibilityChanged() {
        for (BouncerExpansionCallback bouncerExpansionCallback : this.mExpansionCallbacks) {
            bouncerExpansionCallback.onVisibilityChanged(this.mRoot.getVisibility() == 0);
        }
    }

    public void updateResources() {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.updateResources();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("KeyguardBouncer");
        printWriter.println("  isShowing(): " + isShowing());
        printWriter.println("  mStatusBarHeight: " + this.mStatusBarHeight);
        printWriter.println("  mExpansion: " + this.mExpansion);
        printWriter.println("  mKeyguardViewController; " + this.mKeyguardViewController);
        printWriter.println("  mShowingSoon: " + this.mShowingSoon);
        printWriter.println("  mBouncerPromptReason: " + this.mBouncerPromptReason);
        printWriter.println("  mIsAnimatingAway: " + this.mIsAnimatingAway);
    }

    public void updateKeyguardPosition(float f) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            keyguardHostViewController.updateKeyguardPosition(f);
        }
    }

    public void addKeyguardResetCallback(KeyguardResetCallback keyguardResetCallback) {
        this.mResetCallbacks.add(keyguardResetCallback);
    }

    public void removeKeyguardResetCallback(KeyguardResetCallback keyguardResetCallback) {
        this.mResetCallbacks.remove(keyguardResetCallback);
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final ViewMediatorCallback mCallback;
        private final Context mContext;
        private final DismissCallbackRegistry mDismissCallbackRegistry;
        private final FalsingCollector mFalsingCollector;
        private final Handler mHandler;
        private final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
        private final KeyguardBypassController mKeyguardBypassController;
        private final KeyguardSecurityModel mKeyguardSecurityModel;
        private final KeyguardStateController mKeyguardStateController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        public Factory(Context context, ViewMediatorCallback viewMediatorCallback, DismissCallbackRegistry dismissCallbackRegistry, FalsingCollector falsingCollector, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, Handler handler, KeyguardSecurityModel keyguardSecurityModel, KeyguardBouncerComponent.Factory factory) {
            this.mContext = context;
            this.mCallback = viewMediatorCallback;
            this.mDismissCallbackRegistry = dismissCallbackRegistry;
            this.mFalsingCollector = falsingCollector;
            this.mKeyguardStateController = keyguardStateController;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardBypassController = keyguardBypassController;
            this.mHandler = handler;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mKeyguardBouncerComponentFactory = factory;
        }

        public KeyguardBouncer create(ViewGroup viewGroup, BouncerExpansionCallback bouncerExpansionCallback) {
            return new KeyguardBouncer(this.mContext, this.mCallback, viewGroup, this.mDismissCallbackRegistry, this.mFalsingCollector, bouncerExpansionCallback, this.mKeyguardStateController, this.mKeyguardUpdateMonitor, this.mKeyguardBypassController, this.mHandler, this.mKeyguardSecurityModel, this.mKeyguardBouncerComponentFactory);
        }
    }
}
