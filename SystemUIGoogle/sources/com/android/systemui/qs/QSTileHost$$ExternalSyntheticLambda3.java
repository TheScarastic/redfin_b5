package com.android.systemui.qs;

import com.android.systemui.statusbar.phone.StatusBar;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class QSTileHost$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ QSTileHost$$ExternalSyntheticLambda3 INSTANCE = new QSTileHost$$ExternalSyntheticLambda3();

    private /* synthetic */ QSTileHost$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((StatusBar) obj).postAnimateCollapsePanels();
    }
}
