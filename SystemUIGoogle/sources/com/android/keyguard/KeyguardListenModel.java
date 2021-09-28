package com.android.keyguard;

import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: KeyguardListenModel.kt */
/* loaded from: classes.dex */
public abstract class KeyguardListenModel {
    public /* synthetic */ KeyguardListenModel(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract boolean getListening();

    public abstract long getTimeMillis();

    private KeyguardListenModel() {
    }
}
