package androidx.transition;

import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.view.View;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class TransitionValues {
    public View view;
    public final Map<String, Object> values = new HashMap();
    public final ArrayList<Transition> mTargetedTransitions = new ArrayList<>();

    @Deprecated
    public TransitionValues() {
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof TransitionValues)) {
            return false;
        }
        TransitionValues transitionValues = (TransitionValues) obj;
        return this.view == transitionValues.view && this.values.equals(transitionValues.values);
    }

    public int hashCode() {
        return this.values.hashCode() + (this.view.hashCode() * 31);
    }

    public String toString() {
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("TransitionValues@");
        m.append(Integer.toHexString(hashCode()));
        m.append(":\n");
        String m2 = SupportMenuInflater$$ExternalSyntheticOutline0.m(m.toString() + "    view = " + this.view + "\n", "    values:");
        for (String str : this.values.keySet()) {
            m2 = m2 + "    " + str + ": " + this.values.get(str) + "\n";
        }
        return m2;
    }

    public TransitionValues(View view) {
        this.view = view;
    }
}
