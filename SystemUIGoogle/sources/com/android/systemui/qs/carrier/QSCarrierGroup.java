package com.android.systemui.qs.carrier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
/* loaded from: classes.dex */
public class QSCarrierGroup extends LinearLayout {
    public QSCarrierGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public TextView getNoSimTextView() {
        return (TextView) findViewById(R$id.no_carrier_text);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier1View() {
        return (QSCarrier) findViewById(R$id.carrier1);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier2View() {
        return (QSCarrier) findViewById(R$id.carrier2);
    }

    /* access modifiers changed from: package-private */
    public QSCarrier getCarrier3View() {
        return (QSCarrier) findViewById(R$id.carrier3);
    }

    /* access modifiers changed from: package-private */
    public View getCarrierDivider1() {
        return findViewById(R$id.qs_carrier_divider1);
    }

    /* access modifiers changed from: package-private */
    public View getCarrierDivider2() {
        return findViewById(R$id.qs_carrier_divider2);
    }
}
