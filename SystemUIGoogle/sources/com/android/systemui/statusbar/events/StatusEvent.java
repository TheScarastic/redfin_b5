package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public interface StatusEvent {

    /* compiled from: StatusEvent.kt */
    /* loaded from: classes.dex */
    public static final class DefaultImpls {
        public static boolean shouldUpdateFromEvent(StatusEvent statusEvent, StatusEvent statusEvent2) {
            Intrinsics.checkNotNullParameter(statusEvent, "this");
            return false;
        }

        public static void updateFromEvent(StatusEvent statusEvent, StatusEvent statusEvent2) {
            Intrinsics.checkNotNullParameter(statusEvent, "this");
        }
    }

    String getContentDescription();

    boolean getForceVisible();

    int getPriority();

    boolean getShowAnimation();

    Function1<Context, View> getViewCreator();

    boolean shouldUpdateFromEvent(StatusEvent statusEvent);

    void updateFromEvent(StatusEvent statusEvent);
}
