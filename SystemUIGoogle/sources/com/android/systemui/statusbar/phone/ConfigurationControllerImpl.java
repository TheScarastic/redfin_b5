package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.os.LocaleList;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConfigurationControllerImpl.kt */
/* loaded from: classes.dex */
public final class ConfigurationControllerImpl implements ConfigurationController {
    private final Context context;
    private int density;
    private float fontScale;
    private final boolean inCarMode;
    private int layoutDirection;
    private LocaleList localeList;
    private int smallestScreenWidth;
    private int uiMode;
    private final List<ConfigurationController.ConfigurationListener> listeners = new ArrayList();
    private final Configuration lastConfig = new Configuration();

    public ConfigurationControllerImpl(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Configuration configuration = context.getResources().getConfiguration();
        this.context = context;
        this.fontScale = configuration.fontScale;
        this.density = configuration.densityDpi;
        this.smallestScreenWidth = configuration.smallestScreenWidthDp;
        int i = configuration.uiMode;
        this.inCarMode = (i & 15) == 3;
        this.uiMode = i & 48;
        this.localeList = configuration.getLocales();
        this.layoutDirection = configuration.getLayoutDirection();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public void notifyThemeChanged() {
        for (ConfigurationController.ConfigurationListener configurationListener : new ArrayList(this.listeners)) {
            if (this.listeners.contains(configurationListener)) {
                configurationListener.onThemeChanged();
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x004c, code lost:
        if (r4 == false) goto L_0x006e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0074  */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x009e  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00be  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00f3  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0143 A[ORIG_RETURN, RETURN] */
    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onConfigurationChanged(android.content.res.Configuration r11) {
        /*
        // Method dump skipped, instructions count: 324
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.ConfigurationControllerImpl.onConfigurationChanged(android.content.res.Configuration):void");
    }

    public void addCallback(ConfigurationController.ConfigurationListener configurationListener) {
        Intrinsics.checkNotNullParameter(configurationListener, "listener");
        this.listeners.add(configurationListener);
        configurationListener.onDensityOrFontScaleChanged();
    }

    public void removeCallback(ConfigurationController.ConfigurationListener configurationListener) {
        Intrinsics.checkNotNullParameter(configurationListener, "listener");
        this.listeners.remove(configurationListener);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController
    public boolean isLayoutRtl() {
        return this.layoutDirection == 1;
    }
}
