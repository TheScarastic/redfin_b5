package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.service.notification.StatusBarNotification;
import android.view.View;
import com.android.internal.widget.ImageFloatingTextView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
/* loaded from: classes.dex */
public class NotificationBigTextTemplateViewWrapper extends NotificationTemplateViewWrapper {
    private ImageFloatingTextView mBigtext;

    /* access modifiers changed from: protected */
    public NotificationBigTextTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
    }

    private void resolveViews(StatusBarNotification statusBarNotification) {
        this.mBigtext = this.mView.findViewById(16908794);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        resolveViews(expandableNotificationRow.getEntry().getSbn());
        super.onContentUpdated(expandableNotificationRow);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        View view = this.mBigtext;
        if (view != null) {
            this.mTransformationHelper.addTransformedView(2, view);
        }
    }
}
