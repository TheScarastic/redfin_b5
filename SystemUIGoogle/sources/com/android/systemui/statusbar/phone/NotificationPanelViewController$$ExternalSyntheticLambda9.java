package com.android.systemui.statusbar.phone;

import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationPanelViewController$$ExternalSyntheticLambda9 implements BiConsumer {
    public static final /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda9 INSTANCE = new NotificationPanelViewController$$ExternalSyntheticLambda9();

    private /* synthetic */ NotificationPanelViewController$$ExternalSyntheticLambda9() {
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        ((NotificationPanelView) obj).setPanelAlphaInternal(((Float) obj2).floatValue());
    }
}
