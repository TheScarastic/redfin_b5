package com.android.settingslib.animation;

import android.view.animation.Interpolator;
/* loaded from: classes.dex */
public interface AppearAnimationCreator<T> {
    void createAnimation(T t, long j, long j2, float f, boolean z, Interpolator interpolator, Runnable runnable);
}
