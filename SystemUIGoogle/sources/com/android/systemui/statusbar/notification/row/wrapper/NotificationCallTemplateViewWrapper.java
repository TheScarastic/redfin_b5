package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.CallLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationCallTemplateViewWrapper.kt */
/* loaded from: classes.dex */
public final class NotificationCallTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private View appName;
    private final CallLayout callLayout;
    private View conversationBadgeBg;
    private CachingIconView conversationIconView;
    private View conversationTitleView;
    private View expandBtn;
    private final int minHeightWithActions;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationCallTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        Intrinsics.checkNotNullParameter(context, "ctx");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, R$dimen.notification_max_height);
        this.callLayout = (CallLayout) view;
    }

    private final void resolveViews() {
        CallLayout callLayout = this.callLayout;
        CachingIconView requireViewById = callLayout.requireViewById(16908894);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(com.android.internal.R.id.conversation_icon)");
        this.conversationIconView = requireViewById;
        View requireViewById2 = callLayout.requireViewById(16908896);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(com.android.internal.R.id.conversation_icon_badge_bg)");
        this.conversationBadgeBg = requireViewById2;
        View requireViewById3 = callLayout.requireViewById(16908954);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById(com.android.internal.R.id.expand_button)");
        this.expandBtn = requireViewById3;
        View requireViewById4 = callLayout.requireViewById(16908763);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById(com.android.internal.R.id.app_name_text)");
        this.appName = requireViewById4;
        View requireViewById5 = callLayout.requireViewById(16908900);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById(com.android.internal.R.id.conversation_text)");
        this.conversationTitleView = requireViewById5;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        View[] viewArr = new View[2];
        View view = this.appName;
        if (view != null) {
            viewArr[0] = view;
            View view2 = this.conversationTitleView;
            if (view2 != null) {
                viewArr[1] = view2;
                addTransformedViews(viewArr);
                View[] viewArr2 = new View[3];
                CachingIconView cachingIconView = this.conversationIconView;
                if (cachingIconView != null) {
                    viewArr2[0] = cachingIconView;
                    View view3 = this.conversationBadgeBg;
                    if (view3 != null) {
                        viewArr2[1] = view3;
                        View view4 = this.expandBtn;
                        if (view4 != null) {
                            viewArr2[2] = view4;
                            addViewsTransformingToSimilar(viewArr2);
                            return;
                        }
                        Intrinsics.throwUninitializedPropertyAccessException("expandBtn");
                        throw null;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("conversationBadgeBg");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("conversationTitleView");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("appName");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public int getMinLayoutHeight() {
        return this.minHeightWithActions;
    }
}
