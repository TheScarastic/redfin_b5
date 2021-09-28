package com.android.systemui.dock;
/* loaded from: classes.dex */
public interface DockManager {

    /* loaded from: classes.dex */
    public interface AlignmentStateListener {
        void onAlignmentStateChanged(int i);
    }

    /* loaded from: classes.dex */
    public interface DockEventListener {
        void onEvent(int i);
    }

    void addAlignmentStateListener(AlignmentStateListener alignmentStateListener);

    void addListener(DockEventListener dockEventListener);

    boolean isDocked();

    boolean isHidden();

    void removeListener(DockEventListener dockEventListener);
}
