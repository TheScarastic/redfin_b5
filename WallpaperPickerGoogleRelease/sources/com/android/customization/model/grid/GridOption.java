package com.android.customization.model.grid;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import androidx.cardview.R$attr;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.CustomizationOption;
import com.android.customization.widget.GridTileDrawable;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class GridOption implements CustomizationOption<GridOption>, Parcelable {
    public static final Parcelable.Creator<GridOption> CREATOR = new Parcelable.Creator<GridOption>() { // from class: com.android.customization.model.grid.GridOption.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public GridOption createFromParcel(Parcel parcel) {
            return new GridOption(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public GridOption[] newArray(int i) {
            return new GridOption[i];
        }
    };
    public final int cols;
    public final String mIconShapePath;
    public final boolean mIsCurrent;
    public final GridTileDrawable mTileDrawable;
    public final String mTitle;
    public final String name;
    public final Uri previewImageUri;
    public final int previewPagesCount;
    public final int rows;

    public GridOption(String str, String str2, boolean z, int i, int i2, Uri uri, int i3, String str3) {
        this.mTitle = str;
        this.mIsCurrent = z;
        this.mIconShapePath = str3;
        this.mTileDrawable = new GridTileDrawable(i, i2, str3);
        this.name = str2;
        this.rows = i;
        this.cols = i2;
        this.previewImageUri = uri;
        this.previewPagesCount = i3;
    }

    @Override // com.android.customization.model.CustomizationOption
    public void bindThumbnailTile(View view) {
        this.mTileDrawable.setColorFilter(R$attr.getColorAttr(view.getContext(), view.isActivated() ? 16842806 : 16843282), PorterDuff.Mode.SRC_ATOP);
        ((ImageView) view.findViewById(R.id.grid_option_thumbnail)).setImageDrawable(this.mTileDrawable);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof GridOption)) {
            return false;
        }
        GridOption gridOption = (GridOption) obj;
        return TextUtils.equals(this.name, gridOption.name) && this.cols == gridOption.cols && this.rows == gridOption.rows;
    }

    @Override // com.android.customization.model.CustomizationOption
    public int getLayoutResId() {
        return R.layout.grid_option;
    }

    @Override // com.android.customization.model.CustomizationOption
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.android.customization.model.CustomizationOption
    public boolean isActive(CustomizationManager<GridOption> customizationManager) {
        return this.mIsCurrent;
    }

    @Override // java.lang.Object
    public String toString() {
        return String.format("GridOption{mTitle='%s', mIsCurrent=%s, mTileDrawable=%s, name='%s', rows=%d, cols=%d, previewImageUri=%s, previewPagesCount=%d}\n", this.mTitle, Boolean.valueOf(this.mIsCurrent), this.mTileDrawable, this.name, Integer.valueOf(this.rows), Integer.valueOf(this.cols), this.previewImageUri, Integer.valueOf(this.previewPagesCount));
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mTitle);
        parcel.writeByte(this.mIsCurrent ? (byte) 1 : 0);
        parcel.writeString(this.mIconShapePath);
        parcel.writeString(this.name);
        parcel.writeInt(this.rows);
        parcel.writeInt(this.cols);
        parcel.writeParcelable(this.previewImageUri, i);
        parcel.writeInt(this.previewPagesCount);
    }

    public GridOption(Parcel parcel) {
        this.mTitle = parcel.readString();
        this.mIsCurrent = parcel.readByte() != 0;
        String readString = parcel.readString();
        this.mIconShapePath = readString;
        this.name = parcel.readString();
        int readInt = parcel.readInt();
        this.rows = readInt;
        int readInt2 = parcel.readInt();
        this.cols = readInt2;
        this.previewImageUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
        this.previewPagesCount = parcel.readInt();
        this.mTileDrawable = new GridTileDrawable(readInt, readInt2, readString);
    }
}
