package com.android.systemui.util.animation;

import android.view.View;
import com.android.systemui.R$id;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UniqueObjectHostView.kt */
/* loaded from: classes2.dex */
public final class UniqueObjectHostViewKt {
    public static final boolean getRequiresRemeasuring(View view) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        Object tag = view.getTag(R$id.requires_remeasuring);
        if (tag == null) {
            return false;
        }
        return tag.equals(Boolean.TRUE);
    }

    public static final void setRequiresRemeasuring(View view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "<this>");
        view.setTag(R$id.requires_remeasuring, Boolean.valueOf(z));
    }
}
