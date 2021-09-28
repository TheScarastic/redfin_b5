package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import com.android.systemui.R$string;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class DockTooltipView extends BaseTooltipView {
    private final AccessibilityFloatingMenuView mAnchorView;

    /* access modifiers changed from: package-private */
    public DockTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context, accessibilityFloatingMenuView);
        this.mAnchorView = accessibilityFloatingMenuView;
        setDescription(getContext().getText(R$string.accessibility_floating_button_docking_tooltip));
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.floatingmenu.BaseTooltipView
    public void hide() {
        super.hide();
        this.mAnchorView.stopTranslateXAnimation();
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.accessibility.floatingmenu.BaseTooltipView
    public void show() {
        super.show();
        this.mAnchorView.startTranslateXAnimation();
    }
}
