package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$layout;
import com.android.systemui.privacy.OngoingPrivacyChip;
import com.android.systemui.privacy.PrivacyItem;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public final class PrivacyEvent implements StatusEvent {
    private String contentDescription;
    private final boolean forceVisible;
    private final int priority;
    private OngoingPrivacyChip privacyChip;
    private List<PrivacyItem> privacyItems;
    private final boolean showAnimation;
    private final Function1<Context, View> viewCreator;

    public PrivacyEvent() {
        this(false, 1, null);
    }

    public PrivacyEvent(boolean z) {
        this.showAnimation = z;
        this.priority = 100;
        this.forceVisible = true;
        this.privacyItems = CollectionsKt__CollectionsKt.emptyList();
        this.viewCreator = new Function1<Context, OngoingPrivacyChip>(this) { // from class: com.android.systemui.statusbar.events.PrivacyEvent$viewCreator$1
            final /* synthetic */ PrivacyEvent this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public final OngoingPrivacyChip invoke(Context context) {
                Intrinsics.checkNotNullParameter(context, "context");
                View inflate = LayoutInflater.from(context).inflate(R$layout.ongoing_privacy_chip, (ViewGroup) null);
                Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.privacy.OngoingPrivacyChip");
                OngoingPrivacyChip ongoingPrivacyChip = (OngoingPrivacyChip) inflate;
                ongoingPrivacyChip.setPrivacyList(this.this$0.getPrivacyItems());
                ongoingPrivacyChip.setContentDescription(this.this$0.getContentDescription());
                PrivacyEvent.access$setPrivacyChip$p(this.this$0, ongoingPrivacyChip);
                return ongoingPrivacyChip;
            }
        };
    }

    public /* synthetic */ PrivacyEvent(boolean z, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? true : z);
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getShowAnimation() {
        return this.showAnimation;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public String getContentDescription() {
        return this.contentDescription;
    }

    public void setContentDescription(String str) {
        this.contentDescription = str;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public int getPriority() {
        return this.priority;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean getForceVisible() {
        return this.forceVisible;
    }

    public final List<PrivacyItem> getPrivacyItems() {
        return this.privacyItems;
    }

    public final void setPrivacyItems(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "<set-?>");
        this.privacyItems = list;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public Function1<Context, View> getViewCreator() {
        return this.viewCreator;
    }

    public String toString() {
        String simpleName = PrivacyEvent.class.getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public boolean shouldUpdateFromEvent(StatusEvent statusEvent) {
        return (statusEvent instanceof PrivacyEvent) && (!Intrinsics.areEqual(((PrivacyEvent) statusEvent).privacyItems, this.privacyItems) || !Intrinsics.areEqual(statusEvent.getContentDescription(), getContentDescription()));
    }

    @Override // com.android.systemui.statusbar.events.StatusEvent
    public void updateFromEvent(StatusEvent statusEvent) {
        if (statusEvent instanceof PrivacyEvent) {
            this.privacyItems = ((PrivacyEvent) statusEvent).privacyItems;
            setContentDescription(statusEvent.getContentDescription());
            OngoingPrivacyChip ongoingPrivacyChip = this.privacyChip;
            if (ongoingPrivacyChip != null) {
                ongoingPrivacyChip.setContentDescription(statusEvent.getContentDescription());
            }
            OngoingPrivacyChip ongoingPrivacyChip2 = this.privacyChip;
            if (ongoingPrivacyChip2 != null) {
                ongoingPrivacyChip2.setPrivacyList(((PrivacyEvent) statusEvent).privacyItems);
            }
        }
    }
}
