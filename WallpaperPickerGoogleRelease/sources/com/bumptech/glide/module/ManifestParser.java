package com.bumptech.glide.module;

import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import java.lang.reflect.InvocationTargetException;
@Deprecated
/* loaded from: classes.dex */
public final class ManifestParser {
    public static GlideModule parseModule(String str) {
        Class cls;
        Class cls2;
        Class cls3;
        Class cls4;
        try {
            try {
                Object newInstance = Class.forName(str).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
                if (newInstance instanceof GlideModule) {
                    return (GlideModule) newInstance;
                }
                String valueOf = String.valueOf(newInstance);
                throw new RuntimeException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 44, "Expected instanceof GlideModule, but found: ", valueOf));
            } catch (IllegalAccessException e) {
                throwInstantiateGlideModuleException(cls2, e);
                throw null;
            } catch (InstantiationException e2) {
                throwInstantiateGlideModuleException(cls, e2);
                throw null;
            } catch (NoSuchMethodException e3) {
                throwInstantiateGlideModuleException(cls3, e3);
                throw null;
            } catch (InvocationTargetException e4) {
                throwInstantiateGlideModuleException(cls4, e4);
                throw null;
            }
        } catch (ClassNotFoundException e5) {
            throw new IllegalArgumentException("Unable to find GlideModule implementation", e5);
        }
    }

    public static void throwInstantiateGlideModuleException(Class<?> cls, Exception exc) {
        String valueOf = String.valueOf(cls);
        throw new RuntimeException(Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 53, "Unable to instantiate GlideModule implementation for ", valueOf), exc);
    }
}
