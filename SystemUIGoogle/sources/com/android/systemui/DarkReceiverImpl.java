package com.android.systemui;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.plugins.DarkIconDispatcher;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DarkReceiverImpl.kt */
/* loaded from: classes.dex */
public final class DarkReceiverImpl extends View implements DarkIconDispatcher.DarkReceiver {
    private final DualToneHandler dualToneHandler;

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public DarkReceiverImpl(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ DarkReceiverImpl(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DarkReceiverImpl(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this.dualToneHandler = new DualToneHandler(context);
        onDarkChanged(new Rect(), 1.0f, -1);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        if (!DarkIconDispatcher.isInArea(rect, this)) {
            f = 0.0f;
        }
        setBackgroundColor(this.dualToneHandler.getSingleColor(f));
    }
}
