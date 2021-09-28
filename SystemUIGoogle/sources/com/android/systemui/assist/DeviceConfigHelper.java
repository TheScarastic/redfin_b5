package com.android.systemui.assist;

import android.provider.DeviceConfig;
import com.android.systemui.DejankUtils;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class DeviceConfigHelper {
    public long getLong(String str, long j) {
        return ((Long) DejankUtils.whitelistIpcs(new Supplier(str, j) { // from class: com.android.systemui.assist.DeviceConfigHelper$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$0;
            public final /* synthetic */ long f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return DeviceConfigHelper.$r8$lambda$TrTHK9UGFwR20J2G2zxOQhBIBt0(this.f$0, this.f$1);
            }
        })).longValue();
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ Long lambda$getLong$0(String str, long j) {
        return Long.valueOf(DeviceConfig.getLong("systemui", str, j));
    }
}
