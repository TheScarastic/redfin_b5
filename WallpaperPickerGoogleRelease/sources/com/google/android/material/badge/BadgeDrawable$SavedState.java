package com.google.android.material.badge;

import android.os.Parcel;
import android.os.Parcelable;
/* loaded from: classes.dex */
public final class BadgeDrawable$SavedState implements Parcelable {
    public static final Parcelable.Creator<BadgeDrawable$SavedState> CREATOR = new Parcelable.Creator<BadgeDrawable$SavedState>() { // from class: com.google.android.material.badge.BadgeDrawable$SavedState.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public BadgeDrawable$SavedState createFromParcel(Parcel parcel) {
            return new BadgeDrawable$SavedState(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public BadgeDrawable$SavedState[] newArray(int i) {
            return new BadgeDrawable$SavedState[i];
        }
    };
    public int additionalHorizontalOffset;
    public int additionalVerticalOffset;
    public int alpha;
    public int backgroundColor;
    public int badgeGravity;
    public int badgeTextColor;
    public CharSequence contentDescriptionNumberless;
    public int contentDescriptionQuantityStrings;
    public int horizontalOffset;
    public boolean isVisible;
    public int maxCharacterCount;
    public int number;
    public int verticalOffset;

    public BadgeDrawable$SavedState(Parcel parcel) {
        this.alpha = 255;
        this.number = -1;
        this.backgroundColor = parcel.readInt();
        this.badgeTextColor = parcel.readInt();
        this.alpha = parcel.readInt();
        this.number = parcel.readInt();
        this.maxCharacterCount = parcel.readInt();
        this.contentDescriptionNumberless = parcel.readString();
        this.contentDescriptionQuantityStrings = parcel.readInt();
        this.badgeGravity = parcel.readInt();
        this.horizontalOffset = parcel.readInt();
        this.verticalOffset = parcel.readInt();
        this.additionalHorizontalOffset = parcel.readInt();
        this.additionalVerticalOffset = parcel.readInt();
        this.isVisible = parcel.readInt() != 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.backgroundColor);
        parcel.writeInt(this.badgeTextColor);
        parcel.writeInt(this.alpha);
        parcel.writeInt(this.number);
        parcel.writeInt(this.maxCharacterCount);
        parcel.writeString(this.contentDescriptionNumberless.toString());
        parcel.writeInt(this.contentDescriptionQuantityStrings);
        parcel.writeInt(this.badgeGravity);
        parcel.writeInt(this.horizontalOffset);
        parcel.writeInt(this.verticalOffset);
        parcel.writeInt(this.additionalHorizontalOffset);
        parcel.writeInt(this.additionalVerticalOffset);
        parcel.writeInt(this.isVisible ? 1 : 0);
    }
}
