package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes2.dex */
public class EntitiesData implements Parcelable {
    public static final Parcelable.Creator<EntitiesData> CREATOR = new Parcelable.Creator<EntitiesData>() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.EntitiesData.1
        @Override // android.os.Parcelable.Creator
        public EntitiesData createFromParcel(Parcel parcel) {
            return EntitiesData.read(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public EntitiesData[] newArray(int i) {
            return new EntitiesData[i];
        }
    };
    private final Map<String, Bitmap> bitmapMap;
    private final SuggestParcelables$Entities entities;
    private final Map<String, PendingIntent> pendingIntentMap;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public static EntitiesData create(SuggestParcelables$Entities suggestParcelables$Entities, Map<String, Bitmap> map, Map<String, PendingIntent> map2) {
        return new EntitiesData(suggestParcelables$Entities, map, map2);
    }

    public static EntitiesData read(Parcel parcel) {
        SuggestParcelables$Entities create = SuggestParcelables$Entities.create(parcel.readBundle());
        HashMap hashMap = new HashMap();
        if (create.getExtrasInfo() != null && create.getExtrasInfo().getContainsBitmaps()) {
            parcel.readMap(hashMap, Bitmap.class.getClassLoader());
        }
        HashMap hashMap2 = new HashMap();
        if (create.getExtrasInfo() != null && create.getExtrasInfo().getContainsPendingIntents()) {
            parcel.readMap(hashMap2, PendingIntent.class.getClassLoader());
        }
        return create(create, hashMap, hashMap2);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.entities.writeToBundle().writeToParcel(parcel, 0);
        if (this.entities.getExtrasInfo() != null) {
            if (this.entities.getExtrasInfo().getContainsBitmaps()) {
                parcel.writeMap(this.bitmapMap);
            }
            if (this.entities.getExtrasInfo().getContainsPendingIntents()) {
                parcel.writeMap(this.pendingIntentMap);
            }
        }
    }

    @Nullable
    public Bitmap getBitmap(String str) {
        return this.bitmapMap.get(str);
    }

    @Nullable
    public PendingIntent getPendingIntent(String str) {
        return this.pendingIntentMap.get(str);
    }

    public SuggestParcelables$Entities entities() {
        return this.entities;
    }

    private EntitiesData(SuggestParcelables$Entities suggestParcelables$Entities, Map<String, Bitmap> map, Map<String, PendingIntent> map2) {
        this.entities = suggestParcelables$Entities;
        this.bitmapMap = map;
        this.pendingIntentMap = map2;
    }
}
