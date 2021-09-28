package com.android.wifitrackerlib;

import android.net.wifi.ScanResult;
import android.util.Pair;
import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public class ScanResultUpdater {
    private final Clock mClock;
    private final long mMaxScanAgeMillis;
    private Map<Pair<String, String>, ScanResult> mScanResultsBySsidAndBssid = new HashMap();
    private final Object mLock = new Object();

    public ScanResultUpdater(Clock clock, long j) {
        this.mMaxScanAgeMillis = j;
        this.mClock = clock;
    }

    public void update(List<ScanResult> list) {
        synchronized (this.mLock) {
            evictOldScans();
            for (ScanResult scanResult : list) {
                Pair<String, String> pair = new Pair<>(scanResult.SSID, scanResult.BSSID);
                ScanResult scanResult2 = this.mScanResultsBySsidAndBssid.get(pair);
                if (scanResult2 == null || scanResult2.timestamp < scanResult.timestamp) {
                    this.mScanResultsBySsidAndBssid.put(pair, scanResult);
                }
            }
        }
    }

    public List<ScanResult> getScanResults() {
        return getScanResults(this.mMaxScanAgeMillis);
    }

    public List<ScanResult> getScanResults(long j) throws IllegalArgumentException {
        ArrayList arrayList;
        if (j <= this.mMaxScanAgeMillis) {
            synchronized (this.mLock) {
                arrayList = new ArrayList();
                for (ScanResult scanResult : this.mScanResultsBySsidAndBssid.values()) {
                    if (this.mClock.millis() - (scanResult.timestamp / 1000) <= j) {
                        arrayList.add(scanResult);
                    }
                }
            }
            return arrayList;
        }
        throw new IllegalArgumentException("maxScanAgeMillis argument cannot be greater than mMaxScanAgeMillis!");
    }

    private void evictOldScans() {
        synchronized (this.mLock) {
            this.mScanResultsBySsidAndBssid.entrySet().removeIf(new Predicate() { // from class: com.android.wifitrackerlib.ScanResultUpdater$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return ScanResultUpdater.this.lambda$evictOldScans$0((Map.Entry) obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$evictOldScans$0(Map.Entry entry) {
        return this.mClock.millis() - (((ScanResult) entry.getValue()).timestamp / 1000) > this.mMaxScanAgeMillis;
    }
}
