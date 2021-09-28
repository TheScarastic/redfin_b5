package com.google.android.systemui.smartspace;
/* loaded from: classes2.dex */
public class BcSmartspaceCardLoggingInfo {
    private final int mCardinality;
    private final int mDisplaySurface;
    private final int mInstanceId;
    private final int mLoggingCardType;
    private final int mRank;

    private BcSmartspaceCardLoggingInfo(Builder builder) {
        this.mInstanceId = builder.mInstanceId;
        this.mLoggingCardType = builder.mLoggingCardType;
        this.mDisplaySurface = builder.mDisplaySurface;
        this.mRank = builder.mRank;
        this.mCardinality = builder.mCardinality;
    }

    public int getInstanceId() {
        return this.mInstanceId;
    }

    public int getLoggingCardType() {
        return this.mLoggingCardType;
    }

    public int getDisplaySurface() {
        return this.mDisplaySurface;
    }

    public int getRank() {
        return this.mRank;
    }

    public int getCardinality() {
        return this.mCardinality;
    }

    public String toString() {
        return "instance_id = " + getInstanceId() + ", card type = " + getLoggingCardType() + ", display surface = " + getDisplaySurface() + ", rank = " + getRank() + ", cardinality = " + getCardinality();
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private int mCardinality;
        private int mDisplaySurface = 1;
        private int mInstanceId;
        private int mLoggingCardType;
        private int mRank;

        public Builder setInstanceId(int i) {
            this.mInstanceId = i;
            return this;
        }

        public Builder setLoggingCardType(int i) {
            this.mLoggingCardType = i;
            return this;
        }

        public Builder setDisplaySurface(int i) {
            this.mDisplaySurface = i;
            return this;
        }

        public Builder setRank(int i) {
            this.mRank = i;
            return this;
        }

        public Builder setCardinality(int i) {
            this.mCardinality = i;
            return this;
        }

        public BcSmartspaceCardLoggingInfo build() {
            return new BcSmartspaceCardLoggingInfo(this);
        }
    }
}
