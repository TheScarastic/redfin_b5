package com.android.systemui.screenshot;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.drawable.Icon;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class ScreenshotActionChip extends FrameLayout {
    private ImageView mIconView;
    private boolean mIsPending;
    private TextView mTextView;

    public ScreenshotActionChip(Context context) {
        this(context, null);
    }

    public ScreenshotActionChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScreenshotActionChip(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public ScreenshotActionChip(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mIsPending = false;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        this.mIconView = (ImageView) findViewById(R$id.screenshot_action_chip_icon);
        TextView textView = (TextView) findViewById(R$id.screenshot_action_chip_text);
        this.mTextView = textView;
        updatePadding(textView.getText().length() > 0);
    }

    @Override // android.view.View
    public void setPressed(boolean z) {
        super.setPressed(this.mIsPending || z);
    }

    /* access modifiers changed from: package-private */
    public void setIcon(Icon icon, boolean z) {
        this.mIconView.setImageIcon(icon);
        if (!z) {
            this.mIconView.setImageTintList(null);
        }
    }

    /* access modifiers changed from: package-private */
    public void setText(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
        updatePadding(charSequence.length() > 0);
    }

    /* access modifiers changed from: package-private */
    public void setPendingIntent(PendingIntent pendingIntent, Runnable runnable) {
        setOnClickListener(new View.OnClickListener(pendingIntent, runnable) { // from class: com.android.systemui.screenshot.ScreenshotActionChip$$ExternalSyntheticLambda0
            public final /* synthetic */ PendingIntent f$0;
            public final /* synthetic */ Runnable f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenshotActionChip.m221$r8$lambda$YYmy_EMvNzh8dI0lg04ke5WanU(this.f$0, this.f$1, view);
            }
        });
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$setPendingIntent$0(PendingIntent pendingIntent, Runnable runnable, View view) {
        try {
            pendingIntent.send();
            runnable.run();
        } catch (PendingIntent.CanceledException e) {
            Log.e("ScreenshotActionChip", "Intent cancelled", e);
        }
    }

    /* access modifiers changed from: package-private */
    public void setIsPending(boolean z) {
        this.mIsPending = z;
        setPressed(z);
    }

    private void updatePadding(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mTextView.getLayoutParams();
        if (z) {
            int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.screenshot_action_chip_padding_horizontal);
            int dimensionPixelSize2 = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.screenshot_action_chip_spacing);
            layoutParams.setMarginStart(dimensionPixelSize);
            layoutParams.setMarginEnd(dimensionPixelSize2);
            layoutParams2.setMarginEnd(dimensionPixelSize);
        } else {
            int dimensionPixelSize3 = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.screenshot_action_chip_icon_only_padding_horizontal);
            layoutParams.setMarginStart(dimensionPixelSize3);
            layoutParams.setMarginEnd(dimensionPixelSize3);
        }
        this.mTextView.setVisibility(z ? 0 : 8);
        this.mIconView.setLayoutParams(layoutParams);
        this.mTextView.setLayoutParams(layoutParams2);
    }
}
