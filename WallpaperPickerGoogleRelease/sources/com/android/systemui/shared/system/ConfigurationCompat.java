package com.android.systemui.shared.system;

import android.content.res.Configuration;
/* loaded from: classes.dex */
public class ConfigurationCompat {
    public static int getWindowConfigurationRotation(Configuration configuration) {
        return configuration.windowConfiguration.getRotation();
    }
}
