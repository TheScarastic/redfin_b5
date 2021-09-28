package com.android.systemui;

import java.util.ArrayList;
/* loaded from: classes.dex */
public class InitController {
    private boolean mTasksExecuted = false;
    private final ArrayList<Runnable> mTasks = new ArrayList<>();

    public void addPostInitTask(Runnable runnable) {
        if (!this.mTasksExecuted) {
            this.mTasks.add(runnable);
            return;
        }
        throw new IllegalStateException("post init tasks have already been executed!");
    }

    public void executePostInitTasks() {
        while (!this.mTasks.isEmpty()) {
            this.mTasks.remove(0).run();
        }
        this.mTasksExecuted = true;
    }
}
