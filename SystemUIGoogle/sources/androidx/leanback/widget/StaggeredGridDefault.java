package androidx.leanback.widget;

import androidx.leanback.widget.StaggeredGrid;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class StaggeredGridDefault extends StaggeredGrid {
    int getRowMax(int i) {
        int i2;
        StaggeredGrid.Location location;
        int i3 = this.mFirstVisibleIndex;
        if (i3 < 0) {
            return Integer.MIN_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(i3);
            if (getLocation(this.mFirstVisibleIndex).row == i) {
                return edge;
            }
            int i4 = this.mFirstVisibleIndex;
            do {
                i4++;
                if (i4 <= getLastIndex()) {
                    location = getLocation(i4);
                    edge += location.offset;
                }
            } while (location.row != i);
            return edge;
        }
        int edge2 = this.mProvider.getEdge(this.mLastVisibleIndex);
        StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
        if (location2.row != i) {
            int i5 = this.mLastVisibleIndex;
            while (true) {
                i5--;
                if (i5 < getFirstIndex()) {
                    break;
                }
                edge2 -= location2.offset;
                location2 = getLocation(i5);
                if (location2.row == i) {
                    i2 = location2.size;
                    break;
                }
            }
        } else {
            i2 = location2.size;
        }
        return edge2 + i2;
        return Integer.MIN_VALUE;
    }

    int getRowMin(int i) {
        StaggeredGrid.Location location;
        int i2;
        int i3 = this.mFirstVisibleIndex;
        if (i3 < 0) {
            return Integer.MAX_VALUE;
        }
        if (this.mReversedFlow) {
            int edge = this.mProvider.getEdge(this.mLastVisibleIndex);
            StaggeredGrid.Location location2 = getLocation(this.mLastVisibleIndex);
            if (location2.row != i) {
                int i4 = this.mLastVisibleIndex;
                while (true) {
                    i4--;
                    if (i4 < getFirstIndex()) {
                        break;
                    }
                    edge -= location2.offset;
                    location2 = getLocation(i4);
                    if (location2.row == i) {
                        i2 = location2.size;
                        break;
                    }
                }
            } else {
                i2 = location2.size;
            }
            return edge - i2;
        }
        int edge2 = this.mProvider.getEdge(i3);
        if (getLocation(this.mFirstVisibleIndex).row == i) {
            return edge2;
        }
        int i5 = this.mFirstVisibleIndex;
        do {
            i5++;
            if (i5 <= getLastIndex()) {
                location = getLocation(i5);
                edge2 += location.offset;
            }
        } while (location.row != i);
        return edge2;
        return Integer.MAX_VALUE;
    }

    @Override // androidx.leanback.widget.Grid
    public int findRowMax(boolean z, int i, int[] iArr) {
        int i2;
        int edge = this.mProvider.getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            int i4 = i + 1;
            i2 = i3;
            int i5 = i2;
            int i6 = 1;
            int i7 = edge;
            while (i6 < this.mNumRows && i4 <= this.mLastVisibleIndex) {
                StaggeredGrid.Location location2 = getLocation(i4);
                i7 += location2.offset;
                int i8 = location2.row;
                if (i8 != i5) {
                    i6++;
                    if (!z ? i7 >= edge : i7 <= edge) {
                        i5 = i8;
                    } else {
                        edge = i7;
                        i = i4;
                        i2 = i8;
                        i5 = i2;
                    }
                }
                i4++;
            }
        } else {
            edge = this.mProvider.getSize(i) + edge;
            int i9 = i - 1;
            int i10 = 1;
            int i11 = i3;
            StaggeredGrid.Location location3 = location;
            int i12 = edge;
            i2 = i11;
            while (i10 < this.mNumRows && i9 >= this.mFirstVisibleIndex) {
                i12 -= location3.offset;
                location3 = getLocation(i9);
                int i13 = location3.row;
                if (i13 != i11) {
                    i10++;
                    int size = this.mProvider.getSize(i9) + i12;
                    if (!z ? size >= edge : size <= edge) {
                        i11 = i13;
                    } else {
                        edge = size;
                        i = i9;
                        i2 = i13;
                        i11 = i2;
                    }
                }
                i9--;
            }
        }
        if (iArr != null) {
            iArr[0] = i2;
            iArr[1] = i;
        }
        return edge;
    }

    @Override // androidx.leanback.widget.Grid
    public int findRowMin(boolean z, int i, int[] iArr) {
        int i2;
        int edge = this.mProvider.getEdge(i);
        StaggeredGrid.Location location = getLocation(i);
        int i3 = location.row;
        if (this.mReversedFlow) {
            i2 = edge - this.mProvider.getSize(i);
            int i4 = i - 1;
            int i5 = 1;
            int i6 = i3;
            while (i5 < this.mNumRows && i4 >= this.mFirstVisibleIndex) {
                edge -= location.offset;
                location = getLocation(i4);
                int i7 = location.row;
                if (i7 != i6) {
                    i5++;
                    int size = edge - this.mProvider.getSize(i4);
                    if (!z ? size >= i2 : size <= i2) {
                        i6 = i7;
                    } else {
                        i2 = size;
                        i = i4;
                        i3 = i7;
                        i6 = i3;
                    }
                }
                i4--;
            }
        } else {
            int i8 = i + 1;
            int i9 = i3;
            int i10 = i9;
            int i11 = 1;
            int i12 = edge;
            while (i11 < this.mNumRows && i8 <= this.mLastVisibleIndex) {
                StaggeredGrid.Location location2 = getLocation(i8);
                i12 += location2.offset;
                int i13 = location2.row;
                if (i13 != i10) {
                    i11++;
                    if (!z ? i12 >= edge : i12 <= edge) {
                        i10 = i13;
                    } else {
                        edge = i12;
                        i = i8;
                        i9 = i13;
                        i10 = i9;
                    }
                }
                i8++;
            }
            i2 = edge;
            i3 = i9;
        }
        if (iArr != null) {
            iArr[0] = i3;
            iArr[1] = i;
        }
        return i2;
    }

    private int findRowEdgeLimitSearchIndex(boolean z) {
        boolean z2 = false;
        if (z) {
            for (int i = this.mLastVisibleIndex; i >= this.mFirstVisibleIndex; i--) {
                int i2 = getLocation(i).row;
                if (i2 == 0) {
                    z2 = true;
                } else if (z2 && i2 == this.mNumRows - 1) {
                    return i;
                }
            }
            return -1;
        }
        for (int i3 = this.mFirstVisibleIndex; i3 <= this.mLastVisibleIndex; i3++) {
            int i4 = getLocation(i3).row;
            if (i4 == this.mNumRows - 1) {
                z2 = true;
            } else if (z2 && i4 == 0) {
                return i3;
            }
        }
        return -1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:106:0x014c, code lost:
        return r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:98:0x0134, code lost:
        return true;
     */
    @Override // androidx.leanback.widget.StaggeredGrid
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean appendVisibleItemsWithoutCache(int r14, boolean r15) {
        /*
        // Method dump skipped, instructions count: 353
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.appendVisibleItemsWithoutCache(int, boolean):boolean");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:105:0x0146, code lost:
        return r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x012e, code lost:
        return true;
     */
    @Override // androidx.leanback.widget.StaggeredGrid
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected boolean prependVisibleItemsWithoutCache(int r13, boolean r14) {
        /*
        // Method dump skipped, instructions count: 349
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.StaggeredGridDefault.prependVisibleItemsWithoutCache(int, boolean):boolean");
    }
}
