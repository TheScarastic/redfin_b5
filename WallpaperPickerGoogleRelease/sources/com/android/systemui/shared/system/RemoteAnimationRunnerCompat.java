package com.android.systemui.shared.system;
/* loaded from: classes.dex */
public interface RemoteAnimationRunnerCompat {
    void onAnimationCancelled();

    void onAnimationStart(int i, RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr, RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr2, RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr3, Runnable runnable);
}
