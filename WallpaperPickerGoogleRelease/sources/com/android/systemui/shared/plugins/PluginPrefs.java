package com.android.systemui.shared.plugins;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
import java.util.Set;
/* loaded from: classes.dex */
public class PluginPrefs {
    private static final String HAS_PLUGINS = "plugins";
    private static final String PLUGIN_ACTIONS = "actions";
    private static final String PREFS = "plugin_prefs";
    private final Set<String> mPluginActions;
    private final SharedPreferences mSharedPrefs;

    public PluginPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS, 0);
        this.mSharedPrefs = sharedPreferences;
        this.mPluginActions = new ArraySet(sharedPreferences.getStringSet(PLUGIN_ACTIONS, null));
    }

    public static boolean hasPlugins(Context context) {
        return context.getSharedPreferences(PREFS, 0).getBoolean(HAS_PLUGINS, false);
    }

    public static void setHasPlugins(Context context) {
        context.getSharedPreferences(PREFS, 0).edit().putBoolean(HAS_PLUGINS, true).apply();
    }

    public synchronized void addAction(String str) {
        if (this.mPluginActions.add(str)) {
            this.mSharedPrefs.edit().putStringSet(PLUGIN_ACTIONS, this.mPluginActions).apply();
        }
    }

    public Set<String> getPluginList() {
        return new ArraySet(this.mPluginActions);
    }
}
