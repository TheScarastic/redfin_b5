package com.android.systemui.wmshell;

import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory implements Factory<Optional<TaskSurfaceHelperController>> {
    private final Provider<ShellExecutor> mainExecutorProvider;
    private final Provider<ShellTaskOrganizer> taskOrganizerProvider;

    public WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory(Provider<ShellTaskOrganizer> provider, Provider<ShellExecutor> provider2) {
        this.taskOrganizerProvider = provider;
        this.mainExecutorProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Optional<TaskSurfaceHelperController> get() {
        return provideTaskSurfaceHelperController(this.taskOrganizerProvider.get(), this.mainExecutorProvider.get());
    }

    public static WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory create(Provider<ShellTaskOrganizer> provider, Provider<ShellExecutor> provider2) {
        return new WMShellBaseModule_ProvideTaskSurfaceHelperControllerFactory(provider, provider2);
    }

    public static Optional<TaskSurfaceHelperController> provideTaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        return (Optional) Preconditions.checkNotNullFromProvides(WMShellBaseModule.provideTaskSurfaceHelperController(shellTaskOrganizer, shellExecutor));
    }
}
