package com.android.systemui.shared.system;

import android.view.ThreadedRenderer;
/* loaded from: classes.dex */
public class ThreadedRendererCompat {
    public static int EGL_CONTEXT_PRIORITY_HIGH_IMG = 12545;
    public static int EGL_CONTEXT_PRIORITY_REALTIME_NV = 13143;

    public static void setContextPriority(int i) {
        ThreadedRenderer.setContextPriority(i);
    }
}
