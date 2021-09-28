package kotlin.jvm.internal;

import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import com.google.android.material.shape.ShapePath;
import java.util.Arrays;
import kotlin.KotlinNullPointerException;
/* loaded from: classes.dex */
public class Intrinsics {
    public Intrinsics(int i) {
    }

    public static boolean areEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static void checkExpressionValueIsNotNull(Object obj, String str) {
        if (obj == null) {
            IllegalStateException illegalStateException = new IllegalStateException(SupportMenuInflater$$ExternalSyntheticOutline0.m(str, " must not be null"));
            sanitizeStackTrace(illegalStateException, Intrinsics.class.getName());
            throw illegalStateException;
        }
    }

    public static void checkNotNull(Object obj) {
        if (obj == null) {
            NullPointerException nullPointerException = new NullPointerException();
            sanitizeStackTrace(nullPointerException, Intrinsics.class.getName());
            throw nullPointerException;
        }
    }

    public static void checkNotNullExpressionValue(Object obj, String str) {
        if (obj == null) {
            NullPointerException nullPointerException = new NullPointerException(SupportMenuInflater$$ExternalSyntheticOutline0.m(str, " must not be null"));
            sanitizeStackTrace(nullPointerException, Intrinsics.class.getName());
            throw nullPointerException;
        }
    }

    public static void checkNotNullParameter(Object obj, String str) {
        if (obj == null) {
            NullPointerException nullPointerException = new NullPointerException(createParameterIsNullExceptionMessage(str));
            sanitizeStackTrace(nullPointerException, Intrinsics.class.getName());
            throw nullPointerException;
        }
    }

    public static void checkParameterIsNotNull(Object obj, String str) {
        if (obj == null) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(createParameterIsNullExceptionMessage(str));
            sanitizeStackTrace(illegalArgumentException, Intrinsics.class.getName());
            throw illegalArgumentException;
        }
    }

    public static String createParameterIsNullExceptionMessage(String str) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        String className = stackTraceElement.getClassName();
        String methodName = stackTraceElement.getMethodName();
        return "Parameter specified as non-null is null: method " + className + "." + methodName + ", parameter " + str;
    }

    public static <T extends Throwable> T sanitizeStackTrace(T t, String str) {
        StackTraceElement[] stackTrace = t.getStackTrace();
        int length = stackTrace.length;
        int i = -1;
        for (int i2 = 0; i2 < length; i2++) {
            if (str.equals(stackTrace[i2].getClassName())) {
                i = i2;
            }
        }
        t.setStackTrace((StackTraceElement[]) Arrays.copyOfRange(stackTrace, i + 1, length));
        return t;
    }

    public static void throwNpe() {
        KotlinNullPointerException kotlinNullPointerException = new KotlinNullPointerException();
        sanitizeStackTrace(kotlinNullPointerException, Intrinsics.class.getName());
        throw kotlinNullPointerException;
    }

    public void getEdgePath(float f, float f2, float f3, ShapePath shapePath) {
        shapePath.lineTo(f, 0.0f);
    }
}
