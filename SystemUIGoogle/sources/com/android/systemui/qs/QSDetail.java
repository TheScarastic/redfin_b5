package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Animatable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dependency;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSDetail extends LinearLayout {
    private boolean mAnimatingOpen;
    @VisibleForTesting
    QSDetailClipper mClipper;
    private boolean mClosingDetail;
    private NotificationsQuickSettingsContainer mContainer;
    private DetailAdapter mDetailAdapter;
    private ViewGroup mDetailContent;
    protected TextView mDetailDoneButton;
    protected TextView mDetailSettingsButton;
    private FalsingManager mFalsingManager;
    private QSFooter mFooter;
    private boolean mFullyExpanded;
    private QuickStatusBarHeader mHeader;
    protected QSTileHost mHost;
    private int mOpenX;
    private int mOpenY;
    protected View mQsDetailHeader;
    protected ImageView mQsDetailHeaderProgress;
    private Switch mQsDetailHeaderSwitch;
    private ViewStub mQsDetailHeaderSwitchStub;
    protected TextView mQsDetailHeaderTitle;
    private QSPanelController mQsPanelController;
    private boolean mScanState;
    private boolean mShouldAnimate;
    private boolean mSwitchState;
    private boolean mTriggeredExpand;
    private final SparseArray<View> mDetailViews = new SparseArray<>();
    private final UiEventLogger mUiEventLogger = QSEvents.INSTANCE.getQsUiEventsLogger();
    protected Callback mQsPanelCallback = new Callback() { // from class: com.android.systemui.qs.QSDetail.1
        @Override // com.android.systemui.qs.QSDetail.Callback
        public void onToggleStateChanged(final boolean z) {
            QSDetail.this.post(new Runnable() { // from class: com.android.systemui.qs.QSDetail.1.1
                @Override // java.lang.Runnable
                public void run() {
                    QSDetail qSDetail = QSDetail.this;
                    qSDetail.handleToggleStateChanged(z, qSDetail.mDetailAdapter != null && QSDetail.this.mDetailAdapter.getToggleEnabled());
                }
            });
        }

        @Override // com.android.systemui.qs.QSDetail.Callback
        public void onShowingDetail(final DetailAdapter detailAdapter, final int i, final int i2) {
            QSDetail.this.post(new Runnable() { // from class: com.android.systemui.qs.QSDetail.1.2
                @Override // java.lang.Runnable
                public void run() {
                    if (QSDetail.this.isAttachedToWindow()) {
                        QSDetail.this.handleShowingDetail(detailAdapter, i, i2, false);
                    }
                }
            });
        }

        @Override // com.android.systemui.qs.QSDetail.Callback
        public void onScanStateChanged(final boolean z) {
            QSDetail.this.post(new Runnable() { // from class: com.android.systemui.qs.QSDetail.1.3
                @Override // java.lang.Runnable
                public void run() {
                    QSDetail.this.handleScanStateChanged(z);
                }
            });
        }
    };
    private final AnimatorListenerAdapter mHideGridContentWhenDone = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSDetail.2
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            animator.removeListener(this);
            QSDetail.this.mAnimatingOpen = false;
            QSDetail.this.checkPendingAnimations();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (QSDetail.this.mDetailAdapter != null) {
                QSDetail.this.mQsPanelController.setGridContentVisibility(false);
                QSDetail.this.mHeader.setVisibility(4);
                QSDetail.this.mFooter.setVisibility(4);
            }
            QSDetail.this.mAnimatingOpen = false;
            QSDetail.this.checkPendingAnimations();
        }
    };
    private final AnimatorListenerAdapter mTeardownDetailWhenDone = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSDetail.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            QSDetail.this.mDetailContent.removeAllViews();
            QSDetail.this.setVisibility(4);
            QSDetail.this.mClosingDetail = false;
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onScanStateChanged(boolean z);

        void onShowingDetail(DetailAdapter detailAdapter, int i, int i2);

        void onToggleStateChanged(boolean z);
    }

    public QSDetail(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        TextView textView = this.mDetailDoneButton;
        int i = R$dimen.qs_detail_button_text_size;
        FontSizeUtils.updateFontSize(textView, i);
        FontSizeUtils.updateFontSize(this.mDetailSettingsButton, i);
        for (int i2 = 0; i2 < this.mDetailViews.size(); i2++) {
            this.mDetailViews.valueAt(i2).dispatchConfigurationChanged(configuration);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDetailContent = (ViewGroup) findViewById(16908290);
        this.mDetailSettingsButton = (TextView) findViewById(16908314);
        this.mDetailDoneButton = (TextView) findViewById(16908313);
        View findViewById = findViewById(R$id.qs_detail_header);
        this.mQsDetailHeader = findViewById;
        this.mQsDetailHeaderTitle = (TextView) findViewById.findViewById(16908310);
        this.mQsDetailHeaderSwitchStub = (ViewStub) this.mQsDetailHeader.findViewById(R$id.toggle_stub);
        this.mQsDetailHeaderProgress = (ImageView) findViewById(R$id.qs_detail_header_progress);
        updateDetailText();
        this.mClipper = new QSDetailClipper(this);
    }

    public void setContainer(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer) {
        this.mContainer = notificationsQuickSettingsContainer;
    }

    public void setQsPanel(QSPanelController qSPanelController, QuickStatusBarHeader quickStatusBarHeader, QSFooter qSFooter, FalsingManager falsingManager) {
        this.mQsPanelController = qSPanelController;
        this.mHeader = quickStatusBarHeader;
        this.mFooter = qSFooter;
        quickStatusBarHeader.setCallback(this.mQsPanelCallback);
        this.mQsPanelController.setCallback(this.mQsPanelCallback);
        this.mFalsingManager = falsingManager;
    }

    public void setHost(QSTileHost qSTileHost) {
        this.mHost = qSTileHost;
    }

    public boolean isShowingDetail() {
        return this.mDetailAdapter != null;
    }

    public void setFullyExpanded(boolean z) {
        this.mFullyExpanded = z;
    }

    public void setExpanded(boolean z) {
        if (!z) {
            this.mTriggeredExpand = false;
        }
    }

    private void updateDetailText() {
        DetailAdapter detailAdapter = this.mDetailAdapter;
        int i = 0;
        int doneText = detailAdapter != null ? detailAdapter.getDoneText() : 0;
        TextView textView = this.mDetailDoneButton;
        if (doneText == 0) {
            doneText = R$string.quick_settings_done;
        }
        textView.setText(doneText);
        DetailAdapter detailAdapter2 = this.mDetailAdapter;
        if (detailAdapter2 != null) {
            i = detailAdapter2.getSettingsText();
        }
        TextView textView2 = this.mDetailSettingsButton;
        if (i == 0) {
            i = R$string.quick_settings_more_settings;
        }
        textView2.setText(i);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int i = windowInsets.getInsets(WindowInsets.Type.navigationBars()).bottom;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.bottomMargin = i;
        setLayoutParams(marginLayoutParams);
        return super.onApplyWindowInsets(windowInsets);
    }

    public boolean isClosingDetail() {
        return this.mClosingDetail;
    }

    public void handleShowingDetail(DetailAdapter detailAdapter, int i, int i2, boolean z) {
        AnimatorListenerAdapter animatorListenerAdapter;
        boolean z2 = detailAdapter != null;
        boolean z3 = this.mDetailAdapter != null;
        setClickable(z2);
        if (z2) {
            setupDetailHeader(detailAdapter);
            if (!z || this.mFullyExpanded) {
                this.mTriggeredExpand = false;
            } else {
                this.mTriggeredExpand = true;
                ((CommandQueue) Dependency.get(CommandQueue.class)).animateExpandSettingsPanel(null);
            }
            this.mShouldAnimate = detailAdapter.shouldAnimate();
            this.mOpenX = i;
            this.mOpenY = i2;
        } else {
            i = this.mOpenX;
            i2 = this.mOpenY;
            if (z && this.mTriggeredExpand) {
                ((CommandQueue) Dependency.get(CommandQueue.class)).animateCollapsePanels();
                this.mTriggeredExpand = false;
            }
            this.mShouldAnimate = true;
        }
        boolean z4 = z3 != z2;
        if (z4 || z3) {
            if (z2) {
                int metricsCategory = detailAdapter.getMetricsCategory();
                View createDetailView = detailAdapter.createDetailView(((LinearLayout) this).mContext, this.mDetailViews.get(metricsCategory), this.mDetailContent);
                if (createDetailView != null) {
                    setupDetailFooter(detailAdapter);
                    this.mDetailContent.removeAllViews();
                    this.mDetailContent.addView(createDetailView);
                    this.mDetailViews.put(metricsCategory, createDetailView);
                    ((MetricsLogger) Dependency.get(MetricsLogger.class)).visible(detailAdapter.getMetricsCategory());
                    this.mUiEventLogger.log(detailAdapter.openDetailEvent());
                    announceForAccessibility(((LinearLayout) this).mContext.getString(R$string.accessibility_quick_settings_detail, detailAdapter.getTitle()));
                    this.mDetailAdapter = detailAdapter;
                    animatorListenerAdapter = this.mHideGridContentWhenDone;
                    setVisibility(0);
                    updateDetailText();
                } else {
                    throw new IllegalStateException("Must return detail view");
                }
            } else {
                if (z3) {
                    ((MetricsLogger) Dependency.get(MetricsLogger.class)).hidden(this.mDetailAdapter.getMetricsCategory());
                    this.mUiEventLogger.log(this.mDetailAdapter.closeDetailEvent());
                }
                this.mClosingDetail = true;
                this.mDetailAdapter = null;
                animatorListenerAdapter = this.mTeardownDetailWhenDone;
                if (this.mQsPanelController.isExpanded()) {
                    this.mHeader.setVisibility(0);
                    this.mFooter.setVisibility(0);
                    this.mQsPanelController.setGridContentVisibility(true);
                    this.mQsPanelCallback.onScanStateChanged(false);
                }
            }
            sendAccessibilityEvent(32);
            animateDetailVisibleDiff(i, i2, z4, animatorListenerAdapter);
            NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = this.mContainer;
            if (notificationsQuickSettingsContainer != null) {
                notificationsQuickSettingsContainer.setDetailShowing(z2);
            }
        }
    }

    protected void animateDetailVisibleDiff(int i, int i2, boolean z, Animator.AnimatorListener animatorListener) {
        if (z) {
            DetailAdapter detailAdapter = this.mDetailAdapter;
            this.mAnimatingOpen = detailAdapter != null;
            if (this.mFullyExpanded || detailAdapter != null) {
                setAlpha(1.0f);
                this.mClipper.updateCircularClip(this.mShouldAnimate, i, i2, this.mDetailAdapter != null, animatorListener);
                return;
            }
            animate().alpha(0.0f).setDuration(this.mShouldAnimate ? 300 : 0).setListener(animatorListener).start();
        }
    }

    protected void setupDetailFooter(DetailAdapter detailAdapter) {
        Intent settingsIntent = detailAdapter.getSettingsIntent();
        this.mDetailSettingsButton.setVisibility(settingsIntent != null ? 0 : 8);
        this.mDetailSettingsButton.setOnClickListener(new View.OnClickListener(detailAdapter, settingsIntent) { // from class: com.android.systemui.qs.QSDetail$$ExternalSyntheticLambda2
            public final /* synthetic */ DetailAdapter f$1;
            public final /* synthetic */ Intent f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                QSDetail.m173$r8$lambda$VqZCVfPBawAdmiNLLZoxg0UgrE(QSDetail.this, this.f$1, this.f$2, view);
            }
        });
        this.mDetailDoneButton.setOnClickListener(new View.OnClickListener(detailAdapter) { // from class: com.android.systemui.qs.QSDetail$$ExternalSyntheticLambda1
            public final /* synthetic */ DetailAdapter f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                QSDetail.$r8$lambda$VZtj6pgpWxcP7qbcg9rYNAtBk_E(QSDetail.this, this.f$1, view);
            }
        });
    }

    public /* synthetic */ void lambda$setupDetailFooter$0(DetailAdapter detailAdapter, Intent intent, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            ((MetricsLogger) Dependency.get(MetricsLogger.class)).action(929, detailAdapter.getMetricsCategory());
            this.mUiEventLogger.log(detailAdapter.moreSettingsEvent());
            ((ActivityStarter) Dependency.get(ActivityStarter.class)).postStartActivityDismissingKeyguard(intent, 0);
        }
    }

    public /* synthetic */ void lambda$setupDetailFooter$1(DetailAdapter detailAdapter, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            announceForAccessibility(((LinearLayout) this).mContext.getString(R$string.accessibility_desc_quick_settings));
            if (!detailAdapter.onDoneButtonClicked()) {
                this.mQsPanelController.closeDetail();
            }
        }
    }

    protected void setupDetailHeader(DetailAdapter detailAdapter) {
        this.mQsDetailHeaderTitle.setText(detailAdapter.getTitle());
        Boolean toggleState = detailAdapter.getToggleState();
        if (toggleState == null) {
            Switch r4 = this.mQsDetailHeaderSwitch;
            if (r4 != null) {
                r4.setVisibility(4);
            }
            this.mQsDetailHeader.setClickable(false);
            return;
        }
        if (this.mQsDetailHeaderSwitch == null) {
            this.mQsDetailHeaderSwitch = (Switch) this.mQsDetailHeaderSwitchStub.inflate();
        }
        this.mQsDetailHeaderSwitch.setVisibility(0);
        handleToggleStateChanged(toggleState.booleanValue(), detailAdapter.getToggleEnabled());
        this.mQsDetailHeader.setClickable(true);
        this.mQsDetailHeader.setOnClickListener(new View.OnClickListener(detailAdapter) { // from class: com.android.systemui.qs.QSDetail$$ExternalSyntheticLambda0
            public final /* synthetic */ DetailAdapter f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                QSDetail.$r8$lambda$8LUI4R54vSdqfRAPCTrGHJG3tBw(QSDetail.this, this.f$1, view);
            }
        });
    }

    public /* synthetic */ void lambda$setupDetailHeader$2(DetailAdapter detailAdapter, View view) {
        if (!this.mFalsingManager.isFalseTap(1)) {
            boolean z = !this.mQsDetailHeaderSwitch.isChecked();
            this.mQsDetailHeaderSwitch.setChecked(z);
            detailAdapter.setToggleState(z);
        }
    }

    public void handleToggleStateChanged(boolean z, boolean z2) {
        this.mSwitchState = z;
        if (!this.mAnimatingOpen) {
            Switch r0 = this.mQsDetailHeaderSwitch;
            if (r0 != null) {
                r0.setChecked(z);
            }
            this.mQsDetailHeader.setEnabled(z2);
            Switch r1 = this.mQsDetailHeaderSwitch;
            if (r1 != null) {
                r1.setEnabled(z2);
            }
        }
    }

    public void handleScanStateChanged(boolean z) {
        if (this.mScanState != z) {
            this.mScanState = z;
            Animatable animatable = (Animatable) this.mQsDetailHeaderProgress.getDrawable();
            if (z) {
                this.mQsDetailHeaderProgress.animate().cancel();
                ViewPropertyAnimator alpha = this.mQsDetailHeaderProgress.animate().alpha(1.0f);
                Objects.requireNonNull(animatable);
                alpha.withEndAction(new Runnable(animatable) { // from class: com.android.systemui.qs.QSDetail$$ExternalSyntheticLambda3
                    public final /* synthetic */ Animatable f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.start();
                    }
                }).start();
                return;
            }
            this.mQsDetailHeaderProgress.animate().cancel();
            ViewPropertyAnimator alpha2 = this.mQsDetailHeaderProgress.animate().alpha(0.0f);
            Objects.requireNonNull(animatable);
            alpha2.withEndAction(new Runnable(animatable) { // from class: com.android.systemui.qs.QSDetail$$ExternalSyntheticLambda4
                public final /* synthetic */ Animatable f$0;

                {
                    this.f$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.f$0.stop();
                }
            }).start();
        }
    }

    public void checkPendingAnimations() {
        boolean z = this.mSwitchState;
        DetailAdapter detailAdapter = this.mDetailAdapter;
        handleToggleStateChanged(z, detailAdapter != null && detailAdapter.getToggleEnabled());
    }
}
