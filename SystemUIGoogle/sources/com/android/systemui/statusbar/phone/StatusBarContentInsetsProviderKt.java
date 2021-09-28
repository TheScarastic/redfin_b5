package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.WindowMetrics;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarContentInsetsProvider.kt */
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProviderKt {
    private static final boolean isHorizontal(int i) {
        return i == 1 || i == 3;
    }

    private static final Rect getRotationZeroDisplayBounds(WindowMetrics windowMetrics, int i) {
        Rect bounds = windowMetrics.getBounds();
        if (i != 0 && i != 2) {
            return new Rect(0, 0, bounds.bottom, bounds.right);
        }
        Intrinsics.checkNotNullExpressionValue(bounds, "bounds");
        return bounds;
    }

    public static final Rect getPrivacyChipBoundingRectForInsets(Rect rect, int i, int i2, boolean z) {
        Intrinsics.checkNotNullParameter(rect, "contentRect");
        if (z) {
            int i3 = rect.left;
            return new Rect(i3 - i, rect.top, i3 + i2, rect.bottom);
        }
        int i4 = rect.right;
        return new Rect(i4 - i2, rect.top, i4 + i, rect.bottom);
    }

    public static final Rect calculateInsetsForRotationWithRotatedResources(int i, int i2, DisplayCutout displayCutout, WindowMetrics windowMetrics, int i3, int i4, int i5) {
        Intrinsics.checkNotNullParameter(windowMetrics, "windowMetrics");
        Rect rotationZeroDisplayBounds = getRotationZeroDisplayBounds(windowMetrics, i);
        Rect bounds = windowMetrics.getBounds();
        return getStatusBarLeftRight(displayCutout, i3, rotationZeroDisplayBounds.right, rotationZeroDisplayBounds.bottom, bounds.width(), bounds.height(), i4, i5, i2, i);
    }

    private static final Rect getStatusBarLeftRight(DisplayCutout displayCutout, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
        List<Rect> list;
        if (isHorizontal(i8)) {
            i2 = i3;
        }
        if (displayCutout == null) {
            list = null;
        } else {
            list = displayCutout.getBoundingRects();
        }
        if (list == null || list.isEmpty()) {
            return new Rect(i6, 0, i2 - i7, i);
        }
        int i10 = i9 - i8;
        if (i10 < 0) {
            i10 += 4;
        }
        Rect sbRect = sbRect(i10, i, new Pair(Integer.valueOf(i4), Integer.valueOf(i5)));
        int i11 = i6;
        int i12 = i7;
        for (Rect rect : list) {
            Intrinsics.checkNotNullExpressionValue(rect, "cutoutRect");
            if (shareShortEdge(sbRect, rect, i4, i5)) {
                if (touchesLeftEdge(rect, i10, i4, i5)) {
                    i11 = Math.max(Math.max(i6, logicalWidth(rect, i10)), i11);
                } else if (touchesRightEdge(rect, i10, i4, i5)) {
                    i12 = Math.max(i7, logicalWidth(rect, i10));
                }
            }
        }
        return new Rect(i11, 0, i2 - i12, i);
    }

    private static final Rect sbRect(int i, int i2, Pair<Integer, Integer> pair) {
        Integer num = (Integer) pair.first;
        Integer num2 = (Integer) pair.second;
        if (i == 0) {
            Intrinsics.checkNotNullExpressionValue(num, "w");
            return new Rect(0, 0, num.intValue(), i2);
        } else if (i == 1) {
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(0, 0, i2, num2.intValue());
        } else if (i != 2) {
            int intValue = num.intValue() - i2;
            Intrinsics.checkNotNullExpressionValue(num, "w");
            int intValue2 = num.intValue();
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(intValue, 0, intValue2, num2.intValue());
        } else {
            int intValue3 = num2.intValue() - i2;
            Intrinsics.checkNotNullExpressionValue(num, "w");
            int intValue4 = num.intValue();
            Intrinsics.checkNotNullExpressionValue(num2, "h");
            return new Rect(0, intValue3, intValue4, num2.intValue());
        }
    }

    private static final boolean shareShortEdge(Rect rect, Rect rect2, int i, int i2) {
        if (i < i2) {
            return rect.intersects(0, rect2.top, i, rect2.bottom);
        }
        if (i > i2) {
            return rect.intersects(rect2.left, 0, rect2.right, i2);
        }
        return false;
    }

    private static final boolean touchesRightEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.bottom < i3) {
                        return false;
                    }
                } else if (rect.left > 0) {
                    return false;
                }
            } else if (rect.top > 0) {
                return false;
            }
        } else if (rect.right < i2) {
            return false;
        }
        return true;
    }

    private static final boolean touchesLeftEdge(Rect rect, int i, int i2, int i3) {
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (rect.top > 0) {
                        return false;
                    }
                } else if (rect.right < i2) {
                    return false;
                }
            } else if (rect.bottom < i3) {
                return false;
            }
        } else if (rect.left > 0) {
            return false;
        }
        return true;
    }

    private static final int logicalWidth(Rect rect, int i) {
        if (i == 0 || i == 2) {
            return rect.width();
        }
        return rect.height();
    }
}
