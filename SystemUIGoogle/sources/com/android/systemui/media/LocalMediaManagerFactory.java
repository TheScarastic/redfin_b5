package com.android.systemui.media;

import android.content.Context;
import com.android.settingslib.bluetooth.LocalBluetoothManager;
import com.android.settingslib.media.InfoMediaManager;
import com.android.settingslib.media.LocalMediaManager;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LocalMediaManagerFactory.kt */
/* loaded from: classes.dex */
public final class LocalMediaManagerFactory {
    private final Context context;
    private final LocalBluetoothManager localBluetoothManager;

    public LocalMediaManagerFactory(Context context, LocalBluetoothManager localBluetoothManager) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.localBluetoothManager = localBluetoothManager;
    }

    public final LocalMediaManager create(String str) {
        Intrinsics.checkNotNullParameter(str, "packageName");
        return new LocalMediaManager(this.context, this.localBluetoothManager, new InfoMediaManager(this.context, str, null, this.localBluetoothManager), str);
    }
}
