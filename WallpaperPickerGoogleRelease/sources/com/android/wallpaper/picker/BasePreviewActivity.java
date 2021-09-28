package com.android.wallpaper.picker;

import android.os.Bundle;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
/* loaded from: classes.dex */
public abstract class BasePreviewActivity extends BaseActivity {
    public void enableFullScreen() {
        getWindow().setDecorFitsSystemWindows(false);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(this);
        getWindow().setColorMode(1);
        setTheme(R.style.WallpaperTheme);
        getWindow().setFormat(-3);
        if (getIntent() != null && getIntent().getAction() != null) {
            userEventLogger.logAppLaunched(getIntent());
        }
    }
}
