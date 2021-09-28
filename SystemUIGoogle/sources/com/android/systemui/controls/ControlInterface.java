package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlStatus.kt */
/* loaded from: classes.dex */
public interface ControlInterface {

    /* compiled from: ControlStatus.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        public static boolean getRemoved(ControlInterface controlInterface) {
            Intrinsics.checkNotNullParameter(controlInterface, "this");
            return false;
        }
    }

    ComponentName getComponent();

    String getControlId();

    Icon getCustomIcon();

    int getDeviceType();

    boolean getFavorite();

    boolean getRemoved();

    CharSequence getSubtitle();

    CharSequence getTitle();
}
