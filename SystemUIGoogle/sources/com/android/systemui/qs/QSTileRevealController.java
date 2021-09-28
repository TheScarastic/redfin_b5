package com.android.systemui.qs;

import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import com.android.systemui.Prefs;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.customize.QSCustomizerController;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
/* loaded from: classes.dex */
public class QSTileRevealController {
    private final Context mContext;
    private final PagedTileLayout mPagedTileLayout;
    private final QSPanelController mQSPanelController;
    private final QSCustomizerController mQsCustomizerController;
    private final ArraySet<String> mTilesToReveal = new ArraySet<>();
    private final Handler mHandler = new Handler();
    private final Runnable mRevealQsTiles = new Runnable() { // from class: com.android.systemui.qs.QSTileRevealController.1
        @Override // java.lang.Runnable
        public void run() {
            QSTileRevealController.this.mPagedTileLayout.startTileReveal(QSTileRevealController.this.mTilesToReveal, new QSTileRevealController$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            if (QSTileRevealController.this.mQSPanelController.isExpanded()) {
                QSTileRevealController qSTileRevealController = QSTileRevealController.this;
                qSTileRevealController.addTileSpecsToRevealed(qSTileRevealController.mTilesToReveal);
                QSTileRevealController.this.mTilesToReveal.clear();
            }
        }
    };

    QSTileRevealController(Context context, QSPanelController qSPanelController, PagedTileLayout pagedTileLayout, QSCustomizerController qSCustomizerController) {
        this.mContext = context;
        this.mQSPanelController = qSPanelController;
        this.mPagedTileLayout = pagedTileLayout;
        this.mQsCustomizerController = qSCustomizerController;
    }

    public void setExpansion(float f) {
        if (f == 1.0f) {
            this.mHandler.postDelayed(this.mRevealQsTiles, 500);
        } else {
            this.mHandler.removeCallbacks(this.mRevealQsTiles);
        }
    }

    public void updateRevealedTiles(Collection<QSTile> collection) {
        ArraySet<String> arraySet = new ArraySet<>();
        for (QSTile qSTile : collection) {
            arraySet.add(qSTile.getTileSpec());
        }
        Set<String> stringSet = Prefs.getStringSet(this.mContext, "QsTileSpecsRevealed", Collections.EMPTY_SET);
        if (stringSet.isEmpty() || this.mQsCustomizerController.isCustomizing()) {
            addTileSpecsToRevealed(arraySet);
            return;
        }
        arraySet.removeAll(stringSet);
        this.mTilesToReveal.addAll((ArraySet<? extends String>) arraySet);
    }

    /* access modifiers changed from: private */
    public void addTileSpecsToRevealed(ArraySet<String> arraySet) {
        ArraySet arraySet2 = new ArraySet(Prefs.getStringSet(this.mContext, "QsTileSpecsRevealed", Collections.EMPTY_SET));
        arraySet2.addAll((ArraySet) arraySet);
        Prefs.putStringSet(this.mContext, "QsTileSpecsRevealed", arraySet2);
    }

    /* loaded from: classes.dex */
    static class Factory {
        private final Context mContext;
        private final QSCustomizerController mQsCustomizerController;

        /* access modifiers changed from: package-private */
        public Factory(Context context, QSCustomizerController qSCustomizerController) {
            this.mContext = context;
            this.mQsCustomizerController = qSCustomizerController;
        }

        /* access modifiers changed from: package-private */
        public QSTileRevealController create(QSPanelController qSPanelController, PagedTileLayout pagedTileLayout) {
            return new QSTileRevealController(this.mContext, qSPanelController, pagedTileLayout, this.mQsCustomizerController);
        }
    }
}
