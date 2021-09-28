package com.android.systemui.accessibility;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;
import com.android.internal.annotations.VisibleForTesting;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ModeSwitchesController {
    private final DisplayIdIndexSupplier<MagnificationModeSwitch> mSwitchSupplier;

    public ModeSwitchesController(Context context) {
        this.mSwitchSupplier = new SwitchSupplier(context, (DisplayManager) context.getSystemService(DisplayManager.class));
    }

    @VisibleForTesting
    ModeSwitchesController(DisplayIdIndexSupplier<MagnificationModeSwitch> displayIdIndexSupplier) {
        this.mSwitchSupplier = displayIdIndexSupplier;
    }

    /* access modifiers changed from: package-private */
    public void showButton(int i, int i2) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.showButton(i2);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeButton(int i) {
        MagnificationModeSwitch magnificationModeSwitch = this.mSwitchSupplier.get(i);
        if (magnificationModeSwitch != null) {
            magnificationModeSwitch.lambda$new$2();
        }
    }

    /* access modifiers changed from: package-private */
    public void onConfigurationChanged(int i) {
        this.mSwitchSupplier.forEach(new Consumer(i) { // from class: com.android.systemui.accessibility.ModeSwitchesController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((MagnificationModeSwitch) obj).onConfigurationChanged(this.f$0);
            }
        });
    }

    /* loaded from: classes.dex */
    private static class SwitchSupplier extends DisplayIdIndexSupplier<MagnificationModeSwitch> {
        private final Context mContext;

        SwitchSupplier(Context context, DisplayManager displayManager) {
            super(displayManager);
            this.mContext = context;
        }

        /* access modifiers changed from: protected */
        @Override // com.android.systemui.accessibility.DisplayIdIndexSupplier
        public MagnificationModeSwitch createInstance(Display display) {
            return new MagnificationModeSwitch(this.mContext.createWindowContext(display, 2039, null));
        }
    }
}
