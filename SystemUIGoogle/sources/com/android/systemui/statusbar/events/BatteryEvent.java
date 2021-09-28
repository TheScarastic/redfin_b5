package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.View;
import com.android.systemui.statusbar.events.StatusEvent;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public final class BatteryEvent implements StatusEvent {
    private final boolean forceVisible;
    private final int priority = 50;
    private final boolean showAnimation = true;
    private String contentDescription = "";
    private final Function1<Context, View> viewCreator = BatteryEvent$viewCreator$1.INSTANCE;

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean shouldUpdateFromEvent(StatusEvent statusEvent) {
        return StatusEvent.DefaultImpls.shouldUpdateFromEvent(this, statusEvent);
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public void updateFromEvent(StatusEvent statusEvent) {
        StatusEvent.DefaultImpls.updateFromEvent(this, statusEvent);
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public int getPriority() {
        return this.priority;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getForceVisible() {
        return this.forceVisible;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public String getContentDescription() {
        return this.contentDescription;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public Function1<Context, View> getViewCreator() {
        return this.viewCreator;
    }

    public String toString() {
        String simpleName = BatteryEvent.class.getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
