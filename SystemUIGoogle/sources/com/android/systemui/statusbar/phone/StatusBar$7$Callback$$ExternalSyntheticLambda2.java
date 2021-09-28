package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.OverlayPlugin;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$7$Callback$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ StatusBar$7$Callback$$ExternalSyntheticLambda2(boolean z) {
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((OverlayPlugin) obj).setCollapseDesired(this.f$0);
    }
}
