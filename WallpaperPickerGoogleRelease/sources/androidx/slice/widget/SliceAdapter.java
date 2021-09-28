package androidx.slice.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.recyclerview.widget.RecyclerView;
import androidx.slice.ArrayUtils;
import androidx.slice.SliceItem;
import androidx.slice.core.SliceAction;
import androidx.slice.core.SliceQuery;
import androidx.slice.widget.SliceActionView;
import androidx.slice.widget.SliceView;
import com.android.systemui.shared.R;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class SliceAdapter extends RecyclerView.Adapter<SliceViewHolder> implements SliceActionView.SliceActionLoadingListener {
    public boolean mAllowTwoLines;
    public final Context mContext;
    public int mInsetBottom;
    public int mInsetEnd;
    public int mInsetStart;
    public int mInsetTop;
    public long mLastUpdated;
    public SliceView mParent;
    public SliceViewPolicy mPolicy;
    public boolean mShowLastUpdated;
    public List<SliceAction> mSliceActions;
    public SliceView.OnSliceActionListener mSliceObserver;
    public SliceStyle mSliceStyle;
    public TemplateView mTemplateView;
    public final IdGenerator mIdGen = new IdGenerator();
    public List<SliceWrapper> mSlices = new ArrayList();
    public Set<SliceItem> mLoadingActions = new HashSet();

    /* loaded from: classes.dex */
    public static class IdGenerator {
        public long mNextLong = 0;
        public final ArrayMap<String, Long> mCurrentIds = new ArrayMap<>();
        public final ArrayMap<String, Integer> mUsedIds = new ArrayMap<>();
    }

    /* loaded from: classes.dex */
    public class SliceViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, View.OnClickListener {
        public final SliceChildView mSliceChildView;

        public SliceViewHolder(View view) {
            super(view);
            this.mSliceChildView = view instanceof SliceChildView ? (SliceChildView) view : null;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (SliceAdapter.this.mParent != null) {
                int[] iArr = (int[]) view.getTag();
                SliceAdapter.this.mParent.performClick();
            }
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            ListContent listContent;
            TemplateView templateView = SliceAdapter.this.mTemplateView;
            if (templateView != null) {
                SliceView sliceView = templateView.mParent;
                if (sliceView != null) {
                    if (!((sliceView.mOnClickListener == null && ((listContent = sliceView.mListContent) == null || listContent.getShortcut(sliceView.getContext()) == null)) ? false : true)) {
                        templateView.mForeground.setPressed(false);
                    }
                }
                templateView.mForeground.getLocationOnScreen(templateView.mLoc);
                templateView.mForeground.getBackground().setHotspot((float) ((int) (motionEvent.getRawX() - ((float) templateView.mLoc[0]))), (float) ((int) (motionEvent.getRawY() - ((float) templateView.mLoc[1]))));
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked == 0) {
                    templateView.mForeground.setPressed(true);
                } else if (actionMasked == 3 || actionMasked == 1 || actionMasked == 2) {
                    templateView.mForeground.setPressed(false);
                }
            }
            return false;
        }
    }

    /* loaded from: classes.dex */
    public static class SliceWrapper {
        public final long mId;
        public final SliceContent mItem;
        public final int mType;

        public SliceWrapper(SliceContent sliceContent, IdGenerator idGenerator) {
            int i;
            String str;
            this.mItem = sliceContent;
            SliceItem sliceItem = sliceContent.mSliceItem;
            if ("message".equals(sliceItem.mSubType)) {
                i = SliceQuery.findSubtype(sliceItem, (String) null, "source") != null ? 4 : 5;
            } else if (ArrayUtils.contains(sliceItem.mHints, "horizontal")) {
                i = 3;
            } else {
                i = !ArrayUtils.contains(sliceItem.mHints, "list_item") ? 2 : 1;
            }
            this.mType = i;
            SliceItem sliceItem2 = sliceContent.mSliceItem;
            Objects.requireNonNull(idGenerator);
            if ("slice".equals(sliceItem2.mFormat) || "action".equals(sliceItem2.mFormat)) {
                str = String.valueOf(sliceItem2.getSlice().getItems().size());
            } else {
                str = sliceItem2.toString();
            }
            if (!idGenerator.mCurrentIds.containsKey(str)) {
                ArrayMap<String, Long> arrayMap = idGenerator.mCurrentIds;
                long j = idGenerator.mNextLong;
                idGenerator.mNextLong = 1 + j;
                arrayMap.put(str, Long.valueOf(j));
            }
            long longValue = idGenerator.mCurrentIds.get(str).longValue();
            Integer num = idGenerator.mUsedIds.get(str);
            int intValue = num != null ? num.intValue() : 0;
            idGenerator.mUsedIds.put(str, Integer.valueOf(intValue + 1));
            this.mId = longValue + ((long) (intValue * 10000));
        }
    }

    public SliceAdapter(Context context) {
        this.mContext = context;
        if (!this.mObservable.hasObservers()) {
            this.mHasStableIds = true;
            return;
        }
        throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mSlices.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.mSlices.get(i).mId;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return this.mSlices.get(i).mType;
    }

    public void notifyHeaderChanged() {
        if (getItemCount() > 0) {
            notifyItemChanged(0);
        }
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [androidx.recyclerview.widget.RecyclerView$ViewHolder, int] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(SliceViewHolder sliceViewHolder, int i) {
        SliceViewHolder sliceViewHolder2 = sliceViewHolder;
        SliceContent sliceContent = this.mSlices.get(i).mItem;
        if (sliceViewHolder2.mSliceChildView != null && sliceContent != null) {
            RowStyle rowStyle = SliceAdapter.this.mSliceStyle.getRowStyle(sliceContent.mSliceItem);
            sliceViewHolder2.mSliceChildView.setOnClickListener(sliceViewHolder2);
            sliceViewHolder2.mSliceChildView.setOnTouchListener(sliceViewHolder2);
            SliceChildView sliceChildView = sliceViewHolder2.mSliceChildView;
            SliceAdapter sliceAdapter = SliceAdapter.this;
            sliceChildView.mLoadingListener = sliceAdapter;
            boolean z = sliceContent instanceof RowContent ? ((RowContent) sliceContent).mIsHeader : i == 0;
            sliceChildView.setLoadingActions(sliceAdapter.mLoadingActions);
            sliceViewHolder2.mSliceChildView.setPolicy(SliceAdapter.this.mPolicy);
            sliceViewHolder2.mSliceChildView.setTint(rowStyle.getTintColor());
            sliceViewHolder2.mSliceChildView.setStyle(SliceAdapter.this.mSliceStyle, rowStyle);
            sliceViewHolder2.mSliceChildView.setShowLastUpdated(z && SliceAdapter.this.mShowLastUpdated);
            sliceViewHolder2.mSliceChildView.setLastUpdated(z ? SliceAdapter.this.mLastUpdated : -1);
            int i2 = i == 0 ? SliceAdapter.this.mInsetTop : 0;
            int i3 = i == SliceAdapter.this.getItemCount() - 1 ? SliceAdapter.this.mInsetBottom : 0;
            SliceChildView sliceChildView2 = sliceViewHolder2.mSliceChildView;
            SliceAdapter sliceAdapter2 = SliceAdapter.this;
            sliceChildView2.setInsets(sliceAdapter2.mInsetStart, i2, sliceAdapter2.mInsetEnd, i3);
            sliceViewHolder2.mSliceChildView.setAllowTwoLines(SliceAdapter.this.mAllowTwoLines);
            sliceViewHolder2.mSliceChildView.setSliceActions(z ? SliceAdapter.this.mSliceActions : null);
            sliceViewHolder2.mSliceChildView.setSliceItem(sliceContent, z, i, SliceAdapter.this.getItemCount(), SliceAdapter.this.mSliceObserver);
            sliceViewHolder2.mSliceChildView.setTag(new int[]{ListContent.getRowType(sliceContent, z, SliceAdapter.this.mSliceActions), i});
        }
    }

    /* Return type fixed from 'androidx.recyclerview.widget.RecyclerView$ViewHolder' to match base method */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public SliceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == 3) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.abc_slice_grid, (ViewGroup) null);
            if (inflate instanceof GridRowView) {
                view = (GridRowView) inflate;
            } else {
                view = new GridRowView(this.mContext, null);
            }
        } else if (i == 4) {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.abc_slice_message, (ViewGroup) null);
        } else if (i != 5) {
            view = new RowView(this.mContext);
        } else {
            view = LayoutInflater.from(this.mContext).inflate(R.layout.abc_slice_message_local, (ViewGroup) null);
        }
        view.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        return new SliceViewHolder(view);
    }

    public void onSliceActionLoading(SliceItem sliceItem, int i) {
        this.mLoadingActions.add(sliceItem);
        if (getItemCount() > i) {
            this.mObservable.notifyItemRangeChanged(i, 1);
        } else {
            this.mObservable.notifyChanged();
        }
    }

    public void setSliceItems(List<SliceContent> list, int i, int i2) {
        if (list == null) {
            this.mLoadingActions.clear();
            this.mSlices.clear();
        } else {
            this.mIdGen.mUsedIds.clear();
            this.mSlices = new ArrayList(list.size());
            for (SliceContent sliceContent : list) {
                this.mSlices.add(new SliceWrapper(sliceContent, this.mIdGen));
            }
        }
        this.mObservable.notifyChanged();
    }
}
