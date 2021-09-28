package com.google.android.apps.wallpaper.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.wallpaper.module.InjectorProvider;
import java.util.Objects;
/* loaded from: classes.dex */
public class ArcSyncReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Objects.requireNonNull(InjectorProvider.getInjector().getFormFactorChecker(context));
    }
}
