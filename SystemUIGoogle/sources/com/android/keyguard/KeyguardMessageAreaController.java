package com.android.keyguard;

import android.content.res.ColorStateList;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class KeyguardMessageAreaController extends ViewController<KeyguardMessageArea> {
    private final ConfigurationController mConfigurationController;
    private ConfigurationController.ConfigurationListener mConfigurationListener;
    private KeyguardUpdateMonitorCallback mInfoCallback;
    private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

    private KeyguardMessageAreaController(KeyguardMessageArea keyguardMessageArea, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
        super(keyguardMessageArea);
        this.mInfoCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardMessageAreaController.1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onFinishedGoingToSleep(int i) {
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).setSelected(false);
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onStartedWakingUp() {
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).setSelected(true);
            }

            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onKeyguardBouncerChanged(boolean z) {
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).setBouncerVisible(z);
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).update();
            }
        };
        this.mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardMessageAreaController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onThemeChanged() {
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).onThemeChanged();
            }

            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public void onDensityOrFontScaleChanged() {
                ((KeyguardMessageArea) ((ViewController) KeyguardMessageAreaController.this).mView).onDensityOrFontScaleChanged();
            }
        };
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        ((KeyguardMessageArea) this.mView).setSelected(this.mKeyguardUpdateMonitor.isDeviceInteractive());
        ((KeyguardMessageArea) this.mView).onThemeChanged();
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
    }

    public void setAltBouncerShowing(boolean z) {
        ((KeyguardMessageArea) this.mView).setAltBouncerShowing(z);
    }

    public void setMessage(CharSequence charSequence) {
        ((KeyguardMessageArea) this.mView).setMessage(charSequence);
    }

    public void setMessage(int i) {
        ((KeyguardMessageArea) this.mView).setMessage(i);
    }

    public void setNextMessageColor(ColorStateList colorStateList) {
        ((KeyguardMessageArea) this.mView).setNextMessageColor(colorStateList);
    }

    public void reloadColors() {
        ((KeyguardMessageArea) this.mView).reloadColor();
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final ConfigurationController mConfigurationController;
        private final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mConfigurationController = configurationController;
        }

        public KeyguardMessageAreaController create(KeyguardMessageArea keyguardMessageArea) {
            return new KeyguardMessageAreaController(keyguardMessageArea, this.mKeyguardUpdateMonitor, this.mConfigurationController);
        }
    }
}
