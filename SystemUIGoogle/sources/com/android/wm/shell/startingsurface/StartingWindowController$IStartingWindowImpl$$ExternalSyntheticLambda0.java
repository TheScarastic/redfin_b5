package com.android.wm.shell.startingsurface;

import com.android.internal.util.function.TriConsumer;
import com.android.wm.shell.startingsurface.StartingWindowController;
/* loaded from: classes2.dex */
public final /* synthetic */ class StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 implements TriConsumer {
    public final /* synthetic */ StartingWindowController.IStartingWindowImpl f$0;

    public /* synthetic */ StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0(StartingWindowController.IStartingWindowImpl iStartingWindowImpl) {
        this.f$0 = iStartingWindowImpl;
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        this.f$0.notifyIStartingWindowListener(((Integer) obj).intValue(), ((Integer) obj2).intValue(), ((Integer) obj3).intValue());
    }
}
