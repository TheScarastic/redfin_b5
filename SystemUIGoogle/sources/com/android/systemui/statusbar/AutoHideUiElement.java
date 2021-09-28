package com.android.systemui.statusbar;
/* loaded from: classes.dex */
public interface AutoHideUiElement {
    void hide();

    boolean isVisible();

    default boolean shouldHideOnTouch() {
        return true;
    }

    void synchronizeState();
}
