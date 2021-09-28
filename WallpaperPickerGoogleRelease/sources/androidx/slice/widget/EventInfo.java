package androidx.slice.widget;

import android.support.media.ExifInterface$$ExternalSyntheticOutline0;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.slice.core.SliceAction;
import java.util.Comparator;
/* loaded from: classes.dex */
public class EventInfo {
    public int actionType;
    public int rowIndex;
    public int rowTemplateType;
    public int sliceMode;
    public int actionPosition = -1;
    public int actionIndex = -1;
    public int actionCount = -1;
    public int state = -1;

    public EventInfo(int i, int i2, int i3, int i4) {
        this.sliceMode = i;
        this.actionType = i2;
        this.rowTemplateType = i3;
        this.rowIndex = i4;
    }

    public void setPosition(int i, int i2, int i3) {
        this.actionPosition = i;
        this.actionIndex = i2;
        this.actionCount = i3;
    }

    public String toString() {
        String str;
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("mode=");
        int i = this.sliceMode;
        Comparator<SliceAction> comparator = SliceView.SLICE_ACTION_PRIORITY_COMPARATOR;
        m.append(i != 1 ? i != 2 ? i != 3 ? ExifInterface$$ExternalSyntheticOutline0.m("unknown mode: ", i) : "MODE SHORTCUT" : "MODE LARGE" : "MODE SMALL");
        m.append(", actionType=");
        int i2 = this.actionType;
        String str2 = "TIME_PICK";
        switch (i2) {
            case 0:
                str = "TOGGLE";
                break;
            case 1:
                str = "BUTTON";
                break;
            case 2:
                str = "SLIDER";
                break;
            case 3:
                str = "CONTENT";
                break;
            case 4:
                str = "SEE MORE";
                break;
            case 5:
                str = "SELECTION";
                break;
            case 6:
                str = "DATE_PICK";
                break;
            case 7:
                str = str2;
                break;
            default:
                str = ExifInterface$$ExternalSyntheticOutline0.m("unknown action: ", i2);
                break;
        }
        m.append(str);
        m.append(", rowTemplateType=");
        int i3 = this.rowTemplateType;
        switch (i3) {
            case -1:
                str2 = "SHORTCUT";
                break;
            case 0:
                str2 = "LIST";
                break;
            case 1:
                str2 = "GRID";
                break;
            case 2:
                str2 = "MESSAGING";
                break;
            case 3:
                str2 = "TOGGLE";
                break;
            case 4:
                str2 = "SLIDER";
                break;
            case 5:
                str2 = "PROGRESS";
                break;
            case 6:
                str2 = "SELECTION";
                break;
            case 7:
                str2 = "DATE_PICK";
                break;
            case 8:
                break;
            default:
                str2 = ExifInterface$$ExternalSyntheticOutline0.m("unknown row type: ", i3);
                break;
        }
        m.append(str2);
        m.append(", rowIndex=");
        m.append(this.rowIndex);
        m.append(", actionPosition=");
        int i4 = this.actionPosition;
        m.append(i4 != 0 ? i4 != 1 ? i4 != 2 ? ExifInterface$$ExternalSyntheticOutline0.m("unknown position: ", i4) : "CELL" : "END" : "START");
        m.append(", actionIndex=");
        m.append(this.actionIndex);
        m.append(", actionCount=");
        m.append(this.actionCount);
        m.append(", state=");
        m.append(this.state);
        return m.toString();
    }
}
