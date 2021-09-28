package com.android.systemui.accessibility.floatingmenu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.recents.TriangleShape;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class BaseTooltipView extends FrameLayout {
    private final AccessibilityFloatingMenuView mAnchorView;
    private int mArrowCornerRadius;
    private int mArrowHeight;
    private int mArrowMargin;
    private int mArrowWidth;
    private final WindowManager.LayoutParams mCurrentLayoutParams = createDefaultLayoutParams();
    private int mFontSize;
    private boolean mIsShowing;
    private int mScreenWidth;
    private TextView mTextView;
    private int mTextViewCornerRadius;
    private int mTextViewMargin;
    private int mTextViewPadding;
    private final WindowManager mWindowManager;

    /* access modifiers changed from: package-private */
    public BaseTooltipView(Context context, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(context);
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mAnchorView = accessibilityFloatingMenuView;
        initViews();
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mAnchorView.onConfigurationChanged(configuration);
        updateTooltipView();
        this.mWindowManager.updateViewLayout(this, this.mCurrentLayoutParams);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 4) {
            hide();
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS);
    }

    @Override // android.view.View
    public boolean performAccessibilityAction(int i, Bundle bundle) {
        if (i != AccessibilityNodeInfo.AccessibilityAction.ACTION_DISMISS.getId()) {
            return super.performAccessibilityAction(i, bundle);
        }
        hide();
        return true;
    }

    /* access modifiers changed from: package-private */
    public void show() {
        if (!isShowing()) {
            this.mIsShowing = true;
            updateTooltipView();
            this.mWindowManager.addView(this, this.mCurrentLayoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        if (isShowing()) {
            this.mIsShowing = false;
            this.mWindowManager.removeView(this);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDescription(CharSequence charSequence) {
        this.mTextView.setText(charSequence);
    }

    /* access modifiers changed from: package-private */
    public void setMovementMethod(MovementMethod movementMethod) {
        this.mTextView.setMovementMethod(movementMethod);
    }

    private boolean isShowing() {
        return this.mIsShowing;
    }

    private void initViews() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.accessibility_floating_menu_tooltip, (ViewGroup) this, false);
        this.mTextView = (TextView) inflate.findViewById(R$id.text);
        addView(inflate);
    }

    private static WindowManager.LayoutParams createDefaultLayoutParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2024, 262152, -3);
        layoutParams.windowAnimations = 16973827;
        layoutParams.gravity = 8388659;
        return layoutParams;
    }

    private void updateDimensions() {
        Resources resources = getResources();
        this.mScreenWidth = resources.getDisplayMetrics().widthPixels;
        this.mArrowWidth = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_arrow_width);
        this.mArrowHeight = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_arrow_height);
        this.mArrowMargin = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_arrow_margin);
        this.mArrowCornerRadius = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_arrow_corner_radius);
        this.mFontSize = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_font_size);
        this.mTextViewMargin = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_margin);
        this.mTextViewPadding = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_padding);
        this.mTextViewCornerRadius = resources.getDimensionPixelSize(R$dimen.accessibility_floating_tooltip_text_corner_radius);
    }

    private void updateTooltipView() {
        updateDimensions();
        updateTextView();
        Rect windowLocationOnScreen = this.mAnchorView.getWindowLocationOnScreen();
        updateArrowWith(windowLocationOnScreen);
        updateWidthWith(windowLocationOnScreen);
        updateLocationWith(windowLocationOnScreen);
    }

    private void updateTextView() {
        this.mTextView.setTextSize(0, (float) this.mFontSize);
        TextView textView = this.mTextView;
        int i = this.mTextViewPadding;
        textView.setPadding(i, i, i, i);
        GradientDrawable gradientDrawable = (GradientDrawable) this.mTextView.getBackground();
        gradientDrawable.setCornerRadius((float) this.mTextViewCornerRadius);
        gradientDrawable.setColor(Utils.getColorAttrDefaultColor(getContext(), 17956900));
    }

    private void updateArrowWith(Rect rect) {
        int i;
        int i2;
        boolean isAnchorViewOnLeft = isAnchorViewOnLeft(rect);
        if (isAnchorViewOnLeft) {
            i = R$id.arrow_left;
        } else {
            i = R$id.arrow_right;
        }
        View findViewById = findViewById(i);
        findViewById.setVisibility(0);
        drawArrow(findViewById, isAnchorViewOnLeft);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) findViewById.getLayoutParams();
        layoutParams.width = this.mArrowWidth;
        layoutParams.height = this.mArrowHeight;
        if (isAnchorViewOnLeft) {
            i2 = 0;
        } else {
            i2 = this.mArrowMargin;
        }
        layoutParams.setMargins(i2, 0, isAnchorViewOnLeft ? this.mArrowMargin : 0, 0);
        findViewById.setLayoutParams(layoutParams);
    }

    private void updateWidthWith(Rect rect) {
        ViewGroup.LayoutParams layoutParams = this.mTextView.getLayoutParams();
        layoutParams.width = getTextWidthWith(rect);
        this.mTextView.setLayoutParams(layoutParams);
    }

    private void updateLocationWith(Rect rect) {
        int i;
        WindowManager.LayoutParams layoutParams = this.mCurrentLayoutParams;
        if (isAnchorViewOnLeft(rect)) {
            i = rect.width();
        } else {
            i = (this.mScreenWidth - getWindowWidthWith(rect)) - rect.width();
        }
        layoutParams.x = i;
        this.mCurrentLayoutParams.y = rect.centerY() - (getTextHeightWith(rect) / 2);
    }

    private void drawArrow(View view, boolean z) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        ShapeDrawable shapeDrawable = new ShapeDrawable(TriangleShape.createHorizontal((float) layoutParams.width, (float) layoutParams.height, z));
        Paint paint = shapeDrawable.getPaint();
        paint.setColor(Utils.getColorAttrDefaultColor(getContext(), 17956900));
        paint.setPathEffect(new CornerPathEffect((float) this.mArrowCornerRadius));
        view.setBackground(shapeDrawable);
    }

    private boolean isAnchorViewOnLeft(Rect rect) {
        return rect.left < this.mScreenWidth / 2;
    }

    private int getTextWidthWith(Rect rect) {
        this.mTextView.measure(View.MeasureSpec.makeMeasureSpec(getAvailableTextWidthWith(rect), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        return this.mTextView.getMeasuredWidth();
    }

    private int getTextHeightWith(Rect rect) {
        this.mTextView.measure(View.MeasureSpec.makeMeasureSpec(getAvailableTextWidthWith(rect), Integer.MIN_VALUE), View.MeasureSpec.makeMeasureSpec(0, 0));
        return this.mTextView.getMeasuredHeight();
    }

    private int getAvailableTextWidthWith(Rect rect) {
        return (((this.mScreenWidth - rect.width()) - this.mArrowWidth) - this.mArrowMargin) - this.mTextViewMargin;
    }

    private int getWindowWidthWith(Rect rect) {
        return getTextWidthWith(rect) + this.mArrowWidth + this.mArrowMargin;
    }
}
