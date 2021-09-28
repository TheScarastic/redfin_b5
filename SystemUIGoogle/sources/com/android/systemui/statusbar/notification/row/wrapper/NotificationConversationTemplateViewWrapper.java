package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationUtils;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationConversationTemplateViewWrapper.kt */
/* loaded from: classes.dex */
public final class NotificationConversationTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private View appName;
    private View conversationBadgeBg;
    private CachingIconView conversationIconView;
    private final ConversationLayout conversationLayout;
    private View conversationTitleView;
    private View expandBtn;
    private View expandBtnContainer;
    private View facePileBottom;
    private View facePileBottomBg;
    private View facePileTop;
    private ViewGroup imageMessageContainer;
    private View importanceRing;
    private MessagingLinearLayout messagingLinearLayout;
    private final int minHeightWithActions;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationConversationTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        Intrinsics.checkNotNullParameter(context, "ctx");
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(expandableNotificationRow, "row");
        this.minHeightWithActions = NotificationUtils.getFontScaledHeight(context, R$dimen.notification_messaging_actions_min_height);
        this.conversationLayout = (ConversationLayout) view;
    }

    private final void resolveViews() {
        MessagingLinearLayout messagingLinearLayout = this.conversationLayout.getMessagingLinearLayout();
        Intrinsics.checkNotNullExpressionValue(messagingLinearLayout, "conversationLayout.messagingLinearLayout");
        this.messagingLinearLayout = messagingLinearLayout;
        ViewGroup imageMessageContainer = this.conversationLayout.getImageMessageContainer();
        Intrinsics.checkNotNullExpressionValue(imageMessageContainer, "conversationLayout.imageMessageContainer");
        this.imageMessageContainer = imageMessageContainer;
        ConversationLayout conversationLayout = this.conversationLayout;
        CachingIconView requireViewById = conversationLayout.requireViewById(16908894);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(com.android.internal.R.id.conversation_icon)");
        this.conversationIconView = requireViewById;
        View requireViewById2 = conversationLayout.requireViewById(16908896);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "requireViewById(com.android.internal.R.id.conversation_icon_badge_bg)");
        this.conversationBadgeBg = requireViewById2;
        View requireViewById3 = conversationLayout.requireViewById(16908954);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "requireViewById(com.android.internal.R.id.expand_button)");
        this.expandBtn = requireViewById3;
        View requireViewById4 = conversationLayout.requireViewById(16908956);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "requireViewById(com.android.internal.R.id.expand_button_container)");
        this.expandBtnContainer = requireViewById4;
        View requireViewById5 = conversationLayout.requireViewById(16908897);
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "requireViewById(com.android.internal.R.id.conversation_icon_badge_ring)");
        this.importanceRing = requireViewById5;
        View requireViewById6 = conversationLayout.requireViewById(16908763);
        Intrinsics.checkNotNullExpressionValue(requireViewById6, "requireViewById(com.android.internal.R.id.app_name_text)");
        this.appName = requireViewById6;
        View requireViewById7 = conversationLayout.requireViewById(16908900);
        Intrinsics.checkNotNullExpressionValue(requireViewById7, "requireViewById(com.android.internal.R.id.conversation_text)");
        this.conversationTitleView = requireViewById7;
        this.facePileTop = conversationLayout.findViewById(16908892);
        this.facePileBottom = conversationLayout.findViewById(16908890);
        this.facePileBottomBg = conversationLayout.findViewById(16908891);
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
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        View view = this.conversationTitleView;
        if (view != null) {
            viewTransformationHelper.addTransformedView(1, view);
            View[] viewArr = new View[2];
            MessagingLinearLayout messagingLinearLayout = this.messagingLinearLayout;
            if (messagingLinearLayout != null) {
                viewArr[0] = messagingLinearLayout;
                View view2 = this.appName;
                if (view2 != null) {
                    viewArr[1] = view2;
                    addTransformedViews(viewArr);
                    ViewTransformationHelper viewTransformationHelper2 = this.mTransformationHelper;
                    ViewGroup viewGroup = this.imageMessageContainer;
                    if (viewGroup != null) {
                        NotificationMessagingTemplateViewWrapper.setCustomImageMessageTransform(viewTransformationHelper2, viewGroup);
                        View[] viewArr2 = new View[7];
                        CachingIconView cachingIconView = this.conversationIconView;
                        if (cachingIconView != null) {
                            viewArr2[0] = cachingIconView;
                            View view3 = this.conversationBadgeBg;
                            if (view3 != null) {
                                viewArr2[1] = view3;
                                View view4 = this.expandBtn;
                                if (view4 != null) {
                                    viewArr2[2] = view4;
                                    View view5 = this.importanceRing;
                                    if (view5 != null) {
                                        viewArr2[3] = view5;
                                        viewArr2[4] = this.facePileTop;
                                        viewArr2[5] = this.facePileBottom;
                                        viewArr2[6] = this.facePileBottomBg;
                                        addViewsTransformingToSimilar(viewArr2);
                                        return;
                                    }
                                    Intrinsics.throwUninitializedPropertyAccessException("importanceRing");
                                    throw null;
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
                    Intrinsics.throwUninitializedPropertyAccessException("imageMessageContainer");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("appName");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("messagingLinearLayout");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("conversationTitleView");
        throw null;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public View getShelfTransformationTarget() {
        if (!this.conversationLayout.isImportantConversation()) {
            return super.getShelfTransformationTarget();
        }
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
            throw null;
        } else if (cachingIconView.getVisibility() == 8) {
            return super.getShelfTransformationTarget();
        } else {
            CachingIconView cachingIconView2 = this.conversationIconView;
            if (cachingIconView2 != null) {
                return cachingIconView2;
            }
            Intrinsics.throwUninitializedPropertyAccessException("conversationIconView");
            throw null;
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void setRemoteInputVisible(boolean z) {
        this.conversationLayout.showHistoricMessages(z);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void updateExpandability(boolean z, View.OnClickListener onClickListener, boolean z2) {
        Intrinsics.checkNotNullParameter(onClickListener, "onClickListener");
        this.conversationLayout.updateExpandability(z, onClickListener);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public int getMinLayoutHeight() {
        View view = this.mActionsContainer;
        if (view == null || view.getVisibility() == 8) {
            return super.getMinLayoutHeight();
        }
        return this.minHeightWithActions;
    }
}
