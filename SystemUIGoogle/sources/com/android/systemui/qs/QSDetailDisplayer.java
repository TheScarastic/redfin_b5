package com.android.systemui.qs;

import com.android.systemui.plugins.qs.DetailAdapter;
/* loaded from: classes.dex */
public class QSDetailDisplayer {
    private QSPanelController mQsPanelController;

    public void setQsPanelController(QSPanelController qSPanelController) {
        this.mQsPanelController = qSPanelController;
    }

    public void showDetailAdapter(DetailAdapter detailAdapter, int i, int i2) {
        QSPanelController qSPanelController = this.mQsPanelController;
        if (qSPanelController != null) {
            qSPanelController.showDetailAdapter(detailAdapter, i, i2);
        }
    }
}
