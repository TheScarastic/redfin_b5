package com.android.systemui.qs.tileimpl;

import com.android.systemui.R$array;
import java.util.Map;
import kotlin.TuplesKt;
import kotlin.collections.MapsKt__MapsKt;
/* compiled from: QSTileViewImpl.kt */
/* loaded from: classes.dex */
public final class SubtitleArrayMapping {
    public static final SubtitleArrayMapping INSTANCE = new SubtitleArrayMapping();
    private static final Map<String, Integer> subtitleIdsMap = MapsKt__MapsKt.mapOf(TuplesKt.to("internet", Integer.valueOf(R$array.tile_states_internet)), TuplesKt.to("wifi", Integer.valueOf(R$array.tile_states_wifi)), TuplesKt.to("cell", Integer.valueOf(R$array.tile_states_cell)), TuplesKt.to("battery", Integer.valueOf(R$array.tile_states_battery)), TuplesKt.to("dnd", Integer.valueOf(R$array.tile_states_dnd)), TuplesKt.to("flashlight", Integer.valueOf(R$array.tile_states_flashlight)), TuplesKt.to("rotation", Integer.valueOf(R$array.tile_states_rotation)), TuplesKt.to("bt", Integer.valueOf(R$array.tile_states_bt)), TuplesKt.to("airplane", Integer.valueOf(R$array.tile_states_airplane)), TuplesKt.to("location", Integer.valueOf(R$array.tile_states_location)), TuplesKt.to("hotspot", Integer.valueOf(R$array.tile_states_hotspot)), TuplesKt.to("inversion", Integer.valueOf(R$array.tile_states_inversion)), TuplesKt.to("saver", Integer.valueOf(R$array.tile_states_saver)), TuplesKt.to("dark", Integer.valueOf(R$array.tile_states_dark)), TuplesKt.to("work", Integer.valueOf(R$array.tile_states_work)), TuplesKt.to("cast", Integer.valueOf(R$array.tile_states_cast)), TuplesKt.to("night", Integer.valueOf(R$array.tile_states_night)), TuplesKt.to("screenrecord", Integer.valueOf(R$array.tile_states_screenrecord)), TuplesKt.to("reverse", Integer.valueOf(R$array.tile_states_reverse)), TuplesKt.to("reduce_brightness", Integer.valueOf(R$array.tile_states_reduce_brightness)), TuplesKt.to("cameratoggle", Integer.valueOf(R$array.tile_states_cameratoggle)), TuplesKt.to("mictoggle", Integer.valueOf(R$array.tile_states_mictoggle)), TuplesKt.to("controls", Integer.valueOf(R$array.tile_states_controls)), TuplesKt.to("wallet", Integer.valueOf(R$array.tile_states_wallet)), TuplesKt.to("alarm", Integer.valueOf(R$array.tile_states_alarm)));

    private SubtitleArrayMapping() {
    }

    public final int getSubtitleId(String str) {
        return subtitleIdsMap.getOrDefault(str, Integer.valueOf(R$array.tile_states_default)).intValue();
    }
}
