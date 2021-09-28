package com.android.systemui.media;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaCarouselScrollHandler.kt */
/* loaded from: classes.dex */
public final class MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1 extends FloatPropertyCompat<MediaCarouselScrollHandler> {
    /* access modifiers changed from: package-private */
    public MediaCarouselScrollHandler$Companion$CONTENT_TRANSLATION$1() {
        super("contentTranslation");
    }

    public float getValue(MediaCarouselScrollHandler mediaCarouselScrollHandler) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "handler");
        return mediaCarouselScrollHandler.getContentTranslation();
    }

    public void setValue(MediaCarouselScrollHandler mediaCarouselScrollHandler, float f) {
        Intrinsics.checkNotNullParameter(mediaCarouselScrollHandler, "handler");
        MediaCarouselScrollHandler.access$setContentTranslation(mediaCarouselScrollHandler, f);
    }
}
