package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class DataUsageDetailView extends LinearLayout {
    private final DecimalFormat FORMAT = new DecimalFormat("#.##");

    public DataUsageDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int i = R$dimen.qs_data_usage_text_size;
        FontSizeUtils.updateFontSize(this, 16908310, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_text, R$dimen.qs_data_usage_usage_text_size);
        FontSizeUtils.updateFontSize(this, R$id.usage_carrier_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_top_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_period_text, i);
        FontSizeUtils.updateFontSize(this, R$id.usage_info_bottom_text, i);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x008b  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x00ed  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00ef  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0100  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0102  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0111  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void bind(com.android.settingslib.net.DataUsageController.DataUsageInfo r24) {
        /*
        // Method dump skipped, instructions count: 294
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.DataUsageDetailView.bind(com.android.settingslib.net.DataUsageController$DataUsageInfo):void");
    }

    private String formatBytes(long j) {
        String str;
        double d;
        double abs = (double) Math.abs(j);
        if (abs > 1.048576E8d) {
            d = abs / 1.073741824E9d;
            str = "GB";
        } else if (abs > 102400.0d) {
            d = abs / 1048576.0d;
            str = "MB";
        } else {
            d = abs / 1024.0d;
            str = "KB";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.FORMAT.format(d * ((double) (j < 0 ? -1 : 1))));
        sb.append(" ");
        sb.append(str);
        return sb.toString();
    }
}
