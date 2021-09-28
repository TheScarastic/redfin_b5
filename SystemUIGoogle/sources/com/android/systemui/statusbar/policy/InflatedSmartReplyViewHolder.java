package com.android.systemui.statusbar.policy;

import android.widget.Button;
import java.util.List;
/* compiled from: InflatedSmartReplyViewHolder.kt */
/* loaded from: classes.dex */
public final class InflatedSmartReplyViewHolder {
    private final SmartReplyView smartReplyView;
    private final List<Button> smartSuggestionButtons;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.util.List<? extends android.widget.Button> */
    /* JADX WARN: Multi-variable type inference failed */
    public InflatedSmartReplyViewHolder(SmartReplyView smartReplyView, List<? extends Button> list) {
        this.smartReplyView = smartReplyView;
        this.smartSuggestionButtons = list;
    }

    public final SmartReplyView getSmartReplyView() {
        return this.smartReplyView;
    }

    public final List<Button> getSmartSuggestionButtons() {
        return this.smartSuggestionButtons;
    }
}
