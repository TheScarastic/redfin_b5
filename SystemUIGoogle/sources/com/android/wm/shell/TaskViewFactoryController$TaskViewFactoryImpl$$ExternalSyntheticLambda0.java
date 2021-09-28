package com.android.wm.shell;

import android.content.Context;
import com.android.wm.shell.TaskViewFactoryController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TaskViewFactoryController.TaskViewFactoryImpl f$0;
    public final /* synthetic */ Context f$1;
    public final /* synthetic */ Executor f$2;
    public final /* synthetic */ Consumer f$3;

    public /* synthetic */ TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0(TaskViewFactoryController.TaskViewFactoryImpl taskViewFactoryImpl, Context context, Executor executor, Consumer consumer) {
        this.f$0 = taskViewFactoryImpl;
        this.f$1 = context;
        this.f$2 = executor;
        this.f$3 = consumer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$create$0(this.f$1, this.f$2, this.f$3);
    }
}
