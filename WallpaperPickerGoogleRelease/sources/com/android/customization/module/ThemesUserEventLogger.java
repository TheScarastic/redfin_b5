package com.android.customization.module;

import com.android.customization.model.grid.GridOption;
import com.android.wallpaper.module.UserEventLogger;
/* loaded from: classes.dex */
public interface ThemesUserEventLogger extends UserEventLogger {
    void logColorApplied(int i, int i2);

    void logGridApplied(GridOption gridOption);

    void logGridSelected(GridOption gridOption);
}
