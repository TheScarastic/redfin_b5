package androidx.slice;

import android.net.Uri;
/* loaded from: classes.dex */
public class SliceStructure {
    public final String mStructure;
    public final Uri mUri;

    public SliceStructure(Slice slice) {
        StringBuilder sb = new StringBuilder();
        getStructure(slice, sb);
        this.mStructure = sb.toString();
        this.mUri = slice.getUri();
    }

    public static void getStructure(Slice slice, StringBuilder sb) {
        sb.append("s{");
        for (SliceItem sliceItem : slice.getItems()) {
            getStructure(sliceItem, sb);
        }
        sb.append("}");
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SliceStructure)) {
            return false;
        }
        return this.mStructure.equals(((SliceStructure) obj).mStructure);
    }

    public int hashCode() {
        return this.mStructure.hashCode();
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public static void getStructure(SliceItem sliceItem, StringBuilder sb) {
        char c;
        String str = sliceItem.mFormat;
        switch (str.hashCode()) {
            case -1422950858:
                if (str.equals("action")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case -1377881982:
                if (str.equals("bundle")) {
                    c = 7;
                    break;
                }
                c = 65535;
                break;
            case 104431:
                if (str.equals("int")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            case 3327612:
                if (str.equals("long")) {
                    c = 5;
                    break;
                }
                c = 65535;
                break;
            case 3556653:
                if (str.equals("text")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 100313435:
                if (str.equals("image")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 100358090:
                if (str.equals("input")) {
                    c = 6;
                    break;
                }
                c = 65535;
                break;
            case 109526418:
                if (str.equals("slice")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            getStructure(sliceItem.getSlice(), sb);
        } else if (c == 1) {
            sb.append('a');
            if ("range".equals(sliceItem.mSubType)) {
                sb.append('r');
            }
            getStructure(sliceItem.getSlice(), sb);
        } else if (c == 2) {
            sb.append('t');
        } else if (c == 3) {
            sb.append('i');
        }
    }

    public SliceStructure(SliceItem sliceItem) {
        StringBuilder sb = new StringBuilder();
        getStructure(sliceItem, sb);
        this.mStructure = sb.toString();
        if ("action".equals(sliceItem.mFormat) || "slice".equals(sliceItem.mFormat)) {
            this.mUri = sliceItem.getSlice().getUri();
        } else {
            this.mUri = null;
        }
    }
}
