package com.android.customization.model.color;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@DebugMetadata(c = "com.android.customization.model.color.ColorProvider$loadPreset$2", f = "ColorProvider.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class ColorProvider$loadPreset$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public int label;
    private /* synthetic */ CoroutineScope p$;
    public final /* synthetic */ ColorProvider this$0;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ColorProvider$loadPreset$2(ColorProvider colorProvider, Continuation<? super ColorProvider$loadPreset$2> continuation) {
        super(2, continuation);
        this.this$0 = colorProvider;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ColorProvider$loadPreset$2 colorProvider$loadPreset$2 = new ColorProvider$loadPreset$2(this.this$0, continuation);
        colorProvider$loadPreset$2.p$ = (CoroutineScope) obj;
        return colorProvider$loadPreset$2;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ColorProvider$loadPreset$2 colorProvider$loadPreset$2 = new ColorProvider$loadPreset$2(this.this$0, continuation);
        colorProvider$loadPreset$2.p$ = coroutineScope;
        Unit unit = Unit.INSTANCE;
        colorProvider$loadPreset$2.invokeSuspend(unit);
        return unit;
    }

    /* JADX WARNING: Removed duplicated region for block: B:68:0x0275  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x027d  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0299  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x029e  */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x02a1  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x02b6 A[SYNTHETIC] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @org.jetbrains.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object invokeSuspend(@org.jetbrains.annotations.NotNull java.lang.Object r25) {
        /*
        // Method dump skipped, instructions count: 762
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.customization.model.color.ColorProvider$loadPreset$2.invokeSuspend(java.lang.Object):java.lang.Object");
    }
}
