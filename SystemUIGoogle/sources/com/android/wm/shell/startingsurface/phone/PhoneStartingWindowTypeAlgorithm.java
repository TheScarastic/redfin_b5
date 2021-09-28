package com.android.wm.shell.startingsurface.phone;

import android.util.Slog;
import android.window.StartingWindowInfo;
import android.window.TaskSnapshot;
import com.android.wm.shell.startingsurface.StartingWindowController;
import com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm;
/* loaded from: classes2.dex */
public class PhoneStartingWindowTypeAlgorithm implements StartingWindowTypeAlgorithm {
    private static final String TAG = "PhoneStartingWindowTypeAlgorithm";

    @Override // com.android.wm.shell.startingsurface.StartingWindowTypeAlgorithm
    public int getSuggestedWindowType(StartingWindowInfo startingWindowInfo) {
        int i = startingWindowInfo.startingWindowTypeParameter;
        int i2 = 1;
        boolean z = (i & 1) != 0;
        boolean z2 = (i & 2) != 0;
        boolean z3 = (i & 4) != 0;
        boolean z4 = (i & 8) != 0;
        boolean z5 = (i & 16) != 0;
        boolean z6 = (i & 32) != 0;
        boolean z7 = (i & Integer.MIN_VALUE) != 0;
        boolean z8 = startingWindowInfo.taskInfo.topActivityType == 2;
        if (StartingWindowController.DEBUG_SPLASH_SCREEN) {
            String str = TAG;
            Slog.d(str, "preferredStartingWindowType newTask:" + z + " taskSwitch:" + z2 + " processRunning:" + z3 + " allowTaskSnapshot:" + z4 + " activityCreated:" + z5 + " useEmptySplashScreen:" + z6 + " legacySplashScreen:" + z7 + " topIsHome:" + z8);
        }
        if (z7) {
            i2 = 4;
        }
        if (!z8) {
            if (!z3) {
                if (z6) {
                    return 3;
                }
                return i2;
            } else if (z) {
                if (z6) {
                    return 3;
                }
                return i2;
            } else if (z2 && !z5) {
                return i2;
            }
        }
        if (z2 && z4) {
            if (isSnapshotCompatible(startingWindowInfo)) {
                return 2;
            }
            if (!z8) {
                return 3;
            }
        }
        return 0;
    }

    private boolean isSnapshotCompatible(StartingWindowInfo startingWindowInfo) {
        TaskSnapshot taskSnapshot = startingWindowInfo.mTaskSnapshot;
        if (taskSnapshot == null) {
            if (StartingWindowController.DEBUG_SPLASH_SCREEN) {
                String str = TAG;
                Slog.d(str, "isSnapshotCompatible no snapshot " + startingWindowInfo.taskInfo.taskId);
            }
            return false;
        } else if (!taskSnapshot.getTopActivityComponent().equals(startingWindowInfo.taskInfo.topActivity)) {
            if (StartingWindowController.DEBUG_SPLASH_SCREEN) {
                String str2 = TAG;
                Slog.d(str2, "isSnapshotCompatible obsoleted snapshot " + startingWindowInfo.taskInfo.topActivity);
            }
            return false;
        } else {
            int rotation = startingWindowInfo.taskInfo.configuration.windowConfiguration.getRotation();
            int rotation2 = taskSnapshot.getRotation();
            if (StartingWindowController.DEBUG_SPLASH_SCREEN) {
                String str3 = TAG;
                Slog.d(str3, "isSnapshotCompatible rotation " + rotation + " snapshot " + rotation2);
            }
            if (rotation == rotation2) {
                return true;
            }
            return false;
        }
    }
}
