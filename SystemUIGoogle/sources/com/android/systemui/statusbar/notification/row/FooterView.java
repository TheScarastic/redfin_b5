package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.systemui.statusbar.notification.stack.ViewState;
/* loaded from: classes.dex */
public class FooterView extends StackScrollerDecorView {
    private final int mClearAllTopPadding;
    private FooterViewButton mDismissButton;
    private FooterViewButton mManageButton;
    private boolean mShowHistory;

    public FooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClearAllTopPadding = context.getResources().getDimensionPixelSize(R$dimen.clear_all_padding_top);
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findContentView() {
        return findViewById(R$id.content);
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findSecondaryView() {
        return findViewById(R$id.dismiss_text);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDismissButton = (FooterViewButton) findSecondaryView();
        this.mManageButton = (FooterViewButton) findViewById(R$id.manage_text);
    }

    public void setTextColor(int i) {
        this.mManageButton.setTextColor(i);
        this.mDismissButton.setTextColor(i);
    }

    public void setManageButtonClickListener(View.OnClickListener onClickListener) {
        this.mManageButton.setOnClickListener(onClickListener);
    }

    public void setDismissButtonClickListener(View.OnClickListener onClickListener) {
        this.mDismissButton.setOnClickListener(onClickListener);
    }

    public boolean isOnEmptySpace(float f, float f2) {
        return f < this.mContent.getX() || f > this.mContent.getX() + ((float) this.mContent.getWidth()) || f2 < this.mContent.getY() || f2 > this.mContent.getY() + ((float) this.mContent.getHeight());
    }

    public void showHistory(boolean z) {
        this.mShowHistory = z;
        if (z) {
            FooterViewButton footerViewButton = this.mManageButton;
            int i = R$string.manage_notifications_history_text;
            footerViewButton.setText(i);
            this.mManageButton.setContentDescription(((FrameLayout) this).mContext.getString(i));
            return;
        }
        FooterViewButton footerViewButton2 = this.mManageButton;
        int i2 = R$string.manage_notifications_text;
        footerViewButton2.setText(i2);
        this.mManageButton.setContentDescription(((FrameLayout) this).mContext.getString(i2));
    }

    public boolean isHistoryShown() {
        return this.mShowHistory;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int color = getResources().getColor(R$color.notif_pill_text);
        Resources.Theme theme = getContext().getTheme();
        FooterViewButton footerViewButton = this.mDismissButton;
        Resources resources = getResources();
        int i = R$drawable.notif_footer_btn_background;
        footerViewButton.setBackground(resources.getDrawable(i, theme));
        this.mDismissButton.setTextColor(color);
        this.mManageButton.setBackground(getResources().getDrawable(i, theme));
        this.mManageButton = (FooterViewButton) findViewById(R$id.manage_text);
        this.mDismissButton.setText(R$string.clear_all_notifications_text);
        this.mManageButton.setTextColor(color);
        this.mDismissButton.setContentDescription(((FrameLayout) this).mContext.getString(R$string.accessibility_clear_all));
        showHistory(this.mShowHistory);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public ExpandableViewState createExpandableViewState() {
        return new FooterViewState();
    }

    /* loaded from: classes.dex */
    public class FooterViewState extends ExpandableViewState {
        public boolean hideContent;

        public FooterViewState() {
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void copyFrom(ViewState viewState) {
            super.copyFrom(viewState);
            if (viewState instanceof FooterViewState) {
                this.hideContent = ((FooterViewState) viewState).hideContent;
            }
        }

        @Override // com.android.systemui.statusbar.notification.stack.ExpandableViewState, com.android.systemui.statusbar.notification.stack.ViewState
        public void applyToView(View view) {
            super.applyToView(view);
            if (view instanceof FooterView) {
                ((FooterView) view).setContentVisible(!this.hideContent);
            }
        }
    }
}
