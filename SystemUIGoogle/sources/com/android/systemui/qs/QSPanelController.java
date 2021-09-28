package com.android.systemui.qs;

import android.content.res.Configuration;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.PagedTileLayout;
import com.android.systemui.qs.QSDetail;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.settings.brightness.BrightnessController;
import com.android.systemui.settings.brightness.BrightnessSlider;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class QSPanelController extends QSPanelControllerBase<QSPanel> {
    private final BrightnessController mBrightnessController;
    private BrightnessMirrorController mBrightnessMirrorController;
    private final BrightnessSlider mBrightnessSlider;
    private final BrightnessSlider.Factory mBrightnessSliderFactory;
    private final FalsingManager mFalsingManager;
    private final QSCustomizerController mQsCustomizerController;
    private final QSSecurityFooter mQsSecurityFooter;
    private final QSTileRevealController.Factory mQsTileRevealControllerFactory;
    private final TunerService mTunerService;
    private boolean mGridContentVisible = true;
    private final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() { // from class: com.android.systemui.qs.QSPanelController.1
        @Override // com.android.systemui.qs.QSPanel.OnConfigurationChangedListener
        public void onConfigurationChange(Configuration configuration) {
            ((QSPanel) ((ViewController) QSPanelController.this).mView).updateResources();
            QSPanelController.this.mQsSecurityFooter.onConfigurationChanged();
            if (((QSPanel) ((ViewController) QSPanelController.this).mView).isListening()) {
                QSPanelController.this.refreshAllTiles();
            }
            QSPanelController.this.updateBrightnessMirror();
        }
    };
    private final BrightnessMirrorController.BrightnessMirrorListener mBrightnessMirrorListener = new BrightnessMirrorController.BrightnessMirrorListener() { // from class: com.android.systemui.qs.QSPanelController$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.policy.BrightnessMirrorController.BrightnessMirrorListener
        public final void onBrightnessMirrorReinflated(View view) {
            QSPanelController.this.lambda$new$0(view);
        }
    };
    private View.OnTouchListener mTileLayoutTouchListener = new View.OnTouchListener() { // from class: com.android.systemui.qs.QSPanelController.2
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getActionMasked() != 1) {
                return false;
            }
            QSPanelController.this.mFalsingManager.isFalseTouch(15);
            return false;
        }
    };

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        updateBrightnessMirror();
    }

    /* access modifiers changed from: package-private */
    public QSPanelController(QSPanel qSPanel, QSSecurityFooter qSSecurityFooter, TunerService tunerService, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, QSTileRevealController.Factory factory, DumpManager dumpManager, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, BrightnessController.Factory factory2, BrightnessSlider.Factory factory3, FalsingManager falsingManager, FeatureFlags featureFlags) {
        super(qSPanel, qSTileHost, qSCustomizerController, z, mediaHost, metricsLogger, uiEventLogger, qSLogger, dumpManager, featureFlags);
        this.mQsSecurityFooter = qSSecurityFooter;
        this.mTunerService = tunerService;
        this.mQsCustomizerController = qSCustomizerController;
        this.mQsTileRevealControllerFactory = factory;
        this.mFalsingManager = falsingManager;
        qSSecurityFooter.setHostEnvironment(qSTileHost);
        this.mBrightnessSliderFactory = factory3;
        BrightnessSlider create = factory3.create(getContext(), (ViewGroup) this.mView);
        this.mBrightnessSlider = create;
        ((QSPanel) this.mView).setBrightnessView(create.getRootView());
        this.mBrightnessController = factory2.create(create);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public void onInit() {
        super.onInit();
        this.mMediaHost.setExpansion(1.0f);
        this.mMediaHost.setShowsOnlyActiveMedia(false);
        this.mMediaHost.init(0);
        this.mQsCustomizerController.init();
        this.mBrightnessSlider.init();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public void onViewAttached() {
        super.onViewAttached();
        updateMediaDisappearParameters();
        this.mTunerService.addTunable((TunerService.Tunable) this.mView, "qs_show_brightness");
        ((QSPanel) this.mView).updateResources();
        if (((QSPanel) this.mView).isListening()) {
            refreshAllTiles();
        }
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        ((QSPanel) this.mView).setSecurityFooter(this.mQsSecurityFooter.getView());
        switchTileLayout(true);
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.mBrightnessMirrorListener);
        }
        ((PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout()).setOnTouchListener(this.mTileLayoutTouchListener);
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    protected QSTileRevealController createTileRevealController() {
        return this.mQsTileRevealControllerFactory.create(this, (PagedTileLayout) ((QSPanel) this.mView).getOrCreateTileLayout());
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.qs.QSPanelControllerBase, com.android.systemui.util.ViewController
    public void onViewDetached() {
        this.mTunerService.removeTunable((TunerService.Tunable) this.mView);
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.removeCallback(this.mBrightnessMirrorListener);
        }
        super.onViewDetached();
    }

    public void setHeaderContainer(ViewGroup viewGroup) {
        ((QSPanel) this.mView).setHeaderContainer(viewGroup);
    }

    public void setVisibility(int i) {
        ((QSPanel) this.mView).setVisibility(i);
    }

    public void setListening(boolean z, boolean z2) {
        setListening(z && z2);
        if (((QSPanel) this.mView).isListening()) {
            refreshAllTiles();
        }
        this.mQsSecurityFooter.setListening(z);
        if (z) {
            this.mBrightnessController.registerCallbacks();
        } else {
            this.mBrightnessController.unregisterCallbacks();
        }
    }

    public void setBrightnessMirror(BrightnessMirrorController brightnessMirrorController) {
        this.mBrightnessMirrorController = brightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.removeCallback(this.mBrightnessMirrorListener);
        }
        this.mBrightnessMirrorController = brightnessMirrorController;
        if (brightnessMirrorController != null) {
            brightnessMirrorController.addCallback(this.mBrightnessMirrorListener);
        }
        updateBrightnessMirror();
    }

    /* access modifiers changed from: private */
    public void updateBrightnessMirror() {
        BrightnessMirrorController brightnessMirrorController = this.mBrightnessMirrorController;
        if (brightnessMirrorController != null) {
            this.mBrightnessSlider.setMirrorControllerAndMirror(brightnessMirrorController);
        }
    }

    public QSTileHost getHost() {
        return this.mHost;
    }

    public void openDetails(String str) {
        QSTile tile = getTile(str);
        if (tile != null) {
            ((QSPanel) this.mView).openDetails(tile);
        }
    }

    public void showDeviceMonitoringDialog() {
        this.mQsSecurityFooter.showDeviceMonitoringDialog();
    }

    public void updateResources() {
        ((QSPanel) this.mView).updateResources();
    }

    @Override // com.android.systemui.qs.QSPanelControllerBase
    public void refreshAllTiles() {
        this.mBrightnessController.checkRestrictionAndSetEnabled();
        super.refreshAllTiles();
        this.mQsSecurityFooter.refreshState();
    }

    public void showEdit(View view) {
        view.post(new Runnable(view) { // from class: com.android.systemui.qs.QSPanelController$$ExternalSyntheticLambda1
            public final /* synthetic */ View f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                QSPanelController.this.lambda$showEdit$1(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$showEdit$1(View view) {
        if (!this.mQsCustomizerController.isCustomizing()) {
            int[] locationOnScreen = view.getLocationOnScreen();
            this.mQsCustomizerController.show(locationOnScreen[0] + (view.getWidth() / 2), locationOnScreen[1] + (view.getHeight() / 2), false);
        }
    }

    public void setCallback(QSDetail.Callback callback) {
        ((QSPanel) this.mView).setCallback(callback);
    }

    public void setGridContentVisibility(boolean z) {
        int i = z ? 0 : 4;
        setVisibility(i);
        if (this.mGridContentVisible != z) {
            this.mMetricsLogger.visibility(111, i);
        }
        this.mGridContentVisible = z;
    }

    public View getBrightnessView() {
        return ((QSPanel) this.mView).getBrightnessView();
    }

    public void setPageListener(PagedTileLayout.PageListener pageListener) {
        ((QSPanel) this.mView).setPageListener(pageListener);
    }

    public void setContentMargins(int i, int i2) {
        ((QSPanel) this.mView).setContentMargins(i, i2, this.mMediaHost.getHostView());
    }

    public void showDetailAdapter(DetailAdapter detailAdapter, int i, int i2) {
        ((QSPanel) this.mView).showDetailAdapter(true, detailAdapter, new int[]{i, i2});
    }

    public void setFooterPageIndicator(PageIndicator pageIndicator) {
        ((QSPanel) this.mView).setFooterPageIndicator(pageIndicator);
    }

    public boolean isExpanded() {
        return ((QSPanel) this.mView).isExpanded();
    }

    /* access modifiers changed from: package-private */
    public void setPageMargin(int i) {
        ((QSPanel) this.mView).setPageMargin(i);
    }
}
