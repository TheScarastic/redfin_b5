package androidx.leanback.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class NonOverlappingLinearLayout extends LinearLayout {
    boolean mDeferFocusableViewAvailableInLayout;
    boolean mFocusableViewAvailableFixEnabled;
    final ArrayList<ArrayList<View>> mSortedAvailableViews;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public NonOverlappingLinearLayout(Context context) {
        this(context, null);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NonOverlappingLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mFocusableViewAvailableFixEnabled = false;
        this.mSortedAvailableViews = new ArrayList<>();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v7 */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onLayout(boolean r5, int r6, int r7, int r8, int r9) {
        /*
            r4 = this;
            r0 = 0
            boolean r1 = r4.mFocusableViewAvailableFixEnabled     // Catch: all -> 0x009a
            r2 = 1
            if (r1 == 0) goto L_0x0014
            int r1 = r4.getOrientation()     // Catch: all -> 0x009a
            if (r1 != 0) goto L_0x0014
            int r1 = r4.getLayoutDirection()     // Catch: all -> 0x009a
            if (r1 != r2) goto L_0x0014
            r1 = r2
            goto L_0x0015
        L_0x0014:
            r1 = r0
        L_0x0015:
            r4.mDeferFocusableViewAvailableInLayout = r1     // Catch: all -> 0x009a
            if (r1 == 0) goto L_0x0047
        L_0x0019:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r1 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            int r1 = r1.size()     // Catch: all -> 0x009a
            int r3 = r4.getChildCount()     // Catch: all -> 0x009a
            if (r1 <= r3) goto L_0x0030
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r1 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            int r3 = r1.size()     // Catch: all -> 0x009a
            int r3 = r3 - r2
            r1.remove(r3)     // Catch: all -> 0x009a
            goto L_0x0019
        L_0x0030:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r1 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            int r1 = r1.size()     // Catch: all -> 0x009a
            int r2 = r4.getChildCount()     // Catch: all -> 0x009a
            if (r1 >= r2) goto L_0x0047
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r1 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            java.util.ArrayList r2 = new java.util.ArrayList     // Catch: all -> 0x009a
            r2.<init>()     // Catch: all -> 0x009a
            r1.add(r2)     // Catch: all -> 0x009a
            goto L_0x0030
        L_0x0047:
            super.onLayout(r5, r6, r7, r8, r9)     // Catch: all -> 0x009a
            boolean r5 = r4.mDeferFocusableViewAvailableInLayout     // Catch: all -> 0x009a
            if (r5 == 0) goto L_0x007d
            r5 = r0
        L_0x004f:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r6 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            int r6 = r6.size()     // Catch: all -> 0x009a
            if (r5 >= r6) goto L_0x007d
            r6 = r0
        L_0x0058:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r7 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            java.lang.Object r7 = r7.get(r5)     // Catch: all -> 0x009a
            java.util.ArrayList r7 = (java.util.ArrayList) r7     // Catch: all -> 0x009a
            int r7 = r7.size()     // Catch: all -> 0x009a
            if (r6 >= r7) goto L_0x007a
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r7 = r4.mSortedAvailableViews     // Catch: all -> 0x009a
            java.lang.Object r7 = r7.get(r5)     // Catch: all -> 0x009a
            java.util.ArrayList r7 = (java.util.ArrayList) r7     // Catch: all -> 0x009a
            java.lang.Object r7 = r7.get(r6)     // Catch: all -> 0x009a
            android.view.View r7 = (android.view.View) r7     // Catch: all -> 0x009a
            super.focusableViewAvailable(r7)     // Catch: all -> 0x009a
            int r6 = r6 + 1
            goto L_0x0058
        L_0x007a:
            int r5 = r5 + 1
            goto L_0x004f
        L_0x007d:
            boolean r5 = r4.mDeferFocusableViewAvailableInLayout
            if (r5 == 0) goto L_0x0099
            r4.mDeferFocusableViewAvailableInLayout = r0
        L_0x0083:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r5 = r4.mSortedAvailableViews
            int r5 = r5.size()
            if (r0 >= r5) goto L_0x0099
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r5 = r4.mSortedAvailableViews
            java.lang.Object r5 = r5.get(r0)
            java.util.ArrayList r5 = (java.util.ArrayList) r5
            r5.clear()
            int r0 = r0 + 1
            goto L_0x0083
        L_0x0099:
            return
        L_0x009a:
            r5 = move-exception
            boolean r6 = r4.mDeferFocusableViewAvailableInLayout
            if (r6 == 0) goto L_0x00b7
            r4.mDeferFocusableViewAvailableInLayout = r0
        L_0x00a1:
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r6 = r4.mSortedAvailableViews
            int r6 = r6.size()
            if (r0 >= r6) goto L_0x00b7
            java.util.ArrayList<java.util.ArrayList<android.view.View>> r6 = r4.mSortedAvailableViews
            java.lang.Object r6 = r6.get(r0)
            java.util.ArrayList r6 = (java.util.ArrayList) r6
            r6.clear()
            int r0 = r0 + 1
            goto L_0x00a1
        L_0x00b7:
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.NonOverlappingLinearLayout.onLayout(boolean, int, int, int, int):void");
    }

    @Override // android.view.ViewParent, android.view.ViewGroup
    public void focusableViewAvailable(View view) {
        int i;
        if (this.mDeferFocusableViewAvailableInLayout) {
            View view2 = view;
            while (view2 != this && view2 != null) {
                if (view2.getParent() == this) {
                    i = indexOfChild(view2);
                    break;
                }
                view2 = (View) view2.getParent();
            }
            i = -1;
            if (i != -1) {
                this.mSortedAvailableViews.get(i).add(view);
                return;
            }
            return;
        }
        super.focusableViewAvailable(view);
    }
}
