package com.android.wm.shell.bubbles.storage;

import android.util.SparseArray;
import android.util.Xml;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;
/* compiled from: BubbleXmlHelper.kt */
/* loaded from: classes2.dex */
public final class BubbleXmlHelperKt {
    public static final void writeXml(OutputStream outputStream, SparseArray<List<BubbleEntity>> sparseArray) throws IOException {
        Intrinsics.checkNotNullParameter(outputStream, "stream");
        Intrinsics.checkNotNullParameter(sparseArray, "bubbles");
        FastXmlSerializer fastXmlSerializer = new FastXmlSerializer();
        fastXmlSerializer.setOutput(outputStream, StandardCharsets.UTF_8.name());
        fastXmlSerializer.startDocument(null, Boolean.TRUE);
        fastXmlSerializer.startTag(null, "bs");
        fastXmlSerializer.attribute(null, "v", "2");
        int size = sparseArray.size();
        if (size > 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                int keyAt = sparseArray.keyAt(i);
                List<BubbleEntity> valueAt = sparseArray.valueAt(i);
                fastXmlSerializer.startTag(null, "bs");
                fastXmlSerializer.attribute(null, "uid", String.valueOf(keyAt));
                Intrinsics.checkNotNullExpressionValue(valueAt, "v");
                for (BubbleEntity bubbleEntity : valueAt) {
                    writeXmlEntry(fastXmlSerializer, bubbleEntity);
                }
                fastXmlSerializer.endTag(null, "bs");
                if (i2 >= size) {
                    break;
                }
                i = i2;
            }
        }
        fastXmlSerializer.endTag(null, "bs");
        fastXmlSerializer.endDocument();
    }

    private static final void writeXmlEntry(XmlSerializer xmlSerializer, BubbleEntity bubbleEntity) {
        try {
            xmlSerializer.startTag(null, "bb");
            xmlSerializer.attribute(null, "uid", String.valueOf(bubbleEntity.getUserId()));
            xmlSerializer.attribute(null, "pkg", bubbleEntity.getPackageName());
            xmlSerializer.attribute(null, "sid", bubbleEntity.getShortcutId());
            xmlSerializer.attribute(null, "key", bubbleEntity.getKey());
            xmlSerializer.attribute(null, "h", String.valueOf(bubbleEntity.getDesiredHeight()));
            xmlSerializer.attribute(null, "hid", String.valueOf(bubbleEntity.getDesiredHeightResId()));
            String title = bubbleEntity.getTitle();
            if (title != null) {
                xmlSerializer.attribute(null, "t", title);
            }
            xmlSerializer.attribute(null, "tid", String.valueOf(bubbleEntity.getTaskId()));
            String locus = bubbleEntity.getLocus();
            if (locus != null) {
                xmlSerializer.attribute(null, "l", locus);
            }
            xmlSerializer.endTag(null, "bb");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final SparseArray<List<BubbleEntity>> readXml(InputStream inputStream) {
        Intrinsics.checkNotNullParameter(inputStream, "stream");
        SparseArray<List<BubbleEntity>> sparseArray = new SparseArray<>();
        XmlPullParser newPullParser = Xml.newPullParser();
        Intrinsics.checkNotNullExpressionValue(newPullParser, "newPullParser()");
        newPullParser.setInput(inputStream, StandardCharsets.UTF_8.name());
        XmlUtils.beginDocument(newPullParser, "bs");
        int depth = newPullParser.getDepth();
        String attributeWithName = getAttributeWithName(newPullParser, "v");
        Integer valueOf = attributeWithName == null ? null : Integer.valueOf(Integer.parseInt(attributeWithName));
        if (valueOf == null) {
            return sparseArray;
        }
        int intValue = valueOf.intValue();
        if (intValue == 1) {
            int depth2 = newPullParser.getDepth();
            ArrayList arrayList = new ArrayList();
            while (XmlUtils.nextElementWithin(newPullParser, depth2)) {
                BubbleEntity readXmlEntry = readXmlEntry(newPullParser);
                if (readXmlEntry != null && readXmlEntry.getUserId() == 0) {
                    arrayList.add(readXmlEntry);
                }
            }
            if (!arrayList.isEmpty()) {
                sparseArray.put(0, CollectionsKt___CollectionsKt.toList(arrayList));
            }
        } else if (intValue == 2) {
            while (XmlUtils.nextElementWithin(newPullParser, depth)) {
                String attributeWithName2 = getAttributeWithName(newPullParser, "uid");
                if (attributeWithName2 != null) {
                    int depth3 = newPullParser.getDepth();
                    ArrayList arrayList2 = new ArrayList();
                    while (XmlUtils.nextElementWithin(newPullParser, depth3)) {
                        BubbleEntity readXmlEntry2 = readXmlEntry(newPullParser);
                        if (readXmlEntry2 != null) {
                            arrayList2.add(readXmlEntry2);
                        }
                    }
                    if (!arrayList2.isEmpty()) {
                        sparseArray.put(Integer.parseInt(attributeWithName2), CollectionsKt___CollectionsKt.toList(arrayList2));
                    }
                }
            }
        }
        return sparseArray;
    }

    private static final BubbleEntity readXmlEntry(XmlPullParser xmlPullParser) {
        String attributeWithName;
        String attributeWithName2;
        while (xmlPullParser.getEventType() != 2) {
            xmlPullParser.next();
        }
        String attributeWithName3 = getAttributeWithName(xmlPullParser, "uid");
        Integer valueOf = attributeWithName3 == null ? null : Integer.valueOf(Integer.parseInt(attributeWithName3));
        if (valueOf == null) {
            return null;
        }
        int intValue = valueOf.intValue();
        String attributeWithName4 = getAttributeWithName(xmlPullParser, "pkg");
        if (attributeWithName4 == null || (attributeWithName = getAttributeWithName(xmlPullParser, "sid")) == null || (attributeWithName2 = getAttributeWithName(xmlPullParser, "key")) == null) {
            return null;
        }
        String attributeWithName5 = getAttributeWithName(xmlPullParser, "h");
        Integer valueOf2 = attributeWithName5 == null ? null : Integer.valueOf(Integer.parseInt(attributeWithName5));
        if (valueOf2 == null) {
            return null;
        }
        int intValue2 = valueOf2.intValue();
        String attributeWithName6 = getAttributeWithName(xmlPullParser, "hid");
        Integer valueOf3 = attributeWithName6 == null ? null : Integer.valueOf(Integer.parseInt(attributeWithName6));
        if (valueOf3 == null) {
            return null;
        }
        int intValue3 = valueOf3.intValue();
        String attributeWithName7 = getAttributeWithName(xmlPullParser, "t");
        String attributeWithName8 = getAttributeWithName(xmlPullParser, "tid");
        return new BubbleEntity(intValue, attributeWithName4, attributeWithName, attributeWithName2, intValue2, intValue3, attributeWithName7, attributeWithName8 == null ? -1 : Integer.parseInt(attributeWithName8), getAttributeWithName(xmlPullParser, "l"));
    }

    private static final String getAttributeWithName(XmlPullParser xmlPullParser, String str) {
        int attributeCount = xmlPullParser.getAttributeCount();
        if (attributeCount <= 0) {
            return null;
        }
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (Intrinsics.areEqual(xmlPullParser.getAttributeName(i), str)) {
                return xmlPullParser.getAttributeValue(i);
            }
            if (i2 >= attributeCount) {
                return null;
            }
            i = i2;
        }
    }
}
