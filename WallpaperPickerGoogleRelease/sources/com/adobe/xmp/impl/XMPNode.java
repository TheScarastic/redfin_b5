package com.adobe.xmp.impl;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.adobe.xmp.options.PropertyOptions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class XMPNode implements Comparable {
    public boolean alias;
    public boolean hasAliases;
    public boolean hasValueChild;
    public boolean implicit;
    public String name;
    public PropertyOptions options;
    public XMPNode parent;
    public String value;
    public List children = null;
    public List qualifier = null;

    public XMPNode(String str, String str2, PropertyOptions propertyOptions) {
        this.options = null;
        this.name = str;
        this.value = str2;
        this.options = propertyOptions;
    }

    public void addChild(int i, XMPNode xMPNode) throws XMPException {
        assertChildNotExisting(xMPNode.name);
        xMPNode.parent = this;
        getChildren().add(i - 1, xMPNode);
    }

    public void addQualifier(XMPNode xMPNode) throws XMPException {
        String str = xMPNode.name;
        if ("[]".equals(str) || find(this.qualifier, str) == null) {
            xMPNode.parent = this;
            xMPNode.getOptions().setOption(32, true);
            getOptions().setOption(16, true);
            if ("xml:lang".equals(xMPNode.name)) {
                this.options.setOption(64, true);
                getQualifier().add(0, xMPNode);
            } else if ("rdf:type".equals(xMPNode.name)) {
                this.options.setOption(128, true);
                getQualifier().add(this.options.getHasLanguage() ? 1 : 0, xMPNode);
            } else {
                getQualifier().add(xMPNode);
            }
        } else {
            throw new XMPException(FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(str, 22), "Duplicate '", str, "' qualifier"), 203);
        }
    }

    public final void assertChildNotExisting(String str) throws XMPException {
        if (!"[]".equals(str) && find(getChildren(), str) != null) {
            throw new XMPException(FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(str, 35), "Duplicate property or field node '", str, "'"), 203);
        }
    }

    @Override // java.lang.Object
    public Object clone() {
        PropertyOptions propertyOptions;
        try {
            propertyOptions = new PropertyOptions(getOptions().options);
        } catch (XMPException unused) {
            propertyOptions = new PropertyOptions();
        }
        XMPNode xMPNode = new XMPNode(this.name, this.value, propertyOptions);
        try {
            Iterator iterateChildren = iterateChildren();
            while (iterateChildren.hasNext()) {
                xMPNode.addChild((XMPNode) ((XMPNode) iterateChildren.next()).clone());
            }
            Iterator iterateQualifier = iterateQualifier();
            while (iterateQualifier.hasNext()) {
                xMPNode.addQualifier((XMPNode) ((XMPNode) iterateQualifier.next()).clone());
            }
        } catch (XMPException unused2) {
        }
        return xMPNode;
    }

    @Override // java.lang.Comparable
    public int compareTo(Object obj) {
        if (getOptions().isSchemaNode()) {
            return this.value.compareTo(((XMPNode) obj).value);
        }
        return this.name.compareTo(((XMPNode) obj).name);
    }

    public final XMPNode find(List list, String str) {
        if (list == null) {
            return null;
        }
        Iterator it = list.iterator();
        while (it.hasNext()) {
            XMPNode xMPNode = (XMPNode) it.next();
            if (xMPNode.name.equals(str)) {
                return xMPNode;
            }
        }
        return null;
    }

    public XMPNode getChild(int i) {
        return (XMPNode) getChildren().get(i - 1);
    }

    public final List getChildren() {
        if (this.children == null) {
            this.children = new ArrayList(0);
        }
        return this.children;
    }

    public int getChildrenLength() {
        List list = this.children;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public PropertyOptions getOptions() {
        if (this.options == null) {
            this.options = new PropertyOptions();
        }
        return this.options;
    }

    public XMPNode getQualifier(int i) {
        return (XMPNode) getQualifier().get(i - 1);
    }

    public int getQualifierLength() {
        List list = this.qualifier;
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public boolean hasChildren() {
        List list = this.children;
        return list != null && list.size() > 0;
    }

    public boolean hasQualifier() {
        List list = this.qualifier;
        return list != null && list.size() > 0;
    }

    public Iterator iterateChildren() {
        if (this.children != null) {
            return getChildren().iterator();
        }
        return Collections.EMPTY_LIST.listIterator();
    }

    public Iterator iterateQualifier() {
        if (this.qualifier == null) {
            return Collections.EMPTY_LIST.iterator();
        }
        final Iterator it = getQualifier().iterator();
        return new Iterator() { // from class: com.adobe.xmp.impl.XMPNode.1
            @Override // java.util.Iterator
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override // java.util.Iterator
            public Object next() {
                return it.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("remove() is not allowed due to the internal contraints");
            }
        };
    }

    public void removeChild(XMPNode xMPNode) {
        getChildren().remove(xMPNode);
        if (this.children.isEmpty()) {
            this.children = null;
        }
    }

    public void removeQualifier(XMPNode xMPNode) {
        PropertyOptions options = getOptions();
        if ("xml:lang".equals(xMPNode.name)) {
            options.setOption(64, false);
        } else if ("rdf:type".equals(xMPNode.name)) {
            options.setOption(128, false);
        }
        getQualifier().remove(xMPNode);
        if (this.qualifier.isEmpty()) {
            options.setOption(16, false);
            this.qualifier = null;
        }
    }

    public final List getQualifier() {
        if (this.qualifier == null) {
            this.qualifier = new ArrayList(0);
        }
        return this.qualifier;
    }

    public void addChild(XMPNode xMPNode) throws XMPException {
        assertChildNotExisting(xMPNode.name);
        xMPNode.parent = this;
        getChildren().add(xMPNode);
    }
}
