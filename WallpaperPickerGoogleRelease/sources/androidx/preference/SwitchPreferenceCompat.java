package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import androidx.core.content.res.TypedArrayUtils;
import com.android.systemui.shared.R;
import java.util.Objects;
/* loaded from: classes.dex */
public class SwitchPreferenceCompat extends TwoStatePreference {
    public final Listener mListener = new Listener();
    public CharSequence mSwitchOff;
    public CharSequence mSwitchOn;

    /* loaded from: classes.dex */
    public class Listener implements CompoundButton.OnCheckedChangeListener {
        public Listener() {
        }

        @Override // android.widget.CompoundButton.OnCheckedChangeListener
        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Objects.requireNonNull(SwitchPreferenceCompat.this);
            SwitchPreferenceCompat.this.setChecked(z);
        }
    }

    public SwitchPreferenceCompat(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, R.attr.switchPreferenceCompatStyle, 0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SwitchPreferenceCompat, R.attr.switchPreferenceCompatStyle, 0);
        this.mSummaryOn = TypedArrayUtils.getString(obtainStyledAttributes, 7, 0);
        String string = obtainStyledAttributes.getString(6);
        this.mSummaryOff = string == null ? obtainStyledAttributes.getString(1) : string;
        String string2 = obtainStyledAttributes.getString(9);
        this.mSwitchOn = string2 == null ? obtainStyledAttributes.getString(3) : string2;
        String string3 = obtainStyledAttributes.getString(8);
        this.mSwitchOff = string3 == null ? obtainStyledAttributes.getString(4) : string3;
        this.mDisableDependentsState = obtainStyledAttributes.getBoolean(5, obtainStyledAttributes.getBoolean(2, false));
        obtainStyledAttributes.recycle();
    }
}
