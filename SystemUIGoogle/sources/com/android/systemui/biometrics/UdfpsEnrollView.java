package com.android.systemui.biometrics;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.systemui.R$id;
import java.util.Objects;
/* loaded from: classes.dex */
public class UdfpsEnrollView extends UdfpsAnimationView {
    private ImageView mFingerprintView;
    private final UdfpsEnrollDrawable mFingerprintDrawable = new UdfpsEnrollDrawable(((FrameLayout) this).mContext);
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public /* bridge */ /* synthetic */ void onExpansionChanged(float f, boolean z) {
        super.onExpansionChanged(f, z);
    }

    public UdfpsEnrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        ImageView imageView = (ImageView) findViewById(R$id.udfps_enroll_animation_fp_view);
        this.mFingerprintView = imageView;
        imageView.setImageDrawable(this.mFingerprintDrawable);
    }

    @Override // com.android.systemui.biometrics.UdfpsAnimationView
    public UdfpsDrawable getDrawable() {
        return this.mFingerprintDrawable;
    }

    /* access modifiers changed from: package-private */
    public void setEnrollHelper(UdfpsEnrollHelper udfpsEnrollHelper) {
        this.mFingerprintDrawable.setEnrollHelper(udfpsEnrollHelper);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEnrollmentProgress$0(int i, int i2) {
        this.mFingerprintDrawable.onEnrollmentProgress(i, i2);
    }

    /* access modifiers changed from: package-private */
    public void onEnrollmentProgress(int i, int i2) {
        this.mHandler.post(new Runnable(i, i2) { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UdfpsEnrollView.$r8$lambda$3fyEijjqCwXTZ9eGYlNR8uSTZys(UdfpsEnrollView.this, this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onLastStepAcquired() {
        Handler handler = this.mHandler;
        UdfpsEnrollDrawable udfpsEnrollDrawable = this.mFingerprintDrawable;
        Objects.requireNonNull(udfpsEnrollDrawable);
        handler.post(new Runnable() { // from class: com.android.systemui.biometrics.UdfpsEnrollView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                UdfpsEnrollDrawable.this.onLastStepAcquired();
            }
        });
    }
}
