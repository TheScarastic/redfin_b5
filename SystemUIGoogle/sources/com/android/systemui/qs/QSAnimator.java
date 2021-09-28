package com.android.systemui.qs;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.animation.UniqueObjectHostView;
import com.android.wm.shell.animation.Interpolators;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class QSAnimator implements QSHost.Callback, PagedTileLayout.PageListener, TouchAnimator.Listener, View.OnLayoutChangeListener, View.OnAttachStateChangeListener, TunerService.Tunable {
    private TouchAnimator mAllPagesDelayedAnimator;
    private boolean mAllowFancy;
    private TouchAnimator mBrightnessAnimator;
    private final Executor mExecutor;
    private TouchAnimator mFirstPageAnimator;
    private TouchAnimator mFirstPageDelayedAnimator;
    private boolean mFullRows;
    private final QSTileHost mHost;
    private float mLastPosition;
    private TouchAnimator mNonfirstPageAnimator;
    private TouchAnimator mNonfirstPageDelayedAnimator;
    private int mNumQuickTiles;
    private boolean mOnKeyguard;
    private HeightExpansionAnimator mOtherTilesExpandAnimator;
    private PagedTileLayout mPagedLayout;
    private HeightExpansionAnimator mQQSTileHeightAnimator;
    private QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private final QS mQs;
    private final QSPanelController mQsPanelController;
    private final QuickQSPanelController mQuickQSPanelController;
    private final QuickQSPanel mQuickQsPanel;
    private final QuickStatusBarHeader mQuickStatusBarHeader;
    private final QSSecurityFooter mSecurityFooter;
    private boolean mShowCollapsedOnKeyguard;
    private boolean mToShowing;
    private boolean mTranslateWhileExpanding;
    private TouchAnimator mTranslationXAnimator;
    private TouchAnimator mTranslationYAnimator;
    private final TunerService mTunerService;
    private final ArrayList<View> mAllViews = new ArrayList<>();
    private final ArrayList<View> mQuickQsViews = new ArrayList<>();
    private boolean mOnFirstPage = true;
    private boolean mNeedsAnimatorUpdate = false;
    private final TouchAnimator.Listener mNonFirstPageListener = new TouchAnimator.ListenerAdapter() { // from class: com.android.systemui.qs.QSAnimator.1
        @Override // com.android.systemui.qs.TouchAnimator.ListenerAdapter, com.android.systemui.qs.TouchAnimator.Listener
        public void onAnimationAtEnd() {
            QSAnimator.this.mQuickQsPanel.setVisibility(4);
        }

        @Override // com.android.systemui.qs.TouchAnimator.Listener
        public void onAnimationStarted() {
            QSAnimator.this.mQuickQsPanel.setVisibility(0);
        }
    };
    private final Runnable mUpdateAnimators = new Runnable() { // from class: com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            QSAnimator.this.lambda$new$0();
        }
    };

    public QSAnimator(QS qs, QuickQSPanel quickQSPanel, QuickStatusBarHeader quickStatusBarHeader, QSPanelController qSPanelController, QuickQSPanelController quickQSPanelController, QSTileHost qSTileHost, QSSecurityFooter qSSecurityFooter, Executor executor, TunerService tunerService, QSExpansionPathInterpolator qSExpansionPathInterpolator) {
        this.mQs = qs;
        this.mQuickQsPanel = quickQSPanel;
        this.mQsPanelController = qSPanelController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQuickStatusBarHeader = quickStatusBarHeader;
        this.mSecurityFooter = qSSecurityFooter;
        this.mHost = qSTileHost;
        this.mExecutor = executor;
        this.mTunerService = tunerService;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        qSTileHost.addCallback(this);
        qSPanelController.addOnAttachStateChangeListener(this);
        qs.getView().addOnLayoutChangeListener(this);
        if (qSPanelController.isAttachedToWindow()) {
            onViewAttachedToWindow(null);
        }
        QSPanel.QSTileLayout tileLayout = qSPanelController.getTileLayout();
        if (tileLayout instanceof PagedTileLayout) {
            this.mPagedLayout = (PagedTileLayout) tileLayout;
        } else {
            Log.w("QSAnimator", "QS Not using page layout");
        }
        qSPanelController.setPageListener(this);
    }

    public void onRtlChanged() {
        updateAnimators();
    }

    public void requestAnimatorUpdate() {
        this.mNeedsAnimatorUpdate = true;
    }

    public void setOnKeyguard(boolean z) {
        this.mOnKeyguard = z;
        updateQQSVisibility();
        if (this.mOnKeyguard) {
            clearAnimationState();
        }
    }

    /* access modifiers changed from: package-private */
    public void startAlphaAnimation(boolean z) {
        if (z != this.mToShowing) {
            this.mToShowing = z;
            if (z) {
                CrossFadeHelper.fadeIn(this.mQs.getView(), 200, 0);
            } else {
                CrossFadeHelper.fadeOut(this.mQs.getView(), 50, 0, null);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setShowCollapsedOnKeyguard(boolean z) {
        this.mShowCollapsedOnKeyguard = z;
        updateQQSVisibility();
        setCurrentPosition();
    }

    private void setCurrentPosition() {
        setPosition(this.mLastPosition);
    }

    private void updateQQSVisibility() {
        this.mQuickQsPanel.setVisibility((!this.mOnKeyguard || this.mShowCollapsedOnKeyguard) ? 0 : 4);
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        this.mTunerService.addTunable(this, "sysui_qs_fancy_anim", "sysui_qs_move_whole_rows");
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        this.mHost.removeCallback(this);
        this.mTunerService.removeTunable(this);
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("sysui_qs_fancy_anim".equals(str)) {
            boolean parseIntegerSwitch = TunerService.parseIntegerSwitch(str2, true);
            this.mAllowFancy = parseIntegerSwitch;
            if (!parseIntegerSwitch) {
                clearAnimationState();
            }
        } else if ("sysui_qs_move_whole_rows".equals(str)) {
            this.mFullRows = TunerService.parseIntegerSwitch(str2, true);
        }
        updateAnimators();
    }

    @Override // com.android.systemui.qs.PagedTileLayout.PageListener
    public void onPageChanged(boolean z) {
        if (this.mOnFirstPage != z) {
            if (!z) {
                clearAnimationState();
            }
            this.mOnFirstPage = z;
        }
    }

    private void translateContent(View view, View view2, View view3, int i, int i2, int[] iArr, TouchAnimator.Builder builder, TouchAnimator.Builder builder2) {
        getRelativePosition(iArr, view, view3);
        int i3 = iArr[0];
        int i4 = iArr[1];
        getRelativePosition(iArr, view2, view3);
        int i5 = iArr[0];
        int i6 = iArr[1];
        int i7 = (i5 - i3) - i;
        builder.addFloat(view, "translationX", 0.0f, (float) i7);
        builder.addFloat(view2, "translationX", (float) (-i7), 0.0f);
        int i8 = (i6 - i4) - i2;
        builder2.addFloat(view, "translationY", 0.0f, (float) i8);
        builder2.addFloat(view2, "translationY", (float) (-i8), 0.0f);
        this.mAllViews.add(view);
        this.mAllViews.add(view2);
    }

    private void updateAnimators() {
        QSPanel.QSTileLayout qSTileLayout;
        UniqueObjectHostView uniqueObjectHostView;
        String str;
        QSPanel.QSTileLayout qSTileLayout2;
        String str2;
        String str3;
        char c;
        int i;
        int i2;
        QSAnimator qSAnimator;
        QSAnimator qSAnimator2 = this;
        qSAnimator2.mNeedsAnimatorUpdate = false;
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        TouchAnimator.Builder builder2 = new TouchAnimator.Builder();
        TouchAnimator.Builder builder3 = new TouchAnimator.Builder();
        Collection<QSTile> tiles = qSAnimator2.mHost.getTiles();
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        clearAnimationState();
        qSAnimator2.mAllViews.clear();
        qSAnimator2.mQuickQsViews.clear();
        qSAnimator2.mQQSTileHeightAnimator = null;
        qSAnimator2.mOtherTilesExpandAnimator = null;
        qSAnimator2.mNumQuickTiles = qSAnimator2.mQuickQsPanel.getNumQuickTiles();
        QSPanel.QSTileLayout tileLayout = qSAnimator2.mQsPanelController.getTileLayout();
        qSAnimator2.mAllViews.add((View) tileLayout);
        int measuredHeight = ((qSAnimator2.mQs.getView() != null ? qSAnimator2.mQs.getView().getMeasuredHeight() : 0) - qSAnimator2.mQs.getHeader().getBottom()) + qSAnimator2.mQs.getHeader().getPaddingBottom();
        if (!qSAnimator2.mTranslateWhileExpanding) {
            measuredHeight = (int) (((float) measuredHeight) * 0.1f);
        }
        int i3 = measuredHeight;
        char c2 = 1;
        String str4 = "translationY";
        builder.addFloat(tileLayout, str4, (float) i3, 0.0f);
        String str5 = "alpha";
        if (qSAnimator2.mQsPanelController.areThereTiles()) {
            int i4 = 0;
            int i5 = 0;
            for (QSTile qSTile : tiles) {
                QSTileView tileView = qSAnimator2.mQsPanelController.getTileView(qSTile);
                if (tileView == null) {
                    Log.e("QSAnimator", "tileView is null " + qSTile.getTileSpec());
                    str = str5;
                } else {
                    View iconView = tileView.getIcon().getIconView();
                    View view = qSAnimator2.mQs.getView();
                    str = str5;
                    if (i5 >= qSAnimator2.mQuickQSPanelController.getTileLayout().getNumVisibleTiles() || !qSAnimator2.mAllowFancy) {
                        qSTileLayout2 = tileLayout;
                        qSAnimator = qSAnimator2;
                        str2 = str;
                        if (qSAnimator.mFullRows) {
                            i2 = i5;
                            if (qSAnimator.isIconInAnimatedRow(i2)) {
                                i = i3;
                                c = 1;
                                str3 = str4;
                                builder.addFloat(tileView, str3, (float) (-i), 0.0f);
                                qSAnimator.mAllViews.add(iconView);
                                iArr2 = iArr2;
                            } else {
                                str3 = str4;
                                i = i3;
                            }
                        } else {
                            str3 = str4;
                            i = i3;
                            i2 = i5;
                        }
                        c = 1;
                        SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) qSAnimator.mQuickQsPanel.getTileLayout();
                        qSAnimator.getRelativePosition(iArr, sideLabelTileLayout, view);
                        iArr2 = iArr2;
                        qSAnimator.getRelativePosition(iArr2, tileView, view);
                        builder2.addFloat(tileView, str3, (float) (-(iArr2[1] - (iArr[1] + sideLabelTileLayout.getPhantomTopPosition(i2)))), 0.0f);
                        if (qSAnimator.mOtherTilesExpandAnimator == null) {
                            qSAnimator.mOtherTilesExpandAnimator = new HeightExpansionAnimator(qSAnimator, i4, tileView.getHeight());
                        }
                        qSAnimator.mOtherTilesExpandAnimator.addView(tileView);
                        tileView.setClipChildren(true);
                        tileView.setClipToPadding(true);
                        builder.addFloat(tileView.mo193getSecondaryLabel(), str2, 0.0f, 1.0f);
                    } else {
                        QSTileView tileView2 = qSAnimator2.mQuickQSPanelController.getTileView(qSTile);
                        if (tileView2 != null) {
                            qSAnimator2.getRelativePosition(iArr, tileView2, view);
                            qSAnimator2.getRelativePosition(iArr2, tileView, view);
                            int i6 = iArr2[1] - iArr[1];
                            int i7 = iArr2[0] - iArr[0];
                            int offsetTranslation = i6 - qSAnimator2.mQuickStatusBarHeader.getOffsetTranslation();
                            builder2.addFloat(tileView2, str4, 0.0f, (float) offsetTranslation);
                            builder2.addFloat(tileView, str4, (float) (-offsetTranslation), 0.0f);
                            builder3.addFloat(tileView2, "translationX", 0.0f, (float) i7);
                            builder3.addFloat(tileView, "translationX", (float) (-i7), 0.0f);
                            if (qSAnimator2.mQQSTileHeightAnimator == null) {
                                qSAnimator2.mQQSTileHeightAnimator = new HeightExpansionAnimator(qSAnimator2, tileView2.getHeight(), tileView.getHeight());
                                i4 = tileView2.getHeight();
                            }
                            qSAnimator2.mQQSTileHeightAnimator.addView(tileView2);
                            str2 = str;
                            qSTileLayout2 = tileLayout;
                            translateContent(tileView2.getIcon(), tileView.getIcon(), view, i7, i6, iArr, builder3, builder2);
                            translateContent(tileView2.getLabelContainer(), tileView.getLabelContainer(), view, i7, i6, iArr, builder3, builder2);
                            translateContent(tileView2.getSecondaryIcon(), tileView.getSecondaryIcon(), view, i7, i6, iArr, builder3, builder2);
                            builder.addFloat(tileView2.mo193getSecondaryLabel(), str2, 0.0f, 1.0f);
                            qSAnimator = this;
                            qSAnimator.mQuickQsViews.add(tileView);
                            qSAnimator.mAllViews.add(tileView2);
                            qSAnimator.mAllViews.add(tileView2.mo193getSecondaryLabel());
                            i4 = i4;
                            str3 = str4;
                            i = i3;
                            i2 = i5;
                            iArr2 = iArr2;
                            c = 1;
                        }
                    }
                    qSAnimator.mAllViews.add(tileView);
                    i5 = i2 + 1;
                    c2 = c;
                    i3 = i;
                    str4 = str3;
                    tileLayout = qSTileLayout2;
                    qSAnimator2 = qSAnimator;
                    str5 = str2;
                }
                str5 = str;
                c2 = 1;
            }
        }
        if (qSAnimator2.mAllowFancy) {
            View brightnessView = qSAnimator2.mQsPanelController.getBrightnessView();
            if (brightnessView != null) {
                float[] fArr = new float[2];
                fArr[0] = ((float) brightnessView.getMeasuredHeight()) * 0.5f;
                fArr[c2] = 0.0f;
                builder.addFloat(brightnessView, str4, fArr);
                qSAnimator2.mBrightnessAnimator = new TouchAnimator.Builder().addFloat(brightnessView, str5, 0.0f, 1.0f).addFloat(brightnessView, "sliderScaleY", 0.3f, 1.0f).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(0.3f).build();
                qSAnimator2.mAllViews.add(brightnessView);
            } else {
                qSAnimator2.mBrightnessAnimator = null;
            }
            qSAnimator2.mFirstPageAnimator = builder.setListener(qSAnimator2).build();
            qSTileLayout = tileLayout;
            qSAnimator2.mFirstPageDelayedAnimator = new TouchAnimator.Builder().addFloat(qSTileLayout, str5, 0.0f, 1.0f).build();
            TouchAnimator.Builder startDelay = new TouchAnimator.Builder().setStartDelay(0.86f);
            startDelay.addFloat(qSAnimator2.mSecurityFooter.getView(), str5, 0.0f, 1.0f);
            if (!qSAnimator2.mQsPanelController.shouldUseHorizontalLayout() || (uniqueObjectHostView = qSAnimator2.mQsPanelController.mMediaHost.hostView) == null) {
                qSAnimator2.mQsPanelController.mMediaHost.hostView.setAlpha(1.0f);
            } else {
                startDelay.addFloat(uniqueObjectHostView, str5, 0.0f, 1.0f);
            }
            qSAnimator2.mAllPagesDelayedAnimator = startDelay.build();
            qSAnimator2.mAllViews.add(qSAnimator2.mSecurityFooter.getView());
            builder2.setInterpolator(qSAnimator2.mQSExpansionPathInterpolator.getYInterpolator());
            builder3.setInterpolator(qSAnimator2.mQSExpansionPathInterpolator.getXInterpolator());
            qSAnimator2.mTranslationYAnimator = builder2.build();
            qSAnimator2.mTranslationXAnimator = builder3.build();
            HeightExpansionAnimator heightExpansionAnimator = qSAnimator2.mQQSTileHeightAnimator;
            if (heightExpansionAnimator != null) {
                heightExpansionAnimator.setInterpolator(qSAnimator2.mQSExpansionPathInterpolator.getYInterpolator());
            }
            HeightExpansionAnimator heightExpansionAnimator2 = qSAnimator2.mOtherTilesExpandAnimator;
            if (heightExpansionAnimator2 != null) {
                heightExpansionAnimator2.setInterpolator(qSAnimator2.mQSExpansionPathInterpolator.getYInterpolator());
            }
        } else {
            qSTileLayout = tileLayout;
        }
        qSAnimator2.mNonfirstPageAnimator = new TouchAnimator.Builder().addFloat(qSAnimator2.mQuickQsPanel, str5, 1.0f, 0.0f).setListener(qSAnimator2.mNonFirstPageListener).setEndDelay(0.5f).build();
        qSAnimator2.mNonfirstPageDelayedAnimator = new TouchAnimator.Builder().setStartDelay(0.14f).addFloat(qSTileLayout, str5, 0.0f, 1.0f).build();
    }

    private boolean isIconInAnimatedRow(int i) {
        PagedTileLayout pagedTileLayout = this.mPagedLayout;
        if (pagedTileLayout == null) {
            return false;
        }
        int columnCount = pagedTileLayout.getColumnCount();
        if (i < (((this.mNumQuickTiles + columnCount) - 1) / columnCount) * columnCount) {
            return true;
        }
        return false;
    }

    private void getRelativePosition(int[] iArr, View view, View view2) {
        iArr[0] = (view.getWidth() / 2) + 0;
        iArr[1] = 0;
        getRelativePositionInt(iArr, view, view2);
    }

    private void getRelativePositionInt(int[] iArr, View view, View view2) {
        if (view != view2 && view != null) {
            if (!isAPage(view)) {
                iArr[0] = iArr[0] + view.getLeft();
                iArr[1] = iArr[1] + view.getTop();
            }
            if (!(view instanceof PagedTileLayout)) {
                iArr[0] = iArr[0] - view.getScrollX();
                iArr[1] = iArr[1] - view.getScrollY();
            }
            getRelativePositionInt(iArr, (View) view.getParent(), view2);
        }
    }

    private boolean isAPage(View view) {
        return view.getClass().equals(SideLabelTileLayout.class);
    }

    public void setPosition(float f) {
        if (this.mNeedsAnimatorUpdate) {
            updateAnimators();
        }
        if (this.mFirstPageAnimator != null) {
            if (this.mOnKeyguard) {
                f = this.mShowCollapsedOnKeyguard ? 0.0f : 1.0f;
            }
            this.mLastPosition = f;
            if (!this.mOnFirstPage || !this.mAllowFancy) {
                this.mNonfirstPageAnimator.setPosition(f);
                this.mNonfirstPageDelayedAnimator.setPosition(f);
            } else {
                this.mQuickQsPanel.setAlpha(1.0f);
                this.mFirstPageAnimator.setPosition(f);
                this.mFirstPageDelayedAnimator.setPosition(f);
                this.mTranslationYAnimator.setPosition(f);
                this.mTranslationXAnimator.setPosition(f);
                HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
                if (heightExpansionAnimator != null) {
                    heightExpansionAnimator.setPosition(f);
                }
                HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherTilesExpandAnimator;
                if (heightExpansionAnimator2 != null) {
                    heightExpansionAnimator2.setPosition(f);
                }
            }
            if (this.mAllowFancy) {
                this.mAllPagesDelayedAnimator.setPosition(f);
                TouchAnimator touchAnimator = this.mBrightnessAnimator;
                if (touchAnimator != null) {
                    touchAnimator.setPosition(f);
                }
            }
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public void onAnimationAtStart() {
        this.mQuickQsPanel.setVisibility(0);
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public void onAnimationAtEnd() {
        this.mQuickQsPanel.setVisibility(4);
        int size = this.mQuickQsViews.size();
        for (int i = 0; i < size; i++) {
            this.mQuickQsViews.get(i).setVisibility(0);
        }
    }

    @Override // com.android.systemui.qs.TouchAnimator.Listener
    public void onAnimationStarted() {
        updateQQSVisibility();
        if (this.mOnFirstPage) {
            int size = this.mQuickQsViews.size();
            for (int i = 0; i < size; i++) {
                this.mQuickQsViews.get(i).setVisibility(4);
            }
        }
    }

    private void clearAnimationState() {
        int size = this.mAllViews.size();
        this.mQuickQsPanel.setAlpha(0.0f);
        for (int i = 0; i < size; i++) {
            View view = this.mAllViews.get(i);
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleY(1.0f);
            if (view instanceof SideLabelTileLayout) {
                SideLabelTileLayout sideLabelTileLayout = (SideLabelTileLayout) view;
                sideLabelTileLayout.setClipChildren(false);
                sideLabelTileLayout.setClipToPadding(false);
            }
        }
        HeightExpansionAnimator heightExpansionAnimator = this.mQQSTileHeightAnimator;
        if (heightExpansionAnimator != null) {
            heightExpansionAnimator.resetViewsHeights();
        }
        HeightExpansionAnimator heightExpansionAnimator2 = this.mOtherTilesExpandAnimator;
        if (heightExpansionAnimator2 != null) {
            heightExpansionAnimator2.resetViewsHeights();
        }
        int size2 = this.mQuickQsViews.size();
        for (int i2 = 0; i2 < size2; i2++) {
            this.mQuickQsViews.get(i2).setVisibility(0);
        }
    }

    @Override // android.view.View.OnLayoutChangeListener
    public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    @Override // com.android.systemui.qs.QSHost.Callback
    public void onTilesChanged() {
        this.mExecutor.execute(this.mUpdateAnimators);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        updateAnimators();
        setCurrentPosition();
    }

    public void setTranslateWhileExpanding(boolean z) {
        this.mTranslateWhileExpanding = z;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class HeightExpansionAnimator {
        private final ValueAnimator mAnimator;
        private final TouchAnimator.Listener mListener;
        private final ValueAnimator.AnimatorUpdateListener mUpdateListener;
        private final List<View> mViews = new ArrayList();

        HeightExpansionAnimator(TouchAnimator.Listener listener, int i, int i2) {
            AnonymousClass1 r0 = new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.qs.QSAnimator.HeightExpansionAnimator.1
                float mLastT = -1.0f;

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedFraction = valueAnimator.getAnimatedFraction();
                    int size = HeightExpansionAnimator.this.mViews.size();
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    for (int i3 = 0; i3 < size; i3++) {
                        View view = (View) HeightExpansionAnimator.this.mViews.get(i3);
                        view.setBottom(view.getTop() + intValue);
                        if (view instanceof HeightOverrideable) {
                            ((HeightOverrideable) view).setHeightOverride(intValue);
                        }
                    }
                    if (animatedFraction == 0.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtStart();
                    } else if (animatedFraction == 1.0f) {
                        HeightExpansionAnimator.this.mListener.onAnimationAtEnd();
                    } else {
                        float f = this.mLastT;
                        if (f <= 0.0f || f == 1.0f) {
                            HeightExpansionAnimator.this.mListener.onAnimationStarted();
                        }
                    }
                    this.mLastT = animatedFraction;
                }
            };
            this.mUpdateListener = r0;
            this.mListener = listener;
            ValueAnimator ofInt = ValueAnimator.ofInt(i, i2);
            this.mAnimator = ofInt;
            ofInt.setRepeatCount(-1);
            ofInt.setRepeatMode(2);
            ofInt.addUpdateListener(r0);
        }

        void addView(View view) {
            this.mViews.add(view);
        }

        void setInterpolator(TimeInterpolator timeInterpolator) {
            this.mAnimator.setInterpolator(timeInterpolator);
        }

        void setPosition(float f) {
            this.mAnimator.setCurrentFraction(f);
        }

        void resetViewsHeights() {
            int size = this.mViews.size();
            for (int i = 0; i < size; i++) {
                View view = this.mViews.get(i);
                view.setBottom(view.getTop() + view.getMeasuredHeight());
                if (view instanceof HeightOverrideable) {
                    ((HeightOverrideable) view).resetOverride();
                }
            }
        }
    }
}
