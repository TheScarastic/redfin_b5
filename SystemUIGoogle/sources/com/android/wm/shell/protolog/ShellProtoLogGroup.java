package com.android.wm.shell.protolog;

import com.android.internal.protolog.common.IProtoLogGroup;
/* loaded from: classes2.dex */
public enum ShellProtoLogGroup implements IProtoLogGroup {
    WM_SHELL_TASK_ORG(true, true, false, "WindowManagerShell"),
    WM_SHELL_TRANSITIONS(true, true, true, "WindowManagerShell"),
    WM_SHELL_DRAG_AND_DROP(true, true, false, "WindowManagerShell"),
    TEST_GROUP(true, true, false, "WindowManagerShellProtoLogTest");
    
    private final boolean mEnabled;
    private volatile boolean mLogToLogcat;
    private volatile boolean mLogToProto;
    private final String mTag;

    ShellProtoLogGroup(boolean z, boolean z2, boolean z3, String str) {
        this.mEnabled = z;
        this.mLogToProto = z2;
        this.mLogToLogcat = z3;
        this.mTag = str;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    @Override // com.android.internal.protolog.common.IProtoLogGroup
    public boolean isLogToProto() {
        return this.mLogToProto;
    }

    @Override // com.android.internal.protolog.common.IProtoLogGroup
    public boolean isLogToLogcat() {
        return this.mLogToLogcat;
    }

    public boolean isLogToAny() {
        return this.mLogToLogcat || this.mLogToProto;
    }

    public String getTag() {
        return this.mTag;
    }

    public void setLogToProto(boolean z) {
        this.mLogToProto = z;
    }

    public void setLogToLogcat(boolean z) {
        this.mLogToLogcat = z;
    }
}
