package com.android.systemui.qs.customize;

import android.content.Context;
import android.text.TextUtils;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CustomizeTileView.kt */
/* loaded from: classes.dex */
public final class CustomizeTileView extends QSTileViewImpl {
    private boolean showAppLabel;
    private boolean showSideView = true;

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    protected boolean animationsEnabled() {
        return false;
    }

    @Override // android.view.View
    public boolean isLongClickable() {
        return false;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public CustomizeTileView(Context context, QSIconView qSIconView) {
        super(context, qSIconView, false);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, "icon");
    }

    public final void setShowAppLabel(boolean z) {
        this.showAppLabel = z;
        mo193getSecondaryLabel().setVisibility(getVisibilityState(mo193getSecondaryLabel().getText()));
    }

    public final void setShowSideView(boolean z) {
        this.showSideView = z;
        if (!z) {
            getSideView().setVisibility(8);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileViewImpl
    protected void handleStateChanged(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        super.handleStateChanged(state);
        setShowRippleEffect(false);
        mo193getSecondaryLabel().setVisibility(getVisibilityState(state.secondaryLabel));
        if (!this.showSideView) {
            getSideView().setVisibility(8);
        }
    }

    private final int getVisibilityState(CharSequence charSequence) {
        return (!this.showAppLabel || TextUtils.isEmpty(charSequence)) ? 8 : 0;
    }

    public final void changeState(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        handleStateChanged(state);
    }
}
