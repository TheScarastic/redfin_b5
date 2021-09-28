package kotlinx.coroutines.internal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import kotlin.ExceptionsKt__ExceptionsKt;
import kotlin.TypeCastException;
import kotlin.collections.CollectionsKt__IterablesKt;
import kotlin.collections.CollectionsKt__MutableCollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsJVMKt;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: FastServiceLoader.kt */
/* loaded from: classes2.dex */
public final class FastServiceLoader {
    public static final FastServiceLoader INSTANCE = new FastServiceLoader();

    private FastServiceLoader() {
    }

    public final <S> List<S> load$kotlinx_coroutines_core(Class<S> cls, ClassLoader classLoader) {
        Intrinsics.checkParameterIsNotNull(cls, "service");
        Intrinsics.checkParameterIsNotNull(classLoader, "loader");
        try {
            return loadProviders$kotlinx_coroutines_core(cls, classLoader);
        } catch (Throwable unused) {
            ServiceLoader load = ServiceLoader.load(cls, classLoader);
            Intrinsics.checkExpressionValueIsNotNull(load, "ServiceLoader.load(service, loader)");
            return CollectionsKt___CollectionsKt.toList(load);
        }
    }

    public final <S> List<S> loadProviders$kotlinx_coroutines_core(Class<S> cls, ClassLoader classLoader) {
        Intrinsics.checkParameterIsNotNull(cls, "service");
        Intrinsics.checkParameterIsNotNull(classLoader, "loader");
        Enumeration<URL> resources = classLoader.getResources("META-INF/services/" + cls.getName());
        Intrinsics.checkExpressionValueIsNotNull(resources, "urls");
        ArrayList<URL> list = Collections.list(resources);
        Intrinsics.checkExpressionValueIsNotNull(list, "java.util.Collections.list(this)");
        ArrayList arrayList = new ArrayList();
        for (URL url : list) {
            FastServiceLoader fastServiceLoader = INSTANCE;
            Intrinsics.checkExpressionValueIsNotNull(url, "it");
            boolean unused = CollectionsKt__MutableCollectionsKt.addAll(arrayList, fastServiceLoader.parse(url));
        }
        Set<String> set = CollectionsKt___CollectionsKt.toSet(arrayList);
        if (!set.isEmpty()) {
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IterablesKt.collectionSizeOrDefault(set, 10));
            for (String str : set) {
                arrayList2.add(INSTANCE.getProviderInstance(str, classLoader, cls));
            }
            return arrayList2;
        }
        throw new IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
    }

    private final <S> S getProviderInstance(String str, ClassLoader classLoader, Class<S> cls) {
        Class<?> cls2 = Class.forName(str, false, classLoader);
        if (cls.isAssignableFrom(cls2)) {
            return cls.cast(cls2.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        }
        throw new IllegalArgumentException(("Expected service of class " + cls + ", but found " + cls2).toString());
    }

    private final List<String> parse(URL url) {
        String url2 = url.toString();
        Intrinsics.checkExpressionValueIsNotNull(url2, "url.toString()");
        if (StringsKt__StringsJVMKt.startsWith$default(url2, "jar", false, 2, null)) {
            String str = StringsKt__StringsKt.substringBefore$default(StringsKt__StringsKt.substringAfter$default(url2, "jar:file:", null, 2, null), '!', (String) null, 2, (Object) null);
            String str2 = StringsKt__StringsKt.substringAfter$default(url2, "!/", null, 2, null);
            JarFile jarFile = new JarFile(str, false);
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(new ZipEntry(str2)), "UTF-8"));
                List<String> parseFile = INSTANCE.parseFile(bufferedReader);
                CloseableKt.closeFinally(bufferedReader, null);
                try {
                    jarFile.close();
                    return parseFile;
                } catch (Throwable th) {
                    throw th;
                }
            } catch (Throwable th2) {
                try {
                    throw th2;
                } catch (Throwable th3) {
                    try {
                        jarFile.close();
                        throw th3;
                    } catch (Throwable th4) {
                        ExceptionsKt__ExceptionsKt.addSuppressed(th2, th4);
                        throw th2;
                    }
                }
            }
        } else {
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                List<String> parseFile2 = INSTANCE.parseFile(bufferedReader2);
                CloseableKt.closeFinally(bufferedReader2, null);
                return parseFile2;
            } catch (Throwable th5) {
                try {
                    throw th5;
                } catch (Throwable th6) {
                    CloseableKt.closeFinally(bufferedReader2, th5);
                    throw th6;
                }
            }
        }
    }

    private final List<String> parseFile(BufferedReader bufferedReader) {
        boolean z;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return CollectionsKt___CollectionsKt.toList(linkedHashSet);
            }
            String str = StringsKt__StringsKt.substringBefore$default(readLine, "#", (String) null, 2, (Object) null);
            if (str != null) {
                String obj = StringsKt__StringsKt.trim(str).toString();
                boolean z2 = false;
                int i = 0;
                while (true) {
                    if (i >= obj.length()) {
                        z = true;
                        break;
                    }
                    char charAt = obj.charAt(i);
                    if (!(charAt == '.' || Character.isJavaIdentifierPart(charAt))) {
                        z = false;
                        break;
                    }
                    i++;
                }
                if (z) {
                    if (obj.length() > 0) {
                        z2 = true;
                    }
                    if (z2) {
                        linkedHashSet.add(obj);
                    }
                } else {
                    throw new IllegalArgumentException(("Illegal service provider class name: " + obj).toString());
                }
            } else {
                throw new TypeCastException("null cannot be cast to non-null type kotlin.CharSequence");
            }
        }
    }
}
