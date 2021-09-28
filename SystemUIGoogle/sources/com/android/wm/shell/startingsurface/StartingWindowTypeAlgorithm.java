package com.android.wm.shell.startingsurface;

import android.window.StartingWindowInfo;
/* loaded from: classes2.dex */
public interface StartingWindowTypeAlgorithm {
    @StartingWindowInfo.StartingWindowType
    int getSuggestedWindowType(StartingWindowInfo startingWindowInfo);
}
