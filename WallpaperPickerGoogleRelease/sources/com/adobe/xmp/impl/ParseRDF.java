package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.XMPSchemaRegistry;
import com.adobe.xmp.options.PropertyOptions;
import java.util.Iterator;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
/* loaded from: classes.dex */
public class ParseRDF {
    public static XMPNode addChildNode(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, String str, boolean z) throws XMPException {
        String str2;
        XMPSchemaRegistry xMPSchemaRegistry = XMPMetaFactory.schema;
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI != null) {
            if ("http://purl.org/dc/1.1/".equals(namespaceURI)) {
                namespaceURI = "http://purl.org/dc/elements/1.1/";
            }
            XMPSchemaRegistryImpl xMPSchemaRegistryImpl = (XMPSchemaRegistryImpl) xMPSchemaRegistry;
            String namespacePrefix = xMPSchemaRegistryImpl.getNamespacePrefix(namespaceURI);
            if (namespacePrefix == null) {
                if (node.getPrefix() != null) {
                    str2 = node.getPrefix();
                } else {
                    str2 = "_dflt";
                }
                namespacePrefix = xMPSchemaRegistryImpl.registerNamespace(namespaceURI, str2);
            }
            String valueOf = String.valueOf(namespacePrefix);
            String valueOf2 = String.valueOf(node.getLocalName());
            String concat = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            PropertyOptions propertyOptions = new PropertyOptions();
            boolean z2 = false;
            if (z) {
                xMPNode = XMPNodeUtils.findSchemaNode(xMPMetaImpl.tree, namespaceURI, "_dflt", true);
                xMPNode.implicit = false;
                if (xMPSchemaRegistryImpl.findAlias(concat) != null) {
                    xMPMetaImpl.tree.hasAliases = true;
                    xMPNode.hasAliases = true;
                    z2 = true;
                }
            }
            boolean equals = "rdf:li".equals(concat);
            boolean equals2 = "rdf:value".equals(concat);
            XMPNode xMPNode2 = new XMPNode(concat, str, propertyOptions);
            xMPNode2.alias = z2;
            if (!equals2) {
                xMPNode.addChild(xMPNode2);
            } else {
                xMPNode.addChild(1, xMPNode2);
            }
            if (equals2) {
                if (z || !xMPNode.getOptions().isStruct()) {
                    throw new XMPException("Misplaced rdf:value element", 202);
                }
                xMPNode.hasValueChild = true;
            }
            if (equals) {
                if (xMPNode.getOptions().isArray()) {
                    xMPNode2.name = "[]";
                } else {
                    throw new XMPException("Misplaced rdf:li element", 202);
                }
            }
            return xMPNode2;
        }
        throw new XMPException("XML namespace required for all elements and attributes", 202);
    }

    public static XMPNode addQualifierNode(XMPNode xMPNode, String str, String str2) throws XMPException {
        if ("xml:lang".equals(str)) {
            str2 = Utils.normalizeLangValue(str2);
        }
        XMPNode xMPNode2 = new XMPNode(str, str2, null);
        xMPNode.addQualifier(xMPNode2);
        return xMPNode2;
    }

    public static void fixupQualifiedNode(XMPNode xMPNode) throws XMPException {
        XMPNode child = xMPNode.getChild(1);
        if (child.getOptions().getHasLanguage()) {
            if (!xMPNode.getOptions().getHasLanguage()) {
                XMPNode qualifier = child.getQualifier(1);
                child.removeQualifier(qualifier);
                xMPNode.addQualifier(qualifier);
            } else {
                throw new XMPException("Redundant xml:lang for rdf:value element", 203);
            }
        }
        for (int i = 1; i <= child.getQualifierLength(); i++) {
            xMPNode.addQualifier(child.getQualifier(i));
        }
        for (int i2 = 2; i2 <= xMPNode.getChildrenLength(); i2++) {
            xMPNode.addQualifier(xMPNode.getChild(i2));
        }
        xMPNode.hasValueChild = false;
        xMPNode.getOptions().setOption(256, false);
        xMPNode.getOptions().mergeWith(child.getOptions());
        xMPNode.value = child.value;
        xMPNode.children = null;
        Iterator iterateChildren = child.iterateChildren();
        while (iterateChildren.hasNext()) {
            xMPNode.addChild((XMPNode) iterateChildren.next());
        }
    }

    public static int getRDFTermKind(Node node) {
        String localName = node.getLocalName();
        String namespaceURI = node.getNamespaceURI();
        if (namespaceURI == null && (("about".equals(localName) || "ID".equals(localName)) && (node instanceof Attr) && "http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(((Attr) node).getOwnerElement().getNamespaceURI()))) {
            namespaceURI = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
        }
        if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI)) {
            return 0;
        }
        if ("li".equals(localName)) {
            return 9;
        }
        if ("parseType".equals(localName)) {
            return 4;
        }
        if ("Description".equals(localName)) {
            return 8;
        }
        if ("about".equals(localName)) {
            return 3;
        }
        if ("resource".equals(localName)) {
            return 5;
        }
        if ("RDF".equals(localName)) {
            return 1;
        }
        if ("ID".equals(localName)) {
            return 2;
        }
        if ("nodeID".equals(localName)) {
            return 6;
        }
        if ("datatype".equals(localName)) {
            return 7;
        }
        if ("aboutEach".equals(localName)) {
            return 10;
        }
        if ("aboutEachPrefix".equals(localName)) {
            return 11;
        }
        return "bagID".equals(localName) ? 12 : 0;
    }

    public static boolean isWhitespaceNode(Node node) {
        if (node.getNodeType() != 3) {
            return false;
        }
        String nodeValue = node.getNodeValue();
        for (int i = 0; i < nodeValue.length(); i++) {
            if (!Character.isWhitespace(nodeValue.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:61:0x00f0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void rdf_EmptyPropertyElement(com.adobe.xmp.impl.XMPMetaImpl r16, com.adobe.xmp.impl.XMPNode r17, org.w3c.dom.Node r18, boolean r19) throws com.adobe.xmp.XMPException {
        /*
        // Method dump skipped, instructions count: 364
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.ParseRDF.rdf_EmptyPropertyElement(com.adobe.xmp.impl.XMPMetaImpl, com.adobe.xmp.impl.XMPNode, org.w3c.dom.Node, boolean):void");
    }

    public static void rdf_LiteralPropertyElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        XMPNode addChildNode = addChildNode(xMPMetaImpl, xMPNode, node, null, z);
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node item = node.getAttributes().item(i);
            if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                String namespaceURI = item.getNamespaceURI();
                String localName = item.getLocalName();
                if ("xml:lang".equals(item.getNodeName())) {
                    addQualifierNode(addChildNode, "xml:lang", item.getNodeValue());
                } else if (!"http://www.w3.org/1999/02/22-rdf-syntax-ns#".equals(namespaceURI) || (!"ID".equals(localName) && !"datatype".equals(localName))) {
                    throw new XMPException("Invalid attribute for literal property element", 202);
                }
            }
        }
        String str = "";
        for (int i2 = 0; i2 < node.getChildNodes().getLength(); i2++) {
            Node item2 = node.getChildNodes().item(i2);
            if (item2.getNodeType() == 3) {
                String valueOf = String.valueOf(str);
                String valueOf2 = String.valueOf(item2.getNodeValue());
                str = valueOf2.length() != 0 ? valueOf.concat(valueOf2) : new String(valueOf);
            } else {
                throw new XMPException("Invalid child of literal property element", 202);
            }
        }
        addChildNode.value = str;
    }

    public static void rdf_NodeElement(XMPMetaImpl xMPMetaImpl, XMPNode xMPNode, Node node, boolean z) throws XMPException {
        int rDFTermKind = getRDFTermKind(node);
        if (rDFTermKind != 8 && rDFTermKind != 0) {
            throw new XMPException("Node element must be rdf:Description or typed node", 202);
        } else if (!z || rDFTermKind != 0) {
            int i = 0;
            for (int i2 = 0; i2 < node.getAttributes().getLength(); i2++) {
                Node item = node.getAttributes().item(i2);
                if (!"xmlns".equals(item.getPrefix()) && (item.getPrefix() != null || !"xmlns".equals(item.getNodeName()))) {
                    int rDFTermKind2 = getRDFTermKind(item);
                    if (rDFTermKind2 == 0) {
                        addChildNode(xMPMetaImpl, xMPNode, item, item.getNodeValue(), z);
                    } else if (rDFTermKind2 != 6 && rDFTermKind2 != 2 && rDFTermKind2 != 3) {
                        throw new XMPException("Invalid nodeElement attribute", 202);
                    } else if (i <= 0) {
                        i++;
                        if (z && rDFTermKind2 == 3) {
                            String str = xMPNode.name;
                            if (str == null || str.length() <= 0) {
                                xMPNode.name = item.getNodeValue();
                            } else if (!xMPNode.name.equals(item.getNodeValue())) {
                                throw new XMPException("Mismatched top level rdf:about values", 203);
                            }
                        }
                    } else {
                        throw new XMPException("Mutally exclusive about, ID, nodeID attributes", 202);
                    }
                }
            }
            rdf_PropertyElementList(xMPMetaImpl, xMPNode, node, z);
        } else {
            throw new XMPException("Top level typed node not allowed", 203);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:227:0x03ef A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void rdf_PropertyElementList(com.adobe.xmp.impl.XMPMetaImpl r16, com.adobe.xmp.impl.XMPNode r17, org.w3c.dom.Node r18, boolean r19) throws com.adobe.xmp.XMPException {
        /*
        // Method dump skipped, instructions count: 1028
        */
        throw new UnsupportedOperationException("Method not decompiled: com.adobe.xmp.impl.ParseRDF.rdf_PropertyElementList(com.adobe.xmp.impl.XMPMetaImpl, com.adobe.xmp.impl.XMPNode, org.w3c.dom.Node, boolean):void");
    }
}
