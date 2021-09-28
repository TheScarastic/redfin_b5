package com.android.customization.model.color;

import android.app.WallpaperColors;
import com.android.customization.model.CustomizationManager;
import java.util.Objects;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@DebugMetadata(c = "com.android.customization.model.color.ColorProvider$fetch$1", f = "ColorProvider.kt", l = {92}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class ColorProvider$fetch$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ CustomizationManager.OptionsFetchedListener<ColorOption> $callback;
    public final /* synthetic */ WallpaperColors $homeWallpaperColors;
    public final /* synthetic */ WallpaperColors $lockWallpaperColors;
    public final /* synthetic */ boolean $reload;
    public final /* synthetic */ boolean $wallpaperColorsChanged;
    public int label;
    private /* synthetic */ CoroutineScope p$;
    public final /* synthetic */ ColorProvider this$0;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ColorProvider$fetch$1(ColorProvider colorProvider, boolean z, boolean z2, WallpaperColors wallpaperColors, WallpaperColors wallpaperColors2, CustomizationManager.OptionsFetchedListener<ColorOption> optionsFetchedListener, Continuation<? super ColorProvider$fetch$1> continuation) {
        super(2, continuation);
        this.this$0 = colorProvider;
        this.$reload = z;
        this.$wallpaperColorsChanged = z2;
        this.$homeWallpaperColors = wallpaperColors;
        this.$lockWallpaperColors = wallpaperColors2;
        this.$callback = optionsFetchedListener;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ColorProvider$fetch$1 colorProvider$fetch$1 = new ColorProvider$fetch$1(this.this$0, this.$reload, this.$wallpaperColorsChanged, this.$homeWallpaperColors, this.$lockWallpaperColors, this.$callback, continuation);
        colorProvider$fetch$1.p$ = (CoroutineScope) obj;
        return colorProvider$fetch$1;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((ColorProvider$fetch$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r5v3, resolved type: com.android.customization.model.CustomizationManager$OptionsFetchedListener<com.android.customization.model.color.ColorOption> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        Object obj2 = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ColorProvider colorProvider = this.this$0;
                if (colorProvider.colorBundles == null || this.$reload) {
                    this.label = 1;
                    Dispatchers dispatchers = Dispatchers.INSTANCE;
                    Object withContext = BuildersKt.withContext(Dispatchers.IO, new ColorProvider$loadPreset$2(colorProvider, null), this);
                    if (withContext != obj2) {
                        withContext = Unit.INSTANCE;
                    }
                    if (withContext == obj2) {
                        return obj2;
                    }
                }
            } else if (i == 1) {
                ResultKt.throwOnFailure(obj);
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            if (this.$wallpaperColorsChanged || this.$reload) {
                ColorProvider.access$loadSeedColors(this.this$0, this.$homeWallpaperColors, this.$lockWallpaperColors);
            }
            CustomizationManager.OptionsFetchedListener<ColorOption> optionsFetchedListener = this.$callback;
            if (optionsFetchedListener != 0) {
                optionsFetchedListener.onOptionsLoaded(this.this$0.colorBundles);
            }
            return Unit.INSTANCE;
        } catch (Throwable th) {
            Objects.requireNonNull(this.this$0);
            CustomizationManager.OptionsFetchedListener<ColorOption> optionsFetchedListener2 = this.$callback;
            if (optionsFetchedListener2 != null) {
                optionsFetchedListener2.onError(th);
            }
            return Unit.INSTANCE;
        }
    }
}
