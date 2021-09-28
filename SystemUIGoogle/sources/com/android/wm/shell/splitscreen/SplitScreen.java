package com.android.wm.shell.splitscreen;
/* loaded from: classes2.dex */
public interface SplitScreen {

    /* loaded from: classes2.dex */
    public interface SplitScreenListener {
        void onStagePositionChanged(int i, int i2);

        void onTaskStageChanged(int i, int i2, boolean z);
    }

    default ISplitScreen createExternalInterface() {
        return null;
    }

    static String stageTypeToString(int i) {
        if (i == -1) {
            return "UNDEFINED";
        }
        if (i == 0) {
            return "MAIN";
        }
        if (i == 1) {
            return "SIDE";
        }
        return "UNKNOWN(" + i + ")";
    }
}
