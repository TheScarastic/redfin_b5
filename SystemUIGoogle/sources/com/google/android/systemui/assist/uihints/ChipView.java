package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.google.common.base.Preconditions;
/* loaded from: classes2.dex */
public class ChipView extends FrameLayout {
    private final Drawable BACKGROUND_DARK;
    private final Drawable BACKGROUND_LIGHT;
    private final int TEXT_COLOR_DARK;
    private final int TEXT_COLOR_LIGHT;
    private LinearLayout mChip;
    private ImageView mIconView;
    private TextView mLabelView;
    private Space mSpaceView;

    public ChipView(Context context) {
        this(context, null);
    }

    public ChipView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ChipView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ChipView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.BACKGROUND_DARK = context.getDrawable(R$drawable.assist_chip_background_dark);
        this.BACKGROUND_LIGHT = context.getDrawable(R$drawable.assist_chip_background_light);
        this.TEXT_COLOR_DARK = context.getColor(R$color.assist_chip_text_dark);
        this.TEXT_COLOR_LIGHT = context.getColor(R$color.assist_chip_text_light);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mChip = (LinearLayout) Preconditions.checkNotNull((LinearLayout) findViewById(R$id.chip_background));
        this.mIconView = (ImageView) Preconditions.checkNotNull((ImageView) findViewById(R$id.chip_icon));
        this.mLabelView = (TextView) Preconditions.checkNotNull((TextView) findViewById(R$id.chip_label));
        this.mSpaceView = (Space) Preconditions.checkNotNull((Space) findViewById(R$id.chip_element_padding));
    }

    public boolean setChip(Bundle bundle) {
        Icon icon = (Icon) bundle.getParcelable("icon");
        String string = bundle.getString("label");
        if (icon == null && (string == null || string.length() == 0)) {
            Log.w("ChipView", "Neither icon nor label provided; ignoring chip");
            return false;
        }
        if (icon == null) {
            this.mIconView.setVisibility(8);
            this.mSpaceView.setVisibility(8);
            this.mLabelView.setText(string);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mLabelView.getLayoutParams();
            int i = layoutParams.rightMargin;
            layoutParams.setMargins(i, layoutParams.topMargin, i, layoutParams.bottomMargin);
        } else if (string == null || string.length() == 0) {
            this.mLabelView.setVisibility(8);
            this.mSpaceView.setVisibility(8);
            this.mIconView.setImageIcon(icon);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            int i2 = layoutParams2.leftMargin;
            layoutParams2.setMargins(i2, layoutParams2.topMargin, i2, layoutParams2.bottomMargin);
        } else {
            this.mIconView.setImageIcon(icon);
            this.mLabelView.setText(string);
        }
        if (bundle.getParcelable("tap_action") == null) {
            Log.w("ChipView", "No tap action provided; ignoring chip");
            return false;
        }
        setTapAction((PendingIntent) bundle.getParcelable("tap_action"));
        return true;
    }

    public void updateTextSize(float f) {
        this.mLabelView.setTextSize(0, f);
    }

    public void setHasDarkBackground(boolean z) {
        this.mChip.setBackground(z ? this.BACKGROUND_DARK : this.BACKGROUND_LIGHT);
        this.mLabelView.setTextColor(z ? this.TEXT_COLOR_DARK : this.TEXT_COLOR_LIGHT);
    }

    private void setTapAction(PendingIntent pendingIntent) {
        setOnClickListener(new View.OnClickListener(pendingIntent) { // from class: com.google.android.systemui.assist.uihints.ChipView$$ExternalSyntheticLambda0
            public final /* synthetic */ PendingIntent f$0;

            {
                this.f$0 = r1;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ChipView.m615$r8$lambda$186gLOD3fOLnCQhHUVjX5G57Rs(this.f$0, view);
            }
        });
    }

    public static /* synthetic */ void lambda$setTapAction$0(PendingIntent pendingIntent, View view) {
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
            Log.w("ChipView", "Pending intent cancelled", e);
        }
    }
}
