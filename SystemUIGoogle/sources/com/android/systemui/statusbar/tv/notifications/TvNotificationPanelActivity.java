package com.android.systemui.statusbar.tv.notifications;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.SparseArray;
import android.view.View;
import androidx.leanback.widget.VerticalGridView;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.tv.notifications.TvNotificationHandler;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class TvNotificationPanelActivity extends Activity implements TvNotificationHandler.Listener {
    private VerticalGridView mNotificationListView;
    private View mNotificationPlaceholder;
    private TvNotificationAdapter mTvNotificationAdapter;
    private final TvNotificationHandler mTvNotificationHandler;
    private boolean mPanelAlreadyOpen = false;
    private final Consumer<Boolean> mBlurConsumer = new Consumer() { // from class: com.android.systemui.statusbar.tv.notifications.TvNotificationPanelActivity$$ExternalSyntheticLambda0
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            TvNotificationPanelActivity.this.enableBlur(((Boolean) obj).booleanValue());
        }
    };

    public TvNotificationPanelActivity(TvNotificationHandler tvNotificationHandler) {
        this.mTvNotificationHandler = tvNotificationHandler;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!maybeClosePanel(getIntent())) {
            this.mPanelAlreadyOpen = true;
            setContentView(R$layout.tv_notification_panel);
            this.mNotificationPlaceholder = findViewById(R$id.no_tv_notifications);
            this.mTvNotificationAdapter = new TvNotificationAdapter();
            VerticalGridView verticalGridView = (VerticalGridView) findViewById(R$id.notifications_list);
            this.mNotificationListView = verticalGridView;
            verticalGridView.setAdapter(this.mTvNotificationAdapter);
            this.mNotificationListView.setColumnWidth(R$dimen.tv_notification_panel_width);
            this.mTvNotificationHandler.setTvNotificationListener(this);
            notificationsUpdated(this.mTvNotificationHandler.getCurrentNotifications());
        }
    }

    @Override // com.android.systemui.statusbar.tv.notifications.TvNotificationHandler.Listener
    public void notificationsUpdated(SparseArray<StatusBarNotification> sparseArray) {
        this.mTvNotificationAdapter.setNotifications(sparseArray);
        int i = 0;
        boolean z = sparseArray.size() == 0;
        this.mNotificationListView.setVisibility(z ? 8 : 0);
        View view = this.mNotificationPlaceholder;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
    }

    @Override // android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        maybeClosePanel(intent);
    }

    private boolean maybeClosePanel(Intent intent) {
        if (!"android.app.action.CLOSE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()) && (!this.mPanelAlreadyOpen || !"android.app.action.TOGGLE_NOTIFICATION_HANDLER_PANEL".equals(intent.getAction()))) {
            return false;
        }
        finish();
        return true;
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().setGravity(8388613);
        getWindowManager().addCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    /* access modifiers changed from: private */
    public void enableBlur(boolean z) {
        if (z) {
            int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.tv_notification_blur_radius);
            getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R$color.tv_notification_blur_background_color)));
            getWindow().setBackgroundBlurRadius(dimensionPixelSize);
            return;
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(getColor(R$color.tv_notification_default_background_color)));
        getWindow().setBackgroundBlurRadius(0);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getWindowManager().removeCrossWindowBlurEnabledListener(this.mBlurConsumer);
    }

    @Override // android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mTvNotificationHandler.setTvNotificationListener(null);
    }
}
