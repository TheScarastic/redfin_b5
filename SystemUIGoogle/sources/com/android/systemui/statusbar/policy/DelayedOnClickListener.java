package com.android.systemui.statusbar.policy;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
final class DelayedOnClickListener implements View.OnClickListener {
    private final View.OnClickListener mActualListener;
    private final long mInitDelayMs;
    private final long mInitTimeMs = SystemClock.elapsedRealtime();

    public DelayedOnClickListener(View.OnClickListener onClickListener, long j) {
        Intrinsics.checkNotNullParameter(onClickListener, "mActualListener");
        this.mActualListener = onClickListener;
        this.mInitDelayMs = j;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Intrinsics.checkNotNullParameter(view, "v");
        if (hasFinishedInitialization()) {
            this.mActualListener.onClick(view);
        } else {
            Log.i("SmartReplyViewInflater", Intrinsics.stringPlus("Accidental Smart Suggestion click registered, delay: ", Long.valueOf(this.mInitDelayMs)));
        }
    }

    private final boolean hasFinishedInitialization() {
        return SystemClock.elapsedRealtime() >= this.mInitTimeMs + this.mInitDelayMs;
    }
}
