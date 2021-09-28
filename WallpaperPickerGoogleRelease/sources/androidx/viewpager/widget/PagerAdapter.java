package androidx.viewpager.widget;

import android.database.DataSetObservable;
/* loaded from: classes.dex */
public abstract class PagerAdapter {
    public final DataSetObservable mObservable = new DataSetObservable();

    public abstract int getCount();
}
