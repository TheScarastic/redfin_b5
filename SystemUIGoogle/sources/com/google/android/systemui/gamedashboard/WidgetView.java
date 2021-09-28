package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
/* loaded from: classes2.dex */
public class WidgetView extends FrameLayout {
    private LinearLayout mContentView;
    private final int mDefaultBackgroundColor;
    private TextView mDescription;
    private ImageView mIcon;
    private boolean mLoading;
    private LinearLayout mLoadingView;
    private final Drawable mOvalBackgroundDrawable;
    private TextView mTipText;
    private TextView mTitle;

    public WidgetView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOvalBackgroundDrawable = context.getDrawable(R$drawable.game_dashboard_button_background);
        this.mDefaultBackgroundColor = context.getColor(R$color.game_dashboard_color_surface);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mIcon = (ImageView) findViewById(R$id.icon);
        this.mTipText = (TextView) findViewById(R$id.tip_text);
        this.mTitle = (TextView) findViewById(R$id.title);
        this.mDescription = (TextView) findViewById(R$id.description);
        this.mContentView = (LinearLayout) findViewById(R$id.content_view);
        this.mLoadingView = (LinearLayout) findViewById(R$id.loading_view);
        this.mLoading = false;
    }

    public void update(Drawable drawable, int i, int i2, View.OnClickListener onClickListener) {
        updateInternal(drawable, this.mDefaultBackgroundColor, i, i2, onClickListener);
    }

    public void update(Bitmap bitmap, String str, String str2, String str3, View.OnClickListener onClickListener) {
        this.mIcon.setImageBitmap(bitmap);
        this.mTipText.setText(str);
        this.mTitle.setText(str2);
        this.mDescription.setText(str3);
        setOnClickListener(onClickListener);
    }

    public void setLoading(boolean z) {
        if (z != this.mLoading) {
            this.mLoading = z;
            if (z) {
                this.mLoadingView.setVisibility(0);
                this.mContentView.setVisibility(8);
                return;
            }
            this.mLoadingView.setVisibility(8);
            this.mContentView.setVisibility(0);
        }
    }

    private void updateInternal(Drawable drawable, int i, int i2, int i3, View.OnClickListener onClickListener) {
        this.mIcon.setImageDrawable(drawable);
        this.mOvalBackgroundDrawable.setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_ATOP));
        this.mIcon.setBackground(this.mOvalBackgroundDrawable);
        this.mTitle.setText(i2);
        this.mDescription.setText(i3);
        setOnClickListener(onClickListener);
    }

    @Override // android.view.View
    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setAlpha(isEnabled() ? 1.0f : 0.5f);
    }
}
