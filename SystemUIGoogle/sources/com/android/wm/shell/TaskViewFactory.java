package com.android.wm.shell;

import android.content.Context;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public interface TaskViewFactory {
    void create(Context context, Executor executor, Consumer<TaskView> consumer);
}
