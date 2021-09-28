package com.google.android.systemui.assist.uihints;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.SpannableStringBuilder;
import android.text.style.ReplacementSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.TextView;
import androidx.core.math.MathUtils;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.assist.DeviceConfigHelper;
import com.google.android.systemui.assist.uihints.StringUtils;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class TranscriptionView extends TextView implements TranscriptionController.TranscriptionSpaceView {
    private static final PathInterpolator INTERPOLATOR_SCROLL = new PathInterpolator(0.17f, 0.17f, 0.67f, 1.0f);
    private final float BUMPER_DISTANCE_END_PX;
    private final float BUMPER_DISTANCE_START_PX;
    private final float FADE_DISTANCE_END_PX;
    private final float FADE_DISTANCE_START_PX;
    private final int TEXT_COLOR_DARK;
    private final int TEXT_COLOR_LIGHT;
    private boolean mCardVisible;
    private DeviceConfigHelper mDeviceConfigHelper;
    private int mDisplayWidthPx;
    private boolean mHasDarkBackground;
    private SettableFuture<Void> mHideFuture;
    private Matrix mMatrix;
    private int mRequestedTextColor;
    private float[] mStops;
    private ValueAnimator mTranscriptionAnimation;
    private TranscriptionAnimator mTranscriptionAnimator;
    private SpannableStringBuilder mTranscriptionBuilder;
    private AnimatorSet mVisibilityAnimators;

    @VisibleForTesting
    static float interpolate(long j, long j2, float f) {
        return (((float) (j2 - j)) * f) + ((float) j);
    }

    public TranscriptionView(Context context) {
        this(context, null);
    }

    public TranscriptionView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TranscriptionView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TranscriptionView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mTranscriptionBuilder = new SpannableStringBuilder();
        this.mVisibilityAnimators = new AnimatorSet();
        this.mHideFuture = null;
        this.mHasDarkBackground = false;
        this.mCardVisible = false;
        this.mRequestedTextColor = 0;
        this.mMatrix = new Matrix();
        this.mDisplayWidthPx = 0;
        this.mTranscriptionAnimator = new TranscriptionAnimator();
        initializeDeviceConfigHelper(new DeviceConfigHelper());
        this.BUMPER_DISTANCE_START_PX = context.getResources().getDimension(R$dimen.zerostate_icon_left_margin) + context.getResources().getDimension(R$dimen.zerostate_icon_tap_padding);
        this.BUMPER_DISTANCE_END_PX = context.getResources().getDimension(R$dimen.keyboard_icon_right_margin) + context.getResources().getDimension(R$dimen.keyboard_icon_tap_padding);
        this.FADE_DISTANCE_START_PX = context.getResources().getDimension(R$dimen.zerostate_icon_size);
        this.FADE_DISTANCE_END_PX = context.getResources().getDimension(R$dimen.keyboard_icon_size) / 2.0f;
        this.TEXT_COLOR_DARK = context.getResources().getColor(R$color.transcription_text_dark);
        this.TEXT_COLOR_LIGHT = context.getResources().getColor(R$color.transcription_text_light);
        updateDisplayWidth();
        setHasDarkBackground(!this.mHasDarkBackground);
    }

    @VisibleForTesting
    long getAdaptiveDuration(float f, float f2) {
        return Math.min(getDurationMaxMs(), Math.max(getDurationMinMs(), (long) (f * interpolate(getDurationRegularMs(), getDurationFastMs(), f / f2))));
    }

    @VisibleForTesting
    void initializeDeviceConfigHelper(DeviceConfigHelper deviceConfigHelper) {
        this.mDeviceConfigHelper = deviceConfigHelper;
    }

    private void updateDisplayWidth() {
        int rotatedWidth = DisplayUtils.getRotatedWidth(((TextView) this).mContext);
        this.mDisplayWidthPx = rotatedWidth;
        float f = this.BUMPER_DISTANCE_START_PX;
        this.mStops = new float[]{f / ((float) rotatedWidth), (f + this.FADE_DISTANCE_START_PX) / ((float) rotatedWidth), ((((float) rotatedWidth) - this.FADE_DISTANCE_END_PX) - this.BUMPER_DISTANCE_END_PX) / ((float) rotatedWidth), 1.0f};
        updateColor();
    }

    @Override // android.widget.TextView, android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        String spannableStringBuilder = this.mTranscriptionBuilder.toString();
        resetTranscription();
        setTranscription(spannableStringBuilder);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public ListenableFuture<Void> hide(boolean z) {
        SettableFuture<Void> settableFuture = this.mHideFuture;
        if (settableFuture != null && !settableFuture.isDone()) {
            return this.mHideFuture;
        }
        this.mHideFuture = SettableFuture.create();
        final TranscriptionView$$ExternalSyntheticLambda0 transcriptionView$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.google.android.systemui.assist.uihints.TranscriptionView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                TranscriptionView.$r8$lambda$Vp_D3LmZ9KP_rj5nPny1ovUjkXk(TranscriptionView.this);
            }
        };
        if (!z) {
            if (this.mVisibilityAnimators.isRunning()) {
                this.mVisibilityAnimators.end();
            } else {
                transcriptionView$$ExternalSyntheticLambda0.run();
            }
            return Futures.immediateFuture(null);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        this.mVisibilityAnimators = animatorSet;
        animatorSet.play(ObjectAnimator.ofFloat(this, View.ALPHA, getAlpha(), 0.0f).setDuration(400L));
        if (!this.mCardVisible) {
            this.mVisibilityAnimators.play(ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, getTranslationY(), (float) (getHeight() * -1)).setDuration(700L));
        }
        this.mVisibilityAnimators.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.assist.uihints.TranscriptionView.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                transcriptionView$$ExternalSyntheticLambda0.run();
            }
        });
        this.mVisibilityAnimators.start();
        return this.mHideFuture;
    }

    public /* synthetic */ void lambda$hide$0() {
        setVisibility(8);
        setAlpha(1.0f);
        setTranslationY(0.0f);
        resetTranscription();
        this.mHideFuture.set(null);
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public void setHasDarkBackground(boolean z) {
        if (z != this.mHasDarkBackground) {
            this.mHasDarkBackground = z;
            updateColor();
        }
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public void setCardVisible(boolean z) {
        this.mCardVisible = z;
    }

    @Override // com.google.android.systemui.assist.uihints.TranscriptionController.TranscriptionSpaceView
    public void onFontSizeChanged() {
        setTextSize(0, ((TextView) this).mContext.getResources().getDimension(R$dimen.transcription_text_size));
    }

    public void setTranscription(String str) {
        boolean z = false;
        setVisibility(0);
        updateDisplayWidth();
        ValueAnimator valueAnimator = this.mTranscriptionAnimation;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            z = true;
        }
        if (z) {
            this.mTranscriptionAnimation.cancel();
        }
        boolean isEmpty = this.mTranscriptionBuilder.toString().isEmpty();
        StringUtils.StringStabilityInfo calculateStringStabilityInfo = StringUtils.calculateStringStabilityInfo(this.mTranscriptionBuilder.toString(), str);
        this.mTranscriptionBuilder.clear();
        this.mTranscriptionBuilder.append((CharSequence) calculateStringStabilityInfo.stable);
        this.mTranscriptionBuilder.append((CharSequence) calculateStringStabilityInfo.unstable);
        int ceil = (int) Math.ceil((double) getPaint().measureText(this.mTranscriptionBuilder.toString()));
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = ceil;
            setLayoutParams(layoutParams);
        }
        updateColor();
        TranscriptionSpan transcriptionSpan = null;
        if (isEmpty || calculateStringStabilityInfo.stable.isEmpty()) {
            setUpSpans(calculateStringStabilityInfo.stable.length() + calculateStringStabilityInfo.unstable.length(), null);
            setX(getFullyVisibleDistance((float) ceil));
            updateColor();
            return;
        }
        int length = calculateStringStabilityInfo.stable.length();
        if (z && !calculateStringStabilityInfo.stable.endsWith(" ") && !calculateStringStabilityInfo.unstable.startsWith(" ")) {
            String[] split = calculateStringStabilityInfo.stable.split("\\s+");
            if (split.length > 0) {
                length -= split[split.length - 1].length();
            }
            List<TranscriptionSpan> spans = this.mTranscriptionAnimator.getSpans();
            if (!spans.isEmpty()) {
                transcriptionSpan = spans.get(spans.size() - 1);
            }
        }
        setUpSpans(length, transcriptionSpan);
        ValueAnimator createAnimator = this.mTranscriptionAnimator.createAnimator();
        this.mTranscriptionAnimation = createAnimator;
        createAnimator.start();
    }

    private void setUpSpans(int i, TranscriptionSpan transcriptionSpan) {
        TranscriptionSpan transcriptionSpan2;
        this.mTranscriptionAnimator.clearSpans();
        String spannableStringBuilder = this.mTranscriptionBuilder.toString();
        String substring = spannableStringBuilder.substring(i);
        if (substring.length() > 0) {
            int indexOf = spannableStringBuilder.indexOf(substring, i);
            int length = substring.length() + indexOf;
            if (transcriptionSpan == null) {
                transcriptionSpan2 = new TranscriptionSpan();
            } else {
                transcriptionSpan2 = new TranscriptionSpan(transcriptionSpan);
            }
            this.mTranscriptionBuilder.setSpan(transcriptionSpan2, indexOf, length, 33);
            this.mTranscriptionAnimator.addSpan(transcriptionSpan2);
        }
        setText(this.mTranscriptionBuilder, TextView.BufferType.SPANNABLE);
        updateColor();
    }

    public void setTranscriptionColor(int i) {
        this.mRequestedTextColor = i;
        updateColor();
    }

    public float getFullyVisibleDistance(float f) {
        int i = this.mDisplayWidthPx;
        float f2 = this.BUMPER_DISTANCE_END_PX;
        float f3 = this.FADE_DISTANCE_END_PX;
        return f < ((float) i) - (((this.BUMPER_DISTANCE_START_PX + f2) + f3) + this.FADE_DISTANCE_START_PX) ? (((float) i) - f) / 2.0f : ((((float) i) - f) - f3) - f2;
    }

    public void updateColor() {
        int i = this.mRequestedTextColor;
        if (i == 0) {
            i = this.mHasDarkBackground ? this.TEXT_COLOR_DARK : this.TEXT_COLOR_LIGHT;
        }
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, (float) this.mDisplayWidthPx, 0.0f, new int[]{0, i, i, 0}, this.mStops, Shader.TileMode.CLAMP);
        this.mMatrix.setTranslate(-getTranslationX(), 0.0f);
        linearGradient.setLocalMatrix(this.mMatrix);
        getPaint().setShader(linearGradient);
        invalidate();
    }

    private void resetTranscription() {
        setTranscription("");
        this.mTranscriptionAnimator = new TranscriptionAnimator();
    }

    private long getDurationRegularMs() {
        return this.mDeviceConfigHelper.getLong("assist_transcription_duration_per_px_regular", 4);
    }

    private long getDurationFastMs() {
        return this.mDeviceConfigHelper.getLong("assist_transcription_duration_per_px_fast", 3);
    }

    private long getDurationMaxMs() {
        return this.mDeviceConfigHelper.getLong("assist_transcription_max_duration", 400);
    }

    private long getDurationMinMs() {
        return this.mDeviceConfigHelper.getLong("assist_transcription_min_duration", 20);
    }

    public long getFadeInDurationMs() {
        return this.mDeviceConfigHelper.getLong("assist_transcription_fade_in_duration", 50);
    }

    /* loaded from: classes2.dex */
    public class TranscriptionAnimator implements ValueAnimator.AnimatorUpdateListener {
        private float mDistance;
        private List<TranscriptionSpan> mSpans;
        private float mStartX;

        private TranscriptionAnimator() {
            TranscriptionView.this = r1;
            this.mSpans = new ArrayList();
        }

        void addSpan(TranscriptionSpan transcriptionSpan) {
            this.mSpans.add(transcriptionSpan);
        }

        List<TranscriptionSpan> getSpans() {
            return this.mSpans;
        }

        void clearSpans() {
            this.mSpans.clear();
        }

        ValueAnimator createAnimator() {
            float measureText = TranscriptionView.this.getPaint().measureText(TranscriptionView.this.mTranscriptionBuilder.toString());
            this.mStartX = TranscriptionView.this.getX();
            this.mDistance = TranscriptionView.this.getFullyVisibleDistance(measureText) - this.mStartX;
            TranscriptionView.this.updateColor();
            long adaptiveDuration = TranscriptionView.this.getAdaptiveDuration(Math.abs(this.mDistance), (float) TranscriptionView.this.mDisplayWidthPx);
            long fadeInDurationMs = measureText > ((float) TranscriptionView.this.mDisplayWidthPx) - TranscriptionView.this.getX() ? TranscriptionView.this.getFadeInDurationMs() + adaptiveDuration : adaptiveDuration;
            float f = this.mDistance * (((float) fadeInDurationMs) / ((float) adaptiveDuration));
            float f2 = this.mStartX;
            ValueAnimator duration = ValueAnimator.ofFloat(f2, f2 + f).setDuration(fadeInDurationMs);
            duration.setInterpolator(TranscriptionView.INTERPOLATOR_SCROLL);
            duration.addUpdateListener(this);
            return duration;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            if (Math.abs(floatValue - this.mStartX) < Math.abs(this.mDistance)) {
                TranscriptionView.this.setX(floatValue);
                TranscriptionView.this.updateColor();
            }
            this.mSpans.forEach(new TranscriptionView$TranscriptionAnimator$$ExternalSyntheticLambda0(valueAnimator));
            TranscriptionView.this.invalidate();
        }

        public static /* synthetic */ void lambda$onAnimationUpdate$0(ValueAnimator valueAnimator, TranscriptionSpan transcriptionSpan) {
            transcriptionSpan.setCurrentFraction(valueAnimator.getAnimatedFraction());
        }
    }

    /* loaded from: classes2.dex */
    public class TranscriptionSpan extends ReplacementSpan {
        private float mCurrentFraction;
        private float mStartFraction;

        TranscriptionSpan() {
            TranscriptionView.this = r1;
            this.mCurrentFraction = 0.0f;
            this.mStartFraction = 0.0f;
        }

        TranscriptionSpan(TranscriptionSpan transcriptionSpan) {
            TranscriptionView.this = r2;
            this.mCurrentFraction = 0.0f;
            this.mStartFraction = 0.0f;
            this.mStartFraction = MathUtils.clamp(transcriptionSpan.getCurrentFraction(), 0.0f, 1.0f);
        }

        @Override // android.text.style.ReplacementSpan
        public int getSize(Paint paint, CharSequence charSequence, int i, int i2, Paint.FontMetricsInt fontMetricsInt) {
            return (int) Math.ceil((double) TranscriptionView.this.getPaint().measureText(charSequence, 0, charSequence.length()));
        }

        @Override // android.text.style.ReplacementSpan
        public void draw(Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, Paint paint) {
            paint.setAlpha((int) Math.ceil((double) (getAlpha() * 255.0f)));
            canvas.drawText(charSequence, i, i2, f, (float) i4, paint);
        }

        private float getAlpha() {
            float f = this.mStartFraction;
            if (f == 1.0f) {
                return 1.0f;
            }
            return MathUtils.clamp((((1.0f - f) / 1.0f) * this.mCurrentFraction) + f, 0.0f, 1.0f);
        }

        float getCurrentFraction() {
            return this.mCurrentFraction;
        }

        void setCurrentFraction(float f) {
            this.mCurrentFraction = f;
        }
    }
}
