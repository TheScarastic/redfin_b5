package com.google.common.base;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public enum StandardSystemProperty {
    /* Fake field, exist only in values array */
    JAVA_VERSION("java.version"),
    /* Fake field, exist only in values array */
    JAVA_VENDOR("java.vendor"),
    /* Fake field, exist only in values array */
    JAVA_VENDOR_URL("java.vendor.url"),
    /* Fake field, exist only in values array */
    JAVA_HOME("java.home"),
    /* Fake field, exist only in values array */
    JAVA_VM_SPECIFICATION_VERSION("java.vm.specification.version"),
    /* Fake field, exist only in values array */
    JAVA_VM_SPECIFICATION_VENDOR("java.vm.specification.vendor"),
    /* Fake field, exist only in values array */
    JAVA_VM_SPECIFICATION_NAME("java.vm.specification.name"),
    /* Fake field, exist only in values array */
    JAVA_VM_VERSION("java.vm.version"),
    /* Fake field, exist only in values array */
    JAVA_VM_VENDOR("java.vm.vendor"),
    /* Fake field, exist only in values array */
    JAVA_VM_NAME("java.vm.name"),
    /* Fake field, exist only in values array */
    JAVA_SPECIFICATION_VERSION("java.specification.version"),
    /* Fake field, exist only in values array */
    JAVA_SPECIFICATION_VENDOR("java.specification.vendor"),
    /* Fake field, exist only in values array */
    FILE_SEPARATOR("java.specification.name"),
    /* Fake field, exist only in values array */
    OS_VERSION("java.class.version"),
    JAVA_CLASS_PATH("java.class.path"),
    /* Fake field, exist only in values array */
    OS_VERSION("java.library.path"),
    /* Fake field, exist only in values array */
    FILE_SEPARATOR("java.io.tmpdir"),
    /* Fake field, exist only in values array */
    OS_VERSION("java.compiler"),
    /* Fake field, exist only in values array */
    FILE_SEPARATOR("java.ext.dirs"),
    /* Fake field, exist only in values array */
    OS_VERSION("os.name"),
    /* Fake field, exist only in values array */
    FILE_SEPARATOR("os.arch"),
    /* Fake field, exist only in values array */
    OS_VERSION("os.version"),
    /* Fake field, exist only in values array */
    FILE_SEPARATOR("file.separator"),
    PATH_SEPARATOR("path.separator"),
    /* Fake field, exist only in values array */
    LINE_SEPARATOR("line.separator"),
    /* Fake field, exist only in values array */
    USER_NAME("user.name"),
    /* Fake field, exist only in values array */
    USER_HOME("user.home"),
    /* Fake field, exist only in values array */
    USER_DIR("user.dir");
    
    private final String key;

    StandardSystemProperty(String str) {
        this.key = str;
    }

    @Override // java.lang.Enum, java.lang.Object
    public String toString() {
        String str = this.key;
        String value = value();
        return FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(value, XMPPathFactory$$ExternalSyntheticOutline0.m(str, 1)), str, "=", value);
    }

    public String value() {
        return System.getProperty(this.key);
    }
}
