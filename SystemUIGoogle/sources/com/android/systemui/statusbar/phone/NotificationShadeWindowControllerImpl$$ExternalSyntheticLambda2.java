package com.android.systemui.statusbar.phone;

import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2 implements Predicate {
    public static final /* synthetic */ NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2 INSTANCE = new NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2();

    private /* synthetic */ NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda2() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return Objects.nonNull((StatusBarWindowCallback) obj);
    }
}
