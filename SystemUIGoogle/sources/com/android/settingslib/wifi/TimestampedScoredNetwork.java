package com.android.settingslib.wifi;

import android.net.ScoredNetwork;
import android.os.Parcel;
import android.os.Parcelable;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class TimestampedScoredNetwork implements Parcelable {
    public static final Parcelable.Creator<TimestampedScoredNetwork> CREATOR = new Parcelable.Creator<TimestampedScoredNetwork>() { // from class: com.android.settingslib.wifi.TimestampedScoredNetwork.1
        @Override // android.os.Parcelable.Creator
        public TimestampedScoredNetwork createFromParcel(Parcel parcel) {
            return new TimestampedScoredNetwork(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public TimestampedScoredNetwork[] newArray(int i) {
            return new TimestampedScoredNetwork[i];
        }
    };
    private ScoredNetwork mScore;
    private long mUpdatedTimestampMillis;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public TimestampedScoredNetwork(ScoredNetwork scoredNetwork, long j) {
        this.mScore = scoredNetwork;
        this.mUpdatedTimestampMillis = j;
    }

    protected TimestampedScoredNetwork(Parcel parcel) {
        this.mScore = parcel.readParcelable(ScoredNetwork.class.getClassLoader());
        this.mUpdatedTimestampMillis = parcel.readLong();
    }

    public void update(ScoredNetwork scoredNetwork, long j) {
        this.mScore = scoredNetwork;
        this.mUpdatedTimestampMillis = j;
    }

    public ScoredNetwork getScore() {
        return this.mScore;
    }

    public long getUpdatedTimestampMillis() {
        return this.mUpdatedTimestampMillis;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.mScore, i);
        parcel.writeLong(this.mUpdatedTimestampMillis);
    }
}
