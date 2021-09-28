package com.android.systemui.plugins.qs;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.annotations.ProvidesInterface;
@ProvidesInterface(version = 1)
/* loaded from: classes.dex */
public interface DetailAdapter {
    public static final UiEventLogger.UiEventEnum INVALID = DetailAdapter$$ExternalSyntheticLambda0.INSTANCE;
    public static final int VERSION = 1;

    /* access modifiers changed from: private */
    static /* synthetic */ int lambda$static$0() {
        return 0;
    }

    View createDetailView(Context context, View view, ViewGroup viewGroup);

    default int getDoneText() {
        return 0;
    }

    int getMetricsCategory();

    Intent getSettingsIntent();

    default int getSettingsText() {
        return 0;
    }

    CharSequence getTitle();

    default boolean getToggleEnabled() {
        return true;
    }

    Boolean getToggleState();

    default boolean hasHeader() {
        return true;
    }

    default boolean onDoneButtonClicked() {
        return false;
    }

    void setToggleState(boolean z);

    default boolean shouldAnimate() {
        return true;
    }

    default UiEventLogger.UiEventEnum openDetailEvent() {
        return INVALID;
    }

    default UiEventLogger.UiEventEnum closeDetailEvent() {
        return INVALID;
    }

    default UiEventLogger.UiEventEnum moreSettingsEvent() {
        return INVALID;
    }
}
