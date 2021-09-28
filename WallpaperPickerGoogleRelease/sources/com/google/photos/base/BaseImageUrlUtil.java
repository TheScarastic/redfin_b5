package com.google.photos.base;

import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.libraries.imageurl.FifeImageUrlUtil;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIndexedListIterator;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class BaseImageUrlUtil<T> {
    public static final ImmutableList<OptionInfo> OPTIONS;
    public static final Splitter SPLIT_ON_EQUALS;
    public static final Splitter SPLIT_ON_SLASH = Splitter.on('/');
    public static final Splitter SPLIT_ON_DASH = Splitter.on('-');
    public static final Joiner JOIN_ON_SLASH = new Joiner("/");
    public static final Joiner JOIN_ON_DASH = new Joiner("-");
    public static final Joiner JOIN_ON_EQUALS = new Joiner("=");

    /* loaded from: classes.dex */
    public static class InvalidUrlException extends Exception {
        public InvalidUrlException(String str) {
            super(str);
        }
    }

    /* loaded from: classes.dex */
    public static class OptionInfo {
        public boolean isBool;
        public String key;

        public OptionInfo(String str, boolean z) {
            this.key = str;
            this.isBool = z;
        }

        public String toString() {
            String str = this.key;
            boolean z = this.isBool;
            StringBuilder sb = new StringBuilder(XMPPathFactory$$ExternalSyntheticOutline0.m(str, 9));
            sb.append("{");
            sb.append(str);
            sb.append(", ");
            sb.append(z);
            sb.append("}");
            return sb.toString();
        }
    }

    /* loaded from: classes.dex */
    public interface UriWrapper<T> {
    }

    static {
        Splitter on = Splitter.on('=');
        SPLIT_ON_EQUALS = new Splitter(on.strategy, true, on.trimmer, 2);
        Splitter.on(':');
        OptionInfo optionInfo = new OptionInfo("s", false);
        OptionInfo optionInfo2 = new OptionInfo("w", false);
        OptionInfo optionInfo3 = new OptionInfo("c", true);
        OptionInfo optionInfo4 = new OptionInfo("d", true);
        OptionInfo optionInfo5 = new OptionInfo("h", false);
        OptionInfo optionInfo6 = new OptionInfo("s", true);
        OptionInfo optionInfo7 = new OptionInfo("h", true);
        OptionInfo optionInfo8 = new OptionInfo("p", true);
        OptionInfo optionInfo9 = new OptionInfo("pp", true);
        OptionInfo optionInfo10 = new OptionInfo("pf", true);
        OptionInfo optionInfo11 = new OptionInfo("n", true);
        OptionInfo optionInfo12 = new OptionInfo("r", false);
        OptionInfo[] optionInfoArr = {new OptionInfo("r", true), new OptionInfo("o", true), new OptionInfo("o", false), new OptionInfo("j", false), new OptionInfo("x", false), new OptionInfo("y", false), new OptionInfo("z", false), new OptionInfo("g", true), new OptionInfo("e", false), new OptionInfo("f", false), new OptionInfo("k", true), new OptionInfo("u", true), new OptionInfo("ut", true), new OptionInfo("i", true), new OptionInfo("a", true), new OptionInfo("b", true), new OptionInfo("b", false), new OptionInfo("c", false), new OptionInfo("t", false), new OptionInfo("nt0", false), new OptionInfo("v", true), new OptionInfo("q", false), new OptionInfo("fh", true), new OptionInfo("fv", true), new OptionInfo("fg", true), new OptionInfo("ci", true), new OptionInfo("rw", true), new OptionInfo("rwu", true), new OptionInfo("rwa", true), new OptionInfo("nw", true), new OptionInfo("rh", true), new OptionInfo("no", true), new OptionInfo("ns", true), new OptionInfo("k", false), new OptionInfo("p", false), new OptionInfo("l", false), new OptionInfo("v", false), new OptionInfo("nu", true), new OptionInfo("ft", true), new OptionInfo("cc", true), new OptionInfo("nd", true), new OptionInfo("ip", true), new OptionInfo("nc", true), new OptionInfo("a", false), new OptionInfo("rj", true), new OptionInfo("rp", true), new OptionInfo("rg", true), new OptionInfo("pd", true), new OptionInfo("pa", true), new OptionInfo("m", false), new OptionInfo("vb", false), new OptionInfo("vl", false), new OptionInfo("lf", true), new OptionInfo("mv", true), new OptionInfo("id", true), new OptionInfo("al", true), new OptionInfo("ic", false), new OptionInfo("pg", true), new OptionInfo("mo", true), new OptionInfo("iv", false), new OptionInfo("il", false), new OptionInfo("ba", false)};
        AbstractIndexedListIterator<Object> abstractIndexedListIterator = ImmutableList.EMPTY_ITR;
        Object[] objArr = new Object[74];
        objArr[0] = optionInfo;
        objArr[1] = optionInfo2;
        objArr[2] = optionInfo3;
        objArr[3] = optionInfo4;
        objArr[4] = optionInfo5;
        objArr[5] = optionInfo6;
        objArr[6] = optionInfo7;
        objArr[7] = optionInfo8;
        objArr[8] = optionInfo9;
        objArr[9] = optionInfo10;
        objArr[10] = optionInfo11;
        objArr[11] = optionInfo12;
        System.arraycopy(optionInfoArr, 0, objArr, 12, 62);
        OPTIONS = ImmutableList.construct(objArr);
    }

    public static List<String> getPathComponents(String str) {
        ArrayList newArrayList = Lists.newArrayList(SPLIT_ON_SLASH.split(str));
        return (newArrayList.size() < 1 || !((String) newArrayList.get(0)).isEmpty()) ? newArrayList : newArrayList.subList(1, newArrayList.size());
    }

    public static String makeOptions(String str, String str2, boolean z) {
        Preconditions.checkArgument(str != null, "oldOptions is null");
        Preconditions.checkArgument(str2 != null, "newOptions is null");
        if (str.isEmpty()) {
            return str2;
        }
        ArrayList arrayList = (ArrayList) splitOptions(str, !z);
        if (arrayList.isEmpty()) {
            return str2;
        }
        arrayList.addAll(splitOptions(str2, false));
        Preconditions.checkArgument(true, "options is null");
        ArrayListMultimap arrayListMultimap = new ArrayListMultimap();
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str3 = (String) it.next();
            OptionInfo optionInfo = null;
            UnmodifiableIterator<OptionInfo> it2 = OPTIONS.iterator();
            while (true) {
                AbstractIndexedListIterator abstractIndexedListIterator = (AbstractIndexedListIterator) it2;
                if (!abstractIndexedListIterator.hasNext()) {
                    break;
                }
                OptionInfo optionInfo2 = (OptionInfo) abstractIndexedListIterator.next();
                if (str3.toLowerCase().startsWith(optionInfo2.key)) {
                    if (!optionInfo2.isBool) {
                        optionInfo = optionInfo2;
                    } else if (str3.length() == optionInfo2.key.length()) {
                        optionInfo = optionInfo2;
                        break;
                    }
                }
            }
            if (optionInfo != null) {
                arrayListMultimap.put(optionInfo, str3);
            } else {
                arrayList2.add(str3);
            }
        }
        ArrayList arrayList3 = new ArrayList();
        UnmodifiableIterator<OptionInfo> it3 = OPTIONS.iterator();
        while (true) {
            AbstractIndexedListIterator abstractIndexedListIterator2 = (AbstractIndexedListIterator) it3;
            if (abstractIndexedListIterator2.hasNext()) {
                String str4 = "";
                for (String str5 : arrayListMultimap.get((OptionInfo) abstractIndexedListIterator2.next())) {
                    if (Character.isUpperCase(str5.charAt(0))) {
                        arrayList3.add(str5);
                    } else {
                        str4 = str5;
                    }
                }
                if (!str4.isEmpty()) {
                    arrayList3.add(str4);
                }
            } else {
                arrayList3.addAll(arrayList2);
                return JOIN_ON_DASH.join(arrayList3);
            }
        }
    }

    public static List<String> splitOptions(String str, boolean z) {
        Preconditions.checkArgument(str != null, "options is null");
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = ((Splitter.AnonymousClass5) SPLIT_ON_DASH.split(str)).iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (!next.isEmpty()) {
                String str2 = "";
                if (next.startsWith("O") || next.startsWith("J")) {
                    while (next.length() < 12) {
                        next = JOIN_ON_DASH.join(next, it.hasNext() ? it.next() : str2, new Object[0]);
                    }
                }
                if (next.equals("pi") || next.equals("ya") || next.equals("ro")) {
                    Joiner joiner = JOIN_ON_DASH;
                    if (it.hasNext()) {
                        str2 = it.next();
                    }
                    next = joiner.join(next, str2, new Object[0]);
                }
                if (!z || Character.isUpperCase(next.charAt(0))) {
                    arrayList.add(next);
                }
            }
        }
        return arrayList;
    }

    public T changeImageUrlOptions(ImageUrlOptionsStringBuilder imageUrlOptionsStringBuilder, UriWrapper<T> uriWrapper, boolean z, boolean z2) throws InvalidUrlException {
        boolean z3;
        boolean z4 = true;
        Preconditions.checkArgument(imageUrlOptionsStringBuilder != null, "options is null");
        Preconditions.checkArgument(uriWrapper != null, "url is null");
        FifeImageUrlUtil.AndroidUriWrapper androidUriWrapper = (FifeImageUrlUtil.AndroidUriWrapper) uriWrapper;
        if (androidUriWrapper.getPath() != null) {
            List<String> pathComponents = getPathComponents(androidUriWrapper);
            if (pathComponents.size() > 0 && "image".equals(pathComponents.get(0))) {
                pathComponents.remove(0);
            }
            if (pathComponents.size() == 2) {
                pathComponents.remove(0);
            }
            int size = pathComponents.size();
            if (size >= 4 && ((size != 4 || !pathComponents.get(3).isEmpty()) && size <= 6)) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3) {
                return setLegacyImageUrlOptions(imageUrlOptionsStringBuilder, uriWrapper, z, z2);
            }
            int size2 = pathComponents.size();
            if (size2 < 1 || size2 > 1 || pathComponents.get(size2 - 1).isEmpty()) {
                z4 = false;
            }
            if (z4) {
                return setContentImageUrlOptions(imageUrlOptionsStringBuilder, uriWrapper, z, z2);
            }
            throw new InvalidUrlException(androidUriWrapper.toString());
        }
        throw new InvalidUrlException("url path is null");
    }

    public T setContentImageUrlOptions(ImageUrlOptionsStringBuilder imageUrlOptionsStringBuilder, UriWrapper<T> uriWrapper, boolean z, boolean z2) {
        Preconditions.checkArgument(imageUrlOptionsStringBuilder != null, "options is null");
        Preconditions.checkArgument(uriWrapper != null, "url is null");
        FifeImageUrlUtil.AndroidUriWrapper androidUriWrapper = (FifeImageUrlUtil.AndroidUriWrapper) uriWrapper;
        Preconditions.checkArgument(androidUriWrapper.getPath() != null, "url path is null");
        Splitter splitter = SPLIT_ON_EQUALS;
        ArrayList newArrayList = Lists.newArrayList(splitter.split(androidUriWrapper.getPath()));
        String str = "";
        String imageUrlOptionsStringBuilder2 = imageUrlOptionsStringBuilder.toString(str, z);
        ArrayList newArrayList2 = Lists.newArrayList(splitter.split(androidUriWrapper.getPath()));
        if (newArrayList2.size() == 2) {
            str = (String) newArrayList2.get(1);
        }
        String makeOptions = makeOptions(str, imageUrlOptionsStringBuilder2, z2);
        String str2 = (String) newArrayList.get(0);
        if (!makeOptions.isEmpty()) {
            str2 = JOIN_ON_EQUALS.join(str2, makeOptions, new Object[0]);
        }
        return (T) androidUriWrapper.uri.buildUpon().encodedPath(str2).build();
    }

    public T setLegacyImageUrlOptions(ImageUrlOptionsStringBuilder imageUrlOptionsStringBuilder, UriWrapper<T> uriWrapper, boolean z, boolean z2) {
        boolean z3 = true;
        Preconditions.checkArgument(imageUrlOptionsStringBuilder != null, "options is null");
        Preconditions.checkArgument(uriWrapper != null, "url is null");
        FifeImageUrlUtil.AndroidUriWrapper androidUriWrapper = (FifeImageUrlUtil.AndroidUriWrapper) uriWrapper;
        Preconditions.checkArgument(androidUriWrapper.getPath() != null, "url path is null");
        List<String> pathComponents = getPathComponents(androidUriWrapper);
        if (pathComponents.size() <= 0 || !pathComponents.get(0).equals("image")) {
            z3 = false;
        } else {
            pathComponents.remove(0);
        }
        String imageUrlOptionsStringBuilder2 = imageUrlOptionsStringBuilder.toString("", z);
        if (pathComponents.size() == 4) {
            pathComponents.add("");
        } else if (pathComponents.size() == 5) {
            pathComponents.add(4, "");
        }
        String makeOptions = makeOptions(pathComponents.get(4), imageUrlOptionsStringBuilder2, z2);
        pathComponents.set(4, makeOptions);
        if (makeOptions.equals("") && pathComponents.size() > 5) {
            pathComponents.remove(4);
        }
        if (z3) {
            pathComponents.add(0, "image");
        }
        String valueOf = String.valueOf(JOIN_ON_SLASH.join(pathComponents));
        return (T) androidUriWrapper.uri.buildUpon().encodedPath(valueOf.length() != 0 ? "/".concat(valueOf) : new String("/")).build();
    }

    public List<String> getPathComponents(UriWrapper<T> uriWrapper) {
        Objects.requireNonNull(uriWrapper);
        return getPathComponents(((FifeImageUrlUtil.AndroidUriWrapper) uriWrapper).getPath());
    }
}
