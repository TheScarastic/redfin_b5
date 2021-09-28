package com.android.systemui.statusbar.phone;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
/* loaded from: classes.dex */
public class TapAgainViewController extends ViewController<TapAgainView> {
    private final ConfigurationController mConfigurationController;
    @VisibleForTesting
    final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.TapAgainViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onOverlayChanged() {
            ((TapAgainView) ((ViewController) TapAgainViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onUiModeChanged() {
            ((TapAgainView) ((ViewController) TapAgainViewController.this).mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onThemeChanged() {
            ((TapAgainView) ((ViewController) TapAgainViewController.this).mView).updateColor();
        }
    };
    private final DelayableExecutor mDelayableExecutor;
    private final long mDoubleTapTimeMs;
    private Runnable mHideCanceler;

    /* access modifiers changed from: protected */
    public TapAgainViewController(TapAgainView tapAgainView, DelayableExecutor delayableExecutor, ConfigurationController configurationController, long j) {
        super(tapAgainView);
        this.mDelayableExecutor = delayableExecutor;
        this.mConfigurationController = configurationController;
        this.mDoubleTapTimeMs = j;
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public void show() {
        Runnable runnable = this.mHideCanceler;
        if (runnable != null) {
            runnable.run();
        }
        ((TapAgainView) this.mView).animateIn();
        this.mHideCanceler = this.mDelayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.phone.TapAgainViewController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TapAgainViewController.this.hide();
            }
        }, this.mDoubleTapTimeMs);
    }

    public void hide() {
        this.mHideCanceler = null;
        ((TapAgainView) this.mView).animateOut();
    }
}
