package com.android.systemui.media;

import android.view.View;
import android.view.ViewParent;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaHierarchyManager.kt */
/* loaded from: classes.dex */
public final class MediaHierarchyManagerKt {
    public static final boolean isShownNotFaded(View view) {
        ViewParent parent;
        Intrinsics.checkNotNullParameter(view, "<this>");
        while (view.getVisibility() == 0) {
            if ((view.getAlpha() == 0.0f) || (parent = view.getParent()) == null) {
                return false;
            }
            if (!(parent instanceof View)) {
                return true;
            }
            view = (View) parent;
        }
        return false;
    }
}
