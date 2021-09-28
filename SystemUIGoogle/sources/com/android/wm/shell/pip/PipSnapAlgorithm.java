package com.android.wm.shell.pip;

import android.graphics.Rect;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes2.dex */
public class PipSnapAlgorithm {
    public float getSnapFraction(Rect rect, Rect rect2) {
        return getSnapFraction(rect, rect2, 0);
    }

    public float getSnapFraction(Rect rect, Rect rect2, int i) {
        Rect rect3 = new Rect();
        snapRectToClosestEdge(rect, rect2, rect3, i);
        float width = ((float) (rect3.left - rect2.left)) / ((float) rect2.width());
        float height = ((float) (rect3.top - rect2.top)) / ((float) rect2.height());
        int i2 = rect3.top;
        if (i2 == rect2.top) {
            return width;
        }
        if (rect3.left == rect2.right) {
            return height + 1.0f;
        }
        return i2 == rect2.bottom ? (1.0f - width) + 2.0f : (1.0f - height) + 3.0f;
    }

    public void applySnapFraction(Rect rect, Rect rect2, float f) {
        if (f < 1.0f) {
            rect.offsetTo(rect2.left + ((int) (f * ((float) rect2.width()))), rect2.top);
        } else if (f < 2.0f) {
            rect.offsetTo(rect2.right, rect2.top + ((int) ((f - 1.0f) * ((float) rect2.height()))));
        } else if (f < 3.0f) {
            rect.offsetTo(rect2.left + ((int) ((1.0f - (f - 2.0f)) * ((float) rect2.width()))), rect2.bottom);
        } else {
            rect.offsetTo(rect2.left, rect2.top + ((int) ((1.0f - (f - 3.0f)) * ((float) rect2.height()))));
        }
    }

    public void applySnapFraction(Rect rect, Rect rect2, float f, int i, int i2, Rect rect3, Rect rect4) {
        int i3;
        applySnapFraction(rect, rect2, f);
        if (i != 0) {
            if (i == 1) {
                i3 = (i2 - rect.width()) + rect4.left;
            } else {
                i3 = (rect3.right - i2) - rect4.right;
            }
            rect.offsetTo(i3, rect.top);
        }
    }

    @VisibleForTesting
    void snapRectToClosestEdge(Rect rect, Rect rect2, Rect rect3, int i) {
        int i2 = rect.left;
        if (i == 1) {
            i2 = rect2.left;
        } else if (i == 2) {
            i2 = rect2.right;
        }
        int max = Math.max(rect2.left, Math.min(rect2.right, i2));
        int max2 = Math.max(rect2.top, Math.min(rect2.bottom, rect.top));
        rect3.set(rect);
        int abs = Math.abs(i2 - rect2.left);
        int abs2 = Math.abs(rect.top - rect2.top);
        int abs3 = Math.abs(rect2.right - i2);
        int min = Math.min(Math.min(abs, abs3), Math.min(abs2, Math.abs(rect2.bottom - rect.top)));
        if (min == abs) {
            rect3.offsetTo(rect2.left, max2);
        } else if (min == abs2) {
            rect3.offsetTo(max, rect2.top);
        } else if (min == abs3) {
            rect3.offsetTo(rect2.right, max2);
        } else {
            rect3.offsetTo(max, rect2.bottom);
        }
    }
}
