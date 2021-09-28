package android.support.v4.media.session;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.support.v4.media.MediaDescriptionCompat;
/* loaded from: classes.dex */
public final class MediaSessionCompat$QueueItem implements Parcelable {
    public static final Parcelable.Creator<MediaSessionCompat$QueueItem> CREATOR = new Parcelable.Creator<MediaSessionCompat$QueueItem>() { // from class: android.support.v4.media.session.MediaSessionCompat$QueueItem.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public MediaSessionCompat$QueueItem createFromParcel(Parcel parcel) {
            return new MediaSessionCompat$QueueItem(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public MediaSessionCompat$QueueItem[] newArray(int i) {
            return new MediaSessionCompat$QueueItem[i];
        }
    };
    public final MediaDescriptionCompat mDescription;
    public final long mId;

    public MediaSessionCompat$QueueItem(Parcel parcel) {
        this.mDescription = MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        this.mId = parcel.readLong();
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.lang.Object
    public String toString() {
        StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("MediaSession.QueueItem {Description=");
        m.append(this.mDescription);
        m.append(", Id=");
        m.append(this.mId);
        m.append(" }");
        return m.toString();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.mDescription.writeToParcel(parcel, i);
        parcel.writeLong(this.mId);
    }
}
