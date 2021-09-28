package com.google.android.systemui.assist.uihints.input;

import com.google.android.systemui.assist.uihints.GlowController;
import com.google.android.systemui.assist.uihints.IconController;
import com.google.android.systemui.assist.uihints.ScrimController;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes2.dex */
public abstract class InputModule {
    /* access modifiers changed from: package-private */
    public static Set<TouchActionRegion> provideTouchActionRegions(IconController iconController, TranscriptionController transcriptionController) {
        return new HashSet(Arrays.asList(iconController, transcriptionController));
    }

    /* access modifiers changed from: package-private */
    public static Set<TouchInsideRegion> provideTouchInsideRegions(GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController) {
        return new HashSet(Arrays.asList(glowController, scrimController, transcriptionController));
    }
}
