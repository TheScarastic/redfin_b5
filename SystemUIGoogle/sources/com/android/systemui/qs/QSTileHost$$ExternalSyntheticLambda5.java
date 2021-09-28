package com.android.systemui.qs;

import com.android.systemui.statusbar.phone.StatusBar;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda5 implements Consumer {
    public static final /* synthetic */ QSTileHost$$ExternalSyntheticLambda5 INSTANCE = new QSTileHost$$ExternalSyntheticLambda5();

    private /* synthetic */ QSTileHost$$ExternalSyntheticLambda5() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((StatusBar) obj).postAnimateOpenPanels();
    }
}
