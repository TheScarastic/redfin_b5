package com.android.systemui.qs;

import com.android.systemui.statusbar.phone.StatusBar;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda4 implements Consumer {
    public static final /* synthetic */ QSTileHost$$ExternalSyntheticLambda4 INSTANCE = new QSTileHost$$ExternalSyntheticLambda4();

    private /* synthetic */ QSTileHost$$ExternalSyntheticLambda4() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((StatusBar) obj).postAnimateForceCollapsePanels();
    }
}
