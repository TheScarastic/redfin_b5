package com.android.keyguard;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.systemui.R$layout;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class KeyguardSecurityViewFlipperController extends ViewController<KeyguardSecurityViewFlipper> {
    private final List<KeyguardInputViewController<KeyguardInputView>> mChildren = new ArrayList();
    private final EmergencyButtonController.Factory mEmergencyButtonControllerFactory;
    private final KeyguardInputViewController.Factory mKeyguardSecurityViewControllerFactory;
    private final LayoutInflater mLayoutInflater;

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
    }

    /* access modifiers changed from: protected */
    public KeyguardSecurityViewFlipperController(KeyguardSecurityViewFlipper keyguardSecurityViewFlipper, LayoutInflater layoutInflater, KeyguardInputViewController.Factory factory, EmergencyButtonController.Factory factory2) {
        super(keyguardSecurityViewFlipper);
        this.mKeyguardSecurityViewControllerFactory = factory;
        this.mLayoutInflater = layoutInflater;
        this.mEmergencyButtonControllerFactory = factory2;
    }

    public void reset() {
        for (KeyguardInputViewController<KeyguardInputView> keyguardInputViewController : this.mChildren) {
            keyguardInputViewController.reset();
        }
    }

    public void reloadColors() {
        for (KeyguardInputViewController<KeyguardInputView> keyguardInputViewController : this.mChildren) {
            keyguardInputViewController.reloadColors();
        }
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public KeyguardInputViewController<KeyguardInputView> getSecurityView(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback) {
        KeyguardInputViewController keyguardInputViewController;
        int layoutIdFor;
        Iterator<KeyguardInputViewController<KeyguardInputView>> it = this.mChildren.iterator();
        while (true) {
            if (!it.hasNext()) {
                keyguardInputViewController = null;
                break;
            }
            keyguardInputViewController = it.next();
            if (keyguardInputViewController.getSecurityMode() == securityMode) {
                break;
            }
        }
        if (!(keyguardInputViewController != null || securityMode == KeyguardSecurityModel.SecurityMode.None || securityMode == KeyguardSecurityModel.SecurityMode.Invalid || (layoutIdFor = getLayoutIdFor(securityMode)) == 0)) {
            KeyguardInputView keyguardInputView = (KeyguardInputView) this.mLayoutInflater.inflate(layoutIdFor, (ViewGroup) this.mView, false);
            ((KeyguardSecurityViewFlipper) this.mView).addView(keyguardInputView);
            keyguardInputViewController = this.mKeyguardSecurityViewControllerFactory.create(keyguardInputView, securityMode, keyguardSecurityCallback);
            keyguardInputViewController.init();
            this.mChildren.add(keyguardInputViewController);
        }
        return keyguardInputViewController == null ? new NullKeyguardInputViewController(securityMode, keyguardSecurityCallback, this.mEmergencyButtonControllerFactory.create(null)) : keyguardInputViewController;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: com.android.keyguard.KeyguardSecurityViewFlipperController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode;

        static {
            int[] iArr = new int[KeyguardSecurityModel.SecurityMode.values().length];
            $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode = iArr;
            try {
                iArr[KeyguardSecurityModel.SecurityMode.Pattern.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.PIN.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[KeyguardSecurityModel.SecurityMode.Password.ordinal()] = 3;
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

    private int getLayoutIdFor(KeyguardSecurityModel.SecurityMode securityMode) {
        int i = AnonymousClass1.$SwitchMap$com$android$keyguard$KeyguardSecurityModel$SecurityMode[securityMode.ordinal()];
        if (i == 1) {
            return R$layout.keyguard_pattern_view;
        }
        if (i == 2) {
            return R$layout.keyguard_pin_view;
        }
        if (i == 3) {
            return R$layout.keyguard_password_view;
        }
        if (i == 4) {
            return R$layout.keyguard_sim_pin_view;
        }
        if (i != 5) {
            return 0;
        }
        return R$layout.keyguard_sim_puk_view;
    }

    public void show(KeyguardInputViewController<KeyguardInputView> keyguardInputViewController) {
        int indexIn = keyguardInputViewController.getIndexIn((KeyguardSecurityViewFlipper) this.mView);
        if (indexIn != -1) {
            ((KeyguardSecurityViewFlipper) this.mView).setDisplayedChild(indexIn);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class NullKeyguardInputViewController extends KeyguardInputViewController<KeyguardInputView> {
        @Override // com.android.keyguard.KeyguardSecurityView
        public boolean needsInput() {
            return false;
        }

        @Override // com.android.keyguard.KeyguardSecurityView
        public void onStartingToHide() {
        }

        protected NullKeyguardInputViewController(KeyguardSecurityModel.SecurityMode securityMode, KeyguardSecurityCallback keyguardSecurityCallback, EmergencyButtonController emergencyButtonController) {
            super(null, securityMode, keyguardSecurityCallback, emergencyButtonController);
        }
    }
}
