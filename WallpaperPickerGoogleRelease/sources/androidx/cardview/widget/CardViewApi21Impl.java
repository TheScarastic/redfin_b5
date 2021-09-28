package androidx.cardview.widget;

import androidx.cardview.widget.CardView;
/* loaded from: classes.dex */
public class CardViewApi21Impl implements CardViewImpl {
    public final RoundRectDrawable getCardBackground(CardViewDelegate cardViewDelegate) {
        return (RoundRectDrawable) ((CardView.AnonymousClass1) cardViewDelegate).mCardBackground;
    }

    public float getRadius(CardViewDelegate cardViewDelegate) {
        return getCardBackground(cardViewDelegate).mRadius;
    }
}
