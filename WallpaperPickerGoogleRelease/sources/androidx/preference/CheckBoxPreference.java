package androidx.preference;

import android.widget.CompoundButton;
import java.util.Objects;
/* loaded from: classes.dex */
public class CheckBoxPreference extends TwoStatePreference {
    public final Listener mListener = new Listener();

    /* loaded from: classes.dex */
    public class Listener implements CompoundButton.OnCheckedChangeListener {
        public Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Objects.requireNonNull(CheckBoxPreference.this);
            CheckBoxPreference.this.setChecked(z);
        }
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CheckBoxPreference(android.content.Context r4, android.util.AttributeSet r5) {
        /*
            r3 = this;
            r0 = 2130968715(0x7f04008b, float:1.7546091E38)
            r1 = 16842895(0x101008f, float:2.369396E-38)
            int r0 = androidx.core.content.res.TypedArrayUtils.getAttr(r4, r0, r1)
            r1 = 0
            r3.<init>(r4, r5, r0, r1)
            androidx.preference.CheckBoxPreference$Listener r2 = new androidx.preference.CheckBoxPreference$Listener
            r2.<init>()
            r3.mListener = r2
            int[] r2 = androidx.preference.R$styleable.CheckBoxPreference
            android.content.res.TypedArray r4 = r4.obtainStyledAttributes(r5, r2, r0, r1)
            r5 = 5
            java.lang.String r5 = androidx.core.content.res.TypedArrayUtils.getString(r4, r5, r1)
            r3.mSummaryOn = r5
            r5 = 4
            r0 = 1
            java.lang.String r5 = r4.getString(r5)
            if (r5 != 0) goto L_0x002e
            java.lang.String r5 = r4.getString(r0)
        L_0x002e:
            r3.mSummaryOff = r5
            r5 = 3
            r0 = 2
            boolean r0 = r4.getBoolean(r0, r1)
            boolean r5 = r4.getBoolean(r5, r0)
            r3.mDisableDependentsState = r5
            r4.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.preference.CheckBoxPreference.<init>(android.content.Context, android.util.AttributeSet):void");
    }
}
