package com.android.wm.shell.protolog;

import com.android.internal.protolog.BaseProtoLogImpl;
/* loaded from: classes2.dex */
public class ShellProtoLogCache {
    public static boolean TEST_GROUP_enabled = false;
    public static boolean WM_SHELL_DRAG_AND_DROP_enabled = false;
    public static boolean WM_SHELL_TASK_ORG_enabled = false;
    public static boolean WM_SHELL_TRANSITIONS_enabled = false;

    static {
        BaseProtoLogImpl.sCacheUpdater = ShellProtoLogCache$$ExternalSyntheticLambda0.INSTANCE;
        update();
    }

    /* access modifiers changed from: package-private */
    public static void update() {
        WM_SHELL_TASK_ORG_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_TASK_ORG);
        WM_SHELL_TRANSITIONS_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_TRANSITIONS);
        WM_SHELL_DRAG_AND_DROP_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.WM_SHELL_DRAG_AND_DROP);
        TEST_GROUP_enabled = ShellProtoLogImpl.isEnabled(ShellProtoLogGroup.TEST_GROUP);
    }
}
