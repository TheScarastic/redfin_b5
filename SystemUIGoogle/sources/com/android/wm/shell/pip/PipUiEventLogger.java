package com.android.wm.shell.pip;

import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import com.android.internal.logging.UiEventLogger;
/* loaded from: classes2.dex */
public class PipUiEventLogger {
    private final PackageManager mPackageManager;
    private String mPackageName;
    private int mPackageUid = -1;
    private final UiEventLogger mUiEventLogger;

    public PipUiEventLogger(UiEventLogger uiEventLogger, PackageManager packageManager) {
        this.mUiEventLogger = uiEventLogger;
        this.mPackageManager = packageManager;
    }

    public void setTaskInfo(TaskInfo taskInfo) {
        ComponentName componentName;
        if (taskInfo == null || (componentName = taskInfo.topActivity) == null) {
            this.mPackageName = null;
            this.mPackageUid = -1;
            return;
        }
        String packageName = componentName.getPackageName();
        this.mPackageName = packageName;
        this.mPackageUid = getUid(packageName, taskInfo.userId);
    }

    public void log(PipUiEventEnum pipUiEventEnum) {
        int i;
        String str = this.mPackageName;
        if (str != null && (i = this.mPackageUid) != -1) {
            this.mUiEventLogger.log(pipUiEventEnum, i, str);
        }
    }

    private int getUid(String str, int i) {
        try {
            return this.mPackageManager.getApplicationInfoAsUser(str, 0, i).uid;
        } catch (PackageManager.NameNotFoundException unused) {
            return -1;
        }
    }

    /* loaded from: classes2.dex */
    public enum PipUiEventEnum implements UiEventLogger.UiEventEnum {
        PICTURE_IN_PICTURE_ENTER(603),
        PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN(604),
        PICTURE_IN_PICTURE_TAP_TO_REMOVE(605),
        PICTURE_IN_PICTURE_DRAG_TO_REMOVE(606),
        PICTURE_IN_PICTURE_SHOW_MENU(607),
        PICTURE_IN_PICTURE_HIDE_MENU(608),
        PICTURE_IN_PICTURE_CHANGE_ASPECT_RATIO(609),
        PICTURE_IN_PICTURE_RESIZE(610),
        PICTURE_IN_PICTURE_STASH_UNSTASHED(709),
        PICTURE_IN_PICTURE_STASH_LEFT(710),
        PICTURE_IN_PICTURE_STASH_RIGHT(711);
        
        private final int mId;

        PipUiEventEnum(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
