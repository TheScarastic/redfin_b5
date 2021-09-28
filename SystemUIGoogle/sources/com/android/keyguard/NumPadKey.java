package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.TextView;
import com.android.internal.widget.LockPatternUtils;
import com.android.settingslib.Utils;
import com.android.systemui.R$array;
import com.android.systemui.R$attr;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$style;
import com.android.systemui.R$styleable;
/* loaded from: classes.dex */
public class NumPadKey extends ViewGroup {
    static String[] sKlondike;
    private NumPadAnimator mAnimator;
    private int mDigit;
    private final TextView mDigitText;
    private final TextView mKlondikeText;
    private View.OnClickListener mListener;
    private final LockPatternUtils mLockPatternUtils;
    private int mOrientation;
    private final PowerManager mPM;
    private PasswordTextView mTextView;
    private int mTextViewResId;

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public void userActivity() {
        this.mPM.userActivity(SystemClock.uptimeMillis(), false);
    }

    public NumPadKey(Context context) {
        this(context, null);
    }

    public NumPadKey(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R$attr.numPadKeyStyle);
    }

    public NumPadKey(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, R$layout.keyguard_num_pad_key);
    }

    /* JADX INFO: finally extract failed */
    protected NumPadKey(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i);
        int i3;
        this.mDigit = -1;
        this.mListener = new View.OnClickListener() { // from class: com.android.keyguard.NumPadKey.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                View findViewById;
                if (NumPadKey.this.mTextView == null && NumPadKey.this.mTextViewResId > 0 && (findViewById = NumPadKey.this.getRootView().findViewById(NumPadKey.this.mTextViewResId)) != null && (findViewById instanceof PasswordTextView)) {
                    NumPadKey.this.mTextView = (PasswordTextView) findViewById;
                }
                if (NumPadKey.this.mTextView != null && NumPadKey.this.mTextView.isEnabled()) {
                    NumPadKey.this.mTextView.append(Character.forDigit(NumPadKey.this.mDigit, 10));
                }
                NumPadKey.this.userActivity();
            }
        };
        setFocusable(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.NumPadKey, i, i2);
        try {
            this.mDigit = obtainStyledAttributes.getInt(R$styleable.NumPadKey_digit, this.mDigit);
            this.mTextViewResId = obtainStyledAttributes.getResourceId(R$styleable.NumPadKey_textView, 0);
            obtainStyledAttributes.recycle();
            setOnClickListener(this.mListener);
            setOnHoverListener(new LiftToActivateListener((AccessibilityManager) context.getSystemService("accessibility")));
            this.mLockPatternUtils = new LockPatternUtils(context);
            this.mPM = (PowerManager) ((ViewGroup) this).mContext.getSystemService("power");
            ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(i2, (ViewGroup) this, true);
            TextView textView = (TextView) findViewById(R$id.digit_text);
            this.mDigitText = textView;
            textView.setText(Integer.toString(this.mDigit));
            TextView textView2 = (TextView) findViewById(R$id.klondike_text);
            this.mKlondikeText = textView2;
            if (this.mDigit >= 0) {
                if (sKlondike == null) {
                    sKlondike = getResources().getStringArray(R$array.lockscreen_num_pad_klondike);
                }
                String[] strArr = sKlondike;
                if (strArr != null && strArr.length > (i3 = this.mDigit)) {
                    String str = strArr[i3];
                    if (str.length() > 0) {
                        textView2.setText(str);
                    } else if (textView2.getVisibility() != 8) {
                        textView2.setVisibility(4);
                    }
                }
            }
            setContentDescription(textView.getText().toString());
            Drawable background = getBackground();
            if (background instanceof RippleDrawable) {
                this.mAnimator = new NumPadAnimator(context, (RippleDrawable) background, R$style.NumPadKey);
            } else {
                this.mAnimator = null;
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        this.mOrientation = configuration.orientation;
    }

    public void reloadColors() {
        int defaultColor = Utils.getColorAttr(getContext(), 16842806).getDefaultColor();
        int defaultColor2 = Utils.getColorAttr(getContext(), 16842808).getDefaultColor();
        this.mDigitText.setTextColor(defaultColor);
        this.mKlondikeText.setTextColor(defaultColor2);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.reloadColors(getContext());
        }
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            doHapticKeyClick();
            NumPadAnimator numPadAnimator = this.mAnimator;
            if (numPadAnimator != null) {
                numPadAnimator.start();
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        measureChildren(i, i2);
        int measuredWidth = getMeasuredWidth();
        if (this.mAnimator == null || this.mOrientation == 2) {
            measuredWidth = (int) (((float) measuredWidth) * 0.66f);
        }
        setMeasuredDimension(getMeasuredWidth(), measuredWidth);
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredHeight = this.mDigitText.getMeasuredHeight();
        int measuredHeight2 = this.mKlondikeText.getMeasuredHeight();
        int height = (getHeight() / 2) - ((measuredHeight + measuredHeight2) / 2);
        int width = getWidth() / 2;
        int measuredWidth = width - (this.mDigitText.getMeasuredWidth() / 2);
        int i5 = measuredHeight + height;
        TextView textView = this.mDigitText;
        textView.layout(measuredWidth, height, textView.getMeasuredWidth() + measuredWidth, i5);
        int i6 = (int) (((float) i5) - (((float) measuredHeight2) * 0.35f));
        int measuredWidth2 = width - (this.mKlondikeText.getMeasuredWidth() / 2);
        TextView textView2 = this.mKlondikeText;
        textView2.layout(measuredWidth2, i6, textView2.getMeasuredWidth() + measuredWidth2, measuredHeight2 + i6);
        NumPadAnimator numPadAnimator = this.mAnimator;
        if (numPadAnimator != null) {
            numPadAnimator.onLayout(i4 - i2);
        }
    }

    public void doHapticKeyClick() {
        if (this.mLockPatternUtils.isTactileFeedbackEnabled()) {
            performHapticFeedback(1, 3);
        }
    }
}
