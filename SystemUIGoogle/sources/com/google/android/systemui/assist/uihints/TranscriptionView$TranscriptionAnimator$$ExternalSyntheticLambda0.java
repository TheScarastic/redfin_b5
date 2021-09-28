package com.google.android.systemui.assist.uihints;

import android.animation.ValueAnimator;
import com.google.android.systemui.assist.uihints.TranscriptionView;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class TranscriptionView$TranscriptionAnimator$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ ValueAnimator f$0;

    public /* synthetic */ TranscriptionView$TranscriptionAnimator$$ExternalSyntheticLambda0(ValueAnimator valueAnimator) {
        this.f$0 = valueAnimator;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        TranscriptionView.TranscriptionAnimator.$r8$lambda$vYsezX2hFbW3lNnWDhJ_7x6WNDc(this.f$0, (TranscriptionView.TranscriptionSpan) obj);
    }
}
