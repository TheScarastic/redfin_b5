package com.android.systemui;
/* compiled from: BootCompleteCache.kt */
/* loaded from: classes.dex */
public interface BootCompleteCache {

    /* compiled from: BootCompleteCache.kt */
    /* loaded from: classes.dex */
    public interface BootCompleteListener {
        void onBootComplete();
    }

    boolean addListener(BootCompleteListener bootCompleteListener);

    boolean isBootComplete();
}
