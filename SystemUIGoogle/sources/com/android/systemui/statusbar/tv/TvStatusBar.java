package com.android.systemui.statusbar.tv;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import com.android.internal.statusbar.IStatusBarService;
import com.android.systemui.SystemUI;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.CommandQueue;
import dagger.Lazy;
/* loaded from: classes.dex */
public class TvStatusBar extends SystemUI implements CommandQueue.Callbacks {
    private final Lazy<AssistManager> mAssistManagerLazy;
    private final CommandQueue mCommandQueue;

    public TvStatusBar(Context context, CommandQueue commandQueue, Lazy<AssistManager> lazy) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mAssistManagerLazy = lazy;
    }

    @Override // com.android.systemui.SystemUI
    public void start() {
        IStatusBarService asInterface = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        try {
            asInterface.registerStatusBar(this.mCommandQueue);
        } catch (RemoteException unused) {
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void startAssist(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }
}
