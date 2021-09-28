package com.android.wallpaper.picker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.android.wallpaper.module.InjectorProvider;
/* loaded from: classes.dex */
public class DeepLinkActivity extends Activity {
    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent deepLinkRedirectIntent = InjectorProvider.getInjector().getDeepLinkRedirectIntent(this, getIntent().getData());
        deepLinkRedirectIntent.putExtra("com.android.wallpaper.LAUNCH_SOURCE", "app_launched_deeplink");
        startActivity(deepLinkRedirectIntent);
        finish();
    }
}
