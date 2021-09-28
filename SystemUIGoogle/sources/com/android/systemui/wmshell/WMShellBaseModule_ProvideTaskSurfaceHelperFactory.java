package com.android.systemui.wmshell;

import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideTaskSurfaceHelperFactory implements Factory<Optional<TaskSurfaceHelper>> {
    private final Provider<Optional<TaskSurfaceHelperController>> taskSurfaceControllerProvider;

    public WMShellBaseModule_ProvideTaskSurfaceHelperFactory(Provider<Optional<TaskSurfaceHelperController>> provider) {
        this.taskSurfaceControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<TaskSurfaceHelper> get() {
        return provideTaskSurfaceHelper(this.taskSurfaceControllerProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskSurfaceHelperFactory create(Provider<Optional<TaskSurfaceHelperController>> provider) {
        return new WMShellBaseModule_ProvideTaskSurfaceHelperFactory(provider);
    }

    public static Optional<TaskSurfaceHelper> provideTaskSurfaceHelper(Optional<TaskSurfaceHelperController> optional) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskSurfaceHelper(optional));
    }
}
