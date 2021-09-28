package kotlinx.coroutines.internal;

import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import kotlin.ExceptionsKt;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.EmptySet;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
import org.jetbrains.annotations.NotNull;
/* loaded from: classes.dex */
public final class FastServiceLoader {
    @NotNull
    public static final <S> List<S> loadProviders$kotlinx_coroutines_core(@NotNull Class<S> cls, @NotNull ClassLoader classLoader) {
        Collection<String> collection;
        List<String> list;
        Enumeration<URL> resources = classLoader.getResources("META-INF/services/" + cls.getName());
        Intrinsics.checkExpressionValueIsNotNull(resources, "urls");
        ArrayList<URL> list2 = Collections.list(resources);
        Intrinsics.checkExpressionValueIsNotNull(list2, "java.util.Collections.list(this)");
        ArrayList arrayList = new ArrayList();
        for (URL url : list2) {
            Intrinsics.checkExpressionValueIsNotNull(url, "it");
            String url2 = url.toString();
            Intrinsics.checkExpressionValueIsNotNull(url2, "url.toString()");
            if (StringsKt__StringsKt.startsWith$default(url2, "jar", false, 2)) {
                String substringAfter$default = StringsKt__StringsKt.substringAfter$default(url2, "jar:file:", null, 2);
                int indexOf$default = StringsKt__StringsKt.indexOf$default((CharSequence) substringAfter$default, '!', 0, false, 6);
                if (indexOf$default != -1) {
                    substringAfter$default = substringAfter$default.substring(0, indexOf$default);
                    Intrinsics.checkNotNullExpressionValue(substringAfter$default, "(this as java.lang.Strin…ing(startIndex, endIndex)");
                }
                String substringAfter$default2 = StringsKt__StringsKt.substringAfter$default(url2, "!/", null, 2);
                JarFile jarFile = new JarFile(substringAfter$default, false);
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(new ZipEntry(substringAfter$default2)), "UTF-8"));
                    list = parseFile(bufferedReader);
                    CloseableKt.closeFinally(bufferedReader, null);
                    try {
                        jarFile.close();
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
                            ExceptionsKt.addSuppressed(th2, th4);
                            throw th2;
                        }
                    }
                }
            } else {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(url.openStream()));
                try {
                    List<String> parseFile = parseFile(bufferedReader2);
                    CloseableKt.closeFinally(bufferedReader2, null);
                    list = parseFile;
                } catch (Throwable th5) {
                    try {
                        throw th5;
                    } catch (Throwable th6) {
                        CloseableKt.closeFinally(bufferedReader2, th5);
                        throw th6;
                    }
                }
            }
            Intrinsics.checkNotNullParameter(list, "elements");
            arrayList.addAll(list);
        }
        int size = arrayList.size();
        if (size == 0) {
            collection = EmptySet.INSTANCE;
        } else if (size != 1) {
            collection = new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(arrayList.size()));
            CollectionsKt___CollectionsKt.toCollection(arrayList, collection);
        } else {
            collection = Collections.singleton(arrayList.get(0));
            Intrinsics.checkNotNullExpressionValue(collection, "java.util.Collections.singleton(element)");
        }
        if (!collection.isEmpty()) {
            ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(collection, 10));
            for (String str : collection) {
                Class<?> cls2 = Class.forName(str, false, classLoader);
                if (cls.isAssignableFrom(cls2)) {
                    arrayList2.add(cls.cast(cls2.getDeclaredConstructor(new Class[0]).newInstance(new Object[0])));
                } else {
                    throw new IllegalArgumentException(("Expected service of class " + cls + ", but found " + cls2).toString());
                }
            }
            return arrayList2;
        }
        throw new IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
    }

    public static final List<String> parseFile(BufferedReader bufferedReader) {
        boolean z;
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return CollectionsKt___CollectionsKt.toList(linkedHashSet);
            }
            boolean z2 = false;
            int indexOf$default = StringsKt__StringsKt.indexOf$default((CharSequence) readLine, "#", 0, false, 6);
            if (indexOf$default != -1) {
                readLine = readLine.substring(0, indexOf$default);
                Intrinsics.checkNotNullExpressionValue(readLine, "(this as java.lang.Strin…ing(startIndex, endIndex)");
            }
            int length = readLine.length() - 1;
            int i = 0;
            boolean z3 = false;
            while (i <= length) {
                char charAt = readLine.charAt(!z3 ? i : length);
                boolean z4 = Character.isWhitespace(charAt) || Character.isSpaceChar(charAt);
                if (!z3) {
                    if (!z4) {
                        z3 = true;
                    } else {
                        i++;
                    }
                } else if (!z4) {
                    break;
                } else {
                    length--;
                }
            }
            String obj = readLine.subSequence(i, length + 1).toString();
            int i2 = 0;
            while (true) {
                if (i2 >= obj.length()) {
                    z = true;
                    break;
                }
                char charAt2 = obj.charAt(i2);
                if (!(charAt2 == '.' || Character.isJavaIdentifierPart(charAt2))) {
                    z = false;
                    break;
                }
                i2++;
            }
            if (z) {
                if (obj.length() > 0) {
                    z2 = true;
                }
                if (z2) {
                    linkedHashSet.add(obj);
                }
            } else {
                throw new IllegalArgumentException(SupportMenuInflater$$ExternalSyntheticOutline0.m("Illegal service provider class name: ", obj).toString());
            }
        }
    }
}
