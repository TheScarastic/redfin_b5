package com.android.systemui.statusbar.notification;

import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ViewGroupFadeHelper.kt */
/* loaded from: classes.dex */
final class ViewGroupFadeHelper$Companion$visibilityIncluder$1 extends Lambda implements Function1<View, Boolean> {
    public static final ViewGroupFadeHelper$Companion$visibilityIncluder$1 INSTANCE = new ViewGroupFadeHelper$Companion$visibilityIncluder$1();

    ViewGroupFadeHelper$Companion$visibilityIncluder$1() {
        super(1);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Boolean invoke(View view) {
        return Boolean.valueOf(invoke(view));
    }

    public final boolean invoke(View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        return view.getVisibility() == 0;
    }
}
