package com.google.android.systemui.columbus.actions;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OpenNotificationShade.kt */
/* loaded from: classes2.dex */
public final class OpenNotificationShade extends UserAction {
    public static final Companion Companion = new Companion(null);
    private final Lazy<NotificationShadeWindowController> notificationShadeWindowController;
    private final Lazy<StatusBar> statusBar;
    private final String tag = "Columbus/OpenNotif";
    private final UiEventLogger uiEventLogger;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public OpenNotificationShade(Context context, Lazy<NotificationShadeWindowController> lazy, Lazy<StatusBar> lazy2, UiEventLogger uiEventLogger) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazy, "notificationShadeWindowController");
        Intrinsics.checkNotNullParameter(lazy2, "statusBar");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.notificationShadeWindowController = lazy;
        this.statusBar = lazy2;
        this.uiEventLogger = uiEventLogger;
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        if (this.notificationShadeWindowController.get().getPanelExpanded()) {
            this.statusBar.get().postAnimateCollapsePanels();
            this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_NOTIFICATION_SHADE_CLOSE);
            return;
        }
        this.statusBar.get().animateExpandNotificationsPanel();
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_NOTIFICATION_SHADE_OPEN);
    }

    /* compiled from: OpenNotificationShade.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
