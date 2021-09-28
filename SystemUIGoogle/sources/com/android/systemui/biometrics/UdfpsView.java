package com.android.systemui.biometrics;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.widget.FrameLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$styleable;
import com.android.systemui.biometrics.UdfpsSurfaceView;
import com.android.systemui.doze.DozeReceiver;
/* loaded from: classes.dex */
public class UdfpsView extends FrameLayout implements DozeReceiver {
    private UdfpsAnimationViewController mAnimationViewController;
    private String mDebugMessage;
    private final Paint mDebugTextPaint;
    private UdfpsSurfaceView mGhbmView;
    private UdfpsHbmProvider mHbmProvider;
    private final int mHbmType;
    private boolean mIlluminationRequested;
    private final int mOnIlluminatedDelayMs;
    private FingerprintSensorPropertiesInternal mSensorProps;
    private final RectF mSensorRect;
    private final float mSensorTouchAreaCoefficient;

    /* JADX INFO: finally extract failed */
    public UdfpsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.UdfpsView, 0, 0);
        try {
            int i = R$styleable.UdfpsView_sensorTouchAreaCoefficient;
            if (obtainStyledAttributes.hasValue(i)) {
                this.mSensorTouchAreaCoefficient = obtainStyledAttributes.getFloat(i, 0.0f);
                obtainStyledAttributes.recycle();
                this.mSensorRect = new RectF();
                Paint paint = new Paint();
                this.mDebugTextPaint = paint;
                paint.setAntiAlias(true);
                paint.setColor(-16776961);
                paint.setTextSize(32.0f);
                this.mOnIlluminatedDelayMs = ((FrameLayout) this).mContext.getResources().getInteger(17694932);
                if (Build.IS_ENG || Build.IS_USERDEBUG) {
                    this.mHbmType = Settings.Secure.getIntForUser(((FrameLayout) this).mContext.getContentResolver(), "com.android.systemui.biometrics.UdfpsSurfaceView.hbmType", 1, -2);
                } else {
                    this.mHbmType = 1;
                }
            } else {
                throw new IllegalArgumentException("UdfpsView must contain sensorTouchAreaCoefficient");
            }
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        return udfpsAnimationViewController == null || !udfpsAnimationViewController.shouldPauseAuth();
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        if (this.mHbmType == 0) {
            this.mGhbmView = (UdfpsSurfaceView) findViewById(R$id.hbm_view);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSensorProperties(FingerprintSensorPropertiesInternal fingerprintSensorPropertiesInternal) {
        this.mSensorProps = fingerprintSensorPropertiesInternal;
    }

    public void setHbmProvider(UdfpsHbmProvider udfpsHbmProvider) {
        this.mHbmProvider = udfpsHbmProvider;
    }

    @Override // com.android.systemui.doze.DozeReceiver
    public void dozeTimeTick() {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.dozeTimeTick();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        super.onLayout(z, i, i2, i3, i4);
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        int i6 = 0;
        if (udfpsAnimationViewController == null) {
            i5 = 0;
        } else {
            i5 = udfpsAnimationViewController.getPaddingX();
        }
        UdfpsAnimationViewController udfpsAnimationViewController2 = this.mAnimationViewController;
        if (udfpsAnimationViewController2 != null) {
            i6 = udfpsAnimationViewController2.getPaddingY();
        }
        int i7 = this.mSensorProps.sensorRadius;
        this.mSensorRect.set((float) i5, (float) i6, (float) ((i7 * 2) + i5), (float) ((i7 * 2) + i6));
        UdfpsAnimationViewController udfpsAnimationViewController3 = this.mAnimationViewController;
        if (udfpsAnimationViewController3 != null) {
            udfpsAnimationViewController3.onSensorRectUpdated(new RectF(this.mSensorRect));
        }
    }

    /* access modifiers changed from: package-private */
    public void onTouchOutsideView() {
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onTouchOutsideView();
        }
    }

    /* access modifiers changed from: package-private */
    public void setAnimationViewController(UdfpsAnimationViewController udfpsAnimationViewController) {
        this.mAnimationViewController = udfpsAnimationViewController;
    }

    /* access modifiers changed from: package-private */
    public UdfpsAnimationViewController getAnimationViewController() {
        return this.mAnimationViewController;
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("UdfpsView", "onAttachedToWindow");
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("UdfpsView", "onDetachedFromWindow");
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!this.mIlluminationRequested && !TextUtils.isEmpty(this.mDebugMessage)) {
            canvas.drawText(this.mDebugMessage, 0.0f, 160.0f, this.mDebugTextPaint);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDebugMessage(String str) {
        this.mDebugMessage = str;
        postInvalidate();
    }

    /* access modifiers changed from: package-private */
    public boolean isWithinSensorArea(float f, float f2) {
        PointF pointF;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController == null) {
            pointF = new PointF(0.0f, 0.0f);
        } else {
            pointF = udfpsAnimationViewController.getTouchTranslation();
        }
        float centerX = this.mSensorRect.centerX() + pointF.x;
        float centerY = this.mSensorRect.centerY() + pointF.y;
        RectF rectF = this.mSensorRect;
        float f3 = (rectF.right - rectF.left) / 2.0f;
        float f4 = (rectF.bottom - rectF.top) / 2.0f;
        float f5 = this.mSensorTouchAreaCoefficient;
        return f > centerX - (f3 * f5) && f < centerX + (f3 * f5) && f2 > centerY - (f4 * f5) && f2 < centerY + (f4 * f5) && !this.mAnimationViewController.shouldPauseAuth();
    }

    /* access modifiers changed from: package-private */
    public boolean isIlluminationRequested() {
        return this.mIlluminationRequested;
    }

    public void startIllumination(Runnable runnable) {
        this.mIlluminationRequested = true;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStarting();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(new UdfpsSurfaceView.GhbmIlluminationListener() { // from class: com.android.systemui.biometrics.UdfpsView$$ExternalSyntheticLambda0
                @Override // com.android.systemui.biometrics.UdfpsSurfaceView.GhbmIlluminationListener
                public final void enableGhbm(Surface surface, Runnable runnable2) {
                    UdfpsView.$r8$lambda$gzdric4kY7Zkp8LXS9PbLbNASyA(UdfpsView.this, surface, runnable2);
                }
            });
            this.mGhbmView.setVisibility(0);
            this.mGhbmView.startGhbmIllumination(runnable);
            return;
        }
        doIlluminate(null, runnable);
    }

    /* access modifiers changed from: private */
    public void doIlluminate(Surface surface, Runnable runnable) {
        if (this.mGhbmView != null && surface == null) {
            Log.e("UdfpsView", "doIlluminate | surface must be non-null for GHBM");
        }
        this.mHbmProvider.enableHbm(this.mHbmType, surface, new Runnable(runnable) { // from class: com.android.systemui.biometrics.UdfpsView$$ExternalSyntheticLambda1
            public final /* synthetic */ Runnable f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UdfpsView.m83$r8$lambda$E79ZFAUCc0wRcWUYo7gxLMKPpo(UdfpsView.this, this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$doIlluminate$0(Runnable runnable) {
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.drawIlluminationDot(this.mSensorRect);
        }
        if (runnable != null) {
            postDelayed(runnable, (long) this.mOnIlluminatedDelayMs);
        } else {
            Log.w("UdfpsView", "doIlluminate | onIlluminatedRunnable is null");
        }
    }

    public void stopIllumination() {
        this.mIlluminationRequested = false;
        UdfpsAnimationViewController udfpsAnimationViewController = this.mAnimationViewController;
        if (udfpsAnimationViewController != null) {
            udfpsAnimationViewController.onIlluminationStopped();
        }
        UdfpsSurfaceView udfpsSurfaceView = this.mGhbmView;
        if (udfpsSurfaceView != null) {
            udfpsSurfaceView.setGhbmIlluminationListener(null);
            this.mGhbmView.setVisibility(4);
        }
        this.mHbmProvider.disableHbm(null);
    }
}
