package androidx.leanback.widget.picker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.KeyEvent;
import androidx.core.view.ViewCompat;
import androidx.leanback.R$attr;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class PinPicker extends Picker {
    public PinPicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.pinPickerStyle);
    }

    @SuppressLint({"CustomViewStyleable"})
    public PinPicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        int[] iArr = R$styleable.lbPinPicker;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, 0);
        ViewCompat.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, 0);
        try {
            setSeparator(" ");
            setNumberOfColumns(obtainStyledAttributes.getInt(R$styleable.lbPinPicker_columnCount, 4));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public void setNumberOfColumns(int i) {
        ArrayList arrayList = new ArrayList(i);
        for (int i2 = 0; i2 < i; i2++) {
            PickerColumn pickerColumn = new PickerColumn();
            pickerColumn.setMinValue(0);
            pickerColumn.setMaxValue(9);
            pickerColumn.setLabelFormat("%d");
            arrayList.add(pickerColumn);
        }
        setColumns(arrayList);
    }

    @Override // android.view.View
    public boolean performClick() {
        int selectedColumn = getSelectedColumn();
        if (selectedColumn == getColumnsCount() - 1) {
            return super.performClick();
        }
        setSelectedColumn(selectedColumn + 1);
        return false;
    }

    @Override // androidx.leanback.widget.picker.Picker, android.view.View, android.view.ViewGroup
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (keyEvent.getAction() != 1 || keyCode < 7 || keyCode > 16) {
            return super.dispatchKeyEvent(keyEvent);
        }
        setColumnValue(getSelectedColumn(), keyCode - 7, false);
        performClick();
        return true;
    }
}
