package com.adobe.xmp;
/* loaded from: classes.dex */
public class XMPException extends Exception {
    private int errorCode;

    public XMPException(String str, int i) {
        super(str);
        this.errorCode = i;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public XMPException(String str, int i, Throwable th) {
        super(str, th);
        this.errorCode = i;
    }
}
