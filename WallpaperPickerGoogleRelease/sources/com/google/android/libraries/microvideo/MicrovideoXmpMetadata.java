package com.google.android.libraries.microvideo;

import android.util.Log;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.impl.ParameterAsserts;
import com.adobe.xmp.impl.XMPMetaImpl;
import com.adobe.xmp.impl.XMPNode;
import com.adobe.xmp.impl.XMPNodeUtils;
import com.adobe.xmp.impl.xpath.XMPPathParser;
import com.google.android.libraries.microvideo.xmp.AutoValue_MicroVideoXmpContainerItem;
import com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainer;
import com.google.android.libraries.microvideo.xmp.MicroVideoXmpContainerItem;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class MicrovideoXmpMetadata {

    /* loaded from: classes.dex */
    public interface ThrowableSupplier<T, E extends Throwable> {
        T get() throws Throwable;
    }

    public static <T, E extends Throwable> T firstNonNull(ThrowableSupplier<T, E>... throwableSupplierArr) throws Throwable {
        for (ThrowableSupplier<T, E> throwableSupplier : throwableSupplierArr) {
            T t = throwableSupplier.get();
            if (t != null) {
                return t;
            }
        }
        return null;
    }

    public static int getFileFormatVersion(XMPMeta xMPMeta) throws XMPException {
        return ((Integer) firstNonNull(new MicrovideoXmpMetadata$$Lambda$6(xMPMeta, 0), new MicrovideoXmpMetadata$$Lambda$6(xMPMeta, 2), MicrovideoXmpMetadata$$Lambda$8.$instance)).intValue();
    }

    public static int getMicrovideoPayloadLength(XMPMeta xMPMeta) throws XMPException {
        int i;
        List<MicroVideoXmpContainerItem> unmodifiableList;
        String str;
        boolean z = true;
        if (getFileFormatVersion(xMPMeta) == 1) {
            return ((Integer) firstNonNull(new MicrovideoXmpMetadata$$Lambda$6(xMPMeta, 1), MicrovideoXmpMetadata$$Lambda$13.$instance)).intValue();
        }
        if (getFileFormatVersion(xMPMeta) != 1) {
            ParameterAsserts.assertSchemaNS("http://ns.google.com/photos/1.0/container/");
            XMPNode findNode = XMPNodeUtils.findNode(((XMPMetaImpl) xMPMeta).tree, XMPPathParser.expandXPath("http://ns.google.com/photos/1.0/container/", "Directory"), false, null);
            if (findNode == null) {
                i = 0;
            } else if (findNode.getOptions().isArray()) {
                i = findNode.getChildrenLength();
            } else {
                throw new XMPException("The named property is not an array", 102);
            }
            MicroVideoXmpContainer microVideoXmpContainer = new MicroVideoXmpContainer();
            for (int i2 = 1; i2 <= i; i2++) {
                if (i2 > 0) {
                    StringBuilder sb = new StringBuilder(22);
                    sb.append("Directory");
                    sb.append('[');
                    sb.append(i2);
                    sb.append(']');
                    str = sb.toString();
                } else if (i2 == -1) {
                    str = "Directory".concat("[last()]");
                } else {
                    throw new XMPException("Array index must be larger than zero", 104);
                }
                String structField = MicroVideoXmpContainerItem.getStructField(xMPMeta, "http://ns.google.com/photos/1.0/container/", str, "http://ns.google.com/photos/1.0/container/item/", "Mime");
                MicroVideoXmpContainerItem.requiredNonNullValue(structField, "Mime");
                String structField2 = MicroVideoXmpContainerItem.getStructField(xMPMeta, "http://ns.google.com/photos/1.0/container/", str, "http://ns.google.com/photos/1.0/container/item/", "Semantic");
                MicroVideoXmpContainerItem.requiredNonNullValue(structField2, "Semantic");
                String structField3 = MicroVideoXmpContainerItem.getStructField(xMPMeta, "http://ns.google.com/photos/1.0/container/", str, "http://ns.google.com/photos/1.0/container/item/", "Length");
                String str2 = "0";
                if (structField3 == null) {
                    structField3 = str2;
                }
                String structField4 = MicroVideoXmpContainerItem.getStructField(xMPMeta, "http://ns.google.com/photos/1.0/container/", str, "http://ns.google.com/photos/1.0/container/item/", "Padding");
                if (structField4 != null) {
                    str2 = structField4;
                }
                Integer valueOf = Integer.valueOf(Integer.parseInt(structField3));
                Integer valueOf2 = Integer.valueOf(Integer.parseInt(str2));
                String str3 = "";
                if (valueOf == null) {
                    str3 = str3.concat(" length");
                }
                if (valueOf2 == null) {
                    str3 = String.valueOf(str3).concat(" padding");
                }
                if (!str3.isEmpty()) {
                    throw new IllegalStateException(str3.length() != 0 ? "Missing required properties:".concat(str3) : new String("Missing required properties:"));
                }
                AutoValue_MicroVideoXmpContainerItem autoValue_MicroVideoXmpContainerItem = new AutoValue_MicroVideoXmpContainerItem(structField, structField2, valueOf.intValue(), valueOf2.intValue(), null);
                synchronized (microVideoXmpContainer) {
                    microVideoXmpContainer.items.add(autoValue_MicroVideoXmpContainerItem);
                }
            }
            synchronized (microVideoXmpContainer) {
                unmodifiableList = Collections.unmodifiableList(microVideoXmpContainer.items);
            }
            int i3 = 0;
            for (MicroVideoXmpContainerItem microVideoXmpContainerItem : unmodifiableList) {
                if (z) {
                    String str4 = "";
                    if (!microVideoXmpContainerItem.getSemantic().contentEquals("Primary")) {
                        str4 = str4.concat("First container item must be primary.\n");
                        Log.w("MVXmpMetadata", "Badly formatted file. First container item is not primary");
                    }
                    if (microVideoXmpContainerItem.getLength() > 0) {
                        String.valueOf(str4).concat("First container item must have length of 0.\n");
                        int length = microVideoXmpContainerItem.getLength();
                        StringBuilder sb2 = new StringBuilder(59);
                        sb2.append("First container length expected to be 0. Found: ");
                        sb2.append(length);
                        Log.w("MVXmpMetadata", sb2.toString());
                    }
                    i3 += microVideoXmpContainerItem.getPadding();
                    z = false;
                } else {
                    String str5 = "";
                    if (microVideoXmpContainerItem.getSemantic().contentEquals("Primary")) {
                        str5 = str5.concat("Secondary container items must not be primary.\n");
                        Log.w("MVXmpMetadata", "Badly formatted file. Only first container item should be primary");
                    }
                    if (microVideoXmpContainerItem.getPadding() > 0) {
                        String.valueOf(str5).concat("Secondary container items must have 0 padding.\n");
                        Log.w("MVXmpMetadata", "Badly formatted file. Only primary container items may have padding.");
                    }
                    i3 += microVideoXmpContainerItem.getPadding() + microVideoXmpContainerItem.getLength();
                }
            }
            return i3;
        }
        throw new XMPException("V1 format does not have a container", 5);
    }

    public static <T> T requiredValueMissing(String str) throws XMPException {
        throw new XMPException(str.length() != 0 ? "Property value missing for ".concat(str) : new String("Property value missing for "), 5);
    }
}
