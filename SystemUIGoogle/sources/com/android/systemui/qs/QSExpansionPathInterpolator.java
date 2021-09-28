package com.android.systemui.qs;

import android.view.animation.Interpolator;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: QSExpansionPathInterpolator.kt */
/* loaded from: classes.dex */
public final class QSExpansionPathInterpolator {
    private PathInterpolatorBuilder pathInterpolatorBuilder = new PathInterpolatorBuilder(0.0f, 0.0f, 0.0f, 1.0f);

    public final Interpolator getXInterpolator() {
        Interpolator xInterpolator = this.pathInterpolatorBuilder.getXInterpolator();
        Intrinsics.checkNotNullExpressionValue(xInterpolator, "pathInterpolatorBuilder.xInterpolator");
        return xInterpolator;
    }

    public final Interpolator getYInterpolator() {
        Interpolator yInterpolator = this.pathInterpolatorBuilder.getYInterpolator();
        Intrinsics.checkNotNullExpressionValue(yInterpolator, "pathInterpolatorBuilder.yInterpolator");
        return yInterpolator;
    }
}
