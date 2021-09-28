package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;
/* loaded from: classes.dex */
public class ParseState {
    public int pos = 0;
    public String str;

    public ParseState(String str) {
        this.str = str;
    }

    public char ch(int i) {
        if (i < this.str.length()) {
            return this.str.charAt(i);
        }
        return 0;
    }

    public int gatherInt(String str, int i) throws XMPException {
        char ch = ch(this.pos);
        int i2 = 0;
        boolean z = false;
        while ('0' <= ch && ch <= '9') {
            i2 = (i2 * 10) + (ch - '0');
            int i3 = this.pos + 1;
            this.pos = i3;
            ch = ch(i3);
            z = true;
        }
        if (!z) {
            throw new XMPException(str, 5);
        } else if (i2 > i) {
            return i;
        } else {
            if (i2 < 0) {
                return 0;
            }
            return i2;
        }
    }

    public boolean hasNext() {
        return this.pos < this.str.length();
    }

    public void skip() {
        this.pos++;
    }

    public char ch() {
        if (this.pos < this.str.length()) {
            return this.str.charAt(this.pos);
        }
        return 0;
    }
}
