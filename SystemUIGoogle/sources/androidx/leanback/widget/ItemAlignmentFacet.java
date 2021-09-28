package androidx.leanback.widget;
/* loaded from: classes.dex */
public final class ItemAlignmentFacet {
    private ItemAlignmentDef[] mAlignmentDefs = {new ItemAlignmentDef()};

    /* loaded from: classes.dex */
    public static class ItemAlignmentDef {
        private boolean mAlignToBaseline;
        int mViewId = -1;
        int mFocusViewId = -1;
        int mOffset = 0;
        float mOffsetPercent = 50.0f;
        boolean mOffsetWithPadding = false;

        public final int getItemAlignmentFocusViewId() {
            int i = this.mFocusViewId;
            return i != -1 ? i : this.mViewId;
        }

        public boolean isAlignedToTextViewBaseLine() {
            return this.mAlignToBaseline;
        }
    }

    public ItemAlignmentDef[] getAlignmentDefs() {
        return this.mAlignmentDefs;
    }
}
