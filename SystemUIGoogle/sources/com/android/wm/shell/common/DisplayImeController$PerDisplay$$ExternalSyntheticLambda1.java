package com.android.wm.shell.common;

import android.view.SurfaceControl;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class DisplayImeController$PerDisplay$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ DisplayImeController$PerDisplay$$ExternalSyntheticLambda1 INSTANCE = new DisplayImeController$PerDisplay$$ExternalSyntheticLambda1();

    private /* synthetic */ DisplayImeController$PerDisplay$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SurfaceControl) obj).release();
    }
}
