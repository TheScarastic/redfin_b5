package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserAction.kt */
/* loaded from: classes2.dex */
public abstract class UserAction extends Action {
    public boolean availableOnLockscreen() {
        return false;
    }

    public boolean availableOnScreenOff() {
        return false;
    }

    public /* synthetic */ UserAction(Context context, Set set, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? null : set);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public UserAction(Context context, Set<? extends FeedbackEffect> set) {
        super(context, set);
        Intrinsics.checkNotNullParameter(context, "context");
    }
}
