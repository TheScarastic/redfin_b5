package com.android.wm.shell;

import android.content.Context;
import com.android.wm.shell.common.ShellExecutor;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class TaskViewFactoryController {
    private final TaskViewFactory mImpl = new TaskViewFactoryImpl();
    private final ShellExecutor mShellExecutor;
    private final ShellTaskOrganizer mTaskOrganizer;

    public TaskViewFactoryController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mShellExecutor = shellExecutor;
    }

    public TaskViewFactory asTaskViewFactory() {
        return this.mImpl;
    }

    public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
        executor.execute(new Runnable(consumer, new TaskView(context, this.mTaskOrganizer)) { // from class: com.android.wm.shell.TaskViewFactoryController$$ExternalSyntheticLambda0
            public final /* synthetic */ Consumer f$0;
            public final /* synthetic */ TaskView f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.accept(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TaskViewFactoryImpl implements TaskViewFactory {
        private TaskViewFactoryImpl() {
        }

        @Override // com.android.wm.shell.TaskViewFactory
        public void create(Context context, Executor executor, Consumer<TaskView> consumer) {
            TaskViewFactoryController.this.mShellExecutor.execute(new TaskViewFactoryController$TaskViewFactoryImpl$$ExternalSyntheticLambda0(this, context, executor, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$create$0(Context context, Executor executor, Consumer consumer) {
            TaskViewFactoryController.this.create(context, executor, consumer);
        }
    }
}
