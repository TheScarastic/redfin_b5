package com.android.systemui.dagger;

import com.android.systemui.BootCompleteCacheImpl;
import com.android.systemui.Dependency;
import com.android.systemui.InitController;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.TaskViewFactory;
import com.android.wm.shell.apppairs.AppPairs;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.startingsurface.StartingSurface;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.android.wm.shell.transition.ShellTransitions;
import java.util.Optional;
/* loaded from: classes.dex */
public interface SysUIComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        SysUIComponent build();

        Builder setAppPairs(Optional<AppPairs> optional);

        Builder setBubbles(Optional<Bubbles> optional);

        Builder setHideDisplayCutout(Optional<HideDisplayCutout> optional);

        Builder setLegacySplitScreen(Optional<LegacySplitScreen> optional);

        Builder setOneHanded(Optional<OneHanded> optional);

        Builder setPip(Optional<Pip> optional);

        Builder setShellCommandHandler(Optional<ShellCommandHandler> optional);

        Builder setSplitScreen(Optional<SplitScreen> optional);

        Builder setStartingSurface(Optional<StartingSurface> optional);

        Builder setTaskSurfaceHelper(Optional<TaskSurfaceHelper> optional);

        Builder setTaskViewFactory(Optional<TaskViewFactory> optional);

        Builder setTransitions(ShellTransitions shellTransitions);
    }

    Dependency createDependency();

    DumpManager createDumpManager();

    ConfigurationController getConfigurationController();

    ContextComponentHelper getContextComponentHelper();

    InitController getInitController();

    default void init() {
    }

    void inject(SystemUIAppComponentFactory systemUIAppComponentFactory);

    BootCompleteCacheImpl provideBootCacheImpl();
}
