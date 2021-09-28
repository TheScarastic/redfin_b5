package kotlin.jvm;

import java.util.Objects;
import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KClass;
/* compiled from: JvmClassMapping.kt */
/* loaded from: classes2.dex */
public final class JvmClassMappingKt {
    public static final <T> Class<T> getJavaClass(KClass<T> kClass) {
        Intrinsics.checkNotNullParameter(kClass, "$this$java");
        Class<T> cls = (Class<T>) ((ClassBasedDeclarationContainer) kClass).getJClass();
        Objects.requireNonNull(cls, "null cannot be cast to non-null type java.lang.Class<T>");
        return cls;
    }

    public static final <T> Class<T> getJavaObjectType(KClass<T> kClass) {
        Intrinsics.checkNotNullParameter(kClass, "$this$javaObjectType");
        Class<T> cls = (Class<T>) ((ClassBasedDeclarationContainer) kClass).getJClass();
        if (!cls.isPrimitive()) {
            return cls;
        }
        String name = cls.getName();
        switch (name.hashCode()) {
            case -1325958191:
                return name.equals("double") ? Double.class : cls;
            case 104431:
                if (name.equals("int")) {
                    return Integer.class;
                }
                return cls;
            case 3039496:
                if (name.equals("byte")) {
                    return Byte.class;
                }
                return cls;
            case 3052374:
                if (name.equals("char")) {
                    return Character.class;
                }
                return cls;
            case 3327612:
                if (name.equals("long")) {
                    return Long.class;
                }
                return cls;
            case 3625364:
                if (name.equals("void")) {
                    return Void.class;
                }
                return cls;
            case 64711720:
                if (name.equals("boolean")) {
                    return Boolean.class;
                }
                return cls;
            case 97526364:
                if (name.equals("float")) {
                    return Float.class;
                }
                return cls;
            case 109413500:
                if (name.equals("short")) {
                    return Short.class;
                }
                return cls;
            default:
                return cls;
        }
    }

    public static final <T> KClass<T> getKotlinClass(Class<T> cls) {
        Intrinsics.checkNotNullParameter(cls, "$this$kotlin");
        return Reflection.getOrCreateKotlinClass(cls);
    }
}
