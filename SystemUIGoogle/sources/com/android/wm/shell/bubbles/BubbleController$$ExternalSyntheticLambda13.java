package com.android.wm.shell.bubbles;

import android.os.Looper;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda13 implements Supplier {
    public static final /* synthetic */ BubbleController$$ExternalSyntheticLambda13 INSTANCE = new BubbleController$$ExternalSyntheticLambda13();

    private /* synthetic */ BubbleController$$ExternalSyntheticLambda13() {
    }

    @Override // java.util.function.Supplier
    public final Object get() {
        return Looper.myLooper();
    }
}
