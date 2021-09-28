package com.android.systemui.controls.ui;

import com.android.systemui.R$color;
import com.android.systemui.R$drawable;
import java.util.Map;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapWithDefaultKt;
import kotlin.collections.MapsKt__MapsKt;
/* compiled from: RenderInfo.kt */
/* loaded from: classes.dex */
public final class RenderInfoKt {
    private static final Map<Integer, Pair<Integer, Integer>> deviceColorMap = MapsKt__MapWithDefaultKt.withDefault(MapsKt__MapsKt.mapOf(TuplesKt.to(49001, new Pair(Integer.valueOf(R$color.control_default_foreground), Integer.valueOf(R$color.control_default_background))), TuplesKt.to(49002, new Pair(Integer.valueOf(R$color.thermo_heat_foreground), Integer.valueOf(R$color.control_enabled_thermo_heat_background))), TuplesKt.to(49003, new Pair(Integer.valueOf(R$color.thermo_cool_foreground), Integer.valueOf(R$color.control_enabled_thermo_cool_background))), TuplesKt.to(13, new Pair(Integer.valueOf(R$color.light_foreground), Integer.valueOf(R$color.control_enabled_light_background))), TuplesKt.to(50, new Pair(Integer.valueOf(R$color.camera_foreground), Integer.valueOf(R$color.control_enabled_default_background)))), RenderInfoKt$deviceColorMap$1.INSTANCE);
    private static final Map<Integer, Integer> deviceIconMap;

    static {
        int i = R$drawable.ic_device_thermostat_off;
        int i2 = R$drawable.ic_device_thermostat;
        int i3 = R$drawable.ic_device_air_freshener;
        int i4 = R$drawable.ic_device_kettle;
        int i5 = R$drawable.ic_device_washer;
        int i6 = R$drawable.ic_device_blinds;
        int i7 = R$drawable.ic_device_drawer;
        int i8 = R$drawable.ic_device_pergola;
        int i9 = R$drawable.ic_device_window;
        deviceIconMap = MapsKt__MapWithDefaultKt.withDefault(MapsKt__MapsKt.mapOf(TuplesKt.to(49001, Integer.valueOf(i)), TuplesKt.to(49002, Integer.valueOf(i2)), TuplesKt.to(49003, Integer.valueOf(i2)), TuplesKt.to(49004, Integer.valueOf(i2)), TuplesKt.to(49005, Integer.valueOf(i)), TuplesKt.to(49, Integer.valueOf(i2)), TuplesKt.to(13, Integer.valueOf(R$drawable.ic_device_light)), TuplesKt.to(50, Integer.valueOf(R$drawable.ic_device_camera)), TuplesKt.to(45, Integer.valueOf(R$drawable.ic_device_lock)), TuplesKt.to(21, Integer.valueOf(R$drawable.ic_device_switch)), TuplesKt.to(15, Integer.valueOf(R$drawable.ic_device_outlet)), TuplesKt.to(32, Integer.valueOf(R$drawable.ic_device_vacuum)), TuplesKt.to(26, Integer.valueOf(R$drawable.ic_device_mop)), TuplesKt.to(3, Integer.valueOf(i3)), TuplesKt.to(4, Integer.valueOf(R$drawable.ic_device_air_purifier)), TuplesKt.to(8, Integer.valueOf(R$drawable.ic_device_fan)), TuplesKt.to(10, Integer.valueOf(R$drawable.ic_device_hood)), TuplesKt.to(12, Integer.valueOf(i4)), TuplesKt.to(14, Integer.valueOf(R$drawable.ic_device_microwave)), TuplesKt.to(17, Integer.valueOf(R$drawable.ic_device_remote_control)), TuplesKt.to(18, Integer.valueOf(R$drawable.ic_device_set_top)), TuplesKt.to(20, Integer.valueOf(R$drawable.ic_device_styler)), TuplesKt.to(22, Integer.valueOf(R$drawable.ic_device_tv)), TuplesKt.to(23, Integer.valueOf(R$drawable.ic_device_water_heater)), TuplesKt.to(24, Integer.valueOf(R$drawable.ic_device_dishwasher)), TuplesKt.to(28, Integer.valueOf(R$drawable.ic_device_multicooker)), TuplesKt.to(30, Integer.valueOf(R$drawable.ic_device_sprinkler)), TuplesKt.to(31, Integer.valueOf(i5)), TuplesKt.to(34, Integer.valueOf(i6)), TuplesKt.to(38, Integer.valueOf(i7)), TuplesKt.to(39, Integer.valueOf(R$drawable.ic_device_garage)), TuplesKt.to(40, Integer.valueOf(R$drawable.ic_device_gate)), TuplesKt.to(41, Integer.valueOf(i8)), TuplesKt.to(43, Integer.valueOf(i9)), TuplesKt.to(44, Integer.valueOf(R$drawable.ic_device_valve)), TuplesKt.to(46, Integer.valueOf(R$drawable.ic_device_security_system)), TuplesKt.to(48, Integer.valueOf(R$drawable.ic_device_refrigerator)), TuplesKt.to(51, Integer.valueOf(R$drawable.ic_device_doorbell)), TuplesKt.to(52, -1), TuplesKt.to(1, Integer.valueOf(i2)), TuplesKt.to(2, Integer.valueOf(i2)), TuplesKt.to(5, Integer.valueOf(i4)), TuplesKt.to(6, Integer.valueOf(i3)), TuplesKt.to(16, Integer.valueOf(i2)), TuplesKt.to(19, Integer.valueOf(R$drawable.ic_device_cooking)), TuplesKt.to(7, Integer.valueOf(R$drawable.ic_device_display)), TuplesKt.to(25, Integer.valueOf(i5)), TuplesKt.to(27, Integer.valueOf(R$drawable.ic_device_outdoor_garden)), TuplesKt.to(29, Integer.valueOf(R$drawable.ic_device_water)), TuplesKt.to(33, Integer.valueOf(i8)), TuplesKt.to(35, Integer.valueOf(i7)), TuplesKt.to(36, Integer.valueOf(i6)), TuplesKt.to(37, Integer.valueOf(R$drawable.ic_device_door)), TuplesKt.to(42, Integer.valueOf(i9)), TuplesKt.to(47, Integer.valueOf(i2)), TuplesKt.to(-1000, Integer.valueOf(R$drawable.ic_error_outline))), RenderInfoKt$deviceIconMap$1.INSTANCE);
    }
}
