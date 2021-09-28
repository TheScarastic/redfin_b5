package com.google.android.systemui.power;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.util.KeyValueListParser;
import android.util.Log;
import com.android.settingslib.fuelgauge.Estimate;
import com.android.settingslib.utils.PowerUtil;
import com.android.systemui.power.EnhancedEstimates;
import java.time.Duration;
/* loaded from: classes2.dex */
public class EnhancedEstimatesGoogleImpl implements EnhancedEstimates {
    private Context mContext;
    private final KeyValueListParser mParser = new KeyValueListParser(',');

    public EnhancedEstimatesGoogleImpl(Context context) {
        this.mContext = context;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public boolean isHybridNotificationEnabled() {
        try {
            if (!this.mContext.getPackageManager().getPackageInfo("com.google.android.apps.turbo", 512).applicationInfo.enabled) {
                return false;
            }
            updateFlags();
            return this.mParser.getBoolean("hybrid_enabled", true);
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public Estimate getEstimate() {
        Cursor query;
        try {
            query = this.mContext.getContentResolver().query(new Uri.Builder().scheme("content").authority("com.google.android.apps.turbo.estimated_time_remaining").appendPath("time_remaining").build(), null, null, null, null);
        } catch (Exception e) {
            Log.d("EnhancedEstimates", "Something went wrong when getting an estimate from Turbo", e);
        }
        if (query == null || !query.moveToFirst()) {
            if (query != null) {
                query.close();
            }
            return new Estimate(-1, false, -1);
        }
        boolean z = true;
        if (query.getColumnIndex("is_based_on_usage") != -1 && query.getInt(query.getColumnIndex("is_based_on_usage")) == 0) {
            z = false;
        }
        int columnIndex = query.getColumnIndex("average_battery_life");
        long j = -1;
        if (columnIndex != -1) {
            long j2 = query.getLong(columnIndex);
            if (j2 != -1) {
                long millis = Duration.ofMinutes(15).toMillis();
                if (Duration.ofMillis(j2).compareTo(Duration.ofDays(1)) >= 0) {
                    millis = Duration.ofHours(1).toMillis();
                }
                j = PowerUtil.roundTimeToNearestThreshold(j2, millis);
            }
        }
        Estimate estimate = new Estimate(query.getLong(query.getColumnIndex("battery_estimate")), z, j);
        query.close();
        return estimate;
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public long getLowWarningThreshold() {
        updateFlags();
        return this.mParser.getLong("low_threshold", Duration.ofHours(3).toMillis());
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public long getSevereWarningThreshold() {
        updateFlags();
        return this.mParser.getLong("severe_threshold", Duration.ofHours(1).toMillis());
    }

    @Override // com.android.systemui.power.EnhancedEstimates
    public boolean getLowWarningEnabled() {
        updateFlags();
        return this.mParser.getBoolean("low_warning_enabled", false);
    }

    protected void updateFlags() {
        try {
            this.mParser.setString(Settings.Global.getString(this.mContext.getContentResolver(), "hybrid_sysui_battery_warning_flags"));
        } catch (IllegalArgumentException unused) {
            Log.e("EnhancedEstimates", "Bad hybrid sysui warning flags");
        }
    }
}
