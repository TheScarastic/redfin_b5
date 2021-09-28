package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes2.dex */
class SelectionListeningTextView extends TextView {
    private SelectionPathListener selectionPathListener;

    /* loaded from: classes2.dex */
    public interface SelectionPathListener {
        void onSelectionPathChanged(@Nullable Path path);
    }

    public SelectionListeningTextView(Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SelectionListeningTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public SelectionListeningTextView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        setTextIsSelectable(true);
    }

    @Override // android.widget.TextView
    protected void onSelectionChanged(int i, int i2) {
        super.onSelectionChanged(i, i2);
        if (this.selectionPathListener != null) {
            if (i == i2 || getLayout() == null) {
                this.selectionPathListener.onSelectionPathChanged(null);
                return;
            }
            Path path = new Path();
            getLayout().getSelectionPath(i, i2, path);
            this.selectionPathListener.onSelectionPathChanged(path);
        }
    }
}
