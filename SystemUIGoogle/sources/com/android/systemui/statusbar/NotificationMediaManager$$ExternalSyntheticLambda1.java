package com.android.systemui.statusbar;

import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationMediaManager$$ExternalSyntheticLambda1 implements Function {
    public static final /* synthetic */ NotificationMediaManager$$ExternalSyntheticLambda1 INSTANCE = new NotificationMediaManager$$ExternalSyntheticLambda1();

    private /* synthetic */ NotificationMediaManager$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((StatusBarIconView) obj).getSourceIcon();
    }
}
