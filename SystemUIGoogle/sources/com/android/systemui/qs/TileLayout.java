package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
import com.android.systemui.R$integer;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class TileLayout extends ViewGroup implements QSPanel.QSTileLayout {
    protected int mCellHeight;
    protected int mCellHeightResId;
    protected int mCellMarginHorizontal;
    protected int mCellMarginVertical;
    protected int mCellWidth;
    protected int mColumns;
    private final boolean mLessRows;
    protected boolean mListening;
    protected int mMaxAllowedRows;
    protected int mMaxCellHeight;
    private int mMaxColumns;
    private int mMinRows;
    protected final ArrayList<QSPanelControllerBase.TileRecord> mRecords;
    protected int mResourceColumns;
    protected int mRows;
    protected int mSidePadding;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    protected boolean useSidePadding() {
        return true;
    }

    public TileLayout(Context context) {
        this(context, null);
    }

    public TileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCellHeightResId = R$dimen.qs_tile_height;
        boolean z = true;
        this.mRows = 1;
        this.mRecords = new ArrayList<>();
        this.mMaxAllowedRows = 3;
        this.mMinRows = 1;
        this.mMaxColumns = 100;
        setFocusableInTouchMode(true);
        if (Settings.System.getInt(context.getContentResolver(), "qs_less_rows", 0) == 0 && !Utils.useQsMediaPlayer(context)) {
            z = false;
        }
        this.mLessRows = z;
        updateResources();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getOffsetTop(QSPanelControllerBase.TileRecord tileRecord) {
        return getTop();
    }

    public void setListening(boolean z) {
        setListening(z, null);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
            while (it.hasNext()) {
                it.next().tile.setListening(this, this.mListening);
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean setMinRows(int i) {
        if (this.mMinRows == i) {
            return false;
        }
        this.mMinRows = i;
        updateResources();
        return true;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean setMaxColumns(int i) {
        this.mMaxColumns = i;
        return updateColumns();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mRecords.add(tileRecord);
        tileRecord.tile.setListening(this, this.mListening);
        addTileView(tileRecord);
    }

    protected void addTileView(QSPanelControllerBase.TileRecord tileRecord) {
        addView(tileRecord.tileView);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mRecords.remove(tileRecord);
        tileRecord.tile.setListening(this, false);
        removeView(tileRecord.tileView);
    }

    @Override // android.view.ViewGroup
    public void removeAllViews() {
        Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.setListening(this, false);
        }
        this.mRecords.clear();
        super.removeAllViews();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean updateResources() {
        Resources resources = ((ViewGroup) this).mContext.getResources();
        this.mResourceColumns = Math.max(1, resources.getInteger(R$integer.quick_settings_num_columns));
        updateColumns();
        this.mMaxCellHeight = ((ViewGroup) this).mContext.getResources().getDimensionPixelSize(this.mCellHeightResId);
        this.mCellMarginHorizontal = resources.getDimensionPixelSize(R$dimen.qs_tile_margin_horizontal);
        this.mSidePadding = useSidePadding() ? this.mCellMarginHorizontal / 2 : 0;
        this.mCellMarginVertical = resources.getDimensionPixelSize(R$dimen.qs_tile_margin_vertical);
        int max = Math.max(1, getResources().getInteger(R$integer.quick_settings_max_rows));
        this.mMaxAllowedRows = max;
        if (this.mLessRows) {
            this.mMaxAllowedRows = Math.max(this.mMinRows, max - 1);
        }
        if (!updateColumns()) {
            return false;
        }
        requestLayout();
        return true;
    }

    private boolean updateColumns() {
        int i = this.mColumns;
        int min = Math.min(this.mResourceColumns, this.mMaxColumns);
        this.mColumns = min;
        return i != min;
    }

    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:18:0x0042 */
    /* JADX DEBUG: Failed to insert an additional move for type inference into block B:19:0x0042 */
    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = this.mRecords.size();
        int size2 = View.MeasureSpec.getSize(i);
        int paddingStart = (size2 - getPaddingStart()) - getPaddingEnd();
        if (View.MeasureSpec.getMode(i2) == 0) {
            int i3 = this.mColumns;
            this.mRows = ((size + i3) - 1) / i3;
        }
        int i4 = this.mColumns;
        this.mCellWidth = ((paddingStart - (this.mCellMarginHorizontal * (i4 - 1))) - (this.mSidePadding * 2)) / i4;
        int exactly = exactly(getCellHeight());
        Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
        View view = this;
        while (it.hasNext()) {
            QSPanelControllerBase.TileRecord next = it.next();
            if (next.tileView.getVisibility() != 8) {
                next.tileView.measure(exactly(this.mCellWidth), exactly);
                view = next.tileView.updateAccessibilityOrder(view);
                this.mCellHeight = next.tileView.getMeasuredHeight();
            }
        }
        int i5 = this.mCellHeight;
        int i6 = this.mCellMarginVertical;
        int i7 = ((i5 + i6) * this.mRows) - i6;
        if (i7 < 0) {
            i7 = 0;
        }
        setMeasuredDimension(size2, i7);
    }

    public boolean updateMaxRows(int i, int i2) {
        int i3 = i + this.mCellMarginVertical;
        int i4 = this.mRows;
        int cellHeight = i3 / (getCellHeight() + this.mCellMarginVertical);
        this.mRows = cellHeight;
        int i5 = this.mMinRows;
        if (cellHeight < i5) {
            this.mRows = i5;
        } else {
            int i6 = this.mMaxAllowedRows;
            if (cellHeight >= i6) {
                this.mRows = i6;
            }
        }
        int i7 = this.mRows;
        int i8 = this.mColumns;
        if (i7 > ((i2 + i8) - 1) / i8) {
            this.mRows = ((i2 + i8) - 1) / i8;
        }
        if (i4 != this.mRows) {
            return true;
        }
        return false;
    }

    protected static int exactly(int i) {
        return View.MeasureSpec.makeMeasureSpec(i, 1073741824);
    }

    protected int getCellHeight() {
        return this.mMaxCellHeight;
    }

    protected void layoutTileRecords(int i) {
        boolean z = getLayoutDirection() == 1;
        int min = Math.min(i, this.mRows * this.mColumns);
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        while (i2 < min) {
            if (i3 == this.mColumns) {
                i4++;
                i3 = 0;
            }
            QSPanelControllerBase.TileRecord tileRecord = this.mRecords.get(i2);
            int rowTop = getRowTop(i4);
            int columnStart = getColumnStart(z ? (this.mColumns - i3) - 1 : i3);
            QSTileView qSTileView = tileRecord.tileView;
            qSTileView.layout(columnStart, rowTop, this.mCellWidth + columnStart, qSTileView.getMeasuredHeight() + rowTop);
            i2++;
            i3++;
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layoutTileRecords(this.mRecords.size());
    }

    /* access modifiers changed from: protected */
    public int getRowTop(int i) {
        return i * (this.mCellHeight + this.mCellMarginVertical);
    }

    protected int getColumnStart(int i) {
        return getPaddingStart() + this.mSidePadding + (i * (this.mCellWidth + this.mCellMarginHorizontal));
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public int getNumVisibleTiles() {
        return this.mRecords.size();
    }

    public int maxTiles() {
        return Math.max(this.mColumns * this.mRows, 1);
    }
}
