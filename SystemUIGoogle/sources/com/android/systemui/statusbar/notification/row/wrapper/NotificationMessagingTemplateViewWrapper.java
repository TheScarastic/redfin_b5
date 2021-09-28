package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.MessagingLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.TransformState;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;
/* loaded from: classes.dex */
public class NotificationMessagingTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private ViewGroup mImageMessageContainer;
    private MessagingLayout mMessagingLayout;
    private MessagingLinearLayout mMessagingLinearLayout;
    private final int mMinHeightWithActions;
    private final View mTitle = this.mView.findViewById(16908310);
    private final View mTitleInHeader = this.mView.findViewById(16909045);

    /* access modifiers changed from: protected */
    public NotificationMessagingTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.mMessagingLayout = (MessagingLayout) view;
        this.mMinHeightWithActions = NotificationUtils.getFontScaledHeight(context, R$dimen.notification_messaging_actions_min_height);
    }

    private void resolveViews() {
        this.mMessagingLinearLayout = this.mMessagingLayout.getMessagingLinearLayout();
        this.mImageMessageContainer = this.mMessagingLayout.getImageMessageContainer();
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        resolveViews();
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public void updateTransformedTypes() {
        View view;
        super.updateTransformedTypes();
        View view2 = this.mMessagingLinearLayout;
        if (view2 != null) {
            this.mTransformationHelper.addTransformedView(view2);
        }
        if (this.mTitle == null && (view = this.mTitleInHeader) != null) {
            this.mTransformationHelper.addTransformedView(1, view);
        }
        setCustomImageMessageTransform(this.mTransformationHelper, this.mImageMessageContainer);
    }

    /* access modifiers changed from: package-private */
    public static void setCustomImageMessageTransform(ViewTransformationHelper viewTransformationHelper, ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewTransformationHelper.setCustomTransformation(new ViewTransformationHelper.CustomTransformation() { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationMessagingTemplateViewWrapper.1
                @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
                public boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                    if (transformableView instanceof HybridNotificationView) {
                        return false;
                    }
                    transformState.ensureVisible();
                    return true;
                }

                @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
                public boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                    return transformTo(transformState, transformableView, f);
                }
            }, viewGroup.getId());
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void setRemoteInputVisible(boolean z) {
        this.mMessagingLayout.showHistoricMessages(z);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public int getMinLayoutHeight() {
        View view = this.mActionsContainer;
        if (view == null || view.getVisibility() == 8) {
            return super.getMinLayoutHeight();
        }
        return this.mMinHeightWithActions;
    }
}
