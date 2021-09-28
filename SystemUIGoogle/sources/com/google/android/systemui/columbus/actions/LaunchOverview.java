package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.recents.Recents;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LaunchOverview.kt */
/* loaded from: classes2.dex */
public final class LaunchOverview extends UserAction {
    public static final Companion Companion = new Companion(null);
    private final Recents recents;
    private final String tag = "Columbus/LaunchOverview";
    private final UiEventLogger uiEventLogger;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LaunchOverview(Context context, Recents recents, UiEventLogger uiEventLogger) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(recents, "recents");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.recents = recents;
        this.uiEventLogger = uiEventLogger;
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.recents.toggleRecentApps();
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_OVERVIEW);
    }

    /* compiled from: LaunchOverview.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
