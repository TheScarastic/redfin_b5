package com.android.systemui.qs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.lifecycle.Lifecycle;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.dagger.QSFragmentComponent;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
import com.android.systemui.statusbar.policy.RemoteInputQuickSettingsDisabler;
import com.android.systemui.util.InjectionInflationController;
import com.android.systemui.util.LifecycleFragment;
import com.android.systemui.util.Utils;
import com.android.systemui.util.animation.UniqueObjectHostView;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class QSFragment extends LifecycleFragment implements QS, CommandQueue.Callbacks, StatusBarStateController.StateListener {
    private final KeyguardBypassController mBypassController;
    private final CommandQueue mCommandQueue;
    private QSContainerImpl mContainer;
    private long mDelay;
    private DumpManager mDumpManager;
    private final FalsingManager mFalsingManager;
    private FeatureFlags mFeatureFlags;
    private QSFooter mFooter;
    protected QuickStatusBarHeader mHeader;
    private boolean mHeaderAnimating;
    private final QSTileHost mHost;
    private final InjectionInflationController mInjectionInflater;
    private float mLastHeaderTranslation;
    private boolean mLastKeyguardAndExpanded;
    private int mLastViewHeight;
    private int mLayoutDirection;
    private boolean mListening;
    private QS.HeightListener mPanelView;
    private QSAnimator mQSAnimator;
    private QSContainerImplController mQSContainerImplController;
    private QSCustomizerController mQSCustomizerController;
    private QSDetail mQSDetail;
    private QSPanelController mQSPanelController;
    protected NonInterceptingScrollView mQSPanelScrollView;
    private final MediaHost mQqsMediaHost;
    private final QSFragmentComponent.Factory mQsComponentFactory;
    private final QSDetailDisplayer mQsDetailDisplayer;
    private boolean mQsDisabled;
    private boolean mQsExpanded;
    private final MediaHost mQsMediaHost;
    private QuickQSPanelController mQuickQSPanelController;
    private final RemoteInputQuickSettingsDisabler mRemoteInputQuickSettingsDisabler;
    private QS.ScrollListener mScrollListener;
    private boolean mShowCollapsedOnKeyguard;
    private boolean mStackScrollerOverscrolling;
    private int mState;
    private final StatusBarStateController mStatusBarStateController;
    private boolean mTransitioningToFullShade;
    private boolean mTranslateWhileExpanding;
    private final Rect mQsBounds = new Rect();
    private float mLastQSExpansion = -1.0f;
    private int[] mTmpLocation = new int[2];
    private final ViewTreeObserver.OnPreDrawListener mStartHeaderSlidingIn = new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.qs.QSFragment.2
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        public boolean onPreDraw() {
            QSFragment.this.getView().getViewTreeObserver().removeOnPreDrawListener(this);
            QSFragment.this.getView().animate().translationY(0.0f).setStartDelay(QSFragment.this.mDelay).setDuration(448).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setListener(QSFragment.this.mAnimateHeaderSlidingInListener).start();
            return true;
        }
    };
    private final Animator.AnimatorListener mAnimateHeaderSlidingInListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSFragment.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            QSFragment.this.mHeaderAnimating = false;
            QSFragment.this.updateQsState();
        }
    };

    @Override // com.android.systemui.plugins.qs.QS
    public void setHasNotifications(boolean z) {
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setHeaderClickable(boolean z) {
    }

    public QSFragment(RemoteInputQuickSettingsDisabler remoteInputQuickSettingsDisabler, InjectionInflationController injectionInflationController, QSTileHost qSTileHost, StatusBarStateController statusBarStateController, CommandQueue commandQueue, QSDetailDisplayer qSDetailDisplayer, MediaHost mediaHost, MediaHost mediaHost2, KeyguardBypassController keyguardBypassController, QSFragmentComponent.Factory factory, FeatureFlags featureFlags, FalsingManager falsingManager, DumpManager dumpManager) {
        this.mRemoteInputQuickSettingsDisabler = remoteInputQuickSettingsDisabler;
        this.mInjectionInflater = injectionInflationController;
        this.mCommandQueue = commandQueue;
        this.mQsDetailDisplayer = qSDetailDisplayer;
        this.mQsMediaHost = mediaHost;
        this.mQqsMediaHost = mediaHost2;
        this.mQsComponentFactory = factory;
        commandQueue.observe(getLifecycle(), (Lifecycle) this);
        this.mHost = qSTileHost;
        this.mFeatureFlags = featureFlags;
        this.mFalsingManager = falsingManager;
        this.mBypassController = keyguardBypassController;
        this.mStatusBarStateController = statusBarStateController;
        this.mDumpManager = dumpManager;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.mInjectionInflater.injectable(layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), R$style.Theme_SystemUI_QuickSettings))).inflate(R$layout.qs_panel, viewGroup, false);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        QSFragmentComponent create = this.mQsComponentFactory.create(this);
        this.mQSPanelController = create.getQSPanelController();
        this.mQuickQSPanelController = create.getQuickQSPanelController();
        this.mQSPanelController.init();
        this.mQuickQSPanelController.init();
        NonInterceptingScrollView nonInterceptingScrollView = (NonInterceptingScrollView) view.findViewById(R$id.expanded_qs_scroll_view);
        this.mQSPanelScrollView = nonInterceptingScrollView;
        nonInterceptingScrollView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                QSFragment.this.lambda$onViewCreated$0(view2, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.mQSPanelScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnScrollChangeListener
            public final void onScrollChange(View view2, int i, int i2, int i3, int i4) {
                QSFragment.this.lambda$onViewCreated$1(view2, i, i2, i3, i4);
            }
        });
        this.mQSDetail = (QSDetail) view.findViewById(R$id.qs_detail);
        this.mHeader = (QuickStatusBarHeader) view.findViewById(R$id.header);
        this.mQSPanelController.setHeaderContainer((ViewGroup) view.findViewById(R$id.header_text_container));
        this.mFooter = create.getQSFooter();
        this.mQsDetailDisplayer.setQsPanelController(this.mQSPanelController);
        QSContainerImplController qSContainerImplController = create.getQSContainerImplController();
        this.mQSContainerImplController = qSContainerImplController;
        qSContainerImplController.init();
        QSContainerImpl view2 = this.mQSContainerImplController.getView();
        this.mContainer = view2;
        this.mDumpManager.registerDumpable(view2.getClass().getName(), this.mContainer);
        this.mQSDetail.setQsPanel(this.mQSPanelController, this.mHeader, this.mFooter, this.mFalsingManager);
        this.mQSAnimator = create.getQSAnimator();
        QSCustomizerController qSCustomizerController = create.getQSCustomizerController();
        this.mQSCustomizerController = qSCustomizerController;
        qSCustomizerController.init();
        this.mQSCustomizerController.setQs(this);
        if (bundle != null) {
            setExpanded(bundle.getBoolean("expanded"));
            setListening(bundle.getBoolean("listening"));
            setEditLocation(view);
            this.mQSCustomizerController.restoreInstanceState(bundle);
            if (this.mQsExpanded) {
                this.mQSPanelController.getTileLayout().restoreInstanceState(bundle);
            }
        }
        setHost(this.mHost);
        this.mStatusBarStateController.addCallback(this);
        onStateChanged(this.mStatusBarStateController.getState());
        view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnLayoutChangeListener
            public final void onLayoutChange(View view3, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                QSFragment.this.lambda$onViewCreated$2(view3, i, i2, i3, i4, i5, i6, i7, i8);
            }
        });
        this.mQSPanelController.setUsingHorizontalLayoutChangeListener(new Runnable() { // from class: com.android.systemui.qs.QSFragment$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                QSFragment.this.lambda$onViewCreated$3();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$0(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        updateQsBounds();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$1(View view, int i, int i2, int i3, int i4) {
        this.mQSAnimator.requestAnimatorUpdate();
        this.mHeader.setExpandedScrollAmount(i2);
        QS.ScrollListener scrollListener = this.mScrollListener;
        if (scrollListener != null) {
            scrollListener.onQsPanelScrollChanged(i2);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$2(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i6 - i8 != i2 - i4) {
            setQsExpansion(this.mLastQSExpansion, this.mLastHeaderTranslation);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onViewCreated$3() {
        this.mQSPanelController.getMediaHost().getHostView().setAlpha(1.0f);
        this.mQSAnimator.requestAnimatorUpdate();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setScrollListener(QS.ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    @Override // com.android.systemui.util.LifecycleFragment, android.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mStatusBarStateController.removeCallback(this);
        if (this.mListening) {
            setListening(false);
        }
        this.mQSCustomizerController.setQs(null);
        this.mQsDetailDisplayer.setQsPanelController(null);
        this.mScrollListener = null;
        this.mDumpManager.unregisterDumpable(this.mContainer.getClass().getName());
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("expanded", this.mQsExpanded);
        bundle.putBoolean("listening", this.mListening);
        this.mQSCustomizerController.saveInstanceState(bundle);
        if (this.mQsExpanded) {
            this.mQSPanelController.getTileLayout().saveInstanceState(bundle);
        }
    }

    boolean isListening() {
        return this.mListening;
    }

    boolean isExpanded() {
        return this.mQsExpanded;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public View getHeader() {
        return this.mHeader;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setPanelView(QS.HeightListener heightListener) {
        this.mPanelView = heightListener;
    }

    @Override // android.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setEditLocation(getView());
        if (configuration.getLayoutDirection() != this.mLayoutDirection) {
            this.mLayoutDirection = configuration.getLayoutDirection();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.onRtlChanged();
            }
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setFancyClipping(int i, int i2, int i3, boolean z) {
        if (getView() instanceof QSContainerImpl) {
            ((QSContainerImpl) getView()).setFancyClipping(i, i2, i3, z);
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public boolean isFullyCollapsed() {
        float f = this.mLastQSExpansion;
        return f == 0.0f || f == -1.0f;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setCollapsedMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mQuickQSPanelController.setMediaVisibilityChangedListener(consumer);
    }

    private void setEditLocation(View view) {
        View findViewById = view.findViewById(16908291);
        int[] locationOnScreen = findViewById.getLocationOnScreen();
        this.mQSCustomizerController.setEditLocation(locationOnScreen[0] + (findViewById.getWidth() / 2), locationOnScreen[1] + (findViewById.getHeight() / 2));
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setContainer(ViewGroup viewGroup) {
        if (viewGroup instanceof NotificationsQuickSettingsContainer) {
            NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = (NotificationsQuickSettingsContainer) viewGroup;
            this.mQSCustomizerController.setContainer(notificationsQuickSettingsContainer);
            this.mQSDetail.setContainer(notificationsQuickSettingsContainer);
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public boolean isCustomizing() {
        return this.mQSCustomizerController.isCustomizing();
    }

    public void setHost(QSTileHost qSTileHost) {
        this.mQSDetail.setHost(qSTileHost);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getContext().getDisplayId()) {
            int adjustDisableFlags = this.mRemoteInputQuickSettingsDisabler.adjustDisableFlags(i3);
            boolean z2 = (adjustDisableFlags & 1) != 0;
            if (z2 != this.mQsDisabled) {
                this.mQsDisabled = z2;
                this.mContainer.disable(i2, adjustDisableFlags, z);
                this.mHeader.disable(i2, adjustDisableFlags, z);
                this.mFooter.disable(i2, adjustDisableFlags, z);
                updateQsState();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateQsState() {
        boolean z = this.mQsExpanded;
        boolean z2 = true;
        int i = 0;
        boolean z3 = z || this.mStackScrollerOverscrolling || this.mHeaderAnimating;
        this.mQSPanelController.setExpanded(z);
        this.mQSDetail.setExpanded(this.mQsExpanded);
        boolean isKeyguardState = isKeyguardState();
        this.mHeader.setVisibility((this.mQsExpanded || !isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) ? 0 : 4);
        this.mHeader.setExpanded((isKeyguardState && !this.mHeaderAnimating && !this.mShowCollapsedOnKeyguard) || (this.mQsExpanded && !this.mStackScrollerOverscrolling), this.mQuickQSPanelController);
        this.mFooter.setVisibility((this.mQsDisabled || (!this.mQsExpanded && isKeyguardState && !this.mHeaderAnimating && !this.mShowCollapsedOnKeyguard)) ? 4 : 0);
        QSFooter qSFooter = this.mFooter;
        if ((!isKeyguardState || this.mHeaderAnimating || this.mShowCollapsedOnKeyguard) && (!this.mQsExpanded || this.mStackScrollerOverscrolling)) {
            z2 = false;
        }
        qSFooter.setExpanded(z2);
        QSPanelController qSPanelController = this.mQSPanelController;
        if (this.mQsDisabled || !z3) {
            i = 4;
        }
        qSPanelController.setVisibility(i);
    }

    private boolean isKeyguardState() {
        return this.mStatusBarStateController.getState() == 1;
    }

    private void updateShowCollapsedOnKeyguard() {
        boolean z = this.mBypassController.getBypassEnabled() || this.mTransitioningToFullShade;
        if (z != this.mShowCollapsedOnKeyguard) {
            this.mShowCollapsedOnKeyguard = z;
            updateQsState();
            QSAnimator qSAnimator = this.mQSAnimator;
            if (qSAnimator != null) {
                qSAnimator.setShowCollapsedOnKeyguard(z);
            }
            if (!z && isKeyguardState()) {
                setQsExpansion(this.mLastQSExpansion, 0.0f);
            }
        }
    }

    public QSPanelController getQSPanelController() {
        return this.mQSPanelController;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public boolean isShowingDetail() {
        return this.mQSCustomizerController.isCustomizing() || this.mQSDetail.isShowingDetail();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setExpanded(boolean z) {
        this.mQsExpanded = z;
        this.mQSPanelController.setListening(this.mListening, z);
        updateQsState();
    }

    private void setKeyguardShowing(boolean z) {
        this.mLastQSExpansion = -1.0f;
        QSAnimator qSAnimator = this.mQSAnimator;
        if (qSAnimator != null) {
            qSAnimator.setOnKeyguard(z);
        }
        this.mFooter.setKeyguardShowing(z);
        updateQsState();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setOverscrolling(boolean z) {
        this.mStackScrollerOverscrolling = z;
        updateQsState();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setListening(boolean z) {
        this.mListening = z;
        this.mQSContainerImplController.setListening(z);
        this.mFooter.setListening(z);
        this.mQSPanelController.setListening(this.mListening, this.mQsExpanded);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setHeaderListening(boolean z) {
        this.mQSContainerImplController.setListening(z);
        this.mFooter.setListening(z);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setTranslateWhileExpanding(boolean z) {
        this.mTranslateWhileExpanding = z;
        this.mQSAnimator.setTranslateWhileExpanding(z);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setTransitionToFullShadeAmount(float f, boolean z) {
        boolean z2 = f > 0.0f;
        if (z2 != this.mTransitioningToFullShade) {
            this.mTransitioningToFullShade = z2;
            updateShowCollapsedOnKeyguard();
            setQsExpansion(this.mLastQSExpansion, this.mLastHeaderTranslation);
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setQsExpansion(float f, float f2) {
        boolean z = this.mTransitioningToFullShade;
        float f3 = z ? 0.0f : f2;
        QSAnimator qSAnimator = this.mQSAnimator;
        boolean z2 = true;
        if (qSAnimator != null) {
            qSAnimator.startAlphaAnimation(((f > 0.0f ? 1 : (f == 0.0f ? 0 : -1)) > 0) || ((f3 > 0.0f ? 1 : (f3 == 0.0f ? 0 : -1)) == 0 || !this.mTranslateWhileExpanding) || z);
        }
        this.mContainer.setExpansion(f);
        float f4 = 1.0f;
        float f5 = (this.mTranslateWhileExpanding ? 1.0f : 0.1f) * (f - 1.0f);
        boolean z3 = isKeyguardState() && !this.mShowCollapsedOnKeyguard;
        if (!this.mHeaderAnimating && !headerWillBeAnimating()) {
            getView().setTranslationY(z3 ? ((float) this.mHeader.getHeight()) * f5 : f3);
        }
        int height = getView().getHeight();
        if (f != this.mLastQSExpansion || this.mLastKeyguardAndExpanded != z3 || this.mLastViewHeight != height || this.mLastHeaderTranslation != f3) {
            this.mLastHeaderTranslation = f3;
            this.mLastQSExpansion = f;
            this.mLastKeyguardAndExpanded = z3;
            this.mLastViewHeight = height;
            boolean z4 = f == 1.0f;
            if (f != 0.0f) {
                z2 = false;
            }
            float bottom = f5 * ((float) ((this.mQSPanelScrollView.getBottom() - this.mHeader.getBottom()) + this.mHeader.getPaddingBottom()));
            this.mHeader.setExpansion(z3, f, bottom);
            if (f < 1.0f && ((double) f) > 0.99d && this.mQuickQSPanelController.switchTileLayout(false)) {
                this.mHeader.updateResources();
            }
            QSFooter qSFooter = this.mFooter;
            if (!z3) {
                f4 = f;
            }
            qSFooter.setExpansion(f4);
            this.mQSPanelController.setRevealExpansion(f);
            this.mQSPanelController.getTileLayout().setExpansion(f, f2);
            this.mQuickQSPanelController.getTileLayout().setExpansion(f, f2);
            this.mQSPanelScrollView.setTranslationY(bottom);
            if (z2) {
                this.mQSPanelScrollView.setScrollY(0);
            }
            this.mQSDetail.setFullyExpanded(z4);
            if (!z4) {
                this.mQsBounds.top = (int) (-this.mQSPanelScrollView.getTranslationY());
                this.mQsBounds.right = this.mQSPanelScrollView.getWidth();
                this.mQsBounds.bottom = this.mQSPanelScrollView.getHeight();
            }
            updateQsBounds();
            QSAnimator qSAnimator2 = this.mQSAnimator;
            if (qSAnimator2 != null) {
                qSAnimator2.setPosition(f);
            }
            updateMediaPositions();
        }
    }

    private void updateQsBounds() {
        if (this.mLastQSExpansion == 1.0f) {
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mQSPanelScrollView.getLayoutParams();
            this.mQsBounds.set(-marginLayoutParams.leftMargin, 0, this.mQSPanelScrollView.getWidth() + marginLayoutParams.rightMargin, this.mQSPanelScrollView.getHeight());
        }
        this.mQSPanelScrollView.setClipBounds(this.mQsBounds);
    }

    private void updateMediaPositions() {
        if (Utils.useQsMediaPlayer(getContext())) {
            this.mContainer.getLocationOnScreen(this.mTmpLocation);
            float height = (float) (this.mTmpLocation[1] + this.mContainer.getHeight());
            pinToBottom((height - ((float) this.mQSPanelScrollView.getScrollY())) + ((float) this.mQSPanelScrollView.getScrollRange()), this.mQsMediaHost, true);
            pinToBottom(height, this.mQqsMediaHost, false);
        }
    }

    private void pinToBottom(float f, MediaHost mediaHost, boolean z) {
        float f2;
        UniqueObjectHostView hostView = mediaHost.getHostView();
        if (this.mLastQSExpansion <= 0.0f || isKeyguardState() || !this.mQqsMediaHost.getVisible()) {
            hostView.setTranslationY(0.0f);
            return;
        }
        float totalBottomMargin = ((f - getTotalBottomMargin(hostView)) - ((float) hostView.getHeight())) - (((float) mediaHost.getCurrentBounds().top) - hostView.getTranslationY());
        if (z) {
            f2 = Math.min(totalBottomMargin, 0.0f);
        } else {
            f2 = Math.max(totalBottomMargin, 0.0f);
        }
        hostView.setTranslationY(f2);
    }

    private float getTotalBottomMargin(View view) {
        View view2 = (View) view.getParent();
        int i = 0;
        while (true) {
            view = view2;
            if ((view instanceof QSContainerImpl) || view == null) {
                break;
            }
            i += view.getHeight() - view.getBottom();
            view2 = (View) view.getParent();
        }
        return (float) i;
    }

    private boolean headerWillBeAnimating() {
        if (this.mState != 1 || !this.mShowCollapsedOnKeyguard || isKeyguardState()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void animateHeaderSlidingOut() {
        if (getView().getY() != ((float) (-this.mHeader.getHeight()))) {
            this.mHeaderAnimating = true;
            getView().animate().y((float) (-this.mHeader.getHeight())).setStartDelay(0).setDuration(360).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.QSFragment.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    if (QSFragment.this.getView() != null) {
                        QSFragment.this.getView().animate().setListener(null);
                    }
                    QSFragment.this.mHeaderAnimating = false;
                    QSFragment.this.updateQsState();
                }
            }).start();
        }
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setExpandClickListener(View.OnClickListener onClickListener) {
        this.mFooter.setExpandClickListener(onClickListener);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void closeDetail() {
        this.mQSPanelController.closeDetail();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void notifyCustomizeChanged() {
        this.mContainer.updateExpansion();
        boolean isCustomizing = isCustomizing();
        int i = 0;
        this.mQSPanelScrollView.setVisibility(!isCustomizing ? 0 : 4);
        this.mFooter.setVisibility(!isCustomizing ? 0 : 4);
        QuickStatusBarHeader quickStatusBarHeader = this.mHeader;
        if (isCustomizing) {
            i = 4;
        }
        quickStatusBarHeader.setVisibility(i);
        this.mPanelView.onQsHeightChanged();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public int getDesiredHeight() {
        if (this.mQSCustomizerController.isCustomizing()) {
            return getView().getHeight();
        }
        if (!this.mQSDetail.isClosingDetail()) {
            return getView().getMeasuredHeight();
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mQSPanelScrollView.getLayoutParams();
        return layoutParams.topMargin + layoutParams.bottomMargin + this.mQSPanelScrollView.getMeasuredHeight() + getView().getPaddingBottom();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void setHeightOverride(int i) {
        this.mContainer.setHeightOverride(i);
    }

    @Override // com.android.systemui.plugins.qs.QS
    public int getQsMinExpansionHeight() {
        return this.mHeader.getHeight();
    }

    @Override // com.android.systemui.plugins.qs.QS
    public void hideImmediately() {
        getView().animate().cancel();
        getView().setY((float) (-this.mHeader.getHeight()));
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
        this.mState = i;
        boolean z = true;
        if (i != 1) {
            z = false;
        }
        setKeyguardShowing(z);
        updateShowCollapsedOnKeyguard();
    }
}
