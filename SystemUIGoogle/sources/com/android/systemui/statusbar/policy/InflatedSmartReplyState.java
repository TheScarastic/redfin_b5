package com.android.systemui.statusbar.policy;

import android.app.Notification;
import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.List;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: InflatedSmartReplyState.kt */
/* loaded from: classes.dex */
public final class InflatedSmartReplyState {
    private final boolean hasPhishingAction;
    private final SmartReplyView.SmartActions smartActions;
    private final SmartReplyView.SmartReplies smartReplies;
    private final SuppressedActions suppressedActions;

    public InflatedSmartReplyState(SmartReplyView.SmartReplies smartReplies, SmartReplyView.SmartActions smartActions, SuppressedActions suppressedActions, boolean z) {
        this.smartReplies = smartReplies;
        this.smartActions = smartActions;
        this.suppressedActions = suppressedActions;
        this.hasPhishingAction = z;
    }

    public final SmartReplyView.SmartReplies getSmartReplies() {
        return this.smartReplies;
    }

    public final SmartReplyView.SmartActions getSmartActions() {
        return this.smartActions;
    }

    public final boolean getHasPhishingAction() {
        return this.hasPhishingAction;
    }

    public final List<CharSequence> getSmartRepliesList() {
        SmartReplyView.SmartReplies smartReplies = this.smartReplies;
        List<CharSequence> list = smartReplies == null ? null : smartReplies.choices;
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    public final List<Notification.Action> getSmartActionsList() {
        SmartReplyView.SmartActions smartActions = this.smartActions;
        List<Notification.Action> list = smartActions == null ? null : smartActions.actions;
        return list == null ? CollectionsKt__CollectionsKt.emptyList() : list;
    }

    public final List<Integer> getSuppressedActionIndices() {
        SuppressedActions suppressedActions = this.suppressedActions;
        return suppressedActions == null ? CollectionsKt__CollectionsKt.emptyList() : suppressedActions.getSuppressedActionIndices();
    }

    /* compiled from: InflatedSmartReplyState.kt */
    /* loaded from: classes.dex */
    public static final class SuppressedActions {
        private final List<Integer> suppressedActionIndices;

        public SuppressedActions(List<Integer> list) {
            Intrinsics.checkNotNullParameter(list, "suppressedActionIndices");
            this.suppressedActionIndices = list;
        }

        public final List<Integer> getSuppressedActionIndices() {
            return this.suppressedActionIndices;
        }
    }
}
