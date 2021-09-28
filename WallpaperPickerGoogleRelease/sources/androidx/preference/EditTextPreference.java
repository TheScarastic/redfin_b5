package androidx.preference;

import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.preference.Preference;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class EditTextPreference extends DialogPreference {

    /* loaded from: classes.dex */
    public static class SavedState extends Preference.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.preference.EditTextPreference.SavedState.1
            /* Return type fixed from 'java.lang.Object' to match base method */
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* Return type fixed from 'java.lang.Object[]' to match base method */
            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public String mText;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mText = parcel.readString();
        }

        @Override // android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.mText);
        }
    }

    /* loaded from: classes.dex */
    public static final class SimpleSummaryProvider implements Preference.SummaryProvider<EditTextPreference> {
        public static SimpleSummaryProvider sSimpleSummaryProvider;

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [androidx.preference.Preference] */
        @Override // androidx.preference.Preference.SummaryProvider
        public CharSequence provideSummary(EditTextPreference editTextPreference) {
            EditTextPreference editTextPreference2 = editTextPreference;
            if (TextUtils.isEmpty(null)) {
                return editTextPreference2.mContext.getString(R.string.not_set);
            }
            return null;
        }
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public EditTextPreference(android.content.Context r4, android.util.AttributeSet r5) {
        /*
            r3 = this;
            r0 = 2130968894(0x7f04013e, float:1.7546455E38)
            r1 = 16842898(0x1010092, float:2.3693967E-38)
            int r0 = androidx.core.content.res.TypedArrayUtils.getAttr(r4, r0, r1)
            r1 = 0
            r3.<init>(r4, r5, r0, r1)
            int[] r2 = androidx.preference.R$styleable.EditTextPreference
            android.content.res.TypedArray r4 = r4.obtainStyledAttributes(r5, r2, r0, r1)
            boolean r5 = androidx.core.content.res.TypedArrayUtils.getBoolean(r4, r1, r1, r1)
            if (r5 == 0) goto L_0x0029
            androidx.preference.EditTextPreference$SimpleSummaryProvider r5 = androidx.preference.EditTextPreference.SimpleSummaryProvider.sSimpleSummaryProvider
            if (r5 != 0) goto L_0x0025
            androidx.preference.EditTextPreference$SimpleSummaryProvider r5 = new androidx.preference.EditTextPreference$SimpleSummaryProvider
            r5.<init>()
            androidx.preference.EditTextPreference.SimpleSummaryProvider.sSimpleSummaryProvider = r5
        L_0x0025:
            androidx.preference.EditTextPreference$SimpleSummaryProvider r5 = androidx.preference.EditTextPreference.SimpleSummaryProvider.sSimpleSummaryProvider
            r3.mSummaryProvider = r5
        L_0x0029:
            r4.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.preference.EditTextPreference.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    @Override // androidx.preference.Preference
    public Object onGetDefaultValue(TypedArray typedArray, int i) {
        return typedArray.getString(i);
    }
}
