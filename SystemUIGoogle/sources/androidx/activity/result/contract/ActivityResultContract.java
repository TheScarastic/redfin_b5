package androidx.activity.result.contract;

import android.annotation.SuppressLint;
import android.content.Intent;
/* loaded from: classes.dex */
public abstract class ActivityResultContract<I, O> {
    @SuppressLint({"UnknownNullness"})
    public abstract O parseResult(int i, Intent intent);
}
