package android.support.v4.media.session;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
/* loaded from: classes.dex */
public final class MediaSessionCompat$ResultReceiverWrapper implements Parcelable {
    public static final Parcelable.Creator<MediaSessionCompat$ResultReceiverWrapper> CREATOR = new Parcelable.Creator<MediaSessionCompat$ResultReceiverWrapper>() { // from class: android.support.v4.media.session.MediaSessionCompat$ResultReceiverWrapper.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public MediaSessionCompat$ResultReceiverWrapper createFromParcel(Parcel parcel) {
            return new MediaSessionCompat$ResultReceiverWrapper(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public MediaSessionCompat$ResultReceiverWrapper[] newArray(int i) {
            return new MediaSessionCompat$ResultReceiverWrapper[i];
        }
    };
    public ResultReceiver mResultReceiver;

    public MediaSessionCompat$ResultReceiverWrapper(Parcel parcel) {
        this.mResultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        this.mResultReceiver.writeToParcel(parcel, i);
    }
}
