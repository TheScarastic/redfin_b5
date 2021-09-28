package com.android.systemui.statusbar.notification.row;

import android.app.Notification;
import android.content.Context;
import android.content.res.Resources;
import android.service.notification.StatusBarNotification;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.widget.ConversationLayout;
import com.android.systemui.R$dimen;
import com.android.systemui.R$layout;
import com.android.systemui.R$plurals;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
/* loaded from: classes.dex */
public class HybridGroupManager {
    private final Context mContext;
    private int mOverflowNumberColor;
    private int mOverflowNumberPadding;
    private float mOverflowNumberSize;

    public HybridGroupManager(Context context) {
        this.mContext = context;
        initDimens();
    }

    public void initDimens() {
        Resources resources = this.mContext.getResources();
        this.mOverflowNumberSize = (float) resources.getDimensionPixelSize(R$dimen.group_overflow_number_size);
        this.mOverflowNumberPadding = resources.getDimensionPixelSize(R$dimen.group_overflow_number_padding);
    }

    private HybridNotificationView inflateHybridViewWithStyle(int i, View view, ViewGroup viewGroup) {
        int i2;
        LayoutInflater layoutInflater = (LayoutInflater) new ContextThemeWrapper(this.mContext, i).getSystemService(LayoutInflater.class);
        if (view instanceof ConversationLayout) {
            i2 = R$layout.hybrid_conversation_notification;
        } else {
            i2 = R$layout.hybrid_notification;
        }
        HybridNotificationView hybridNotificationView = (HybridNotificationView) layoutInflater.inflate(i2, viewGroup, false);
        viewGroup.addView(hybridNotificationView);
        return hybridNotificationView;
    }

    private TextView inflateOverflowNumber(ViewGroup viewGroup) {
        TextView textView = (TextView) ((LayoutInflater) this.mContext.getSystemService(LayoutInflater.class)).inflate(R$layout.hybrid_overflow_number, viewGroup, false);
        viewGroup.addView(textView);
        updateOverFlowNumberColor(textView);
        return textView;
    }

    private void updateOverFlowNumberColor(TextView textView) {
        textView.setTextColor(this.mOverflowNumberColor);
    }

    public void setOverflowNumberColor(TextView textView, int i) {
        this.mOverflowNumberColor = i;
        if (textView != null) {
            updateOverFlowNumberColor(textView);
        }
    }

    public HybridNotificationView bindFromNotification(HybridNotificationView hybridNotificationView, View view, StatusBarNotification statusBarNotification, ViewGroup viewGroup) {
        return bindFromNotificationWithStyle(hybridNotificationView, view, statusBarNotification, R$style.HybridNotification, viewGroup);
    }

    private HybridNotificationView bindFromNotificationWithStyle(HybridNotificationView hybridNotificationView, View view, StatusBarNotification statusBarNotification, int i, ViewGroup viewGroup) {
        if (hybridNotificationView == null) {
            hybridNotificationView = inflateHybridViewWithStyle(i, view, viewGroup);
        }
        hybridNotificationView.bind(resolveTitle(statusBarNotification.getNotification()), resolveText(statusBarNotification.getNotification()), view);
        return hybridNotificationView;
    }

    public static CharSequence resolveText(Notification notification) {
        CharSequence charSequence = notification.extras.getCharSequence("android.text");
        return charSequence == null ? notification.extras.getCharSequence("android.bigText") : charSequence;
    }

    public static CharSequence resolveTitle(Notification notification) {
        CharSequence charSequence = notification.extras.getCharSequence("android.title");
        return charSequence == null ? notification.extras.getCharSequence("android.title.big") : charSequence;
    }

    public TextView bindOverflowNumber(TextView textView, int i, ViewGroup viewGroup) {
        if (textView == null) {
            textView = inflateOverflowNumber(viewGroup);
        }
        String string = this.mContext.getResources().getString(R$string.notification_group_overflow_indicator, Integer.valueOf(i));
        if (!string.equals(textView.getText())) {
            textView.setText(string);
        }
        textView.setContentDescription(String.format(this.mContext.getResources().getQuantityString(R$plurals.notification_group_overflow_description, i), Integer.valueOf(i)));
        textView.setTextSize(0, this.mOverflowNumberSize);
        textView.setPaddingRelative(textView.getPaddingStart(), textView.getPaddingTop(), this.mOverflowNumberPadding, textView.getPaddingBottom());
        updateOverFlowNumberColor(textView);
        return textView;
    }
}
