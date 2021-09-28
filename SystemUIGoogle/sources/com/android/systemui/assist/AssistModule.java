package com.android.systemui.assist;

import android.content.Context;
import com.android.internal.app.AssistUtils;
/* loaded from: classes.dex */
public abstract class AssistModule {
    /* access modifiers changed from: package-private */
    public static AssistUtils provideAssistUtils(Context context) {
        return new AssistUtils(context);
    }
}
