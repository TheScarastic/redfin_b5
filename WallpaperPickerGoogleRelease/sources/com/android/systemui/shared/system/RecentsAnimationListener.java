package com.android.systemui.shared.system;

import android.graphics.Rect;
import com.android.systemui.shared.recents.model.ThumbnailData;
/* loaded from: classes.dex */
public interface RecentsAnimationListener {
    void onAnimationCanceled(ThumbnailData thumbnailData);

    void onAnimationStart(RecentsAnimationControllerCompat recentsAnimationControllerCompat, RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr, RemoteAnimationTargetCompat[] remoteAnimationTargetCompatArr2, Rect rect, Rect rect2);

    void onTaskAppeared(RemoteAnimationTargetCompat remoteAnimationTargetCompat);
}
