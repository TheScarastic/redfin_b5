package com.google.android.apps.wallpaper.module;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import com.android.customization.model.theme.ThemeManager;
import java.util.HashSet;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;
@DebugMetadata(c = "com.google.android.apps.wallpaper.module.ThemeSanitizerReceiver$sanitizeThemes$2", f = "ThemeSanitizerReceiver.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes.dex */
public final class ThemeSanitizerReceiver$sanitizeThemes$2 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Context $context;
    public int label;
    private /* synthetic */ CoroutineScope p$;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ThemeSanitizerReceiver$sanitizeThemes$2(Context context, Continuation<? super ThemeSanitizerReceiver$sanitizeThemes$2> continuation) {
        super(2, continuation);
        this.$context = context;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @NotNull
    public final Continuation<Unit> create(@Nullable Object obj, @NotNull Continuation<?> continuation) {
        ThemeSanitizerReceiver$sanitizeThemes$2 themeSanitizerReceiver$sanitizeThemes$2 = new ThemeSanitizerReceiver$sanitizeThemes$2(this.$context, continuation);
        themeSanitizerReceiver$sanitizeThemes$2.p$ = (CoroutineScope) obj;
        return themeSanitizerReceiver$sanitizeThemes$2;
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        ThemeSanitizerReceiver$sanitizeThemes$2 themeSanitizerReceiver$sanitizeThemes$2 = new ThemeSanitizerReceiver$sanitizeThemes$2(this.$context, continuation);
        themeSanitizerReceiver$sanitizeThemes$2.p$ = coroutineScope;
        return themeSanitizerReceiver$sanitizeThemes$2.invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    @Nullable
    public final Object invokeSuspend(@NotNull Object obj) {
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            JSONObject jSONObject = new JSONObject(Settings.Secure.getString(this.$context.getContentResolver(), "theme_customization_overlay_packages"));
            JSONArray names = jSONObject.names();
            if (names == null) {
                return Unit.INSTANCE;
            }
            if (jSONObject.has("android.theme.customization.system_palette")) {
                int length = names.length();
                int i = 0;
                if (length > 0) {
                    int i2 = 0;
                    while (true) {
                        int i3 = i + 1;
                        String string = names.getString(i);
                        if (!Intrinsics.areEqual("android.theme.customization.accent_color", string) && ((HashSet) ThemeManager.THEME_CATEGORIES).contains(string)) {
                            jSONObject.remove(string);
                            i2 = 1;
                        }
                        if (i3 >= length) {
                            break;
                        }
                        i = i3;
                    }
                    i = i2;
                }
                if (i == 0) {
                    return Unit.INSTANCE;
                }
                Settings.Secure.putString(this.$context.getContentResolver(), "theme_customization_overlay_packages", jSONObject.toString());
            } else {
                Settings.Secure.putString(this.$context.getContentResolver(), "theme_customization_overlay_packages", null);
            }
            Log.i("ThemeSanitizerReceiver", "Theme setting sanitized");
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
