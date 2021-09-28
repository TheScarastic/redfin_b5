package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$styleable;
/* loaded from: classes.dex */
public class VerticalGridView extends BaseGridView {
    public VerticalGridView(Context context) {
        this(context, null);
    }

    public VerticalGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public VerticalGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mLayoutManager.setOrientation(1);
        initAttributes(context, attributeSet);
    }

    @SuppressLint({"CustomViewStyleable"})
    protected void initAttributes(Context context, AttributeSet attributeSet) {
        initBaseGridViewAttributes(context, attributeSet);
        int[] iArr = R$styleable.lbVerticalGridView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, 0, 0);
        setColumnWidth(obtainStyledAttributes);
        setNumColumns(obtainStyledAttributes.getInt(R$styleable.lbVerticalGridView_numberOfColumns, 1));
        obtainStyledAttributes.recycle();
    }

    void setColumnWidth(TypedArray typedArray) {
        int i = R$styleable.lbVerticalGridView_columnWidth;
        if (typedArray.peekValue(i) != null) {
            setColumnWidth(typedArray.getLayoutDimension(i, 0));
        }
    }

    public void setNumColumns(int i) {
        this.mLayoutManager.setNumRows(i);
        requestLayout();
    }

    public void setColumnWidth(int i) {
        this.mLayoutManager.setRowHeight(i);
        requestLayout();
    }
}
