package com.android.systemui.controls.management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.qs.PageIndicator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ManagementPageIndicator.kt */
/* loaded from: classes.dex */
public final class ManagementPageIndicator extends PageIndicator {
    private Function1<? super Integer, Unit> visibilityListener = ManagementPageIndicator$visibilityListener$1.INSTANCE;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ManagementPageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attributeSet, "attrs");
    }

    @Override // com.android.systemui.qs.PageIndicator
    public void setLocation(float f) {
        if (getLayoutDirection() == 1) {
            super.setLocation(((float) (getChildCount() - 1)) - f);
        } else {
            super.setLocation(f);
        }
    }

    public final void setVisibilityListener(Function1<? super Integer, Unit> function1) {
        Intrinsics.checkNotNullParameter(function1, "<set-?>");
        this.visibilityListener = function1;
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i) {
        Intrinsics.checkNotNullParameter(view, "changedView");
        super.onVisibilityChanged(view, i);
        if (Intrinsics.areEqual(view, this)) {
            this.visibilityListener.invoke(Integer.valueOf(i));
        }
    }
}
