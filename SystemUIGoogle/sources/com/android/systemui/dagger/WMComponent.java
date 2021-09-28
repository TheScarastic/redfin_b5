package com.android.systemui.dagger;

import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellInit;
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
public interface WMComponent {

    /* loaded from: classes.dex */
    public interface Builder {
        WMComponent build();
    }

    Optional<AppPairs> getAppPairs();

    Optional<Bubbles> getBubbles();

    Optional<HideDisplayCutout> getHideDisplayCutout();

    Optional<LegacySplitScreen> getLegacySplitScreen();

    Optional<OneHanded> getOneHanded();

    Optional<Pip> getPip();

    Optional<ShellCommandHandler> getShellCommandHandler();

    ShellInit getShellInit();

    Optional<SplitScreen> getSplitScreen();

    Optional<StartingSurface> getStartingSurface();

    Optional<TaskSurfaceHelper> getTaskSurfaceHelper();

    Optional<TaskViewFactory> getTaskViewFactory();

    ShellTransitions getTransitions();

    default void init() {
        getShellInit().init();
    }
}
