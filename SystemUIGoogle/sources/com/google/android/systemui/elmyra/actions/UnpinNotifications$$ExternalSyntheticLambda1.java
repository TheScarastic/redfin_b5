package com.google.android.systemui.elmyra.actions;

import com.android.systemui.statusbar.policy.HeadsUpManager;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class UnpinNotifications$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda1 INSTANCE = new UnpinNotifications$$ExternalSyntheticLambda1();

    private /* synthetic */ UnpinNotifications$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        UnpinNotifications.lambda$onTrigger$1((HeadsUpManager) obj);
    }
}
