package com.google.common.reflect;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.StandardSystemProperty;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.AbstractSetMultimap;
import com.google.common.collect.CollectPreconditions;
import com.google.common.collect.CompactHashMap;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.MultimapBuilder$LinkedHashSetSupplier;
import com.google.common.collect.Multimaps$CustomSetMultimap;
import com.google.common.collect.RegularImmutableList;
import com.google.common.collect.RegularImmutableSet;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.SingletonImmutableSet;
import com.google.common.collect.UnmodifiableIterator;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes.dex */
public final class ClassPath {
    public static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR;
    public static final Logger logger = Logger.getLogger(ClassPath.class.getName());

    /* loaded from: classes.dex */
    public static abstract class Scanner {
        public final Set<File> scannedUris = new HashSet();

        public static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classLoader) {
            ImmutableList immutableList;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            ClassLoader parent = classLoader.getParent();
            if (parent != null) {
                linkedHashMap.putAll(getClassPathEntries(parent));
            }
            if (classLoader instanceof URLClassLoader) {
                URL[] uRLs = ((URLClassLoader) classLoader).getURLs();
                AbstractIndexedListIterator<Object> abstractIndexedListIterator = ImmutableList.EMPTY_ITR;
                if (uRLs.length == 0) {
                    immutableList = RegularImmutableList.EMPTY;
                } else {
                    immutableList = ImmutableList.construct((Object[]) uRLs.clone());
                }
            } else if (classLoader.equals(ClassLoader.getSystemClassLoader())) {
                immutableList = parseJavaClassPath();
            } else {
                AbstractIndexedListIterator<Object> abstractIndexedListIterator2 = ImmutableList.EMPTY_ITR;
                immutableList = RegularImmutableList.EMPTY;
            }
            UnmodifiableIterator it = immutableList.iterator();
            while (true) {
                AbstractIndexedListIterator abstractIndexedListIterator3 = (AbstractIndexedListIterator) it;
                if (!abstractIndexedListIterator3.hasNext()) {
                    break;
                }
                URL url = (URL) abstractIndexedListIterator3.next();
                if (url.getProtocol().equals("file")) {
                    File file = ClassPath.toFile(url);
                    if (!linkedHashMap.containsKey(file)) {
                        linkedHashMap.put(file, classLoader);
                    }
                }
            }
            if ((linkedHashMap instanceof ImmutableMap) && !(linkedHashMap instanceof SortedMap)) {
                ImmutableMap<File, ClassLoader> immutableMap = (ImmutableMap) linkedHashMap;
                if (!immutableMap.isPartialView()) {
                    return immutableMap;
                }
            }
            Set entrySet = linkedHashMap.entrySet();
            ImmutableMap.Builder builder = new ImmutableMap.Builder(entrySet instanceof Collection ? entrySet.size() : 4);
            builder.putAll(entrySet);
            return builder.build();
        }

        public static URL getClassPathEntry(File file, String str) throws MalformedURLException {
            return new URL(file.toURI().toURL(), str);
        }

        public static ImmutableSet<File> getClassPathFromManifest(File file, Manifest manifest) {
            ImmutableSet<File> immutableSet;
            if (manifest == null) {
                int i = ImmutableSet.$r8$clinit;
                return RegularImmutableSet.EMPTY;
            }
            int i2 = ImmutableSet.$r8$clinit;
            ImmutableSet.Builder builder = new ImmutableSet.Builder();
            String value = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
            if (value != null) {
                Iterator<String> it = ((Splitter.AnonymousClass5) ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(value)).iterator();
                while (it.hasNext()) {
                    String next = it.next();
                    try {
                        URL classPathEntry = getClassPathEntry(file, next);
                        if (classPathEntry.getProtocol().equals("file")) {
                            builder.add((ImmutableSet.Builder) ClassPath.toFile(classPathEntry));
                        }
                    } catch (MalformedURLException unused) {
                        Logger logger = ClassPath.logger;
                        Level level = Level.WARNING;
                        String valueOf = String.valueOf(next);
                        logger.logp(level, "com.google.common.reflect.ClassPath$Scanner", "getClassPathFromManifest", valueOf.length() != 0 ? "Invalid Class-Path entry: ".concat(valueOf) : new String("Invalid Class-Path entry: "));
                    }
                }
            }
            int i3 = builder.size;
            if (i3 != 0) {
                boolean z = false;
                if (i3 != 1) {
                    if (builder.hashTable == null || ImmutableSet.chooseTableSize(i3) != builder.hashTable.length) {
                        immutableSet = ImmutableSet.construct(builder.size, builder.contents);
                        builder.size = immutableSet.size();
                    } else {
                        int i4 = builder.size;
                        Object[] objArr = builder.contents;
                        int length = objArr.length;
                        if (i4 < (length >> 1) + (length >> 2)) {
                            z = true;
                        }
                        if (z) {
                            objArr = Arrays.copyOf(objArr, i4);
                        }
                        int i5 = builder.hashCode;
                        Object[] objArr2 = builder.hashTable;
                        immutableSet = new RegularImmutableSet<>(objArr, i5, objArr2, objArr2.length - 1, builder.size);
                    }
                    builder.forceCopy = true;
                    builder.hashTable = null;
                    return immutableSet;
                }
                Object obj = builder.contents[0];
                int i6 = ImmutableSet.$r8$clinit;
                return new SingletonImmutableSet(obj);
            }
            int i7 = ImmutableSet.$r8$clinit;
            return RegularImmutableSet.EMPTY;
        }

        public static ImmutableList<URL> parseJavaClassPath() {
            MalformedURLException malformedURLException;
            int i;
            URL url;
            MalformedURLException e;
            AbstractIndexedListIterator<Object> abstractIndexedListIterator = ImmutableList.EMPTY_ITR;
            CollectPreconditions.checkNonnegative(4, "initialCapacity");
            Object[] objArr = new Object[4];
            Iterator<String> it = ((Splitter.AnonymousClass5) Splitter.on(StandardSystemProperty.PATH_SEPARATOR.value()).split(StandardSystemProperty.JAVA_CLASS_PATH.value())).iterator();
            int i2 = 0;
            while (it.hasNext()) {
                String next = it.next();
                try {
                    try {
                        url = new File(next).toURI().toURL();
                        try {
                            Objects.requireNonNull(url);
                            i = i2 + 1;
                            if (objArr.length < i) {
                                objArr = Arrays.copyOf(objArr, ImmutableCollection.Builder.expandedCapacity(objArr.length, i));
                            }
                        } catch (MalformedURLException e2) {
                            i = i2;
                            e = e2;
                        }
                    } catch (MalformedURLException e3) {
                        malformedURLException = e3;
                    }
                } catch (SecurityException unused) {
                }
                try {
                    try {
                        objArr[i2] = url;
                    } catch (SecurityException unused2) {
                        i2 = i;
                        URL url2 = new URL("file", (String) null, new File(next).getAbsolutePath());
                        i = i2 + 1;
                        if (objArr.length < i) {
                            objArr = Arrays.copyOf(objArr, ImmutableCollection.Builder.expandedCapacity(objArr.length, i));
                        }
                        objArr[i2] = url2;
                        i2 = i;
                    }
                    i2 = i;
                } catch (MalformedURLException e4) {
                    e = e4;
                    malformedURLException = e;
                    i2 = i;
                    Logger logger = ClassPath.logger;
                    Level level = Level.WARNING;
                    String valueOf = String.valueOf(next);
                    logger.logp(level, "com.google.common.reflect.ClassPath$Scanner", "parseJavaClassPath", valueOf.length() != 0 ? "malformed classpath entry: ".concat(valueOf) : new String("malformed classpath entry: "), (Throwable) malformedURLException);
                }
            }
            return ImmutableList.asImmutableList(objArr, i2);
        }

        public final void scan(File file, ClassLoader classLoader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                try {
                    if (file.exists()) {
                        if (file.isDirectory()) {
                            scanDirectory(classLoader, file);
                            return;
                        }
                        try {
                            JarFile jarFile = new JarFile(file);
                            try {
                                UnmodifiableIterator<File> it = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                                while (it.hasNext()) {
                                    scan(it.next(), classLoader);
                                }
                                scanJarFile(classLoader, jarFile);
                                jarFile.close();
                            } catch (Throwable th) {
                                try {
                                    jarFile.close();
                                } catch (IOException unused) {
                                }
                                throw th;
                            }
                        } catch (IOException unused2) {
                        }
                    }
                } catch (SecurityException e) {
                    Logger logger = ClassPath.logger;
                    Level level = Level.WARNING;
                    String valueOf = String.valueOf(file);
                    String valueOf2 = String.valueOf(e);
                    logger.logp(level, "com.google.common.reflect.ClassPath$Scanner", "scanFrom", Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(valueOf2.length() + valueOf.length() + 16, "Cannot access ", valueOf, ": ", valueOf2));
                }
            }
        }

        public abstract void scanDirectory(ClassLoader classLoader, File file) throws IOException;

        public abstract void scanJarFile(ClassLoader classLoader, JarFile jarFile) throws IOException;
    }

    static {
        Splitter on = Splitter.on(" ");
        CLASS_PATH_ATTRIBUTE_SEPARATOR = new Splitter(on.strategy, true, on.trimmer, on.limit);
    }

    public static String getClassName(String str) {
        return str.substring(0, str.length() - 6).replace('/', '.');
    }

    public static File toFile(URL url) {
        Preconditions.checkArgument(url.getProtocol().equals("file"));
        try {
            return new File(url.toURI());
        } catch (URISyntaxException unused) {
            return new File(url.getPath());
        }
    }

    /* loaded from: classes.dex */
    public static final class DefaultScanner extends Scanner {
        public final SetMultimap<ClassLoader, String> resources = new Multimaps$CustomSetMultimap(new CompactHashMap(8), new MultimapBuilder$LinkedHashSetSupplier(2));

        public DefaultScanner() {
            CollectPreconditions.checkNonnegative(8, "expectedKeys");
            CollectPreconditions.checkNonnegative(2, "expectedValuesPerKey");
        }

        @Override // com.google.common.reflect.ClassPath.Scanner
        public void scanDirectory(ClassLoader classLoader, File file) throws IOException {
            HashSet hashSet = new HashSet();
            hashSet.add(file.getCanonicalFile());
            scanDirectory(file, classLoader, "", hashSet);
        }

        @Override // com.google.common.reflect.ClassPath.Scanner
        public void scanJarFile(ClassLoader classLoader, JarFile jarFile) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry nextElement = entries.nextElement();
                if (!nextElement.isDirectory() && !nextElement.getName().equals("META-INF/MANIFEST.MF")) {
                    ((AbstractSetMultimap) this.resources).get(classLoader).add(nextElement.getName());
                }
            }
        }

        public final void scanDirectory(File file, ClassLoader classLoader, String str, Set<File> set) throws IOException {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                Logger logger = ClassPath.logger;
                Level level = Level.WARNING;
                String valueOf = String.valueOf(file);
                logger.logp(level, "com.google.common.reflect.ClassPath$DefaultScanner", "scanDirectory", Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 22, "Cannot read directory ", valueOf));
                return;
            }
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (file2.isDirectory()) {
                    File canonicalFile = file2.getCanonicalFile();
                    if (set.add(canonicalFile)) {
                        scanDirectory(canonicalFile, classLoader, FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(name, XMPPathFactory$$ExternalSyntheticOutline0.m(str, 1)), str, name, "/"), set);
                        set.remove(canonicalFile);
                    }
                } else {
                    String valueOf2 = String.valueOf(str);
                    String valueOf3 = String.valueOf(name);
                    String concat = valueOf3.length() != 0 ? valueOf2.concat(valueOf3) : new String(valueOf2);
                    if (!concat.equals("META-INF/MANIFEST.MF")) {
                        ((AbstractSetMultimap) this.resources).get(classLoader).add(concat);
                    }
                }
            }
        }
    }
}
