package kotlin.jvm.internal;

import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.core.graphics.PathParser$$ExternalSyntheticOutline0;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
/* loaded from: classes.dex */
public class TypeIntrinsics {
    public static Object beforeCheckcastToFunctionOfArity(Object obj, int i) {
        int i2;
        boolean z = true;
        if (obj instanceof FunctionBase) {
            i2 = ((FunctionBase) obj).getArity();
        } else if (obj instanceof Function0) {
            i2 = 0;
        } else {
            i2 = obj instanceof Function1 ? 1 : 2;
        }
        if (i2 != i) {
            z = false;
        }
        if (z) {
            return obj;
        }
        ClassCastException classCastException = new ClassCastException(PathParser$$ExternalSyntheticOutline0.m(obj.getClass().getName(), " cannot be cast to ", ExifInterface$$ExternalSyntheticOutline0.m("kotlin.jvm.functions.Function", i)));
        Intrinsics.sanitizeStackTrace(classCastException, TypeIntrinsics.class.getName());
        throw classCastException;
    }
}
