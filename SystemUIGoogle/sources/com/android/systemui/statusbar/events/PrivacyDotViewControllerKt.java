package com.android.systemui.statusbar.events;
/* compiled from: PrivacyDotViewController.kt */
/* loaded from: classes.dex */
public final class PrivacyDotViewControllerKt {
    /* access modifiers changed from: private */
    public static final void dlog(String str) {
    }

    /* access modifiers changed from: private */
    public static final int toGravity(int i) {
        if (i == 0) {
            return 51;
        }
        if (i == 1) {
            return 53;
        }
        if (i == 2) {
            return 85;
        }
        if (i == 3) {
            return 83;
        }
        throw new IllegalArgumentException("Not a corner");
    }

    /* access modifiers changed from: private */
    public static final int innerGravity(int i) {
        if (i != 0) {
            if (i == 1 || i == 2) {
                return 19;
            }
            if (i != 3) {
                throw new IllegalArgumentException("Not a corner");
            }
        }
        return 21;
    }
}
