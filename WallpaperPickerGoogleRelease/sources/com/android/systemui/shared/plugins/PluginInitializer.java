package com.android.systemui.shared.plugins;

import android.content.Context;
import android.os.Looper;
/* loaded from: classes.dex */
public interface PluginInitializer {
    Looper getBgLooper();

    PluginEnabler getPluginEnabler(Context context);

    String[] getWhitelistedPlugins(Context context);

    void handleWtfs();

    boolean isDebuggable();

    void onPluginManagerInit();
}
