package com.android.systemui.shared.system.smartspace;

import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceState.kt */
/* loaded from: classes.dex */
public final class SmartspaceState implements Parcelable {
    public static final CREATOR CREATOR = new CREATOR(null);
    private Rect boundsOnScreen;
    private int selectedPage;

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public SmartspaceState() {
        this.boundsOnScreen = new Rect();
    }

    public final Rect getBoundsOnScreen() {
        return this.boundsOnScreen;
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public SmartspaceState(Parcel parcel) {
        this();
        Intrinsics.checkNotNullParameter(parcel, "parcel");
        Parcelable readParcelable = parcel.readParcelable(AnonymousClass1.INSTANCE.getClass().getClassLoader());
        Intrinsics.checkNotNullExpressionValue(readParcelable, "parcel.readParcelable(Rect::javaClass.javaClass.classLoader)");
        this.boundsOnScreen = (Rect) readParcelable;
        this.selectedPage = parcel.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeParcelable(this.boundsOnScreen, 0);
        }
        if (parcel != null) {
            parcel.writeInt(this.selectedPage);
        }
    }

    @Override // java.lang.Object
    public String toString() {
        return "boundsOnScreen: " + this.boundsOnScreen + ", selectedPage: " + this.selectedPage;
    }

    /* compiled from: SmartspaceState.kt */
    /* loaded from: classes.dex */
    public static final class CREATOR implements Parcelable.Creator<SmartspaceState> {
        public /* synthetic */ CREATOR(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private CREATOR() {
        }

        @Override // android.os.Parcelable.Creator
        public SmartspaceState createFromParcel(Parcel parcel) {
            Intrinsics.checkNotNullParameter(parcel, "parcel");
            return new SmartspaceState(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public SmartspaceState[] newArray(int i) {
            return new SmartspaceState[i];
        }
    }
}
