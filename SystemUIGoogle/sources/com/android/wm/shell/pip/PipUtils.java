package com.android.wm.shell.pip;

import android.app.ActivityTaskManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;
/* loaded from: classes2.dex */
public class PipUtils {
    public static Pair<ComponentName, Integer> getTopPipActivity(Context context) {
        int[] iArr;
        try {
            String packageName = context.getPackageName();
            ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
            if (!(rootTaskInfo == null || (iArr = rootTaskInfo.childTaskIds) == null || iArr.length <= 0)) {
                for (int length = rootTaskInfo.childTaskNames.length - 1; length >= 0; length--) {
                    ComponentName unflattenFromString = ComponentName.unflattenFromString(rootTaskInfo.childTaskNames[length]);
                    if (!(unflattenFromString == null || unflattenFromString.getPackageName().equals(packageName))) {
                        return new Pair<>(unflattenFromString, Integer.valueOf(rootTaskInfo.childTaskUserIds[length]));
                    }
                }
            }
        } catch (RemoteException unused) {
            Log.w("PipUtils", "Unable to get pinned stack.");
        }
        return new Pair<>(null, 0);
    }
}
