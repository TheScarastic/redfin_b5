package com.android.systemui.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$styleable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.MutablePropertyReference0Impl;
/* compiled from: DualHeightHorizontalLinearLayout.kt */
/* loaded from: classes2.dex */
public final class DualHeightHorizontalLinearLayout extends LinearLayout {
    private int initialPadding;
    private int singleLineHeightPx;
    private final TypedValue singleLineHeightValue;
    private int singleLineVerticalPaddingPx;
    private final TypedValue singleLineVerticalPaddingValue;
    private TextView textView;
    private final int textViewId;

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public DualHeightHorizontalLinearLayout(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this.initialPadding = ((LinearLayout) this).mPaddingTop;
        if (getOrientation() == 0) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DualHeightHorizontalLinearLayout, i, i2);
            TypedValue typedValue = new TypedValue();
            int i3 = R$styleable.DualHeightHorizontalLinearLayout_singleLineHeight;
            TypedValue typedValue2 = null;
            if (obtainStyledAttributes.hasValue(i3)) {
                obtainStyledAttributes.getValue(i3, typedValue);
            } else {
                typedValue = null;
            }
            this.singleLineHeightValue = typedValue;
            TypedValue typedValue3 = new TypedValue();
            int i4 = R$styleable.DualHeightHorizontalLinearLayout_singleLineVerticalPadding;
            if (obtainStyledAttributes.hasValue(i4)) {
                obtainStyledAttributes.getValue(i4, typedValue3);
                typedValue2 = typedValue3;
            }
            this.singleLineVerticalPaddingValue = typedValue2;
            this.textViewId = obtainStyledAttributes.getResourceId(R$styleable.DualHeightHorizontalLinearLayout_textViewId, 0);
            obtainStyledAttributes.recycle();
            updateResources();
            return;
        }
        throw new IllegalStateException("This view should always have horizontal orientation");
    }

    /* access modifiers changed from: private */
    public final DisplayMetrics getDisplayMetrics() {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        Intrinsics.checkNotNullExpressionValue(displayMetrics, "context.resources.displayMetrics");
        return displayMetrics;
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
        this.initialPadding = i2;
    }

    @Override // android.view.View
    public void setPaddingRelative(int i, int i2, int i3, int i4) {
        super.setPaddingRelative(i, i2, i3, i4);
        this.initialPadding = i2;
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextView textView = this.textView;
        if (textView != null) {
            if (textView.getLineCount() < 2) {
                setMeasuredDimension(getMeasuredWidth(), this.singleLineHeightPx);
                ((LinearLayout) this).mPaddingBottom = 0;
                ((LinearLayout) this).mPaddingTop = 0;
                return;
            }
            int i3 = this.initialPadding;
            ((LinearLayout) this).mPaddingBottom = i3;
            ((LinearLayout) this).mPaddingTop = i3;
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.textView = (TextView) findViewById(this.textViewId);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    @Override // android.widget.LinearLayout
    public void setOrientation(int i) {
        if (i != 1) {
            super.setOrientation(i);
            return;
        }
        throw new IllegalStateException("This view should always have horizontal orientation");
    }

    private final void updateResources() {
        int i;
        int i2;
        TypedValue typedValue = this.singleLineHeightValue;
        int minimumHeight = getMinimumHeight();
        DualHeightHorizontalLinearLayout$updateResources$2 dualHeightHorizontalLinearLayout$updateResources$2 = new MutablePropertyReference0Impl(this) { // from class: com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$2
            @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KProperty0
            public Object get() {
                return Integer.valueOf(DualHeightHorizontalLinearLayout.access$getSingleLineHeightPx$p((DualHeightHorizontalLinearLayout) this.receiver));
            }

            @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KMutableProperty0
            public void set(Object obj) {
                DualHeightHorizontalLinearLayout.access$setSingleLineHeightPx$p((DualHeightHorizontalLinearLayout) this.receiver, ((Number) obj).intValue());
            }
        };
        if (typedValue != null) {
            if (typedValue.resourceId != 0) {
                i2 = getContext().getResources().getDimensionPixelSize(typedValue.resourceId);
            } else {
                i2 = (int) typedValue.getDimension(getDisplayMetrics());
            }
            minimumHeight = i2;
        }
        dualHeightHorizontalLinearLayout$updateResources$2.set(Integer.valueOf(minimumHeight));
        TypedValue typedValue2 = this.singleLineVerticalPaddingValue;
        int i3 = 0;
        DualHeightHorizontalLinearLayout$updateResources$4 dualHeightHorizontalLinearLayout$updateResources$4 = new MutablePropertyReference0Impl(this) { // from class: com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$4
            @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KProperty0
            public Object get() {
                return Integer.valueOf(DualHeightHorizontalLinearLayout.access$getSingleLineVerticalPaddingPx$p((DualHeightHorizontalLinearLayout) this.receiver));
            }

            @Override // kotlin.jvm.internal.MutablePropertyReference0Impl, kotlin.reflect.KMutableProperty0
            public void set(Object obj) {
                DualHeightHorizontalLinearLayout.access$setSingleLineVerticalPaddingPx$p((DualHeightHorizontalLinearLayout) this.receiver, ((Number) obj).intValue());
            }
        };
        if (typedValue2 != null) {
            if (typedValue2.resourceId != 0) {
                i = getContext().getResources().getDimensionPixelSize(typedValue2.resourceId);
            } else {
                i = (int) typedValue2.getDimension(getDisplayMetrics());
            }
            i3 = i;
        }
        dualHeightHorizontalLinearLayout$updateResources$4.set(Integer.valueOf(i3));
    }
}
