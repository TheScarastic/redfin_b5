package android.support.v4.media;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import java.util.Objects;
@TargetApi(16)
/* loaded from: classes.dex */
public class MediaController2ImplLegacy implements AutoCloseable {
    public static final /* synthetic */ int $r8$clinit = 0;

    /* renamed from: android.support.v4.media.MediaController2ImplLegacy$3  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass3 extends ResultReceiver {
        @Override // android.os.ResultReceiver
        public void onReceiveResult(int i, Bundle bundle) {
            int i2 = MediaController2ImplLegacy.$r8$clinit;
            Objects.requireNonNull(null);
            throw null;
        }
    }

    static {
        Log.isLoggable("MC2ImplLegacy", 3);
        new Bundle().putBoolean("android.support.v4.media.root_default_root", true);
    }
}
