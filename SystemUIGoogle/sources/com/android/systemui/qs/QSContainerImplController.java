package com.android.systemui.qs;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
/* loaded from: classes.dex */
public class QSContainerImplController extends ViewController<QSContainerImpl> {
    private final ConfigurationController mConfigurationController;
    private final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.qs.QSContainerImplController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public void onConfigChanged(Configuration configuration) {
            ((QSContainerImpl) ((ViewController) QSContainerImplController.this).mView).updateResources(QSContainerImplController.this.mQsPanelController, QSContainerImplController.this.mQuickStatusBarHeaderController);
        }
    };
    private final QSPanelController mQsPanelController;
    private final QuickStatusBarHeaderController mQuickStatusBarHeaderController;

    /* access modifiers changed from: package-private */
    public QSContainerImplController(QSContainerImpl qSContainerImpl, QSPanelController qSPanelController, QuickStatusBarHeaderController quickStatusBarHeaderController, ConfigurationController configurationController) {
        super(qSContainerImpl);
        this.mQsPanelController = qSPanelController;
        this.mQuickStatusBarHeaderController = quickStatusBarHeaderController;
        this.mConfigurationController = configurationController;
    }

    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        this.mQuickStatusBarHeaderController.init();
    }

    public void setListening(boolean z) {
        this.mQuickStatusBarHeaderController.setListening(z);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewAttached() {
        ((QSContainerImpl) this.mView).updateResources(this.mQsPanelController, this.mQuickStatusBarHeaderController);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    protected void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public QSContainerImpl getView() {
        return (QSContainerImpl) this.mView;
    }
}
