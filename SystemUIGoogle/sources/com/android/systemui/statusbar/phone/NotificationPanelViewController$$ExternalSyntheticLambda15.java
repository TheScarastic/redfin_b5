package com.android.systemui.statusbar.phone;

import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationPanelViewController$$ExternalSyntheticLambda15 implements Function {
    public static final /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda15 INSTANCE = new NotificationPanelViewController$$ExternalSyntheticLambda15();

    private /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda15() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Float.valueOf(((NotificationPanelView) obj).getCurrentPanelAlpha());
    }
}
