package com.android.systemui.settings.brightness;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.FrameLayout;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.settings.brightness.BrightnessSlider;
/* loaded from: classes.dex */
public class BrightnessDialog extends Activity {
    private BrightnessController mBrightnessController;
    private final BroadcastDispatcher mBroadcastDispatcher;
    private final BrightnessSlider.Factory mToggleSliderFactory;

    public BrightnessDialog(BroadcastDispatcher broadcastDispatcher, BrightnessSlider.Factory factory) {
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mToggleSliderFactory = factory;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.setGravity(49);
        window.clearFlags(2);
        window.requestFeature(1);
        window.getDecorView();
        window.setLayout(-1, -2);
        setContentView(R$layout.brightness_mirror_container);
        FrameLayout frameLayout = (FrameLayout) findViewById(R$id.brightness_mirror_container);
        frameLayout.setVisibility(0);
        BrightnessSlider create = this.mToggleSliderFactory.create(this, frameLayout);
        create.init();
        frameLayout.addView(create.getRootView(), -1, -2);
        this.mBrightnessController = new BrightnessController(this, create, this.mBroadcastDispatcher);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.mBrightnessController.registerCallbacks();
        MetricsLogger.visible(this, 220);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        MetricsLogger.hidden(this, 220);
        this.mBrightnessController.unregisterCallbacks();
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 25 || i == 24 || i == 164) {
            finish();
        }
        return super.onKeyDown(i, keyEvent);
    }
}
