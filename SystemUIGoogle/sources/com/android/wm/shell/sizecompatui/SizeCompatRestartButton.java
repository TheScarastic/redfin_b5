package com.android.wm.shell.sizecompatui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public class SizeCompatRestartButton extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {
    private SizeCompatUILayout mLayout;

    public SizeCompatRestartButton(Context context) {
        super(context);
    }

    public SizeCompatRestartButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SizeCompatRestartButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SizeCompatRestartButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    /* access modifiers changed from: package-private */
    public void inject(SizeCompatUILayout sizeCompatUILayout) {
        this.mLayout = sizeCompatUILayout;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        ImageButton imageButton = (ImageButton) findViewById(R.id.size_compat_restart_button);
        ColorStateList valueOf = ColorStateList.valueOf(-3355444);
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(1);
        gradientDrawable.setColor(valueOf);
        imageButton.setBackground(new RippleDrawable(valueOf, null, gradientDrawable));
        imageButton.setOnClickListener(this);
        imageButton.setOnLongClickListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mLayout.onRestartButtonClicked();
    }

    @Override // android.view.View.OnLongClickListener
    public boolean onLongClick(View view) {
        this.mLayout.onRestartButtonLongClicked();
        return true;
    }
}
