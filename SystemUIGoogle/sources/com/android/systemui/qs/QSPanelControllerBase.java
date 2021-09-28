package com.android.systemui.qs;

import android.content.ComponentName;
import android.content.res.Configuration;
import android.metrics.LogMaker;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.customize.QSCustomizerController;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.util.Utils;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.animation.DisappearParameters;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public abstract class QSPanelControllerBase<T extends QSPanel> extends ViewController<T> implements Dumpable {
    private final DumpManager mDumpManager;
    private final FeatureFlags mFeatureFlags;
    protected final QSTileHost mHost;
    private int mLastOrientation;
    protected final MediaHost mMediaHost;
    private Consumer<Boolean> mMediaVisibilityChangedListener;
    protected final MetricsLogger mMetricsLogger;
    private final QSLogger mQSLogger;
    private final QSCustomizerController mQsCustomizerController;
    private QSTileRevealController mQsTileRevealController;
    private float mRevealExpansion;
    private boolean mShouldUseSplitNotificationShade;
    private final UiEventLogger mUiEventLogger;
    private boolean mUsingHorizontalLayout;
    private Runnable mUsingHorizontalLayoutChangedListener;
    private final boolean mUsingMediaPlayer;
    protected final ArrayList<TileRecord> mRecords = new ArrayList<>();
    private String mCachedSpecs = "";
    private final QSHost.Callback mQSHostCallback = new QSHost.Callback() { // from class: com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda0
        @Override // com.android.systemui.qs.QSHost.Callback
        public final void onTilesChanged() {
            QSPanelControllerBase.this.setTiles();
        }
    };
    private final QSPanel.OnConfigurationChangedListener mOnConfigurationChangedListener = new QSPanel.OnConfigurationChangedListener() { // from class: com.android.systemui.qs.QSPanelControllerBase.1
        @Override // com.android.systemui.qs.QSPanel.OnConfigurationChangedListener
        public void onConfigurationChange(Configuration configuration) {
            QSPanelControllerBase qSPanelControllerBase = QSPanelControllerBase.this;
            qSPanelControllerBase.mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(qSPanelControllerBase.mFeatureFlags, QSPanelControllerBase.this.getResources());
            if (configuration.orientation != QSPanelControllerBase.this.mLastOrientation) {
                QSPanelControllerBase.this.mLastOrientation = configuration.orientation;
                QSPanelControllerBase.this.switchTileLayout(false);
            }
        }
    };
    private final Function1<Boolean, Unit> mMediaHostVisibilityListener = new Function1() { // from class: com.android.systemui.qs.QSPanelControllerBase$$ExternalSyntheticLambda2
        @Override // kotlin.jvm.functions.Function1
        public final Object invoke(Object obj) {
            return QSPanelControllerBase.this.lambda$new$0((Boolean) obj);
        }
    };

    /* loaded from: classes.dex */
    public static final class TileRecord extends QSPanel.Record {
        public QSTile.Callback callback;
        public boolean scanState;
        public QSTile tile;
        public QSTileView tileView;
    }

    protected QSTileRevealController createTileRevealController() {
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$new$0(Boolean bool) {
        Consumer<Boolean> consumer = this.mMediaVisibilityChangedListener;
        if (consumer != null) {
            consumer.accept(bool);
        }
        switchTileLayout(false);
        return null;
    }

    /* access modifiers changed from: protected */
    public QSPanelControllerBase(T t, QSTileHost qSTileHost, QSCustomizerController qSCustomizerController, boolean z, MediaHost mediaHost, MetricsLogger metricsLogger, UiEventLogger uiEventLogger, QSLogger qSLogger, DumpManager dumpManager, FeatureFlags featureFlags) {
        super(t);
        this.mHost = qSTileHost;
        this.mQsCustomizerController = qSCustomizerController;
        this.mUsingMediaPlayer = z;
        this.mMediaHost = mediaHost;
        this.mMetricsLogger = metricsLogger;
        this.mUiEventLogger = uiEventLogger;
        this.mQSLogger = qSLogger;
        this.mDumpManager = dumpManager;
        this.mFeatureFlags = featureFlags;
        this.mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(featureFlags, getResources());
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onInit() {
        ((QSPanel) this.mView).initialize();
        this.mQSLogger.logAllTilesChangeListening(((QSPanel) this.mView).isListening(), ((QSPanel) this.mView).getDumpableTag(), "");
    }

    public MediaHost getMediaHost() {
        return this.mMediaHost;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        QSTileRevealController createTileRevealController = createTileRevealController();
        this.mQsTileRevealController = createTileRevealController;
        if (createTileRevealController != null) {
            createTileRevealController.setExpansion(this.mRevealExpansion);
        }
        this.mMediaHost.addVisibilityChangeListener(this.mMediaHostVisibilityListener);
        ((QSPanel) this.mView).addOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.addCallback(this.mQSHostCallback);
        setTiles();
        switchTileLayout(true);
        this.mDumpManager.registerDumpable(((QSPanel) this.mView).getDumpableTag(), this);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        ((QSPanel) this.mView).removeOnConfigurationChangedListener(this.mOnConfigurationChangedListener);
        this.mHost.removeCallback(this.mQSHostCallback);
        ((QSPanel) this.mView).getTileLayout().setListening(false, this.mUiEventLogger);
        this.mMediaHost.removeVisibilityChangeListener(this.mMediaHostVisibilityListener);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.removeCallbacks();
        }
        this.mRecords.clear();
        this.mDumpManager.unregisterDumpable(((QSPanel) this.mView).getDumpableTag());
    }

    public void setTiles() {
        setTiles(this.mHost.getTiles(), false);
    }

    public void setTiles(Collection<QSTile> collection, boolean z) {
        QSTileRevealController qSTileRevealController;
        if (!z && (qSTileRevealController = this.mQsTileRevealController) != null) {
            qSTileRevealController.updateRevealedTiles(collection);
        }
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            ((QSPanel) this.mView).removeTile(next);
            next.tile.removeCallback(next.callback);
        }
        this.mRecords.clear();
        this.mCachedSpecs = "";
        for (QSTile qSTile : collection) {
            addTile(qSTile, z);
        }
    }

    public void refreshAllTiles() {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.refreshState();
        }
    }

    private void addTile(QSTile qSTile, boolean z) {
        TileRecord tileRecord = new TileRecord();
        tileRecord.tile = qSTile;
        tileRecord.tileView = this.mHost.createTileView(getContext(), qSTile, z);
        ((QSPanel) this.mView).addTile(tileRecord);
        this.mRecords.add(tileRecord);
        this.mCachedSpecs = getTilesSpecs();
    }

    public void clickTile(ComponentName componentName) {
        String spec = CustomTile.toSpec(componentName);
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile.getTileSpec().equals(spec)) {
                next.tile.click(null);
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public QSTile getTile(String str) {
        for (int i = 0; i < this.mRecords.size(); i++) {
            if (str.equals(this.mRecords.get(i).tile.getTileSpec())) {
                return this.mRecords.get(i).tile;
            }
        }
        return this.mHost.createTile(str);
    }

    /* access modifiers changed from: package-private */
    public boolean areThereTiles() {
        return !this.mRecords.isEmpty();
    }

    /* access modifiers changed from: package-private */
    public QSTileView getTileView(QSTile qSTile) {
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile == qSTile) {
                return next.tileView;
            }
        }
        return null;
    }

    private String getTilesSpecs() {
        return (String) this.mRecords.stream().map(QSPanelControllerBase$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.joining(","));
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ String lambda$getTilesSpecs$1(TileRecord tileRecord) {
        return tileRecord.tile.getTileSpec();
    }

    public void setExpanded(boolean z) {
        if (((QSPanel) this.mView).isExpanded() != z) {
            this.mQSLogger.logPanelExpanded(z, ((QSPanel) this.mView).getDumpableTag());
            ((QSPanel) this.mView).setExpanded(z);
            this.mMetricsLogger.visibility(111, z);
            if (!z) {
                this.mUiEventLogger.log(((QSPanel) this.mView).closePanelEvent());
                closeDetail();
                return;
            }
            this.mUiEventLogger.log(((QSPanel) this.mView).openPanelEvent());
            logTiles();
        }
    }

    public void closeDetail() {
        if (this.mQsCustomizerController.isShown()) {
            this.mQsCustomizerController.hide();
        } else {
            ((QSPanel) this.mView).closeDetail();
        }
    }

    /* access modifiers changed from: package-private */
    public void setListening(boolean z) {
        ((QSPanel) this.mView).setListening(z);
        if (((QSPanel) this.mView).getTileLayout() != null) {
            this.mQSLogger.logAllTilesChangeListening(z, ((QSPanel) this.mView).getDumpableTag(), this.mCachedSpecs);
            ((QSPanel) this.mView).getTileLayout().setListening(z, this.mUiEventLogger);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean switchTileLayout(boolean z) {
        boolean shouldUseHorizontalLayout = shouldUseHorizontalLayout();
        if (shouldUseHorizontalLayout == this.mUsingHorizontalLayout && !z) {
            return false;
        }
        this.mUsingHorizontalLayout = shouldUseHorizontalLayout;
        ((QSPanel) this.mView).setUsingHorizontalLayout(shouldUseHorizontalLayout, this.mMediaHost.getHostView(), z);
        updateMediaDisappearParameters();
        Runnable runnable = this.mUsingHorizontalLayoutChangedListener;
        if (runnable == null) {
            return true;
        }
        runnable.run();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void updateMediaDisappearParameters() {
        if (this.mUsingMediaPlayer) {
            DisappearParameters disappearParameters = this.mMediaHost.getDisappearParameters();
            if (this.mUsingHorizontalLayout) {
                disappearParameters.getDisappearSize().set(0.0f, 0.4f);
                disappearParameters.getGonePivot().set(1.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.25f, 1.0f);
                disappearParameters.setDisappearEnd(0.6f);
            } else {
                disappearParameters.getDisappearSize().set(1.0f, 0.0f);
                disappearParameters.getGonePivot().set(0.0f, 1.0f);
                disappearParameters.getContentTranslationFraction().set(0.0f, 1.05f);
                disappearParameters.setDisappearEnd(0.95f);
            }
            disappearParameters.setFadeStartPosition(0.95f);
            disappearParameters.setDisappearStart(0.0f);
            this.mMediaHost.setDisappearParameters(disappearParameters);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldUseHorizontalLayout() {
        if (!this.mShouldUseSplitNotificationShade && this.mUsingMediaPlayer && this.mMediaHost.getVisible() && getResources().getConfiguration().orientation == 2) {
            return true;
        }
        return false;
    }

    private void logTiles() {
        for (int i = 0; i < this.mRecords.size(); i++) {
            QSTile qSTile = this.mRecords.get(i).tile;
            this.mMetricsLogger.write(qSTile.populate(new LogMaker(qSTile.getMetricsCategory()).setType(1)));
        }
    }

    public void setRevealExpansion(float f) {
        this.mRevealExpansion = f;
        QSTileRevealController qSTileRevealController = this.mQsTileRevealController;
        if (qSTileRevealController != null) {
            qSTileRevealController.setExpansion(f);
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.println("  Tile records:");
        Iterator<TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            TileRecord next = it.next();
            if (next.tile instanceof Dumpable) {
                printWriter.print("    ");
                ((Dumpable) next.tile).dump(fileDescriptor, printWriter, strArr);
                printWriter.print("    ");
                printWriter.println(next.tileView.toString());
            }
        }
    }

    public QSPanel.QSTileLayout getTileLayout() {
        return ((QSPanel) this.mView).getTileLayout();
    }

    public void setMediaVisibilityChangedListener(Consumer<Boolean> consumer) {
        this.mMediaVisibilityChangedListener = consumer;
    }

    public void setUsingHorizontalLayoutChangeListener(Runnable runnable) {
        this.mUsingHorizontalLayoutChangedListener = runnable;
    }
}
