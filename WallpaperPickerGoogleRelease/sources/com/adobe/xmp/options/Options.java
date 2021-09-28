package com.adobe.xmp.options;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
/* loaded from: classes.dex */
public abstract class Options {
    public int options;

    public Options() {
        this.options = 0;
    }

    public void assertConsistency(int i) throws XMPException {
    }

    public final void assertOptionsValid(int i) throws XMPException {
        int i2 = (~getValidOptions()) & i;
        if (i2 == 0) {
            assertConsistency(i);
        } else {
            String hexString = Integer.toHexString(i2);
            throw new XMPException(FakeDrag$$ExternalSyntheticOutline0.m(XMPPathFactory$$ExternalSyntheticOutline0.m(hexString, 33), "The option bit(s) 0x", hexString, " are invalid!"), 103);
        }
    }

    public boolean equals(Object obj) {
        return this.options == ((Options) obj).options;
    }

    public boolean getOption(int i) {
        return (this.options & i) != 0;
    }

    public abstract int getValidOptions();

    public int hashCode() {
        return this.options;
    }

    public void setOption(int i, boolean z) {
        int i2;
        if (z) {
            i2 = i | this.options;
        } else {
            i2 = (~i) & this.options;
        }
        this.options = i2;
    }

    public String toString() {
        String valueOf = String.valueOf(Integer.toHexString(this.options));
        return valueOf.length() != 0 ? "0x".concat(valueOf) : new String("0x");
    }

    public Options(int i) throws XMPException {
        this.options = 0;
        assertOptionsValid(i);
        assertOptionsValid(i);
        this.options = i;
    }
}
