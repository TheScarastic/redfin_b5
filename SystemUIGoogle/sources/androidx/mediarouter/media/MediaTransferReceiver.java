package androidx.mediarouter.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/* loaded from: classes.dex */
public final class MediaTransferReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
    }

    public static boolean isDeclared(Context context) {
        Intent intent = new Intent(context, MediaTransferReceiver.class);
        intent.setPackage(context.getPackageName());
        if (context.getPackageManager().queryBroadcastReceivers(intent, 0).size() > 0) {
            return true;
        }
        return false;
    }
}
