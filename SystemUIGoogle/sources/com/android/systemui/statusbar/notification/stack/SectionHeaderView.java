package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.notification.row.StackScrollerDecorView;
/* loaded from: classes.dex */
public class SectionHeaderView extends StackScrollerDecorView {
    private ImageView mClearAllButton;
    private ViewGroup mContents;
    private Integer mLabelTextId;
    private TextView mLabelView;
    private View.OnClickListener mLabelClickListener = null;
    private View.OnClickListener mOnClearClickListener = null;

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findSecondaryView() {
        return null;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isTransparent() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return true;
    }

    public SectionHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView, android.view.View
    public void onFinishInflate() {
        this.mContents = (ViewGroup) requireViewById(R$id.content);
        bindContents();
        super.onFinishInflate();
        setVisible(true, false);
    }

    private void bindContents() {
        this.mLabelView = (TextView) requireViewById(R$id.header_label);
        ImageView imageView = (ImageView) requireViewById(R$id.btn_clear_all);
        this.mClearAllButton = imageView;
        View.OnClickListener onClickListener = this.mOnClearClickListener;
        if (onClickListener != null) {
            imageView.setOnClickListener(onClickListener);
        }
        View.OnClickListener onClickListener2 = this.mLabelClickListener;
        if (onClickListener2 != null) {
            this.mLabelView.setOnClickListener(onClickListener2);
        }
        Integer num = this.mLabelTextId;
        if (num != null) {
            this.mLabelView.setText(num.intValue());
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.StackScrollerDecorView
    protected View findContentView() {
        return this.mContents;
    }

    /* access modifiers changed from: package-private */
    public void setAreThereDismissableGentleNotifs(boolean z) {
        this.mClearAllButton.setVisibility(z ? 0 : 8);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public void setOnHeaderClickListener(View.OnClickListener onClickListener) {
        this.mLabelClickListener = onClickListener;
        this.mLabelView.setOnClickListener(onClickListener);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public void applyContentTransformation(float f, float f2) {
        super.applyContentTransformation(f, f2);
        this.mLabelView.setAlpha(f);
        this.mLabelView.setTranslationY(f2);
        this.mClearAllButton.setAlpha(f);
        this.mClearAllButton.setTranslationY(f2);
    }

    public void setOnClearAllClickListener(View.OnClickListener onClickListener) {
        this.mOnClearClickListener = onClickListener;
        this.mClearAllButton.setOnClickListener(onClickListener);
    }

    public void setHeaderText(int i) {
        this.mLabelTextId = Integer.valueOf(i);
        this.mLabelView.setText(i);
    }

    /* access modifiers changed from: package-private */
    public void setForegroundColor(int i) {
        this.mLabelView.setTextColor(i);
        this.mClearAllButton.setImageTintList(ColorStateList.valueOf(i));
    }
}
