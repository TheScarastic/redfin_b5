package com.android.systemui.statusbar.phone;

import java.lang.ref.WeakReference;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1 INSTANCE = new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1();

    private /* synthetic */ NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return (StatusBarWindowCallback) ((WeakReference) obj).get();
    }
}
