package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.cardview.R$styleable;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import com.android.systemui.shared.R;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class CardView extends FrameLayout {
    public static final int[] COLOR_BACKGROUND_ATTR = {16842801};
    public static final CardViewImpl IMPL = new CardViewApi21Impl();
    public final CardViewDelegate mCardViewDelegate;
    public boolean mCompatPadding;
    public final Rect mContentPadding;
    public boolean mPreventCornerOverlap;
    public final Rect mShadowBounds;
    public int mUserSetMinHeight;
    public int mUserSetMinWidth;

    public CardView(Context context) {
        this(context, null);
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    public void setMinimumHeight(int i) {
        this.mUserSetMinHeight = i;
        super.setMinimumHeight(i);
    }

    @Override // android.view.View
    public void setMinimumWidth(int i) {
        this.mUserSetMinWidth = i;
        super.setMinimumWidth(i);
    }

    @Override // android.view.View
    public void setPadding(int i, int i2, int i3, int i4) {
    }

    @Override // android.view.View
    public void setPaddingRelative(int i, int i2, int i3, int i4) {
    }

    public void setRadius(float f) {
        RoundRectDrawable cardBackground = ((CardViewApi21Impl) IMPL).getCardBackground(this.mCardViewDelegate);
        if (f != cardBackground.mRadius) {
            cardBackground.mRadius = f;
            cardBackground.updateBounds(null);
            cardBackground.invalidateSelf();
        }
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.cardViewStyle);
    }

    public CardView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        ColorStateList colorStateList;
        int i2;
        Rect rect = new Rect();
        this.mContentPadding = rect;
        this.mShadowBounds = new Rect();
        AnonymousClass1 r1 = new CardViewDelegate() { // from class: androidx.cardview.widget.CardView.1
            public Drawable mCardBackground;

            public void setShadowPadding(int i3, int i4, int i5, int i6) {
                CardView.this.mShadowBounds.set(i3, i4, i5, i6);
                CardView cardView = CardView.this;
                Rect rect2 = cardView.mContentPadding;
                CardView.super.setPadding(i3 + rect2.left, i4 + rect2.top, i5 + rect2.right, i6 + rect2.bottom);
            }
        };
        this.mCardViewDelegate = r1;
        int[] iArr = R$styleable.CardView;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, iArr, i, R.style.CardView);
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        ViewCompat.Api29Impl.saveAttributeDataForStyleable(this, context, iArr, attributeSet, obtainStyledAttributes, i, R.style.CardView);
        if (obtainStyledAttributes.hasValue(2)) {
            colorStateList = obtainStyledAttributes.getColorStateList(2);
        } else {
            TypedArray obtainStyledAttributes2 = getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            int color = obtainStyledAttributes2.getColor(0, 0);
            obtainStyledAttributes2.recycle();
            float[] fArr = new float[3];
            Color.colorToHSV(color, fArr);
            if (fArr[2] > 0.5f) {
                i2 = getResources().getColor(R.color.cardview_light_background);
            } else {
                i2 = getResources().getColor(R.color.cardview_dark_background);
            }
            colorStateList = ColorStateList.valueOf(i2);
        }
        float dimension = obtainStyledAttributes.getDimension(3, 0.0f);
        float dimension2 = obtainStyledAttributes.getDimension(4, 0.0f);
        float dimension3 = obtainStyledAttributes.getDimension(5, 0.0f);
        this.mCompatPadding = obtainStyledAttributes.getBoolean(7, false);
        this.mPreventCornerOverlap = obtainStyledAttributes.getBoolean(6, true);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        rect.left = obtainStyledAttributes.getDimensionPixelSize(10, dimensionPixelSize);
        rect.top = obtainStyledAttributes.getDimensionPixelSize(12, dimensionPixelSize);
        rect.right = obtainStyledAttributes.getDimensionPixelSize(11, dimensionPixelSize);
        rect.bottom = obtainStyledAttributes.getDimensionPixelSize(9, dimensionPixelSize);
        dimension3 = dimension2 > dimension3 ? dimension2 : dimension3;
        this.mUserSetMinWidth = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.mUserSetMinHeight = obtainStyledAttributes.getDimensionPixelSize(1, 0);
        obtainStyledAttributes.recycle();
        CardViewApi21Impl cardViewApi21Impl = (CardViewApi21Impl) IMPL;
        RoundRectDrawable roundRectDrawable = new RoundRectDrawable(colorStateList, dimension);
        AnonymousClass1 r11 = r1;
        r11.mCardBackground = roundRectDrawable;
        CardView.this.setBackgroundDrawable(roundRectDrawable);
        CardView cardView = CardView.this;
        cardView.setClipToOutline(true);
        cardView.setElevation(dimension2);
        RoundRectDrawable cardBackground = cardViewApi21Impl.getCardBackground(r1);
        CardView cardView2 = CardView.this;
        boolean z = cardView2.mCompatPadding;
        boolean z2 = cardView2.mPreventCornerOverlap;
        if (!(dimension3 == cardBackground.mPadding && cardBackground.mInsetForPadding == z && cardBackground.mInsetForRadius == z2)) {
            cardBackground.mPadding = dimension3;
            cardBackground.mInsetForPadding = z;
            cardBackground.mInsetForRadius = z2;
            cardBackground.updateBounds(null);
            cardBackground.invalidateSelf();
        }
        if (!CardView.this.mCompatPadding) {
            r11.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float f = cardViewApi21Impl.getCardBackground(r1).mPadding;
        float f2 = cardViewApi21Impl.getCardBackground(r1).mRadius;
        int ceil = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateHorizontalPadding(f, f2, CardView.this.mPreventCornerOverlap));
        int ceil2 = (int) Math.ceil((double) RoundRectDrawableWithShadow.calculateVerticalPadding(f, f2, CardView.this.mPreventCornerOverlap));
        r11.setShadowPadding(ceil, ceil2, ceil, ceil2);
    }
}
