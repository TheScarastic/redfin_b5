package com.android.settingslib.datetime;

import com.android.i18n.timezone.CountryTimeZones;
import com.android.i18n.timezone.TimeZoneFinder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: classes.dex */
public final class ZoneGetter$ZoneGetterData {
    public List<String> lookupTimeZoneIdsByCountry(String str) {
        CountryTimeZones lookupCountryTimeZones = TimeZoneFinder.getInstance().lookupCountryTimeZones(str);
        if (lookupCountryTimeZones == null) {
            return null;
        }
        return extractTimeZoneIds(lookupCountryTimeZones.getTimeZoneMappings());
    }

    private static List<String> extractTimeZoneIds(List<CountryTimeZones.TimeZoneMapping> list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (CountryTimeZones.TimeZoneMapping timeZoneMapping : list) {
            arrayList.add(timeZoneMapping.getTimeZoneId());
        }
        return Collections.unmodifiableList(arrayList);
    }
}
