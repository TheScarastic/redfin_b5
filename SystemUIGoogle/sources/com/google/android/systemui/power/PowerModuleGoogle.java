package com.google.android.systemui.power;

import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.power.PowerUI;
/* loaded from: classes2.dex */
public interface PowerModuleGoogle {
    static PowerUI.WarningsUI provideWarningsUi(Context context, ActivityStarter activityStarter, UiEventLogger uiEventLogger) {
        return new PowerNotificationWarningsGoogleImpl(context, activityStarter, uiEventLogger);
    }
}
