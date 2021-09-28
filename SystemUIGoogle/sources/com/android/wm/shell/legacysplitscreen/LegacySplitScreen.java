package com.android.wm.shell.legacysplitscreen;

import android.graphics.Rect;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public interface LegacySplitScreen {
    DividerView getDividerView();

    boolean isDividerVisible();

    boolean isHomeStackResizable();

    boolean isMinimized();

    void onAppTransitionFinished();

    void onKeyguardVisibilityChanged(boolean z);

    void onUndockingTask();

    void registerBoundsChangeListener(BiConsumer<Rect, Rect> biConsumer);

    void registerInSplitScreenListener(Consumer<Boolean> consumer);

    void setMinimized(boolean z);

    boolean splitPrimaryTask();
}
