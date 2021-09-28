package com.google.android.systemui.columbus.gates;

import com.android.systemui.statusbar.CommandQueue;
/* compiled from: SystemKeyPress.kt */
/* loaded from: classes2.dex */
public final class SystemKeyPress$commandQueueCallbacks$1 implements CommandQueue.Callbacks {
    final /* synthetic */ SystemKeyPress this$0;

    /* access modifiers changed from: package-private */
    public SystemKeyPress$commandQueueCallbacks$1(SystemKeyPress systemKeyPress) {
        this.this$0 = systemKeyPress;
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void handleSystemKey(int i) {
        if (this.this$0.blockingKeys.contains(Integer.valueOf(i))) {
            SystemKeyPress systemKeyPress = this.this$0;
            systemKeyPress.blockForMillis(systemKeyPress.gateDuration);
        }
    }
}
