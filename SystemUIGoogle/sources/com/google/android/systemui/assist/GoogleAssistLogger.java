package com.google.android.systemui.assist;

import android.content.Context;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.PhoneStateMonitor;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GoogleAssistLogger.kt */
/* loaded from: classes2.dex */
public final class GoogleAssistLogger extends AssistLogger {
    private final AssistantPresenceHandler assistantPresenceHandler;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public GoogleAssistLogger(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor, AssistantPresenceHandler assistantPresenceHandler) {
        super(context, uiEventLogger, assistUtils, phoneStateMonitor);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        Intrinsics.checkNotNullParameter(assistUtils, "assistUtils");
        Intrinsics.checkNotNullParameter(phoneStateMonitor, "phoneStateMonitor");
        Intrinsics.checkNotNullParameter(assistantPresenceHandler, "assistantPresenceHandler");
        this.assistantPresenceHandler = assistantPresenceHandler;
    }

    @Override // com.android.systemui.assist.AssistLogger
    protected void reportAssistantInvocationExtraData() {
        StatsLog.write(StatsEvent.newBuilder().setAtomId(100045).writeInt(getOrCreateInstanceId().getId()).writeBoolean(this.assistantPresenceHandler.isNgaAssistant()).build());
    }
}
