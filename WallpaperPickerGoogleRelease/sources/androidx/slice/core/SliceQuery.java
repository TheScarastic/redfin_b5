package androidx.slice.core;

import android.text.TextUtils;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
/* loaded from: classes.dex */
public class SliceQuery {
    public static boolean checkFormat(SliceItem sliceItem, String str) {
        return str == null || str.equals(sliceItem.mFormat);
    }

    public static boolean checkSubtype(SliceItem sliceItem, String str) {
        return str == null || str.equals(sliceItem.mSubType);
    }

    public static SliceItem find(Slice slice, String str, String str2, String str3) {
        return find(slice, str, new String[]{str2}, new String[]{null});
    }

    public static List<SliceItem> findAll(SliceItem sliceItem, String str, String[] strArr, String[] strArr2) {
        ArrayList arrayList = new ArrayList();
        Deque<SliceItem> queue = toQueue(sliceItem);
        while (true) {
            ArrayDeque arrayDeque = (ArrayDeque) queue;
            if (arrayDeque.isEmpty()) {
                return arrayList;
            }
            SliceItem sliceItem2 = (SliceItem) arrayDeque.poll();
            if (checkFormat(sliceItem2, str) && hasHints(sliceItem2, strArr) && !hasAnyHints(sliceItem2, strArr2)) {
                arrayList.add(sliceItem2);
            }
            if ("slice".equals(sliceItem2.mFormat) || "action".equals(sliceItem2.mFormat)) {
                Collections.addAll(queue, sliceItem2.getSlice().mItems);
            }
        }
    }

    public static SliceItem findSubtype(SliceItem sliceItem, String str, String str2) {
        if (sliceItem == null) {
            return null;
        }
        Deque<SliceItem> queue = toQueue(sliceItem);
        while (true) {
            ArrayDeque arrayDeque = (ArrayDeque) queue;
            if (arrayDeque.isEmpty()) {
                return null;
            }
            SliceItem sliceItem2 = (SliceItem) arrayDeque.poll();
            if (checkFormat(sliceItem2, str) && checkSubtype(sliceItem2, str2)) {
                return sliceItem2;
            }
            if ("slice".equals(sliceItem2.mFormat) || "action".equals(sliceItem2.mFormat)) {
                Collections.addAll(queue, sliceItem2.getSlice().mItems);
            }
        }
    }

    public static SliceItem findTopLevelItem(Slice slice, String str, String str2, String[] strArr, String[] strArr2) {
        SliceItem[] sliceItemArr = slice.mItems;
        for (SliceItem sliceItem : sliceItemArr) {
            if (checkFormat(sliceItem, str) && checkSubtype(sliceItem, str2) && hasHints(sliceItem, strArr) && !hasAnyHints(sliceItem, null)) {
                return sliceItem;
            }
        }
        return null;
    }

    public static boolean hasAnyHints(SliceItem sliceItem, String... strArr) {
        if (strArr == null) {
            return false;
        }
        for (String str : strArr) {
            if (sliceItem.hasHint(str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasHints(SliceItem sliceItem, String... strArr) {
        if (strArr == null) {
            return true;
        }
        for (String str : strArr) {
            if (!(TextUtils.isEmpty(str) || sliceItem.hasHint(str))) {
                return false;
            }
        }
        return true;
    }

    public static Deque<SliceItem> toQueue(SliceItem sliceItem) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(sliceItem);
        return arrayDeque;
    }

    public static SliceItem find(SliceItem sliceItem, String str) {
        return find(sliceItem, str, (String[]) null, (String[]) null);
    }

    public static SliceItem find(SliceItem sliceItem, String str, String str2, String str3) {
        return find(sliceItem, str, new String[]{str2}, new String[]{null});
    }

    public static SliceItem find(SliceItem sliceItem, String str, String[] strArr, String[] strArr2) {
        if (sliceItem == null) {
            return null;
        }
        Deque<SliceItem> queue = toQueue(sliceItem);
        while (!queue.isEmpty()) {
            SliceItem poll = queue.poll();
            if (checkFormat(poll, str) && hasHints(poll, strArr) && !hasAnyHints(poll, strArr2)) {
                return poll;
            }
            if ("slice".equals(poll.mFormat) || "action".equals(poll.mFormat)) {
                Collections.addAll(queue, poll.getSlice().mItems);
            }
        }
        return null;
    }

    public static SliceItem findSubtype(Slice slice, String str, String str2) {
        if (slice == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        Collections.addAll(arrayDeque, slice.mItems);
        while (!arrayDeque.isEmpty()) {
            SliceItem sliceItem = (SliceItem) arrayDeque.poll();
            if (checkFormat(sliceItem, str) && checkSubtype(sliceItem, str2)) {
                return sliceItem;
            }
            if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
                Collections.addAll(arrayDeque, sliceItem.getSlice().mItems);
            }
        }
        return null;
    }

    public static SliceItem find(Slice slice, String str, String[] strArr, String[] strArr2) {
        if (slice == null) {
            return null;
        }
        ArrayDeque arrayDeque = new ArrayDeque();
        Collections.addAll(arrayDeque, slice.mItems);
        while (!arrayDeque.isEmpty()) {
            SliceItem sliceItem = (SliceItem) arrayDeque.poll();
            if (checkFormat(sliceItem, str) && hasHints(sliceItem, strArr) && !hasAnyHints(sliceItem, strArr2)) {
                return sliceItem;
            }
            if ("slice".equals(sliceItem.mFormat) || "action".equals(sliceItem.mFormat)) {
                Collections.addAll(arrayDeque, sliceItem.getSlice().mItems);
            }
        }
        return null;
    }
}
