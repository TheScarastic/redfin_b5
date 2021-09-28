package com.android.wm.shell.common;

import android.content.Context;
/* loaded from: classes2.dex */
public interface RemoteCallable<T> {
    Context getContext();

    ShellExecutor getRemoteCallExecutor();
}
