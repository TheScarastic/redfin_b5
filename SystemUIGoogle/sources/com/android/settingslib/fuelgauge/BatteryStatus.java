package com.android.settingslib.fuelgauge;

import android.content.Context;
import android.content.Intent;
import com.android.settingslib.R$integer;
/* loaded from: classes.dex */
public class BatteryStatus {
    public final int health;
    public final int level;
    public final int maxChargingWattage;
    public final int plugged;
    public final boolean present;
    public final int status;

    public BatteryStatus(int i, int i2, int i3, int i4, int i5, boolean z) {
        this.status = i;
        this.level = i2;
        this.plugged = i3;
        this.health = i4;
        this.maxChargingWattage = i5;
        this.present = z;
    }

    public BatteryStatus(Intent intent) {
        this.status = intent.getIntExtra("status", 1);
        this.plugged = intent.getIntExtra("plugged", 0);
        this.level = intent.getIntExtra("level", 0);
        this.health = intent.getIntExtra("health", 1);
        this.present = intent.getBooleanExtra("present", true);
        int intExtra = intent.getIntExtra("max_charging_current", -1);
        int intExtra2 = intent.getIntExtra("max_charging_voltage", -1);
        intExtra2 = intExtra2 <= 0 ? 5000000 : intExtra2;
        if (intExtra > 0) {
            this.maxChargingWattage = (intExtra / 1000) * (intExtra2 / 1000);
        } else {
            this.maxChargingWattage = -1;
        }
    }

    public boolean isPluggedIn() {
        int i = this.plugged;
        return i == 1 || i == 2 || i == 4;
    }

    public boolean isPluggedInWired() {
        int i = this.plugged;
        return i == 1 || i == 2;
    }

    public boolean isCharged() {
        return this.status == 5 || this.level >= 100;
    }

    public boolean isOverheated() {
        return this.health == 3;
    }

    public final int getChargingSpeed(Context context) {
        int integer = context.getResources().getInteger(R$integer.config_chargingSlowlyThreshold);
        int integer2 = context.getResources().getInteger(R$integer.config_chargingFastThreshold);
        int i = this.maxChargingWattage;
        if (i <= 0) {
            return -1;
        }
        if (i < integer) {
            return 0;
        }
        return i > integer2 ? 2 : 1;
    }

    public String toString() {
        return "BatteryStatus{status=" + this.status + ",level=" + this.level + ",plugged=" + this.plugged + ",health=" + this.health + ",maxChargingWattage=" + this.maxChargingWattage + "}";
    }
}
