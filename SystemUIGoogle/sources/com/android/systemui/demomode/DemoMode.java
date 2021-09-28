package com.android.systemui.demomode;

import com.google.android.collect.Lists;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public interface DemoMode extends DemoModeCommandReceiver {
    public static final List<String> NO_COMMANDS = new ArrayList();
    public static final List<String> COMMANDS = Lists.newArrayList(new String[]{"bars", "battery", "clock", "network", "notifications", "operator", "status", "volume"});

    default List<String> demoCommands() {
        return NO_COMMANDS;
    }
}
