package com.android.systemui.statusbar.phone;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.R$id;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.statusbar.notification.AboveShelfObserver;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.ToIntFunction;
/* loaded from: classes.dex */
public class NotificationsQuickSettingsContainer extends ConstraintLayout implements FragmentHostManager.FragmentListener, AboveShelfObserver.HasViewAboveShelfChangedListener {
    private int mBottomPadding;
    private boolean mCustomizerAnimating;
    private boolean mCustomizing;
    private boolean mDetailShowing;
    private boolean mHasViewsAboveShelf;
    private View mKeyguardStatusBar;
    private boolean mQsExpanded;
    private FrameLayout mQsFrame;
    private NotificationStackScrollLayout mStackScroller;
    private int mStackScrollerMargin;
    private ArrayList<View> mDrawingOrderedChildren = new ArrayList<>();
    private ArrayList<View> mLayoutDrawingOrder = new ArrayList<>();
    private final Comparator<View> mIndexComparator = Comparator.comparingInt(new ToIntFunction() { // from class: com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer$$ExternalSyntheticLambda0
        @Override // java.util.function.ToIntFunction
        public final int applyAsInt(Object obj) {
            return NotificationsQuickSettingsContainer.this.indexOfChild((View) obj);
        }
    });

    public NotificationsQuickSettingsContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mQsFrame = (FrameLayout) findViewById(R$id.qs_frame);
        NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) findViewById(R$id.notification_stack_scroller);
        this.mStackScroller = notificationStackScrollLayout;
        this.mStackScrollerMargin = ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) notificationStackScrollLayout.getLayoutParams())).bottomMargin;
        this.mKeyguardStatusBar = findViewById(R$id.keyguard_header);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        FragmentHostManager.get(this).addTagListener(QS.TAG, this);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        FragmentHostManager.get(this).removeTagListener(QS.TAG, this);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int stableInsetBottom = windowInsets.getStableInsetBottom();
        this.mBottomPadding = stableInsetBottom;
        setPadding(0, 0, 0, stableInsetBottom);
        return windowInsets;
    }

    /* access modifiers changed from: protected */
    @Override // androidx.constraintlayout.widget.ConstraintLayout, android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        this.mDrawingOrderedChildren.clear();
        this.mLayoutDrawingOrder.clear();
        if (this.mKeyguardStatusBar.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mKeyguardStatusBar);
            this.mLayoutDrawingOrder.add(this.mKeyguardStatusBar);
        }
        if (this.mQsFrame.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mQsFrame);
            this.mLayoutDrawingOrder.add(this.mQsFrame);
        }
        if (this.mStackScroller.getVisibility() == 0) {
            this.mDrawingOrderedChildren.add(this.mStackScroller);
            this.mLayoutDrawingOrder.add(this.mStackScroller);
        }
        this.mLayoutDrawingOrder.sort(this.mIndexComparator);
        super.dispatchDraw(canvas);
    }

    @Override // android.view.ViewGroup
    protected boolean drawChild(Canvas canvas, View view, long j) {
        int indexOf = this.mLayoutDrawingOrder.indexOf(view);
        if (indexOf >= 0) {
            return super.drawChild(canvas, this.mDrawingOrderedChildren.get(indexOf), j);
        }
        return super.drawChild(canvas, view, j);
    }

    @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
    public void onFragmentViewCreated(String str, Fragment fragment) {
        ((QS) fragment).setContainer(this);
    }

    public void setQsExpanded(boolean z) {
        if (this.mQsExpanded != z) {
            this.mQsExpanded = z;
            invalidate();
        }
    }

    public void setCustomizerAnimating(boolean z) {
        if (this.mCustomizerAnimating != z) {
            this.mCustomizerAnimating = z;
            invalidate();
        }
    }

    public void setCustomizerShowing(boolean z) {
        this.mCustomizing = z;
        updateBottomMargin();
        this.mStackScroller.setQsCustomizerShowing(z);
    }

    public void setDetailShowing(boolean z) {
        this.mDetailShowing = z;
        updateBottomMargin();
    }

    private void updateBottomMargin() {
        if (this.mCustomizing || this.mDetailShowing) {
            setPadding(0, 0, 0, 0);
            setBottomMargin(this.mStackScroller, 0);
            return;
        }
        setPadding(0, 0, 0, this.mBottomPadding);
        setBottomMargin(this.mStackScroller, this.mStackScrollerMargin);
    }

    private void setBottomMargin(View view, int i) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = i;
        view.setLayoutParams(layoutParams);
    }

    @Override // com.android.systemui.statusbar.notification.AboveShelfObserver.HasViewAboveShelfChangedListener
    public void onHasViewsAboveShelfChanged(boolean z) {
        this.mHasViewsAboveShelf = z;
        invalidate();
    }
}
