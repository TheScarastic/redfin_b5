package com.android.keyguard;

import android.R;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.keyguard.AnimatableClockView;
import com.android.systemui.R$string;
import com.android.systemui.R$styleable;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public class AnimatableClockView extends TextView {
    private static final CharSequence DOUBLE_LINE_FORMAT_12_HOUR = "hh\nmm";
    private static final CharSequence DOUBLE_LINE_FORMAT_24_HOUR = "HH\nmm";
    private int mChargeAnimationDelay;
    private CharSequence mDescFormat;
    private int mDozingColor;
    private final int mDozingWeight;
    private CharSequence mFormat;
    private boolean mIsSingleLine;
    private float mLineSpacingScale;
    private int mLockScreenColor;
    private final int mLockScreenWeight;
    private Runnable mOnTextAnimatorInitialized;
    private TextAnimator mTextAnimator;
    private final Calendar mTime;

    /* loaded from: classes.dex */
    public interface DozeStateGetter {
        boolean isDozing();
    }

    public AnimatableClockView(Context context) {
        this(context, null, 0, 0);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public AnimatableClockView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    /* JADX INFO: finally extract failed */
    public AnimatableClockView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTime = Calendar.getInstance();
        this.mLineSpacingScale = 1.0f;
        this.mChargeAnimationDelay = 0;
        this.mTextAnimator = null;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AnimatableClockView, i, i2);
        try {
            this.mDozingWeight = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_dozeWeight, 100);
            this.mLockScreenWeight = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_lockScreenWeight, 300);
            this.mChargeAnimationDelay = obtainStyledAttributes.getInt(R$styleable.AnimatableClockView_chargeAnimationDelay, 200);
            obtainStyledAttributes.recycle();
            TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, R.styleable.TextView, i, i2);
            try {
                this.mIsSingleLine = obtainStyledAttributes2.getBoolean(32, false);
                obtainStyledAttributes2.recycle();
                refreshFormat();
            } catch (Throwable th) {
                obtainStyledAttributes2.recycle();
                throw th;
            }
        } catch (Throwable th2) {
            obtainStyledAttributes.recycle();
            throw th2;
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        refreshFormat();
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void refreshTime() {
        this.mTime.setTimeInMillis(System.currentTimeMillis());
        setText(DateFormat.format(this.mFormat, this.mTime));
        setContentDescription(DateFormat.format(this.mDescFormat, this.mTime));
    }

    public void onTimeZoneChanged(TimeZone timeZone) {
        this.mTime.setTimeZone(timeZone);
        refreshFormat();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator == null) {
            this.mTextAnimator = new TextAnimator(getLayout(), new Function0() { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda2
                @Override // kotlin.jvm.functions.Function0
                public final Object invoke() {
                    return AnimatableClockView.this.lambda$onMeasure$0();
                }
            });
            Runnable runnable = this.mOnTextAnimatorInitialized;
            if (runnable != null) {
                runnable.run();
                this.mOnTextAnimatorInitialized = null;
                return;
            }
            return;
        }
        textAnimator.updateLayout(getLayout());
    }

    public /* synthetic */ Unit lambda$onMeasure$0() {
        invalidate();
        return Unit.INSTANCE;
    }

    @Override // android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        this.mTextAnimator.draw(canvas);
    }

    public void setLineSpacingScale(float f) {
        this.mLineSpacingScale = f;
        setLineSpacing(0.0f, f);
    }

    public void setColors(int i, int i2) {
        this.mDozingColor = i;
        this.mLockScreenColor = i2;
    }

    public void animateAppearOnLockscreen() {
        if (this.mTextAnimator != null) {
            setTextStyle(this.mDozingWeight, -1.0f, Integer.valueOf(this.mLockScreenColor), false, 0, 0, null);
            setTextStyle(this.mLockScreenWeight, -1.0f, Integer.valueOf(this.mLockScreenColor), true, 350, 0, null);
        }
    }

    public void animateCharge(DozeStateGetter dozeStateGetter) {
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator != null && !textAnimator.isRunning()) {
            setTextStyle(dozeStateGetter.isDozing() ? this.mLockScreenWeight : this.mDozingWeight, -1.0f, null, true, 500, (long) this.mChargeAnimationDelay, new Runnable(dozeStateGetter) { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda1
                public final /* synthetic */ AnimatableClockView.DozeStateGetter f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    AnimatableClockView.$r8$lambda$dcUcX8j0lVI3pyjLUFSEqOO_ito(AnimatableClockView.this, this.f$1);
                }
            });
        }
    }

    public /* synthetic */ void lambda$animateCharge$1(DozeStateGetter dozeStateGetter) {
        setTextStyle(dozeStateGetter.isDozing() ? this.mDozingWeight : this.mLockScreenWeight, -1.0f, null, true, 1000, 0, null);
    }

    public void animateDoze(boolean z, boolean z2) {
        setTextStyle(z ? this.mDozingWeight : this.mLockScreenWeight, -1.0f, Integer.valueOf(z ? this.mDozingColor : this.mLockScreenColor), z2, 300, 0, null);
    }

    private void setTextStyle(int i, float f, Integer num, boolean z, long j, long j2, Runnable runnable) {
        TextAnimator textAnimator = this.mTextAnimator;
        if (textAnimator != null) {
            textAnimator.setTextStyle(i, f, num, z, j, null, j2, runnable);
        } else {
            this.mOnTextAnimatorInitialized = new Runnable(i, f, num, j, j2, runnable) { // from class: com.android.keyguard.AnimatableClockView$$ExternalSyntheticLambda0
                public final /* synthetic */ int f$1;
                public final /* synthetic */ float f$2;
                public final /* synthetic */ Integer f$3;
                public final /* synthetic */ long f$4;
                public final /* synthetic */ long f$5;
                public final /* synthetic */ Runnable f$6;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                    this.f$5 = r7;
                    this.f$6 = r9;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    AnimatableClockView.$r8$lambda$ioWa6O7gZEqZk9x8NZMU7m2t0b0(AnimatableClockView.this, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
                }
            };
        }
    }

    public /* synthetic */ void lambda$setTextStyle$2(int i, float f, Integer num, long j, long j2, Runnable runnable) {
        this.mTextAnimator.setTextStyle(i, f, num, false, j, null, j2, runnable);
    }

    public void refreshFormat() {
        Patterns.update(((TextView) this).mContext);
        boolean is24HourFormat = DateFormat.is24HourFormat(getContext());
        boolean z = this.mIsSingleLine;
        if (z && is24HourFormat) {
            this.mFormat = Patterns.sClockView24;
        } else if (!z && is24HourFormat) {
            this.mFormat = DOUBLE_LINE_FORMAT_24_HOUR;
        } else if (!z || is24HourFormat) {
            this.mFormat = DOUBLE_LINE_FORMAT_12_HOUR;
        } else {
            this.mFormat = Patterns.sClockView12;
        }
        this.mDescFormat = is24HourFormat ? Patterns.sClockView24 : Patterns.sClockView12;
        refreshTime();
    }

    /* loaded from: classes.dex */
    public static final class Patterns {
        static String sCacheKey;
        static String sClockView12;
        static String sClockView24;

        static void update(Context context) {
            Locale locale = Locale.getDefault();
            Resources resources = context.getResources();
            String string = resources.getString(R$string.clock_12hr_format);
            String string2 = resources.getString(R$string.clock_24hr_format);
            String str = locale.toString() + string + string2;
            if (!str.equals(sCacheKey)) {
                sClockView12 = DateFormat.getBestDateTimePattern(locale, string);
                if (!string.contains("a")) {
                    sClockView12 = sClockView12.replaceAll("a", "").trim();
                }
                sClockView24 = DateFormat.getBestDateTimePattern(locale, string2);
                sCacheKey = str;
            }
        }
    }
}
