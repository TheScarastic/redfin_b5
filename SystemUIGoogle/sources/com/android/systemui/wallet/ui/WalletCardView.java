package com.android.systemui.wallet.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.cardview.widget.CardView;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
/* loaded from: classes2.dex */
public class WalletCardView extends CardView {
    private final Paint mBorderPaint;

    public WalletCardView(Context context) {
        this(context, null);
    }

    public WalletCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint();
        this.mBorderPaint = paint;
        paint.setColor(context.getColor(R$color.wallet_card_border));
        paint.setStrokeWidth(context.getResources().getDimension(R$dimen.wallet_card_border_width));
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float radius = getRadius();
        canvas.drawRoundRect(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), radius, radius, this.mBorderPaint);
    }
}
