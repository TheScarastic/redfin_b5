package androidx.slice;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import androidx.core.util.Pair;
import androidx.slice.compat.SliceProviderCompat$2;
import androidx.versionedparcelable.VersionedParcelable;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class SliceItemHolder implements VersionedParcelable {
    public static HolderHandler sHandler;
    public static final Object sSerializeLock = new Object();
    public Bundle mBundle;
    public int mInt;
    public long mLong;
    public Parcelable mParcelable;
    public SliceItemPool mPool;
    public String mStr;
    public VersionedParcelable mVersionedParcelable;

    /* loaded from: classes.dex */
    public interface HolderHandler {
    }

    /* loaded from: classes.dex */
    public static class SliceItemPool {
        public final ArrayList<SliceItemHolder> mCached = new ArrayList<>();
    }

    public SliceItemHolder(SliceItemPool sliceItemPool) {
        this.mVersionedParcelable = null;
        this.mParcelable = null;
        this.mStr = null;
        this.mInt = 0;
        this.mLong = 0;
        this.mBundle = null;
        this.mPool = sliceItemPool;
    }

    public SliceItemHolder(String str, Object obj, boolean z) {
        String str2;
        this.mVersionedParcelable = null;
        this.mParcelable = null;
        this.mStr = null;
        this.mInt = 0;
        this.mLong = 0;
        this.mBundle = null;
        Objects.requireNonNull(str);
        char c = 65535;
        switch (str.hashCode()) {
            case -1422950858:
                if (str.equals("action")) {
                    c = 0;
                    break;
                }
                break;
            case -1377881982:
                if (str.equals("bundle")) {
                    c = 1;
                    break;
                }
                break;
            case 104431:
                if (str.equals("int")) {
                    c = 2;
                    break;
                }
                break;
            case 3327612:
                if (str.equals("long")) {
                    c = 3;
                    break;
                }
                break;
            case 3556653:
                if (str.equals("text")) {
                    c = 4;
                    break;
                }
                break;
            case 100313435:
                if (str.equals("image")) {
                    c = 5;
                    break;
                }
                break;
            case 100358090:
                if (str.equals("input")) {
                    c = 6;
                    break;
                }
                break;
            case 109526418:
                if (str.equals("slice")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                Pair pair = (Pair) obj;
                F f = pair.first;
                if (f instanceof PendingIntent) {
                    this.mParcelable = (Parcelable) f;
                } else if (!z) {
                    throw new IllegalArgumentException("Cannot write callback to parcel");
                }
                this.mVersionedParcelable = (VersionedParcelable) pair.second;
                break;
            case 1:
                this.mBundle = (Bundle) obj;
                break;
            case 2:
                this.mInt = ((Integer) obj).intValue();
                break;
            case 3:
                this.mLong = ((Long) obj).longValue();
                break;
            case 4:
                if (obj instanceof Spanned) {
                    str2 = Html.toHtml((Spanned) obj, 0);
                } else {
                    str2 = (String) obj;
                }
                this.mStr = str2;
                break;
            case 5:
            case 7:
                this.mVersionedParcelable = (VersionedParcelable) obj;
                break;
            case 6:
                this.mParcelable = (Parcelable) obj;
                break;
        }
        HolderHandler holderHandler = sHandler;
        if (holderHandler != null) {
            ((SliceProviderCompat$2) holderHandler).handle(this, str);
        }
    }
}
