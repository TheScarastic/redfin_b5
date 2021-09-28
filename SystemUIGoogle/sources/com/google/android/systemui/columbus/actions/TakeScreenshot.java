package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.os.Handler;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.ScreenshotHelper;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.function.Consumer;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TakeScreenshot.kt */
/* loaded from: classes2.dex */
public final class TakeScreenshot extends UserAction {
    public static final Companion Companion = new Companion(null);
    private final Handler handler;
    private final ScreenshotHelper screenshotHelper;
    private final String tag = "Columbus/TakeScreenshot";
    private final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public boolean availableOnLockscreen() {
        return true;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public TakeScreenshot(Context context, Handler handler, UiEventLogger uiEventLogger) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.handler = handler;
        this.uiEventLogger = uiEventLogger;
        this.screenshotHelper = new ScreenshotHelper(context);
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.screenshotHelper.takeScreenshot(1, true, true, 6, this.handler, (Consumer) null);
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_SCREENSHOT);
    }

    /* compiled from: TakeScreenshot.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
