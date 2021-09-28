package com.android.wm.shell.startingsurface;

import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ StartingWindowController.IStartingWindowImpl f$0;
    public final /* synthetic */ IStartingWindowListener f$1;

    public /* synthetic */ StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1(StartingWindowController.IStartingWindowImpl iStartingWindowImpl, IStartingWindowListener iStartingWindowListener) {
        this.f$0 = iStartingWindowImpl;
        this.f$1 = iStartingWindowListener;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$setStartingWindowListener$0(this.f$1, (StartingWindowController) obj);
    }
}
