package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.os.UserHandle;
import android.text.TextUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
/* loaded from: classes.dex */
public class AccessibilityFloatingMenuController implements AccessibilityButtonModeObserver.ModeChangedListener, AccessibilityButtonTargetsObserver.TargetsChangedListener {
    private final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    private final AccessibilityButtonTargetsObserver mAccessibilityButtonTargetsObserver;
    private int mBtnMode;
    private String mBtnTargets;
    private Context mContext;
    @VisibleForTesting
    IAccessibilityFloatingMenu mFloatingMenu;
    private boolean mIsAccessibilityManagerServiceReady;
    private boolean mIsKeyguardVisible;
    @VisibleForTesting
    final KeyguardUpdateMonitorCallback mKeyguardCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserUnlocked() {
            AccessibilityFloatingMenuController.this.mIsAccessibilityManagerServiceReady = true;
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.handleFloatingMenuVisibility(accessibilityFloatingMenuController.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onKeyguardVisibilityChanged(boolean z) {
            AccessibilityFloatingMenuController.this.mIsKeyguardVisible = z;
            if (AccessibilityFloatingMenuController.this.mIsAccessibilityManagerServiceReady) {
                AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
                accessibilityFloatingMenuController.handleFloatingMenuVisibility(accessibilityFloatingMenuController.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitching(int i) {
            AccessibilityFloatingMenuController.this.destroyFloatingMenu();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public void onUserSwitchComplete(int i) {
            AccessibilityFloatingMenuController accessibilityFloatingMenuController = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController.mContext = accessibilityFloatingMenuController.mContext.createContextAsUser(UserHandle.of(i), 0);
            AccessibilityFloatingMenuController accessibilityFloatingMenuController2 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController2.mBtnMode = accessibilityFloatingMenuController2.mAccessibilityButtonModeObserver.getCurrentAccessibilityButtonMode();
            AccessibilityFloatingMenuController accessibilityFloatingMenuController3 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController3.mBtnTargets = accessibilityFloatingMenuController3.mAccessibilityButtonTargetsObserver.getCurrentAccessibilityButtonTargets();
            AccessibilityFloatingMenuController accessibilityFloatingMenuController4 = AccessibilityFloatingMenuController.this;
            accessibilityFloatingMenuController4.handleFloatingMenuVisibility(accessibilityFloatingMenuController4.mIsKeyguardVisible, AccessibilityFloatingMenuController.this.mBtnMode, AccessibilityFloatingMenuController.this.mBtnTargets);
        }
    };
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    public AccessibilityFloatingMenuController(Context context, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, AccessibilityButtonModeObserver accessibilityButtonModeObserver, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mContext = context;
        this.mAccessibilityButtonTargetsObserver = accessibilityButtonTargetsObserver;
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        init();
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonModeObserver.ModeChangedListener
    public void onAccessibilityButtonModeChanged(int i) {
        this.mBtnMode = i;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, i, this.mBtnTargets);
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonTargetsObserver.TargetsChangedListener
    public void onAccessibilityButtonTargetsChanged(String str) {
        this.mBtnTargets = str;
        handleFloatingMenuVisibility(this.mIsKeyguardVisible, this.mBtnMode, str);
    }

    private void init() {
        this.mIsKeyguardVisible = false;
        this.mIsAccessibilityManagerServiceReady = false;
        this.mBtnMode = this.mAccessibilityButtonModeObserver.getCurrentAccessibilityButtonMode();
        this.mBtnTargets = this.mAccessibilityButtonTargetsObserver.getCurrentAccessibilityButtonTargets();
        registerContentObservers();
    }

    private void registerContentObservers() {
        this.mAccessibilityButtonModeObserver.addListener(this);
        this.mAccessibilityButtonTargetsObserver.addListener(this);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardCallback);
    }

    /* access modifiers changed from: private */
    public void handleFloatingMenuVisibility(boolean z, int i, String str) {
        if (z) {
            destroyFloatingMenu();
        } else if (shouldShowFloatingMenu(i, str)) {
            showFloatingMenu();
        } else {
            destroyFloatingMenu();
        }
    }

    private boolean shouldShowFloatingMenu(int i, String str) {
        return i == 1 && !TextUtils.isEmpty(str);
    }

    private void showFloatingMenu() {
        if (this.mFloatingMenu == null) {
            this.mFloatingMenu = new AccessibilityFloatingMenu(this.mContext);
        }
        this.mFloatingMenu.show();
    }

    /* access modifiers changed from: private */
    public void destroyFloatingMenu() {
        IAccessibilityFloatingMenu iAccessibilityFloatingMenu = this.mFloatingMenu;
        if (iAccessibilityFloatingMenu != null) {
            iAccessibilityFloatingMenu.hide();
            this.mFloatingMenu = null;
        }
    }
}
