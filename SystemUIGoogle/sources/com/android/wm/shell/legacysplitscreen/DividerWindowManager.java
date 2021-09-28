package com.android.wm.shell.legacysplitscreen;

import android.graphics.Region;
import android.os.Binder;
import android.view.View;
import android.view.WindowManager;
import com.android.wm.shell.common.SystemWindows;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class DividerWindowManager {
    private WindowManager.LayoutParams mLp;
    final SystemWindows mSystemWindows;
    private View mView;

    /* access modifiers changed from: package-private */
    public DividerWindowManager(SystemWindows systemWindows) {
        this.mSystemWindows = systemWindows;
    }

    /* access modifiers changed from: package-private */
    public void add(View view, int i, int i2, int i3) {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i2, 2034, 545521704, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        this.mLp.setTitle("DockedStackDivider");
        WindowManager.LayoutParams layoutParams2 = this.mLp;
        layoutParams2.privateFlags |= 536870976;
        layoutParams2.layoutInDisplayCutoutMode = 3;
        view.setSystemUiVisibility(1792);
        this.mSystemWindows.addView(view, this.mLp, i3, 0);
        this.mView = view;
    }

    /* access modifiers changed from: package-private */
    public void remove() {
        View view = this.mView;
        if (view != null) {
            this.mSystemWindows.removeView(view);
        }
        this.mView = null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:15:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setSlippery(boolean r6) {
        /*
            r5 = this;
            r0 = 1
            r1 = 536870912(0x20000000, float:1.0842022E-19)
            if (r6 == 0) goto L_0x0012
            android.view.WindowManager$LayoutParams r2 = r5.mLp
            int r3 = r2.flags
            r4 = r3 & r1
            if (r4 != 0) goto L_0x0012
            r6 = r3 | r1
            r2.flags = r6
            goto L_0x0023
        L_0x0012:
            if (r6 != 0) goto L_0x0022
            android.view.WindowManager$LayoutParams r6 = r5.mLp
            int r2 = r6.flags
            r1 = r1 & r2
            if (r1 == 0) goto L_0x0022
            r1 = -536870913(0xffffffffdfffffff, float:-3.6893486E19)
            r1 = r1 & r2
            r6.flags = r1
            goto L_0x0023
        L_0x0022:
            r0 = 0
        L_0x0023:
            if (r0 == 0) goto L_0x002e
            com.android.wm.shell.common.SystemWindows r6 = r5.mSystemWindows
            android.view.View r0 = r5.mView
            android.view.WindowManager$LayoutParams r5 = r5.mLp
            r6.updateViewLayout(r0, r5)
        L_0x002e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerWindowManager.setSlippery(boolean):void");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0028  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setTouchable(boolean r7) {
        /*
            r6 = this;
            android.view.View r0 = r6.mView
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r1 = 0
            r2 = 1
            if (r7 != 0) goto L_0x0017
            android.view.WindowManager$LayoutParams r3 = r6.mLp
            int r4 = r3.flags
            r5 = r4 & 16
            if (r5 != 0) goto L_0x0017
            r7 = r4 | 16
            r3.flags = r7
        L_0x0015:
            r1 = r2
            goto L_0x0026
        L_0x0017:
            if (r7 == 0) goto L_0x0026
            android.view.WindowManager$LayoutParams r7 = r6.mLp
            int r3 = r7.flags
            r4 = r3 & 16
            if (r4 == 0) goto L_0x0026
            r1 = r3 & -17
            r7.flags = r1
            goto L_0x0015
        L_0x0026:
            if (r1 == 0) goto L_0x002f
            com.android.wm.shell.common.SystemWindows r7 = r6.mSystemWindows
            android.view.WindowManager$LayoutParams r6 = r6.mLp
            r7.updateViewLayout(r0, r6)
        L_0x002f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.DividerWindowManager.setTouchable(boolean):void");
    }

    /* access modifiers changed from: package-private */
    public void setTouchRegion(Region region) {
        View view = this.mView;
        if (view != null) {
            this.mSystemWindows.setTouchableRegion(view, region);
        }
    }
}
