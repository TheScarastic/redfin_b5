package androidx.preference;

import android.widget.CompoundButton;
import java.util.Objects;
/* loaded from: classes.dex */
public class SwitchPreference extends TwoStatePreference {
    public final Listener mListener = new Listener();
    public CharSequence mSwitchOff;
    public CharSequence mSwitchOn;

    /* loaded from: classes.dex */
    public class Listener implements CompoundButton.OnCheckedChangeListener {
        public Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Objects.requireNonNull(SwitchPreference.this);
            SwitchPreference.this.setChecked(z);
        }
    }

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public SwitchPreference(android.content.Context r4, android.util.AttributeSet r5) {
        /*
            r3 = this;
            r0 = 2130969437(0x7f04035d, float:1.7547556E38)
            r1 = 16843629(0x101036d, float:2.3696016E-38)
            int r0 = androidx.core.content.res.TypedArrayUtils.getAttr(r4, r0, r1)
            r1 = 0
            r3.<init>(r4, r5, r0, r1)
            androidx.preference.SwitchPreference$Listener r2 = new androidx.preference.SwitchPreference$Listener
            r2.<init>()
            r3.mListener = r2
            int[] r2 = androidx.preference.R$styleable.SwitchPreference
            android.content.res.TypedArray r4 = r4.obtainStyledAttributes(r5, r2, r0, r1)
            r5 = 7
            java.lang.String r5 = androidx.core.content.res.TypedArrayUtils.getString(r4, r5, r1)
            r3.mSummaryOn = r5
            r5 = 6
            r0 = 1
            java.lang.String r5 = r4.getString(r5)
            if (r5 != 0) goto L_0x002e
            java.lang.String r5 = r4.getString(r0)
        L_0x002e:
            r3.mSummaryOff = r5
            r5 = 9
            r0 = 3
            java.lang.String r5 = r4.getString(r5)
            if (r5 != 0) goto L_0x003d
            java.lang.String r5 = r4.getString(r0)
        L_0x003d:
            r3.mSwitchOn = r5
            r5 = 8
            r0 = 4
            java.lang.String r5 = r4.getString(r5)
            if (r5 != 0) goto L_0x004c
            java.lang.String r5 = r4.getString(r0)
        L_0x004c:
            r3.mSwitchOff = r5
            r5 = 5
            r0 = 2
            boolean r0 = r4.getBoolean(r0, r1)
            boolean r5 = r4.getBoolean(r5, r0)
            r3.mDisableDependentsState = r5
            r4.recycle()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.preference.SwitchPreference.<init>(android.content.Context, android.util.AttributeSet):void");
    }
}
