package com.android.systemui.statusbar.lockscreen;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController$settingsObserver$1 extends ContentObserver {
    final /* synthetic */ LockscreenSmartspaceController this$0;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LockscreenSmartspaceController$settingsObserver$1(LockscreenSmartspaceController lockscreenSmartspaceController, Handler handler) {
        super(handler);
        this.this$0 = lockscreenSmartspaceController;
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z, Uri uri) {
        this.this$0.execution.assertIsMainThread();
        this.this$0.reloadSmartspace();
    }
}
