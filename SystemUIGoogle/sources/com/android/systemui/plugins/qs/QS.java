package com.android.systemui.plugins.qs;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.plugins.FragmentBase;
import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import java.util.function.Consumer;
@ProvidesInterface(action = QS.ACTION, version = 11)
@DependsOn(target = HeightListener.class)
/* loaded from: classes.dex */
public interface QS extends FragmentBase {
    public static final String ACTION = "com.android.systemui.action.PLUGIN_QS";
    public static final String TAG = "QS";
    public static final int VERSION = 11;

    @ProvidesInterface(version = 1)
    /* loaded from: classes.dex */
    public interface HeightListener {
        public static final int VERSION = 1;

        void onQsHeightChanged();
    }

    @ProvidesInterface(version = 1)
    /* loaded from: classes.dex */
    public interface ScrollListener {
        public static final int VERSION = 1;

        void onQsPanelScrollChanged(int i);
    }

    void animateHeaderSlidingOut();

    void closeDetail();

    int getDesiredHeight();

    View getHeader();

    int getQsMinExpansionHeight();

    void hideImmediately();

    boolean isCustomizing();

    default boolean isFullyCollapsed() {
        return true;
    }

    boolean isShowingDetail();

    void notifyCustomizeChanged();

    void setCollapsedMediaVisibilityChangedListener(Consumer<Boolean> consumer);

    void setContainer(ViewGroup viewGroup);

    void setExpandClickListener(View.OnClickListener onClickListener);

    void setExpanded(boolean z);

    void setFancyClipping(int i, int i2, int i3, boolean z);

    default void setHasNotifications(boolean z) {
    }

    void setHeaderClickable(boolean z);

    void setHeaderListening(boolean z);

    void setHeightOverride(int i);

    void setListening(boolean z);

    void setOverscrolling(boolean z);

    void setPanelView(HeightListener heightListener);

    void setQsExpansion(float f, float f2);

    default void setScrollListener(ScrollListener scrollListener) {
    }

    default void setTransitionToFullShadeAmount(float f, boolean z) {
    }

    void setTranslateWhileExpanding(boolean z);

    default boolean disallowPanelTouches() {
        return isShowingDetail();
    }
}
