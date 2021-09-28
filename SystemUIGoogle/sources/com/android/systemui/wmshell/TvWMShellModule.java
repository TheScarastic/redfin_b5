package com.android.systemui.wmshell;

import android.animation.AnimationHandler;
import android.content.Context;
import android.view.IWindowManager;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
import com.android.wm.shell.startingsurface.tv.TvStartingWindowTypeAlgorithm;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes2.dex */
public class TvWMShellModule {
    /* access modifiers changed from: package-private */
    public static DisplayImeController provideDisplayImeController(IWindowManager iWindowManager, DisplayController displayController, ShellExecutor shellExecutor, TransactionPool transactionPool) {
        return new DisplayImeController(iWindowManager, displayController, shellExecutor, transactionPool);
    }

    /* access modifiers changed from: package-private */
    public static LegacySplitScreenController provideSplitScreen(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        return new LegacySplitScreenController(context, displayController, systemWindows, displayImeController, transactionPool, shellTaskOrganizer, syncTransactionQueue, taskStackListenerImpl, transitions, shellExecutor, animationHandler);
    }

    /* access modifiers changed from: package-private */
    public static StartingWindowTypeAlgorithm provideStartingWindowTypeAlgorithm() {
        return new TvStartingWindowTypeAlgorithm();
    }
}
