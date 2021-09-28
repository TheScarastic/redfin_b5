package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public abstract class PanelBar extends FrameLayout {
    public static final String TAG = PanelBar.class.getSimpleName();
    private boolean mBouncerShowing;
    private boolean mExpanded;
    PanelViewController mPanel;
    protected float mPanelFraction;
    private int mState = 0;
    private boolean mTracking;

    public void onClosingFinished() {
    }

    public void onExpandingFinished() {
    }

    public void onPanelCollapsed() {
    }

    public void onPanelFullyOpened() {
    }

    public void onPanelPeeked() {
    }

    public boolean panelEnabled() {
        return true;
    }

    public abstract void panelScrimMinFractionChanged(float f);

    public void go(int i) {
        this.mState = i;
        PanelViewController panelViewController = this.mPanel;
        if (panelViewController != null) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            panelViewController.setIsShadeOpening(z);
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("panel_bar_super_parcelable", super.onSaveInstanceState());
        bundle.putInt("state", this.mState);
        return bundle;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !(parcelable instanceof Bundle)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        Bundle bundle = (Bundle) parcelable;
        super.onRestoreInstanceState(bundle.getParcelable("panel_bar_super_parcelable"));
        if (bundle.containsKey("state")) {
            go(bundle.getInt("state", 0));
        }
    }

    public PanelBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setPanel(PanelViewController panelViewController) {
        this.mPanel = panelViewController;
        panelViewController.setBar(this);
    }

    public void setBouncerShowing(boolean z) {
        this.mBouncerShowing = z;
        int i = z ? 4 : 0;
        setImportantForAccessibility(i);
        updateVisibility();
        PanelViewController panelViewController = this.mPanel;
        if (panelViewController != null) {
            panelViewController.getView().setImportantForAccessibility(i);
        }
    }

    public float getExpansionFraction() {
        return this.mPanelFraction;
    }

    public boolean isExpanded() {
        return this.mExpanded;
    }

    /* access modifiers changed from: protected */
    public void updateVisibility() {
        this.mPanel.getView().setVisibility(shouldPanelBeVisible() ? 0 : 4);
    }

    /* access modifiers changed from: protected */
    public boolean shouldPanelBeVisible() {
        return this.mExpanded || this.mBouncerShowing;
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!panelEnabled()) {
            if (motionEvent.getAction() == 0) {
                Log.v(TAG, String.format("onTouch: all panels disabled, ignoring touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
            }
            return false;
        }
        if (motionEvent.getAction() == 0) {
            PanelViewController panelViewController = this.mPanel;
            if (panelViewController == null) {
                Log.v(TAG, String.format("onTouch: no panel for touch at (%d,%d)", Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
                return true;
            } else if (!panelViewController.isEnabled()) {
                Log.v(TAG, String.format("onTouch: panel (%s) is disabled, ignoring touch at (%d,%d)", panelViewController, Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())));
                return true;
            }
        }
        PanelViewController panelViewController2 = this.mPanel;
        return panelViewController2 == null || panelViewController2.getView().dispatchTouchEvent(motionEvent);
    }

    public void panelExpansionChanged(float f, boolean z) {
        boolean z2;
        if (!Float.isNaN(f)) {
            PanelViewController panelViewController = this.mPanel;
            this.mExpanded = z;
            this.mPanelFraction = f;
            updateVisibility();
            boolean z3 = true;
            if (z) {
                if (this.mState == 0) {
                    go(1);
                    onPanelPeeked();
                }
                if (panelViewController.getExpandedFraction() < 1.0f) {
                    z3 = false;
                }
                z2 = false;
            } else {
                z2 = true;
                z3 = false;
            }
            if (z3 && !this.mTracking) {
                go(2);
                onPanelFullyOpened();
            } else if (z2 && !this.mTracking && this.mState != 0) {
                go(0);
                onPanelCollapsed();
            }
        } else {
            throw new IllegalArgumentException("frac cannot be NaN");
        }
    }

    public void collapsePanel(boolean z, boolean z2, float f) {
        boolean z3;
        PanelViewController panelViewController = this.mPanel;
        if (!z || panelViewController.isFullyCollapsed()) {
            panelViewController.resetViews(false);
            panelViewController.setExpandedFraction(0.0f);
            z3 = false;
        } else {
            panelViewController.collapse(z2, f);
            z3 = true;
        }
        if (!z3 && this.mState != 0) {
            go(0);
            onPanelCollapsed();
        }
    }

    public boolean isClosed() {
        return this.mState == 0;
    }

    public void onTrackingStarted() {
        this.mTracking = true;
    }

    public void onTrackingStopped(boolean z) {
        this.mTracking = false;
    }
}
