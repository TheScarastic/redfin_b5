package com.adobe.xmp.impl;

import androidx.recyclerview.widget.RecyclerView;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.xpath.XMPPath;
import com.adobe.xmp.impl.xpath.XMPPathSegment;
import com.adobe.xmp.options.PropertyOptions;
import com.android.systemui.shared.system.QuickStepContract;
import java.util.Iterator;
/* loaded from: classes.dex */
public class XMPNodeUtils {
    public static void appendLangItem(XMPNode xMPNode, String str, String str2) throws XMPException {
        XMPNode xMPNode2 = new XMPNode("[]", str2, null);
        XMPNode xMPNode3 = new XMPNode("xml:lang", str, null);
        xMPNode2.addQualifier(xMPNode3);
        if (!"x-default".equals(xMPNode3.value)) {
            xMPNode.addChild(xMPNode2);
        } else {
            xMPNode.addChild(1, xMPNode2);
        }
    }

    public static void deleteNode(XMPNode xMPNode) {
        XMPNode xMPNode2 = xMPNode.parent;
        if (xMPNode.getOptions().getOption(32)) {
            xMPNode2.removeQualifier(xMPNode);
        } else {
            xMPNode2.removeChild(xMPNode);
        }
        if (!xMPNode2.hasChildren() && xMPNode2.getOptions().isSchemaNode()) {
            xMPNode2.parent.removeChild(xMPNode2);
        }
    }

    public static XMPNode findChildNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        if (!xMPNode.getOptions().isSchemaNode() && !xMPNode.getOptions().isStruct()) {
            if (!xMPNode.implicit) {
                throw new XMPException("Named children only allowed for schemas and structs", 102);
            } else if (xMPNode.getOptions().isArray()) {
                throw new XMPException("Named children not allowed for arrays", 102);
            } else if (z) {
                xMPNode.getOptions().setOption(256, true);
            }
        }
        XMPNode find = xMPNode.find(xMPNode.getChildren(), str);
        if (find != null || !z) {
            return find;
        }
        XMPNode xMPNode2 = new XMPNode(str, null, new PropertyOptions());
        xMPNode2.implicit = true;
        xMPNode.addChild(xMPNode2);
        return xMPNode2;
    }

    public static XMPNode findNode(XMPNode xMPNode, XMPPath xMPPath, boolean z, PropertyOptions propertyOptions) throws XMPException {
        XMPNode xMPNode2;
        if (xMPPath.size() != 0) {
            XMPNode findSchemaNode = findSchemaNode(xMPNode, xMPPath.getSegment(0).name, null, z);
            if (findSchemaNode == null) {
                return null;
            }
            if (findSchemaNode.implicit) {
                findSchemaNode.implicit = false;
                xMPNode2 = findSchemaNode;
            } else {
                xMPNode2 = null;
            }
            for (int i = 1; i < xMPPath.size(); i++) {
                try {
                    findSchemaNode = followXPathStep(findSchemaNode, xMPPath.getSegment(i), z);
                    if (findSchemaNode == null) {
                        if (z) {
                            deleteNode(xMPNode2);
                        }
                        return null;
                    }
                    if (findSchemaNode.implicit) {
                        findSchemaNode.implicit = false;
                        if (i == 1 && xMPPath.getSegment(i).alias && xMPPath.getSegment(i).aliasForm != 0) {
                            findSchemaNode.getOptions().setOption(xMPPath.getSegment(i).aliasForm, true);
                        } else if (i < xMPPath.size() - 1 && xMPPath.getSegment(i).kind == 1 && !findSchemaNode.getOptions().isCompositeProperty()) {
                            findSchemaNode.getOptions().setOption(256, true);
                        }
                        if (xMPNode2 == null) {
                            xMPNode2 = findSchemaNode;
                        }
                    }
                } catch (XMPException e) {
                    if (xMPNode2 != null) {
                        deleteNode(xMPNode2);
                    }
                    throw e;
                }
            }
            if (xMPNode2 != null) {
                findSchemaNode.getOptions().mergeWith(propertyOptions);
                findSchemaNode.options = findSchemaNode.getOptions();
            }
            return findSchemaNode;
        }
        throw new XMPException("Empty XMPPath", 102);
    }

    public static XMPNode findSchemaNode(XMPNode xMPNode, String str, boolean z) throws XMPException {
        return findSchemaNode(xMPNode, str, null, z);
    }

    public static XMPNode followXPathStep(XMPNode xMPNode, XMPPathSegment xMPPathSegment, boolean z) throws XMPException {
        int i;
        int i2 = xMPPathSegment.kind;
        if (i2 == 1) {
            return findChildNode(xMPNode, xMPPathSegment.name, z);
        }
        if (i2 == 2) {
            String substring = xMPPathSegment.name.substring(1);
            XMPNode find = xMPNode.find(xMPNode.qualifier, substring);
            if (find != null || !z) {
                return find;
            }
            XMPNode xMPNode2 = new XMPNode(substring, null, null);
            xMPNode2.implicit = true;
            xMPNode.addQualifier(xMPNode2);
            return xMPNode2;
        } else if (xMPNode.getOptions().isArray()) {
            if (i2 == 3) {
                String str = xMPPathSegment.name;
                try {
                    i = Integer.parseInt(str.substring(1, str.length() - 1));
                    if (i < 1) {
                        throw new XMPException("Array index must be larger than zero", 102);
                    } else if (z && i == xMPNode.getChildrenLength() + 1) {
                        XMPNode xMPNode3 = new XMPNode("[]", null, null);
                        xMPNode3.implicit = true;
                        xMPNode.addChild(xMPNode3);
                    }
                } catch (NumberFormatException unused) {
                    throw new XMPException("Array index not digits.", 102);
                }
            } else if (i2 == 4) {
                i = xMPNode.getChildrenLength();
            } else {
                int i3 = -1;
                if (i2 == 6) {
                    String[] splitNameAndValue = Utils.splitNameAndValue(xMPPathSegment.name);
                    String str2 = splitNameAndValue[0];
                    String str3 = splitNameAndValue[1];
                    for (int i4 = 1; i4 <= xMPNode.getChildrenLength() && i3 < 0; i4++) {
                        XMPNode child = xMPNode.getChild(i4);
                        if (child.getOptions().isStruct()) {
                            int i5 = 1;
                            while (true) {
                                if (i5 <= child.getChildrenLength()) {
                                    XMPNode child2 = child.getChild(i5);
                                    if (str2.equals(child2.name) && str3.equals(child2.value)) {
                                        i3 = i4;
                                        break;
                                    }
                                    i5++;
                                }
                            }
                        } else {
                            throw new XMPException("Field selector must be used on array of struct", 102);
                        }
                    }
                } else if (i2 == 5) {
                    String[] splitNameAndValue2 = Utils.splitNameAndValue(xMPPathSegment.name);
                    String str4 = splitNameAndValue2[0];
                    String str5 = splitNameAndValue2[1];
                    int i6 = xMPPathSegment.aliasForm;
                    if ("xml:lang".equals(str4)) {
                        int lookupLanguageItem = lookupLanguageItem(xMPNode, Utils.normalizeLangValue(str5));
                        if (lookupLanguageItem >= 0 || (i6 & QuickStepContract.SYSUI_STATE_TRACING_ENABLED) <= 0) {
                            i = lookupLanguageItem;
                        } else {
                            XMPNode xMPNode4 = new XMPNode("[]", null, null);
                            xMPNode4.addQualifier(new XMPNode("xml:lang", "x-default", null));
                            xMPNode.addChild(1, xMPNode4);
                            i = 1;
                        }
                    } else {
                        i = 1;
                        loop2: while (i < xMPNode.getChildrenLength()) {
                            Iterator iterateQualifier = xMPNode.getChild(i).iterateQualifier();
                            while (iterateQualifier.hasNext()) {
                                XMPNode xMPNode5 = (XMPNode) iterateQualifier.next();
                                if (str4.equals(xMPNode5.name) && str5.equals(xMPNode5.value)) {
                                    break loop2;
                                }
                            }
                            i++;
                        }
                    }
                } else {
                    throw new XMPException("Unknown array indexing step in FollowXPathStep", 9);
                }
                i = i3;
            }
            if (1 > i || i > xMPNode.getChildrenLength()) {
                return null;
            }
            return xMPNode.getChild(i);
        } else {
            throw new XMPException("Indexing applied to non-array", 102);
        }
    }

    public static int lookupLanguageItem(XMPNode xMPNode, String str) throws XMPException {
        if (xMPNode.getOptions().isArray()) {
            for (int i = 1; i <= xMPNode.getChildrenLength(); i++) {
                XMPNode child = xMPNode.getChild(i);
                if (child.hasQualifier() && "xml:lang".equals(child.getQualifier(1).name) && str.equals(child.getQualifier(1).value)) {
                    return i;
                }
            }
            return -1;
        }
        throw new XMPException("Language item must be used on array", 102);
    }

    public static PropertyOptions verifySetOptions(PropertyOptions propertyOptions, Object obj) throws XMPException {
        if (propertyOptions.isArrayAltText()) {
            propertyOptions.setOption(QuickStepContract.SYSUI_STATE_QUICK_SETTINGS_EXPANDED, true);
        }
        if (propertyOptions.isArrayAlternate()) {
            propertyOptions.setOption(QuickStepContract.SYSUI_STATE_SEARCH_DISABLED, true);
        }
        if (propertyOptions.getOption(QuickStepContract.SYSUI_STATE_SEARCH_DISABLED)) {
            propertyOptions.setOption(QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED, true);
        }
        propertyOptions.isCompositeProperty();
        propertyOptions.assertConsistency(propertyOptions.options);
        return propertyOptions;
    }

    public static XMPNode findSchemaNode(XMPNode xMPNode, String str, String str2, boolean z) throws XMPException {
        XMPNode find = xMPNode.find(xMPNode.getChildren(), str);
        if (find == null && z) {
            PropertyOptions propertyOptions = new PropertyOptions();
            propertyOptions.setOption(RecyclerView.UNDEFINED_DURATION, true);
            find = new XMPNode(str, null, propertyOptions);
            find.implicit = true;
            String namespacePrefix = ((XMPSchemaRegistryImpl) XMPMetaFactory.schema).getNamespacePrefix(str);
            if (namespacePrefix == null) {
                if (str2 == null || str2.length() == 0) {
                    throw new XMPException("Unregistered schema namespace URI", 101);
                }
                namespacePrefix = ((XMPSchemaRegistryImpl) XMPMetaFactory.schema).registerNamespace(str, str2);
            }
            find.value = namespacePrefix;
            xMPNode.addChild(find);
        }
        return find;
    }
}
